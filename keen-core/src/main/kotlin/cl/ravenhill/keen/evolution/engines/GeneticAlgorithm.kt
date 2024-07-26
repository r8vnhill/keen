package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.dsl.geneticAlgorithm
import cl.ravenhill.keen.evolution.EvolutionInterceptor
import cl.ravenhill.keen.evolution.config.AlterationConfig
import cl.ravenhill.keen.evolution.config.EvolutionConfig
import cl.ravenhill.keen.evolution.config.PopulationConfig
import cl.ravenhill.keen.evolution.config.SelectionConfig
import cl.ravenhill.keen.evolution.engines.GeneticAlgorithm.Factory
import cl.ravenhill.keen.evolution.engines.GeneticAlgorithm.Factory.Companion.DEFAULT_POPULATION_SIZE
import cl.ravenhill.keen.evolution.engines.GeneticAlgorithm.Factory.Companion.DEFAULT_SURVIVAL_RATE
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.evolution.executors.SequentialEvaluator
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.exceptions.EngineException
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.limits.ListenLimit
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.operators.alteration.Alterer
import cl.ravenhill.keen.operators.selection.Selector
import cl.ravenhill.keen.operators.selection.TournamentSelector
import cl.ravenhill.keen.ranking.FitnessMaxRanker
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Represents a genetic algorithm in the evolutionary computation framework.
 *
 * The `GeneticAlgorithm` class manages the evolutionary process, including population generation, fitness evaluation,
 * parent selection, survivor selection, and alteration of offspring. It extends the `AbstractEvolutionaryAlgorithm`
 * class and provides the necessary configurations and operations for running a genetic algorithm.
 *
 * It is **recommended** to use the [geneticAlgorithm] function or the [Factory] class to create instances of this
 * class.
 *
 * ## Usage:
 * This class is used to run genetic algorithms with specific configurations. The [Factory] class or the
 * [geneticAlgorithm] function should be used for creating instances, ensuring proper configuration.
 *
 * ### Example 1: Creating a Genetic Algorithm Manually (Not Recommended)
 * ```
 * val geneticAlgorithm = GeneticAlgorithm(
 *     populationConfig = PopulationConfig(genotypeFactory, populationSize),
 *     selectionConfig = SelectionConfig(survivalRate, parentSelector, survivorSelector),
 *     alterationConfig = AlterationConfig(alterers),
 *     evolutionConfig = EvolutionConfig(limits, ranker, listeners, evaluator, interceptor)
 * )
 * ```
 *
 * ### Example 2: Performing an Evolution Process
 *
 * ```
 * val geneticAlgorithm = geneticAlgorithm(fitnessFunction, genotypeFactory) {
 *    ...
 * }
 * val finalState = geneticAlgorithm.evolve()
 * ```
 *
 * ### Example 3: Performing a Single Evolution Step
 *
 * ```
 * val geneticAlgorithm = geneticAlgorithm(fitnessFunction, genotypeFactory) {
 *   ...
 * }
 * val state = geneticAlgorithm.iterateGeneration(EvolutionState(...))
 * ```
 *
 * ## References:
 * 1. Holland, John H. Adaptation in natural and artificial systems: an introductory analysis with applications to
 *  biology, control, and artificial intelligence. 1st MIT Press ed. Complex adaptive systems. Cambridge, Mass: MIT
 *  Press, 1992.
 * 2. Bergel, Alexandre. Agile Artificial Intelligence in Pharo: Implementing Neural Networks, Genetic Algorithms, and
 *  Neuroevolution. For Professionals by Professionals. New York: Apress, 2020.
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @property genotypeFactory The factory used to create genotypes.
 * @property populationSize The size of the population.
 * @property survivalRate The rate at which individuals survive to the next generation.
 * @property parentSelector The selector used to choose parents for reproduction.
 * @property survivorSelector The selector used to choose survivors for the next generation.
 * @property alterers The list of alterers used to modify the offspring.
 * @constructor Creates an instance of `GeneticAlgorithm` with the specified configurations.
 */
