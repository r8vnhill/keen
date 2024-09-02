/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.EvolutionInterceptor
import cl.ravenhill.keen.evolution.engines.ga.GeneticAlgorithmFactory
import cl.ravenhill.keen.evolution.executors.evaluation.EvaluationExecutorFactory
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.GenotypeFactory
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.listeners.Listener
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.operators.alteration.Alterer
import cl.ravenhill.keen.operators.selection.Selector
import cl.ravenhill.keen.ranking.SyncFitnessMaxRanker
import cl.ravenhill.keen.ranking.SyncFitnessMinRanker
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.utils.arbProbability
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.int

class GeneticAlgorithmFactoryTest : FreeSpec({
    "A GeneticAlgorithmFactory" - {
        ""
    }
})

fun <T, G> arbGeneticAlgorithmFactory(
    fitnessFunction: Arb<(Genotype<T, G>) -> Double>,
    genotypeFactoryArb: Arb<GenotypeFactory<T, G>>,
    initialStateArb: Arb<GeneticEvolutionState<T, G>>,
    populationSizeArb: Arb<Int>? = Arb.int(1..100),
    survivalRateArb: Arb<Double>? = arbProbability(),
    rankerArb: Arb<IndividualRanker<T, G, Genotype<T, G>>>? = Arb.element(SyncFitnessMaxRanker(), SyncFitnessMinRanker()),
    parentSelectorArb: Arb<Selector<T, G, Genotype<T, G>>>?,
    survivorSelectorArb: Arb<Selector<T, G, Genotype<T, G>>>?,
    listenersArb: Arb<List<(ListenerConfiguration<T, G, Genotype<T, G>>) -> Listener>>?,
    limitsArb: Arb<List<LimitFactory<T, G>>>?,
    alterersArb: Arb<List<Alterer<T, G, Genotype<T, G>>>>?,
    evaluatorArb: Arb<EvaluationExecutorFactory<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>>?,
    interceptorArb: Arb<EvolutionInterceptor<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>>?
) where G : Gene<T, G> = arbitrary {
    GeneticAlgorithmFactory
}

private typealias LimitFactory<T, G> =
            (ListenerConfiguration<T, G, Genotype<T, G>>) ->
        Limit<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>, Listener>