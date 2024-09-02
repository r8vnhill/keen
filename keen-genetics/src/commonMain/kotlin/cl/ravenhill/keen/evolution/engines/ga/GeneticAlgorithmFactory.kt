/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines.ga

import arrow.core.getOrElse
import cl.ravenhill.jakt.constrainedTo
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.dsl.geneticAlgorithm
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
import cl.ravenhill.keen.ranking.SyncFitnessMaxRanker
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


/**
 * Factory class for constructing a genetic algorithm tailored to specific evolutionary scenarios.
 *
 * The `GeneticAlgorithmFactory` class provides a flexible and customizable framework for setting up a genetic algorithm
 * in evolutionary computation. It allows users to configure various aspects of the algorithm, such as population size,
 * selection strategies, genetic operators, and evolutionary constraints. The factory pattern enables the creation of
 * complex algorithms by encapsulating the configuration and initialization processes.
 *
 * ## Recommended Usage:
 * While `GeneticAlgorithmFactory` offers fine-grained control over the construction of genetic algorithms, it is
 * recommended to use the [geneticAlgorithm] DSL for creating genetic algorithms. The DSL provides a more concise and
 * user-friendly approach to configuring and initializing genetic algorithms, making the process easier and more
 * intuitive. **Note**: The `geneticAlgorithm` DSL internally utilizes the `GeneticAlgorithmFactory` to create and
 * configure the algorithm, offering the same flexibility with a more streamlined interface.
 *
 * ### Example Usage:
 * ```kotlin
 * // Define the fitness function
 * fun evaluateFitness(genotype: Genotype<MyType, MyGene>): Double { ... }
 * // Define the genotype factory
 * val genotypeFactory = genotypeOf { ... }
 * // Create a genetic algorithm factory
 * val factory = GeneticAlgorithmFactory(
 *     fitnessFunction = ::evaluateFitness,
 *     genotypeFactory = genotypeFactory
 * )
 * // Configure the genetic algorithm
 * factory.populationSize = ...
 * factory.ranker = ...
 * factory.parentSelector = ...
 * // Add other configurations...
 * val geneticAlgorithm = factory.make()
 * geneticAlgorithm.evolve()
 * ```
 *
 * @param T The type of the value held by the genes.
 * @param G The type of gene, which must extend [Gene].
 * @property fitnessFunction The function that evaluates the fitness of a genotype.
 * @property genotypeFactory The factory for generating the initial population of genotypes.
 * @property initialState An optional initial state for the genetic evolution.
 * @property survivalRate The proportion of individuals that survive to the next generation. The default value is
 *   [DEFAULT_SURVIVAL_RATE].
 * @property populationSize The size of the population in the genetic algorithm. The default value is
 *   [DEFAULT_POPULATION_SIZE].
 * @property ranker The ranker used to evaluate and rank individuals in the population. The default ranker is
 *   [defaultRanker].
 * @property parentSelector The selector used to choose parents for reproduction in the genetic algorithm. The default
 *   selector is [defaultParentSelector].
 * @property survivorSelector The selector used to choose individuals that will survive to the next generation. The
 *   default selector is [defaultSurvivorSelector].
 * @property listeners A list of listener factories used to create listeners for monitoring and responding to events in
 *   the genetic algorithm. The default list is empty.
 * @property limits A list of limit factories used to define stopping criteria for the genetic algorithm. The default
 *   list is empty.
 * @property alterers A list of alterers used to introduce genetic variation into the population. The default list is
 *   empty.
 * @property evaluator The evaluator factory used to create an evaluation executor for fitness evaluations. The default
 *   evaluator factory is [defaultEvaluator].
 * @property interceptor The interceptor used to inject custom behavior before and after key operations in the genetic
 *   algorithm. The default interceptor is [defaultInterceptor].
 */
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

    var evaluator: EvaluationExecutorFactory<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> = defaultEvaluator()

    var interceptor: EvolutionInterceptor<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> = defaultInterceptor()

    /**
     * Constructs and returns a fully configured genetic algorithm instance.
     *
     * The `make()` function is responsible for assembling the various components and configurations into a complete
     * [GeneticAlgorithm] instance. This method gathers the population configuration, selection configuration,
     * alteration configuration, and evolution configuration into a cohesive structure that defines how the genetic
     * algorithm will operate.
     *
     * ### Example Usage:
     * ```kotlin
     * val geneticAlgorithm = factory.make()
     * geneticAlgorithm.evolve()
     * ```
     *
     * @return A `GeneticAlgorithm` instance configured with the provided settings and ready to be executed.
     */
    fun make(): GeneticAlgorithm<T, G, out EvolutionListener<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>> {
        // Create a configuration for the listeners, passing the ranker to it.
        val listenerConfiguration = ListenerConfiguration(ranker = ranker)

        // Return a fully configured GeneticAlgorithm instance.
        return GeneticAlgorithm(
            makePopulationConfig(),           // Set up population configuration.
            makeSelectionConfig(),            // Set up selection configuration.
            AlterationConfiguration(alterers), // Set up alteration configuration.
            makeEvolutionConfig(listenerConfiguration) // Set up evolution configuration.
        )
    }

    /**
     * Creates and returns the configuration for the population in the genetic algorithm.
     *
     * The `makePopulationConfig()` function prepares the population configuration by specifying the genotype factory
     * and the size of the population. This configuration is essential for generating the initial population of
     * individuals in the genetic algorithm.
     *
     * @return A `PopulationConfig` instance containing the genotype factory and population size.
     */
    private fun makePopulationConfig(): PopulationConfig<T, G> =
        GeneticPopulationConfiguration(genotypeFactory, populationSize)

    /**
     * Creates and returns the configuration for selection operations in the genetic algorithm.
     *
     * The `makeSelectionConfig()` function sets up the selection configuration by defining the survival rate and the
     * selectors used for both parent selection and survivor selection. This configuration controls how individuals are
     * chosen to reproduce and survive in each generation.
     *
     * @return A `SelectionConfig` instance containing the selection strategies for the genetic algorithm.
     */
    private fun makeSelectionConfig(): SelectionConfig<T, G> = SelectionConfiguration(
        survivalRate,      // The rate at which individuals survive to the next generation.
        parentSelector,    // The strategy used for selecting parents.
        survivorSelector,  // The strategy used for selecting survivors.
    )

    /**
     * Creates and returns the configuration for the evolution process in the genetic algorithm.
     *
     * The `makeEvolutionConfig()` function assembles the evolution configuration, which includes the limits, listeners,
     * interceptor, ranker, evaluator, and the initial state of the algorithm. This comprehensive configuration defines
     * the overall behavior and lifecycle of the genetic algorithm.
     *
     * @param configuration A `ListenerConfiguration` instance used to configure listeners with the current ranker.
     * @return An `EvolutionConfiguration` instance containing all the settings required for the genetic evolution
     *   process.
     */
    private fun makeEvolutionConfig(
        configuration: ListenerConfiguration<T, G, Genotype<T, G>>
    ): EvolutionConfiguration<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> = EvolutionConfiguration(
        limits = limits.map { it(configuration) },  // Apply each limit to the configuration.
        listeners = listeners.map { it(ListenerConfiguration()) },  // Apply each listener to the configuration.
        interceptor = interceptor,  // Set the evolution interceptor.
        ranker = ranker,  // Set the ranker used for evaluating fitness.
        evaluator = evaluator.creator(fitnessFunction),  // Create the evaluator with the fitness function.
        initialState = initialState ?: GeneticEvolutionState.empty(ranker)  // Set the initial state, defaulting to an empty state.
    )

    companion object {

        /**
         * The default population size used in the genetic algorithm if no specific size is provided.
         *
         * The `DEFAULT_POPULATION_SIZE` constant is set to 100, meaning that, by default, the genetic algorithm will
         * operate with a population of 100 individuals. This value can be overridden by configuring the
         * [populationSize] property of the `GeneticAlgorithmFactory`.
         */
        const val DEFAULT_POPULATION_SIZE = 100

        /**
         * The default survival rate used in the genetic algorithm.
         *
         * The `DEFAULT_SURVIVAL_RATE` constant is set to 0.5, indicating that, by default, 50% of the population will
         * survive into the next generation. This survival rate can be adjusted based on the specific needs of the
         * algorithm by configuring the [survivalRate] property of the `GeneticAlgorithmFactory`.
         */
        const val DEFAULT_SURVIVAL_RATE = 0.5

        /**
         * Provides a default ranker that maximizes fitness.
         *
         * The `defaultRanker` function returns a `FitnessMaxRanker`, which is used to rank individuals in the
         * population based on their fitness. The ranker prioritizes individuals with higher fitness values, making them
         * more likely to be selected for reproduction. This ranker can be overridden by specifying a different ranker
         * in the genetic algorithm configuration.
         *
         * @param T The type of value held by the genes.
         * @param G The type of gene, which must extend [Gene].
         * @return A `FitnessMaxRanker` instance.
         */
        fun <T, G> defaultRanker() where G : Gene<T, G> =
            FitnessMaxRanker.async<T, G, Genotype<T, G>>()

        /**
         * Provides a default parent selector using tournament selection.
         *
         * The `defaultParentSelector` function returns a `Result` containing a `TournamentSelector`, which is commonly
         * used to select parents for reproduction in a genetic algorithm. Tournament selection involves randomly
         * choosing a set of individuals and selecting the best one based on fitness. This selector can be replaced with
         * other selection strategies by configuring the `parentSelector` property.
         *
         * @param T The type of value held by the genes.
         * @param G The type of gene, which must extend [Gene].
         * @return A `Result` containing a `TournamentSelector` instance.
         */
        fun <T, G> defaultParentSelector(): Result<Selector<T, G, Genotype<T, G>>> where G : Gene<T, G> = runCatching {
            TournamentSelector()
        }

        /**
         * Provides a default survivor selector using tournament selection.
         *
         * The `defaultSurvivorSelector` function returns a `Result` containing a `TournamentSelector`, which is used to
         * select individuals that will survive to the next generation. The selection is based on fitness, ensuring that
         * the fittest individuals have a higher chance of survival. This selector can be overridden by configuring the
         * `survivorSelector` property.
         *
         * @param T The type of value held by the genes.
         * @param G The type of gene, which must extend [Gene].
         * @return A `Result` containing a `TournamentSelector` instance.
         */
        fun <T, G> defaultSurvivorSelector(): Result<Selector<T, G, Genotype<T, G>>> where G : Gene<T, G> = runCatching {
            TournamentSelector()
        }

        /**
         * Provides a default list of alterers for genetic variation.
         *
         * The `defaultAlterers` function returns a mutable list of `Alterer` instances, which are responsible for
         * introducing genetic variation through operations like mutation and crossover. By default, this list is empty,
         * but alterers can be added by configuring the `alterers` property.
         *
         * @param T The type of value held by the genes.
         * @param G The type of gene, which must extend [Gene].
         * @return A mutable list of `Alterer` instances.
         */
        fun <T, G> defaultAlterers(): MutableList<Alterer<T, G, Genotype<T, G>>> where G : Gene<T, G> = mutableListOf()

        /**
         * Provides a default list of listener factories.
         *
         * The `defaultListenerFactories` function returns a mutable list of `ListenerFactory` instances, which are used
         * to create listeners that monitor and respond to various events during the genetic algorithm's execution. By
         * default, this list is empty, but custom listeners can be added by configuring the `listeners` property.
         *
         * @param T The type of value held by the genes.
         * @param G The type of gene, which must extend [Gene].
         * @return A mutable list of `ListenerFactory` instances.
         */
        fun <T, G> defaultListenerFactories(): MutableList<ListenerFactory<T, G>> where G : Gene<T, G> = mutableListOf()

        /**
         * Provides a default list of limit factories for evolutionary constraints.
         *
         * The `defaultLimits` function returns a mutable list of `LimitFactory` instances, which define constraints or
         * stopping criteria for the genetic algorithm. These limits can include maximum generation count, target
         * fitness, or time limits. By default, this list is empty, but custom limits can be added by configuring the
         * `limits` property.
         *
         * @param T The type of value held by the genes.
         * @param G The type of gene, which must extend [Gene].
         * @return A mutable list of `LimitFactory` instances.
         */
        fun <T, G> defaultLimits(): MutableList<LimitFactory<T, G>> where G : Gene<T, G> = mutableListOf()

        /**
         * Provides a default evaluation executor factory for fitness evaluation.
         *
         * The `defaultEvaluator` function returns an `EvaluationExecutorFactory`, which is responsible for evaluating
         * the fitness of individuals in the population. The factory can be customized to use different evaluation
         * strategies or executors, but by default, it uses a standard evaluation executor.
         *
         * @param T The type of value held by the genes.
         * @param G The type of gene, which must extend [Gene].
         * @return An `EvaluationExecutorFactory` instance.
         */
        fun <T, G> defaultEvaluator(): EvaluationExecutorFactory<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>
                where G : Gene<T, G> = EvaluationExecutorFactory()

        /**
         * Provides a default evolution interceptor for pre- and post-evolution operations.
         *
         * The `defaultInterceptor` function returns an `EvolutionInterceptor`, which allows for custom behavior to be
         * injected before or after the main evolutionary operations (e.g., selection, mutation, crossover). By default,
         * the identity interceptor is used, which performs no additional operations. Custom interceptors can be
         * provided to modify the evolutionary process as needed.
         *
         * @param T The type of value held by the genes.
         * @param G The type of gene, which must extend [Gene].
         * @return An `EvolutionInterceptor` instance.
         */
        fun <T, G> defaultInterceptor(): EvolutionInterceptor<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>
                where G : Gene<T, G> = EvolutionInterceptor.identity()
    }
}
