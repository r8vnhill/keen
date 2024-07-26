package cl.ravenhill.keen.evolution.engines.factories

import cl.ravenhill.jakt.Jakt
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.evolution.EvolutionInterceptor
import cl.ravenhill.keen.evolution.config.AlterationConfig
import cl.ravenhill.keen.evolution.config.EvolutionConfig
import cl.ravenhill.keen.evolution.config.PopulationConfig
import cl.ravenhill.keen.evolution.config.SelectionConfig
import cl.ravenhill.keen.evolution.engines.GeneticAlgorithm
import cl.ravenhill.keen.evolution.engines.ListenLimitFactory
import cl.ravenhill.keen.evolution.engines.ListenerFactory
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.evolution.executors.SequentialEvaluator
import cl.ravenhill.keen.exceptions.EngineException
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.ListenLimit
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.operators.alteration.Alterer
import cl.ravenhill.keen.operators.selection.Selector
import cl.ravenhill.keen.operators.selection.TournamentSelector
import cl.ravenhill.keen.ranking.FitnessMaxRanker

/**
 * Factory type alias for creating `ListenLimit` instances.
 *
 * The `ListenLimitFactory` type alias defines a function type that constructs a `ListenLimit` instance using a
 * `ListenerConfiguration`. This allows for the creation of custom limits based on listener events and predicates.
 */
private typealias ListenLimitFactory<T, G> = (ListenerConfiguration<T, G>) -> ListenLimit<T, G, Individual<T, G>>

/**
 * Factory type alias for creating `EvolutionListener` instances.
 *
 * The `ListenerFactory` type alias defines a function type that constructs an `EvolutionListener` instance using a
 * `ListenerConfiguration`. This allows for the creation of custom listeners for the evolutionary algorithm.
 */
private typealias ListenerFactory<T, G> = (ListenerConfiguration<T, G>) -> EvolutionListener<T, G, Individual<T, G>>

/**
 * Factory class for creating `GeneticAlgorithm` instances.
 *
 * The `Factory` class provides a convenient way to configure and create instances of `GeneticAlgorithm`.
 *
 * ### Example:
 * ```
 * val factory = GeneticAlgorithm.Factory(fitnessFunction, genotypeFactory).apply {
 *     populationSize = 100
 *     survivalRate = 0.5
 *     // Additional configurations...
 * }
 * val geneticAlgorithm = factory.make()
 * ```
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @property fitnessFunction The function used to evaluate the fitness of the genotype.
 * @property genotypeFactory The factory used to create genotypes.
 * @property populationSize The size of the population. Default is [DEFAULT_POPULATION_SIZE].
 * @property survivalRate The rate at which individuals survive to the next generation. Default is
 *  [DEFAULT_SURVIVAL_RATE].
 * @property parentSelector The selector used to choose parents for reproduction. Default is a [TournamentSelector].
 * @property survivorSelector The selector used to choose survivors for the next generation. Default is a
 *  [TournamentSelector].
 * @property alterers The list of alterers used to modify the offspring. Default is [defaultAlterers].
 * @property limits The list of limit factories used to create stopping conditions for the evolutionary process.
 *  Default is [defaultLimits].
 * @property ranker The ranker used to order individuals in the population. Default is a [defaultRanker].
 * @property listeners The list of listener factories used to create listeners for the evolutionary process. Default
 *  is [defaultListeners].
 * @property evaluator The factory used to create the evaluator for the fitness function. Default is
 *  [defaultEvaluator].
 * @property interceptor The interceptor used to modify the evolution process. Default is [defaultInterceptor].
 * @constructor Creates an instance of `Factory` with the specified fitness function and genotype factory.
 */
