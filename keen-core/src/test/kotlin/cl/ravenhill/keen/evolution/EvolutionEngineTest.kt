package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.arb.KeenArb
import cl.ravenhill.keen.arb.arbRanker
import cl.ravenhill.keen.arb.evolution.*
import cl.ravenhill.keen.arb.genetic.arbGenotype
import cl.ravenhill.keen.arb.genetic.arbGenotypeFactory
import cl.ravenhill.keen.arb.genetic.arbIndividual
import cl.ravenhill.keen.arb.genetic.arbPopulation
import cl.ravenhill.keen.arb.genetic.chromosomes.arbDoubleChromosome
import cl.ravenhill.keen.arb.genetic.chromosomes.arbDoubleChromosomeFactory
import cl.ravenhill.keen.arb.limits.arbGenerationLimit
import cl.ravenhill.keen.arb.listeners.arbEvolutionListener
import cl.ravenhill.keen.arb.operators.arbAlterer
import cl.ravenhill.keen.arb.operators.arbRouletteWheelSelector
import cl.ravenhill.keen.arb.operators.arbTournamentSelector
import cl.ravenhill.keen.evolution.config.AlterationConfig
import cl.ravenhill.keen.evolution.config.EvolutionConfig
import cl.ravenhill.keen.evolution.config.PopulationConfig
import cl.ravenhill.keen.evolution.config.SelectionConfig
import cl.ravenhill.keen.evolution.engines.GeneticAlgorithm
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.evolution.executors.SequentialEvaluator
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.operators.alteration.Alterer
import cl.ravenhill.keen.operators.selection.TournamentSelector
import cl.ravenhill.keen.ranking.IndividualRanker
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import kotlin.math.floor
import kotlin.random.Random

class EvolutionEngineTest : FreeSpec({
    "The engine" - {
        "can be created with a custom configuration" {
            checkAll(
                populationConfig(),
                selectionConfig(),
                alterationConfig(),
                evolutionConfig()
            ) { populationConfig, selectionConfig, alterationConfig, evolutionConfig ->
                with(GeneticAlgorithm(populationConfig, selectionConfig, alterationConfig, evolutionConfig)) {
                    genotypeFactory shouldBe populationConfig.genotypeFactory
                    populationSize shouldBe populationConfig.populationSize
                    survivalRate shouldBe selectionConfig.survivalRate
                    parentSelector shouldBe selectionConfig.parentSelector
                    survivorSelector shouldBe selectionConfig.survivorSelector
                    alterers shouldBe alterationConfig.alterers
                    limits shouldBe evolutionConfig.limits
                    this.evolutionConfig shouldBe evolutionConfig.ranker
                    listeners shouldBe evolutionConfig.listeners
                    evaluator shouldBe evolutionConfig.evaluator
                    interceptor shouldBe evolutionConfig.interceptor
                }
            }
        }

        "when starting evolution" - {
            "returns the same state if it already started" {
                checkAll(
                    engine(
                        populationConfig(),
                        selectionConfig(),
                        alterationConfig(),
                        evolutionConfig()
                    ),
                    nonEmptyState()
                ) { engine, state ->
                    with(engine) {
                        val newState = startEvolution(state)
                        newState shouldBe state
                    }
                }
            }

            "returns a new state if it hasn't started" {
                checkAll(
                    engine(
                        populationConfig(),
                        selectionConfig(),
                        alterationConfig(),
                        evolutionConfig()
                    ),
                    ranker()
                ) { engine, ranker ->
                    with(engine) {
                        val state = EvolutionState.empty(ranker)
                        val newState = startEvolution(state)
                        newState.size shouldBe populationSize
                    }
                }
            }
        }

        "when evaluating a population" - {
            "returns a state with all individuals evaluated" {
                checkAll(
                    engine(
                        populationConfig(),
                        selectionConfig(),
                        alterationConfig(),
                        evolutionConfig()
                    ).map { engine ->
                        engine to state(engine.populationSize).next()
                    }
                ) { (engine, state) ->
                    with(engine) {
                        val newState = evaluatePopulation(state)
                        newState.population.all { it.isEvaluated() }
                    }
                }
            }
        }

        "when selecting parents" - {
            "selects the expected number of parents" {
                checkAll(
                    engine(
                        populationConfig(),
                        selectionConfig(),
                        alterationConfig(),
                        evolutionConfig()
                    ).map { engine ->
                        engine to nonEmptyState().next()
                    }
                ) { (engine, state) ->
                    with(engine) {
                        val newState = selectParents(state)
                        newState.population.size shouldBe floor((1 - survivalRate) * populationSize).toInt()
                    }
                }
            }

            "selects parents with the expected selector" {
                checkAll(
                    engine(
                        populationConfig(),
                        selectionConfig(),
                        alterationConfig(),
                        evolutionConfig()
                    ).map { engine ->
                        engine to nonEmptyState().next()
                    },
                    Arb.long().map { Random(it) to Random(it) }
                ) { (engine, state), (r1, r2) ->
                    with(engine) {
                        Domain.random = r1
                        val newState = selectParents(state)
                        Domain.random = r2
                        newState.population shouldBe parentSelector.select(
                            state.population,
                            newState.population.size,
                            evolutionConfig
                        )
                    }
                }
            }
        }
    }
})


