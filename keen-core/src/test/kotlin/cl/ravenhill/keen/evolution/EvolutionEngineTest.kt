package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.arb.KeenArb
import cl.ravenhill.keen.arb.arbRanker
import cl.ravenhill.keen.arb.evolution.*
import cl.ravenhill.keen.arb.genetic.arbGenotypeFactory
import cl.ravenhill.keen.arb.genetic.chromosomes.arbDoubleChromosomeFactory
import cl.ravenhill.keen.arb.genetic.chromosomes.doubleChromosome
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.arb.genetic.individual
import cl.ravenhill.keen.arb.genetic.population
import cl.ravenhill.keen.arb.limits.arbGenerationLimit
import cl.ravenhill.keen.arb.listeners.arbEvolutionListener
import cl.ravenhill.keen.arb.listeners.arbEvolutionRecord
import cl.ravenhill.keen.arb.operators.arbAlterer
import cl.ravenhill.keen.arb.operators.arbRouletteWheelSelector
import cl.ravenhill.keen.arb.operators.arbTournamentSelector
import cl.ravenhill.keen.evolution.config.AlterationConfig
import cl.ravenhill.keen.evolution.config.EvolutionConfig
import cl.ravenhill.keen.evolution.config.PopulationConfig
import cl.ravenhill.keen.evolution.config.SelectionConfig
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.evolution.executors.SequentialEvaluator
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.operators.alteration.Alterer
import cl.ravenhill.keen.operators.selection.TournamentSelector
import cl.ravenhill.keen.ranking.IndividualRanker
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class EvolutionEngineTest : FreeSpec({
    "The engine" - {
        "can be created with a custom configuration" {
            checkAll(
                populationConfig(),
                selectionConfig(),
                alterationConfig(),
                evolutionConfig()
            ) { populationConfig, selectionConfig, alterationConfig, evolutionConfig ->
                with(EvolutionEngine(populationConfig, selectionConfig, alterationConfig, evolutionConfig)) {
                    genotypeFactory shouldBe populationConfig.genotypeFactory
                    populationSize shouldBe populationConfig.populationSize
                    survivalRate shouldBe selectionConfig.survivalRate
                    parentSelector shouldBe selectionConfig.parentSelector
                    survivorSelector shouldBe selectionConfig.survivorSelector
                    alterers shouldBe alterationConfig.alterers
                    limits shouldBe evolutionConfig.limits
                    ranker shouldBe evolutionConfig.ranker
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
                    with(engine) { }
                }
            }

            "returns a new state if it hasn't started" {

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
): Arb<List<EvolutionListener<Double, DoubleGene>>> = Arb.list(arbEvolutionListener(ranker, arbEvolutionRecord()))

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
): Arb<EvolutionEngine<Double, DoubleGene>> = arbitrary {
    EvolutionEngine(
        populationConfig.bind(),
        selectionConfig.bind(),
        alterationConfig.bind(),
        evolutionConfig.bind()
    )
}

private fun individual(): Arb<Individual<Double, DoubleGene>> = Arb.individual(Arb.genotype(Arb.doubleChromosome()))
private fun nonEmptyPopulation(): Arb<Population<Double, DoubleGene>> = Arb.population(individual(), 1..100)

private fun nonEmptyState(): Arb<EvolutionState<Double, DoubleGene>> =
    Arb.evolutionState(nonEmptyPopulation(), ranker())