class GeneticAlgorithm<T, G>(
    populationConfig: PopulationConfig<T, G>,
    selectionConfig: SelectionConfig<T, G>,
    alterationConfig: AlterationConfig<T, G>,
    evolutionConfig: EvolutionConfig<T, G>,
) : AbstractGeneBasedEvolutionaryAlgorithm<T, G>(
    populationConfig,
    evolutionConfig,
    selectionConfig
) where G : Gene<T, G> {

    val alterers: List<Alterer<T, G>> = alterationConfig.alterers

    init {
        limits.forEach { it.engine = this }
    }

    /**
     * Starts the evolutionary process by initializing the population if it is empty.
     *
     * This method checks if the provided evolution state is empty. If it is, it generates an initial population,
     * notifies listeners about the start and end of the initialization process, and returns a new state with the
     * initialized population. If the state is not empty, it continues with the current state.
     *
     * @param state The current evolution state.
     * @return The updated evolution state, either with an initialized population or the current state if already
     *  populated.
     */
    override fun startEvolution(state: GeneticEvolutionState<T, G>) = if (state.isEmpty()) {
        // Notify listeners about the initialization start
        listeners.forEach { it.onInitializationStarted(state) }
        // Generate initial population
        val individuals = generateSequence { genotypeFactory.make() }
            .take(populationSize)
            .map { Individual(it) }
            .toList()
        // Create a new state with the initialized population
        state.copy(population = individuals).apply {
            // Notify listeners about the initialization end
            listeners.forEach { it.onInitializationEnded(this) }
        }
    } else {
        // If the state already has a population, continue with the current state
        state
    }

    /**
     * Evaluates the population within the given evolution state.
     *
     * This method performs the following steps:
     * 1. Validates that the size of the population matches the expected population size before evaluation.
     * 2. Notifies listeners at the start of the evaluation phase.
     * 3. Conducts the fitness evaluation process using the configured evaluator.
     * 4. Validates the population after evaluation to ensure that all individuals are evaluated and the size is
     *  consistent.
     * 5. Notifies listeners at the end of the evaluation phase.
     * 6. Returns the evaluated evolution state.
     *
     * @param state The current evolution state containing the population to be evaluated.
     * @return The updated evolution state with evaluated fitness values.
     */
    override fun evaluatePopulation(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G> {
        // Validate the size of the population before evaluation
        constraints {
            "Population size must be the same as the expected population size." {
                state.population must HaveSize(populationSize)
            }
        }
        // Notify listeners at the start of the evaluation phase
        listeners.forEach { it.onEvaluationStarted(state) }
        // Conduct the fitness evaluation process
        val evaluated = evaluator(state).apply {
            // Validate the population after evaluation
            constraints {
                "Evaluated population size must be the same as the expected population size." {
                    population must HaveSize(populationSize)
                }
                "There are unevaluated individuals in the population." {
                    constraint { population.all { individual -> individual.isEvaluated() } }
                }
            }
        }
        // Notify listeners at the end of the evaluation phase
        listeners.forEach { it.onEvaluationEnded(evaluated) }
        return evaluated
    }

    /**
     * Selects parents from the population within the given evolution state.
     *
     * This method performs the following steps:
     * 1. Notifies listeners at the start of the parent selection phase.
     * 2. Conducts the parent selection process using the configured parent selector.
     * 3. Notifies listeners at the end of the parent selection phase.
     * 4. Returns the updated evolution state with the selected parents.
     *
     * @param state The current evolution state containing the population to select parents from.
     * @return The updated evolution state with the selected parents.
     */
    override fun selectParents(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G> {
        // Notify listeners at the start of the parent selection phase
        listeners.forEach { it.onParentSelectionStarted(state) }
        // Conduct the parent selection process
        val selected = parentSelector(state, floor((1 - survivalRate) * populationSize).toInt()) {
            state.copy(population = it)
        }
        // Notify listeners at the end of the parent selection phase
        listeners.forEach { it.onParentSelectionEnded(selected) }
        return selected
    }

    /**
     * Selects survivors from the population within the given evolution state.
     *
     * This method performs the following steps:
     * 1. Notifies listeners at the start of the survivor selection phase.
     * 2. Conducts the survivor selection process using the configured survivor selector.
     * 3. Notifies listeners at the end of the survivor selection phase.
     * 4. Returns the updated evolution state with the selected survivors.
     *
     * @param state The current evolution state containing the population to select survivors from.
     * @return The updated evolution state with the selected survivors.
     */
    override fun selectSurvivors(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G> {
        // Notify listeners at the start of the survivor selection phase
        listeners.forEach { it.onSurvivorSelectionStarted(state) }
        // Conduct the survivor selection process
        val selected = survivorSelector(state, ceil(survivalRate * populationSize).toInt()) {
            state.copy(population = it)
        }
        // Notify listeners at the end of the survivor selection phase
        listeners.forEach { it.onSurvivorSelectionEnded(selected) }
        return selected
    }

    /**
     * Alters the offspring in the population within the given evolution state.
     *
     * This method performs the following steps:
     * 1. Notifies listeners that the alteration phase has started.
     * 2. Applies each alterer to the current state, altering the offspring.
     * 3. Notifies listeners that the alteration phase has ended.
     * 4. Returns the updated evolution state with the altered offspring.
     *
     * @param state The current evolution state containing the population to alter.
     * @return The updated evolution state with the altered offspring.
     */
    override fun alterOffspring(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G> {
        // Notify listeners that the alteration phase has started
        listeners.forEach { it.onAlterationStarted(state) }
        // Apply each alterer to the current state
        val altered = alterers.fold(state) { acc, alterer ->
            alterer(acc, state.population.size) {
                acc.copy(population = it)
            }
        }
        // Notify listeners that the alteration phase has ended
        listeners.forEach { it.onAlterationEnded(altered) }
        return altered
    }

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
     * @property alterers The list of alterers used to modify the offspring. Default is an empty list.
     * @property limits The list of limits used to control the evolution process. Default is an empty list.
     * @property limitFactories The list of limit factories used to create limits. Default is an empty list.
     * @property ranker The ranker used to order individuals in the population. Default is a [FitnessMaxRanker].
     * @property listeners The list of listeners used to monitor the evolution process. Default is an empty list.
     * @property listenerFactories The list of listener factories used to create listeners. Default is an empty list.
     * @property evaluator The factory used to create the evaluator for the fitness function. Default is a
     *  [SequentialEvaluator].
     * @property interceptor The interceptor used to modify the evolution process. Default is an identity interceptor.
     * @constructor Creates an instance of `Factory` with the specified fitness function and genotype factory.
     */
    class Factory<T, G>(
        val fitnessFunction: (Genotype<T, G>) -> Double,
        val genotypeFactory: Genotype.Factory<T, G>,
    ) where G : Gene<T, G> {

        var populationSize = DEFAULT_POPULATION_SIZE
            set(value) = constraints {
                "Population size ($value) must be positive."(::EngineException) { value must BePositive }
            }.let { field = value }

        var survivalRate = DEFAULT_SURVIVAL_RATE
            set(value) = constraints {
                "Survival rate ($value) must be between 0 and 1."(::EngineException) {
                    value must BeInRange(0.0..1.0)
                }
            }.let { field = value }

        var parentSelector = defaultParentSelector<T, G>()

        var survivorSelector = defaultSurvivorSelector<T, G>()

        var alterers = defaultAlterers<T, G>()

        @Deprecated("Use the 'limitFactories' property instead.")
        var limits = defaultLimits<T, G>()

        var limitFactories = mutableListOf<(ListenerConfiguration<T, G>) -> ListenLimit<T, G>>()

        var ranker = defaultRanker<T, G>()

        @Deprecated("Use the 'listenerFactories' property instead.")
        var listeners = defaultListeners<T, G>()

        var listenerFactories = mutableListOf<(ListenerConfiguration<T, G>) -> EvolutionListener<T, G>>()

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
                if (limitFactories.isEmpty()) limits else limitFactories.map {
                    it(ListenerConfiguration(ranker = ranker))
                },
                ranker,
                // This is meant to be removed in the future in favor of the listenerFactories property
                if (listenerFactories.isEmpty()) listeners else listenerFactories.map {
                    it(ListenerConfiguration(ranker = ranker))
                },
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
             * Provides the default list of limits for the genetic algorithm.
             *
             * This function returns a mutable list of limits used to control the evolutionary process in the genetic
             * algorithm. The list is empty by default.
             *
             * @param T The type of the value held by the genes.
             * @param G The type of the gene, which must extend [Gene].
             * @return A mutable list of limits.
             */
            fun <T, G> defaultLimits() where G : Gene<T, G> = mutableListOf<Limit<T, G>>()

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
             * Provides the default list of listeners for the genetic algorithm.
             *
             * This function returns a mutable list of listeners used to monitor and react to events during the genetic
             * algorithm process. The list is empty by default.
             *
             * @param T The type of the value held by the genes.
             * @param G The type of the gene, which must extend [Gene].
             * @return A mutable list of evolution listeners.
             */
            fun <T, G> defaultListeners() where G : Gene<T, G> = mutableListOf<EvolutionListener<T, G>>()

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
}
