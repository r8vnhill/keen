package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.arb.KeenArb
import cl.ravenhill.keen.arb.evolution.*
import cl.ravenhill.keen.arb.genetic.chromosomes.doubleChromosomeFactory
import cl.ravenhill.keen.arb.genetic.genotypeFactory
import cl.ravenhill.keen.arb.limits.generationLimit
import cl.ravenhill.keen.arb.listeners.evolutionListener
import cl.ravenhill.keen.arb.listeners.evolutionRecord
import cl.ravenhill.keen.arb.operators.alterer
import cl.ravenhill.keen.arb.operators.rouletteWheelSelector
import cl.ravenhill.keen.arb.operators.tournamentSelector
import cl.ravenhill.keen.arb.ranker
import cl.ravenhill.keen.evolution.config.AlterationConfig
import cl.ravenhill.keen.evolution.config.EvolutionConfig
import cl.ravenhill.keen.evolution.config.SelectionConfig
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.evolution.executors.SequentialEvaluator
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.operators.alteration.Alterer
import cl.ravenhill.keen.operators.selection.RouletteWheelSelector
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
    }
})


private fun populationConfig() = KeenArb.populationConfig(
    Arb.genotypeFactory(Arb.list(Arb.doubleChromosomeFactory())),
    Arb.int(0..100)
)

private fun probabilityArb(): Arb<Double> = Arb.double(0.0..1.0, includeNonFiniteEdgeCases = false)
private fun arbTournamentSelector(): Arb<TournamentSelector<Double, DoubleGene>> = Arb.tournamentSelector()
private fun arbRouletteWheelSelector(): Arb<RouletteWheelSelector<Double, DoubleGene>> = Arb.rouletteWheelSelector()
private fun selectionConfig(): Arb<SelectionConfig<Double, DoubleGene>> = Arb.selectionConfig(
    probabilityArb(),
    arbTournamentSelector(),
    arbRouletteWheelSelector()
)

private fun alterers(): Arb<List<Alterer<Double, DoubleGene>>> = Arb.list(Arb.alterer())
private fun alterationConfig(): Arb<AlterationConfig<Double, DoubleGene>> = Arb.alterationConfig(alterers())

private fun limits(): Arb<List<Limit<Double, DoubleGene>>> = Arb.list(KeenArb.generationLimit())
private fun ranker(): Arb<IndividualRanker<Double, DoubleGene>> = KeenArb.ranker()
private fun listeners(
    ranker: Arb<IndividualRanker<Double, DoubleGene>>
): Arb<List<EvolutionListener<Double, DoubleGene>>> = Arb.list(Arb.evolutionListener(ranker, Arb.evolutionRecord()))

private fun evaluator(): Arb<EvaluationExecutor<Double, DoubleGene>> = arbitrary {
    SequentialEvaluator { _ -> 1.0 }
}

fun evolutionConfig(): Arb<EvolutionConfig<Double, DoubleGene>> {
    val ranker = ranker()
    return KeenArb.evolutionConfig(
        limits(),
        ranker,
        listeners(ranker),
        evaluator(),
        arbitrary { EvolutionInterceptor(before = { it }, after = { it }) }
    )
}