class GeneticAlgorithmFactory<T, G>(
    val fitnessFunction: (Genotype<T, G>) -> Double,
    val genotypeFactory: Genotype.Factory<T, G>,
) where G : Gene<T, G> {

    var populationSize = DEFAULT_POPULATION_SIZE
        set(value) = Jakt.constraints {
            "Population size ($value) must be positive."(::EngineException) { value must BePositive }
        }.let { field = value }

    var survivalRate = DEFAULT_SURVIVAL_RATE
        set(value) = Jakt.constraints {
            "Survival rate ($value) must be between 0 and 1."(::EngineException) {
                value must BeInRange(0.0..1.0)
            }
        }.let { field = value }

    var parentSelector = defaultParentSelector<T, G>()

    var survivorSelector = defaultSurvivorSelector<T, G>()

    var alterers = defaultAlterers<T, G>()

    var limits = mutableListOf<(ListenerConfiguration<T, G>) -> ListenLimit<T, G, Individual<T, G>>>()

    var ranker = defaultRanker<T, G>()

    var listeners = defaultListeners<T, G>()

    var evaluator = defaultEvaluator<T, G>()

    var interceptor = defaultInterceptor<T, G>()

    /**
     * Creates a new instance of `GeneticAlgorithm` with the configured settings.
     *
     * @return A new instance of `GeneticAlgorithm`.
     */
    fun make() = GeneticAlgorithm(
        populationConfig = PopulationConfig(genotypeFactory, populationSize),
        selectionConfig = SelectionConfig(survivalRate, parentSelector, survivorSelector),
        alterationConfig = AlterationConfig(alterers),
        evolutionConfig = EvolutionConfig(
            limits.map { it(ListenerConfiguration(ranker = ranker)) },
            ranker,
            listeners.map { it(ListenerConfiguration(ranker = ranker)) },
            evaluator.creator(fitnessFunction),
            interceptor
        )
    )

    companion object {
        /**
         * Default population size for the genetic algorithm.
         *
         * This constant represents the default number of individuals in the population if not otherwise specified.
         * The default value is 50.
         */
        const val DEFAULT_POPULATION_SIZE = 50

        /**
         * Default survival rate for the genetic algorithm.
         *
         * This constant represents the default proportion of individuals that survive to the next generation if not
         * otherwise specified. The default value is 0.4.
         */
        const val DEFAULT_SURVIVAL_RATE = 0.4

        /**
         * Provides the default parent selector for the genetic algorithm.
         *
         * This function returns a default instance of [TournamentSelector], used to select parents for reproduction
         * in the genetic algorithm.
         *
         * @param T The type of the value held by the genes.
         * @param G The type of the gene, which must extend [Gene].
         * @return A default instance of `TournamentSelector`.
         */
        fun <T, G> defaultParentSelector(): Selector<T, G> where G : Gene<T, G> = TournamentSelector()

        /**
         * Provides the default survivor selector for the genetic algorithm.
         *
         * This function returns a default instance of [TournamentSelector], used to select survivors for the next
         * generation in the genetic algorithm.
         *
         * @param T The type of the value held by the genes.
         * @param G The type of the gene, which must extend [Gene].
         * @return A default instance of `TournamentSelector`.
         */
        fun <T, G> defaultSurvivorSelector(): Selector<T, G> where G : Gene<T, G> = TournamentSelector<T, G>()

        /**
         * Provides the default list of alterers for the genetic algorithm.
         *
         * This function returns a mutable list of alterers used to modify the offspring during the genetic
         * algorithm process. The list is empty by default.
         *
         * @param T The type of the value held by the genes.
         * @param G The type of the gene, which must extend [Gene].
         * @return A mutable list of alterers.
         */
        fun <T, G> defaultAlterers() where G : Gene<T, G> = mutableListOf<Alterer<T, G>>()

        /**
         * Creates a default list of limit factories for the evolutionary algorithm.
         *
         * The `defaultLimits` function provides a default implementation for creating an empty list of limit
         * factories. These factories can be used to generate `ListenLimit` instances that define stopping
         * conditions for the evolutionary process.
         *
         * @param T The type of the value held by the genes.
         * @param G The type of the gene, which must extend [Gene].
         * @return A mutable list of limit factories.
         */
        fun <T, G> defaultLimits() where G : Gene<T, G> = mutableListOf<ListenLimitFactory<T, G>>()

        /**
         * Provides the default ranker for the genetic algorithm.
         *
         * This function returns a default instance of `FitnessMaxRanker`, used to rank individuals based on their
         * fitness in the genetic algorithm.
         *
         * @param T The type of the value held by the genes.
         * @param G The type of the gene, which must extend [Gene].
         * @return A default instance of `FitnessMaxRanker`.
         */
        fun <T, G> defaultRanker() where G : Gene<T, G> = FitnessMaxRanker<T, G>()

        /**
         * Creates a default list of listener factories for the genetic algorithm.
         *
         * The `defaultListeners` function provides a default implementation for creating an empty list of listener
         * factories. These factories can be used to generate `EvolutionListener` instances that respond to various
         * events during the evolutionary process.
         *
         * @param T The type of the value held by the genes.
         * @param G The type of the gene, which must extend [Gene].
         * @return A mutable list of listener factories.
         */
        fun <T, G> defaultListeners() where G : Gene<T, G> = mutableListOf<ListenerFactory<T, G>>()

        /**
         * Provides the default evaluator factory for the genetic algorithm.
         *
         * This function returns a default instance of `EvaluationExecutor.Factory`, configured to use
         * `SequentialEvaluator` for evaluating individuals.
         *
         * @param T The type of the value held by the genes.
         * @param G The type of the gene, which must extend [Gene].
         * @return A default instance of `EvaluationExecutor.Factory`.
         */
        fun <T, G> defaultEvaluator() where G : Gene<T, G> = EvaluationExecutor.Factory<T, G>().apply {
            creator = { SequentialEvaluator(it) }
        }

        /**
         * Provides the default evolution interceptor for the genetic algorithm.
         *
         * This function returns a default instance of `EvolutionInterceptor` that performs no modifications to the
         * evolution state.
         *
         * @param T The type of the value held by the genes.
         * @param G The type of the gene, which must extend [Gene].
         * @return A default instance of `EvolutionInterceptor`.
         */
        fun <T, G> defaultInterceptor() where G : Gene<T, G> = EvolutionInterceptor.identity<T, G>()
    }
}