private fun populationConfig() = arbPopulationConfig(
    arbGenotypeFactory(Arb.list(arbDoubleChromosomeFactory())),
    Arb.int(0..100)
)

private fun probability(): Arb<Double> = Arb.double(0.0..1.0, includeNonFiniteEdgeCases = false)
private fun tournamentSelector(): Arb<TournamentSelector<Double, DoubleGene>> = arbTournamentSelector()
private fun selectionConfig(): Arb<SelectionConfig<Double, DoubleGene>> = arbSelectionConfig(
    probability(),
    tournamentSelector(),
    arbRouletteWheelSelector()
)

private fun alterers(): Arb<List<Alterer<Double, DoubleGene>>> = Arb.list(arbAlterer())
private fun alterationConfig(): Arb<AlterationConfig<Double, DoubleGene>> = arbAlterationConfig(alterers())

private fun limits(): Arb<List<Limit<Double, DoubleGene>>> = Arb.list(arbGenerationLimit())
private fun ranker(): Arb<IndividualRanker<Double, DoubleGene>> = arbRanker()
private fun listeners(
    ranker: Arb<IndividualRanker<Double, DoubleGene>>
): Arb<List<EvolutionListener<Double, DoubleGene>>> = Arb.list(arbEvolutionListener())

private fun evaluator(): Arb<EvaluationExecutor<Double, DoubleGene>> = arbitrary {
    SequentialEvaluator { _ -> 1.0 }
}

private fun evolutionConfig(): Arb<EvolutionConfig<Double, DoubleGene>> {
    val ranker = ranker()
    return KeenArb.evolutionConfig(
        limits(),
        ranker,
        listeners(ranker),
        evaluator(),
        arbitrary { EvolutionInterceptor(before = { it }, after = { it }) }
    )
}

private fun engine(
    populationConfig: Arb<PopulationConfig<Double, DoubleGene>>,
    selectionConfig: Arb<SelectionConfig<Double, DoubleGene>>,
    alterationConfig: Arb<AlterationConfig<Double, DoubleGene>>,
    evolutionConfig: Arb<EvolutionConfig<Double, DoubleGene>>
): Arb<GeneticAlgorithm<Double, DoubleGene>> = arbitrary {
    GeneticAlgorithm(
        populationConfig.bind(),
        selectionConfig.bind(),
        alterationConfig.bind(),
        evolutionConfig.bind()
    )
}

private fun individual(): Arb<Individual<Double, DoubleGene>> = arbIndividual(arbGenotype(arbDoubleChromosome()))
private fun nonEmptyPopulation(): Arb<Population<Double, DoubleGene>> = arbPopulation(individual(), 1..100)

private fun nonEmptyState(): Arb<EvolutionState<Double, DoubleGene>> =
    arbEvolutionState(nonEmptyPopulation(), ranker())

private fun state(size: Int): Arb<EvolutionState<Double, DoubleGene>> =
    arbEvolutionState(arbPopulation(individual(), size..size), ranker())