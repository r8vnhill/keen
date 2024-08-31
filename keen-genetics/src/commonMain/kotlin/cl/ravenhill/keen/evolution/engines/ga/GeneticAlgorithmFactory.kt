/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines.ga

import arrow.core.getOrElse
import cl.ravenhill.jakt.constrainedTo
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.evolution.EvolutionInterceptor
import cl.ravenhill.keen.evolution.config.AlterationConfiguration
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
import cl.ravenhill.keen.listeners.Listener
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.operators.alteration.Alterer
import cl.ravenhill.keen.operators.selection.Selector
import cl.ravenhill.keen.operators.selection.TournamentSelector
import cl.ravenhill.keen.ranking.FitnessMaxRanker
import cl.ravenhill.keen.ranking.IndividualRanker

/**
 * A type alias for a factory function that creates an `EvolutionListener` based on a given `ListenerConfiguration`.
 *
 * The `ListenerFactory` type alias defines a function signature that takes a `ListenerConfiguration` as input and
 * returns an `EvolutionListener`. This alias simplifies the function definition and improves code readability when
 * working with listener creation in genetic evolutionary algorithms.
 *
 * @param T The type of the value held by the features.
 * @param G The type of the gene, which must extend [Gene].
 */
private typealias ListenerFactory<T, G> =
            (ListenerConfiguration<T, G, Genotype<T, G>>) -> Listener

/**
 * A typealias for a factory function that creates a `Limit` condition in an evolutionary algorithm.
 *
 * The `LimitFactory` typealias simplifies the definition of a factory function responsible for generating [Limit]
 * conditions. These conditions are used to control the termination of the evolutionary process based on specific
 * criteria, such as a maximum number of generations, a target fitness level, or other custom conditions.
 *
 * @param T The type of value held by the features.
 * @param G The type of gene in the genotype.
 */
private typealias LimitFactory<T, G> =
            (ListenerConfiguration<T, G, Genotype<T, G>>) ->
        Limit<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>, Listener>

/**
 * A type alias for `GeneticPopulationConfiguration`, representing the configuration of a genetic population.
 *
 * The `PopulationConfig` type alias simplifies the reference to `GeneticPopulationConfiguration`, making it easier
 * to define and use population configurations in genetic evolutionary algorithms.
 *
 * @param T The type of the value held by the features.
 * @param G The type of the gene, which must extend [Gene].
 */
private typealias PopulationConfig<T, G> = GeneticPopulationConfiguration<T, G>

/**
 * A type alias for `SelectionConfiguration`, representing the configuration of the selection process in a genetic
 * evolutionary algorithm.
 *
 * The `SelectionConfig` type alias simplifies the reference to `SelectionConfiguration`, improving code readability
 * and making it easier to define and use selection configurations in genetic evolutionary algorithms.
 *
 * @param T The type of the value held by the features.
 * @param G The type of the gene, which must extend [Gene].
 */
private typealias SelectionConfig<T, G> = SelectionConfiguration<T, G, Genotype<T, G>>


class GeneticAlgorithmFactory<T, G>(
    private val fitnessFunction: (Genotype<T, G>) -> Double,
    private val genotypeFactory: GenotypeFactory<T, G>,
    private val initialState: GeneticEvolutionState<T, G>? = null,
) where G : Gene<T, G> {

    /**
     * Represents the size of the population in a genetic evolutionary algorithm.
     *
     * @throws CompositeException if the population size is less than or equal to 0.
     */
    var populationSize: Int = DEFAULT_POPULATION_SIZE
        set(value) {
            field = value.constrainedTo {
                "The population size must be greater than 0" { value must BePositive }
            }.getOrElse { throw it }
        }

    var survivalRate: Double = DEFAULT_SURVIVAL_RATE

    var ranker: IndividualRanker<T, G, Genotype<T, G>> = defaultRanker()

    var parentSelector: Selector<T, G, Genotype<T, G>> = defaultParentSelector<T, G>().getOrThrow()

    var survivorSelector: Selector<T, G, Genotype<T, G>> = defaultSurvivorSelector<T, G>().getOrThrow()

    val listeners: MutableList<ListenerFactory<T, G>> = defaultListenerFactories()

    var limits: MutableList<LimitFactory<T, G>> = defaultLimits()

    var alterers: MutableList<Alterer<T, G, Genotype<T, G>>> = defaultAlterers()

    var evaluator: EvaluationExecutorFactory<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> =
        defaultEvaluator<T, G>()

    var interceptor: EvolutionInterceptor<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> = defaultInterceptor()

    fun make(): GeneticAlgorithm<T, G, out EvolutionListener<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>> {
        val listenerConfiguration = ListenerConfiguration(ranker = ranker)
        return GeneticAlgorithm(
            makePopulationConfig(),
            makeSelectionConfig(),
            AlterationConfiguration(alterers),
            makeEvolutionConfig(listenerConfiguration)
        )
    }

    private fun makePopulationConfig(): PopulationConfig<T, G> =
        GeneticPopulationConfiguration(genotypeFactory, populationSize)

    private fun makeSelectionConfig(): SelectionConfig<T, G> = SelectionConfiguration(
        survivalRate,
        parentSelector,
        survivorSelector,
    )

    private fun makeEvolutionConfig(
        configuration: ListenerConfiguration<T, G, Genotype<T, G>>
    ): EvolutionConfiguration<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> = EvolutionConfiguration(
        limits = limits.map { it(configuration) },
        listeners = listeners.map { it(ListenerConfiguration()) },
        interceptor = interceptor,
        ranker = ranker,
        evaluator = evaluator.creator(fitnessFunction),
        initialState = initialState ?: GeneticEvolutionState.empty(ranker)
    )

    companion object {

        const val DEFAULT_POPULATION_SIZE = 100

        const val DEFAULT_SURVIVAL_RATE = 0.5

        fun <T, G> defaultRanker(): FitnessMaxRanker<T, G, Genotype<T, G>> where G : Gene<T, G> = FitnessMaxRanker()

        fun <T, G> defaultParentSelector(): Result<Selector<T, G, Genotype<T, G>>> where G : Gene<T, G> = runCatching {
            TournamentSelector()
        }

        fun <T, G> defaultSurvivorSelector(): Result<Selector<T, G, Genotype<T, G>>> where G : Gene<T, G> =
            runCatching { TournamentSelector() }

        fun <T, G> defaultAlterers(): MutableList<Alterer<T, G, Genotype<T, G>>> where G : Gene<T, G> = mutableListOf()

        fun <T, G> defaultListenerFactories(): MutableList<ListenerFactory<T, G>> where G : Gene<T, G> = mutableListOf()

        fun <T, G> defaultLimits(): MutableList<LimitFactory<T, G>> where G : Gene<T, G> = mutableListOf()

        fun <T, G> defaultEvaluator(): EvaluationExecutorFactory<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>
                where G : Gene<T, G> = EvaluationExecutorFactory()

        fun <T, G> defaultInterceptor(): EvolutionInterceptor<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>
                where G : Gene<T, G> = EvolutionInterceptor.identity()
    }
}
