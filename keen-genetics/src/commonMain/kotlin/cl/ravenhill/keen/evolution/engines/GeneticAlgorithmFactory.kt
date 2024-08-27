/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.EvolutionInterceptor
import cl.ravenhill.keen.evolution.config.EvolutionConfiguration
import cl.ravenhill.keen.evolution.config.GeneticPopulationConfiguration
import cl.ravenhill.keen.evolution.config.SelectionConfiguration
import cl.ravenhill.keen.evolution.executors.evaluation.EvaluationExecutorFactory
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.GenotypeFactory
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.operators.selection.Selector
import cl.ravenhill.keen.operators.selection.TournamentSelector
import cl.ravenhill.keen.ranking.FitnessMaxRanker
import cl.ravenhill.keen.ranking.IndividualRanker

private typealias ListenerFactory<T, G> =
            (ListenerConfiguration<T, G, Genotype<T, G>>) ->
        EvolutionListener<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>

private typealias PopulationConfig<T, G> = GeneticPopulationConfiguration<T, G>

private typealias SelectionConfig<T, G> = SelectionConfiguration<T, G, Genotype<T, G>>

class GeneticAlgorithmFactory<T, G>(
    val fitnessFunction: (Genotype<T, G>) -> Double,
    val genotypeFactory: GenotypeFactory<T, G>,
) where G : Gene<T, G> {

    var populationSize: Int = DEFAULT_POPULATION_SIZE

    var survivalRate: Double = DEFAULT_SURVIVAL_RATE

    var ranker: IndividualRanker<T, G, Genotype<T, G>> = defaultRanker()

    var parentSelector: Selector<T, G, Genotype<T, G>> = defaultParentSelector<T, G>().getOrThrow()

    var survivorSelector: Selector<T, G, Genotype<T, G>> = defaultSurvivorSelector<T, G>().getOrThrow()

    val listeners: MutableList<ListenerFactory<T, G>> = defaultListenerFactories()

    val limits: MutableList<
            Limit<
                    T,
                    G,
                    Genotype<T, G>,
                    GeneticEvolutionState<T, G>,
                    EvolutionListener<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>
                    >
            > = defaultLimits()

    var evaluator: EvaluationExecutorFactory<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> =
        defaultEvaluator<T, G>()

    var interceptor: EvolutionInterceptor<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> = TODO()

    fun make(): GeneticAlgorithm<T, G, EvolutionListener<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>> =
        TODO()

    private fun makePopulationConfig(): PopulationConfig<T, G> = TODO()

    private fun makeSelectionConfig(): SelectionConfig<T, G> = TODO()

    private fun makeEvolutionConfig(): EvolutionConfiguration<
            T,
            G,
            Genotype<T, G>,
            GeneticEvolutionState<T, G>,
            EvolutionListener<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>
            > = TODO()

    companion object {

        const val DEFAULT_POPULATION_SIZE = 100

        const val DEFAULT_SURVIVAL_RATE = 0.5

        fun <T, G> defaultRanker(): FitnessMaxRanker<T, G, Genotype<T, G>> where G : Gene<T, G> = FitnessMaxRanker()

        fun <T, G> defaultParentSelector(): Result<Selector<T, G, Genotype<T, G>>> where G : Gene<T, G> = runCatching {
            TournamentSelector()
        }

        fun <T, G> defaultSurvivorSelector(): Result<Selector<T, G, Genotype<T, G>>> where G : Gene<T, G> =
            runCatching { TournamentSelector() }

        fun <T, G> defaultListenerFactories(): MutableList<ListenerFactory<T, G>> where G : Gene<T, G> = mutableListOf()

        fun <T, G> defaultLimits(): MutableList<
                Limit<
                        T,
                        G,
                        Genotype<T, G>,
                        GeneticEvolutionState<T, G>,
                        EvolutionListener<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>
                        >
                > where G : Gene<T, G> = mutableListOf()

        fun <T, G> defaultEvaluator(): EvaluationExecutorFactory<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>
                where G : Gene<T, G> = EvaluationExecutorFactory()
    }
}
