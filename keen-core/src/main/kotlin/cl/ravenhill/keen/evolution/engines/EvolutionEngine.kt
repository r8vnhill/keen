package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.jakt.ExperimentalJakt
import cl.ravenhill.jakt.Jakt
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.evolution.EvolutionInterceptor
import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.evolution.config.AlterationConfig
import cl.ravenhill.keen.evolution.config.EvolutionConfig
import cl.ravenhill.keen.evolution.config.PopulationConfig
import cl.ravenhill.keen.evolution.config.SelectionConfig
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.evolution.executors.SequentialEvaluator
import cl.ravenhill.keen.exceptions.EngineException
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.operators.alteration.Alterer
import cl.ravenhill.keen.operators.selection.Selector
import cl.ravenhill.keen.operators.selection.TournamentSelector
import cl.ravenhill.keen.ranking.FitnessMaxRanker
import cl.ravenhill.keen.ranking.IndividualRanker
import kotlin.math.ceil
import kotlin.math.floor

typealias GeneticAlgorithm<T, G> = EvolutionEngine<T, G>

/**
 * Implements the core engine of an evolutionary algorithm, handling the entire process of evolution.
 *
 * `EvolutionEngine` is the central component in an evolutionary algorithm. It orchestrates the evolutionary
 * process, managing the creation and evolution of individuals over generations based on genetic principles
 * like selection, crossover, and mutation.
 *
 * ## Usage:
 * Create an instance of `EvolutionEngine` with the necessary components and configuration to start and run
 * an evolutionary algorithm. The engine handles the creation, evaluation, and evolution of the population,
 * guided by the provided components and parameters.
 *
 * It is __recommended__ to use the Engine [Factory] to create instances of `EvolutionEngine`.
 *
 * ### Example:
 * ```kotlin
 * val engine = EvolutionEngine<MyDataType, MyGene>(
 *     populationConfig = PopulationConfig(
 *         genotypeFactory = /* Define genotype factory */,
 *         populationSize = 100
 *     ),
 *     selectionConfig = SelectionConfig(
 *         survivalRate = 0.5,
 *         parentSelector = /* Define parent selector */,
 *         survivorSelector = /* Define survivor selector */
 *     ),
 *     alterationConfig = AlterationConfig(
 *         alterers = /* Define genetic operators */
 *     ),
 *     evolutionConfig = EvolutionConfig(
 *         limits = /* Define termination conditions */,
 *         ranker = /* Define individual ranker */,
 *         listeners = /* Define evolution listeners */,
 *         evaluator = /* Define fitness evaluator */,
 *         interceptor = /* Define evolution interceptor */
 *     )
 * )
 *
 * // Running the evolutionary algorithm
 * val finalState = engine.evolve()
 * ```
 *
 * @param T The type of data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 * @param populationConfig Configuration parameters for the population, including the genotype factory and population
 *   size.
 * @param selectionConfig Configuration parameters for the selection phase, including the survival rate, parent
 *   selector, and survivor selector.
 * @param alterationConfig Configuration parameters for the alteration phase, including the alterers.
 * @param evolutionConfig Configuration parameters for the evolutionary process, including the limits, ranker,
 *   listeners, evaluator, and interceptor.
 * @property genotypeFactory A factory for creating genotypes.
 * @property populationSize The size of the population in each generation.
 * @property survivalRate The proportion of individuals that survive to the next generation.
 * @property parentSelector A selector for choosing parents from the population.
 * @property survivorSelector A selector for choosing survivors from the population.
 * @property alterers A list of genetic operators for altering the offspring.
 * @property limits A list of termination conditions for the evolutionary process.
 * @property evolutionConfig A ranker for ordering individuals based on their fitness.
 * @property listeners A list of listeners for monitoring and reacting to the evolution process.
 * @property evaluator An executor for evaluating the fitness of individuals.
 * @property interceptor An interceptor for modifying the evolution state before and after each phase.
 * @constructor Creates an instance of [EvolutionEngine] with the specified parameters and associates this engine with
 *   the provided [limits].
 */
class EvolutionEngine<T, G>(
    populationConfig: PopulationConfig<T, G>,
    selectionConfig: SelectionConfig<T, G>,
    alterationConfig: AlterationConfig<T, G>,
    evolutionConfig: EvolutionConfig<T, G>,
) : AbstractEvolutionaryAlgorithm<T, G>(evolutionConfig) where G : Gene<T, G> {

    val genotypeFactory: Genotype.Factory<T, G> = populationConfig.genotypeFactory
    val populationSize: Int = populationConfig.populationSize
    val survivalRate: Double = selectionConfig.survivalRate
    val parentSelector: Selector<T, G> = selectionConfig.parentSelector
    val survivorSelector: Selector<T, G> = selectionConfig.survivorSelector
    val alterers: List<Alterer<T, G>> = alterationConfig.alterers

    init {
        limits.forEach { it.engine = this }
        listeners.onEach { listener -> listener.ranker = this.evolutionConfig.ranker }
    }

    /**
     * Executes one iteration of the evolutionary process, progressing the evolution by one generation.
     *
     * This function encapsulates the core steps of an evolutionary algorithm's cycle. It manages the transition from
     * one generation to the next, ensuring that each phase of the evolutionary process is properly executed.
     *
     * ## Evolutionary Cycle:
     * 1. **Pre-Processing**: Applies any pre-processing steps to the initial state.
     * 2. **Population Initialization/Continuation**: Either initializes a new population or continues with the
     *   existing one.
     * 3. **Population Evaluation**: Assesses the fitness of each individual in the population.
     * 4. **Parent Selection**: Selects a subset of individuals from the evaluated population to act as parents for
     *   offspring.
     * 5. **Survivor Selection**: Chooses individuals from the evaluated population to continue to the next generation.
     * 6. **Offspring Alteration**: Applies genetic alterations (e.g., mutation, crossover) to the offspring.
     * 7. **Merging Offspring and Survivors**: Combines the altered offspring with the survivors to form the next
     *   generation's population.
     * 8. **Next Generation Evaluation**: Evaluates the fitness of the new generation.
     * 9. **Post-Processing**: Applies any post-processing steps to the final state.
     *
     * ## Event Notification:
     * The function notifies registered listeners at the start and end of each generation, providing hooks for external
     * monitoring and intervention in the evolutionary process.
     *
     * ## Usage:
     * This method is typically invoked in a loop within the evolutionary algorithm, with each call representing a
     * single evolutionary step (generation).
     *
     * ### Example:
     * ```kotlin
     * val engine = /* Create an instance of EvolutionEngine */
     * var currentState = /* Initial EvolutionState */
     * repeat(100) {
     *     currentState = engine.iterateGeneration(currentState)
     * }
     * ```
     * In this example, `iterateGeneration` is called in a loop to progress the evolution through 100 generations.
     * The state of the evolution is updated with each iteration, reflecting the new generation's state.
     *
     * @param state The current [EvolutionState] representing the progress of evolution.
     * @return An updated [EvolutionState] that represents the state of the evolution after one generation cycle.
     */
    override fun iterateGeneration(state: EvolutionState<T, G>): EvolutionState<T, G> {
        // Apply pre-processing to the state
        val interceptedStart = interceptor.before(state)
        // Initialize or continue population
        val initialPopulation = startEvolution(interceptedStart)
        // Evaluate population fitness
        val evaluatedPopulation = evaluatePopulation(initialPopulation)
        // Select parents for offspring production
        val parents = selectParents(evaluatedPopulation)
        // Select survivors for the next generation
        val survivors = selectSurvivors(evaluatedPopulation)
        // Alter offspring through genetic operations
        val offspring = alterOffspring(parents)
        // Merge offspring and survivors to form the next generation
        val nextPopulation = survivors.copy(population = survivors.population + offspring.population)
        // Evaluate the next generation
        val nextGeneration = evaluatePopulation(nextPopulation)
        // Apply post-processing to the final state
        val interceptedEnd = interceptor.after(nextGeneration)
        return interceptedEnd.copy(generation = interceptedEnd.generation + 1)
    }

    /**
     * Initiates or continues the evolution process based on the given evolutionary state.
     *
     * This function plays a pivotal role in the evolutionary algorithm, responsible for either starting the evolution
     * with an initial population or continuing with the provided evolutionary state. It ensures that the evolution
     * process begins with a valid state and a properly initialized population.
     *
     * ## Functionality:
     * - **Initialization Check**: Determines whether the given state requires initialization (i.e., if it represents
     *   the start of the evolutionary process).
     * - **Population Initialization**: If initialization is needed, generates an initial population using the provided
     *   genotype factory. Each genotype is wrapped in an Individual object.
     * - **Evolution Continuation**: If the state already contains a population, the function returns the same state,
     *   allowing the evolution process to continue from the current state.
     *
     * ## Event Notification:
     * The function notifies registered listeners at the start and end of the initialization phase (if initialization
     * occurs). This is crucial for tracking and reacting to the evolution lifecycle events.
     *
     * ## Usage:
     * This method is typically invoked at the beginning of the evolutionary process. It can also be used to resume
     * evolution from a specific state.
     *
     * ### Example:
     * ```kotlin
     * val engine = /* Create an instance of EvolutionEngine */
     * val initialState = EvolutionState.empty<MyDataType, MyGene>(ranker)
     * val nextState = engine.startEvolution(initialState)
     * ```
     * In this example, `startEvolution` is used to either initialize the population or continue evolution based on
     * `initialState`. The resulting `nextState` is ready for the next steps in the evolutionary cycle.
     *
     * @param state The current [EvolutionState] representing the evolution's progress. If the state has an empty
     *              population, it triggers population initialization.
     * @return An [EvolutionState] that is ready for the evolutionary process. If initialization occurred, this state
     *         contains a new population; otherwise, it returns the provided state.
     */
    fun startEvolution(state: EvolutionState<T, G>) = if (state.isEmpty()) {
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
     * Evaluates the fitness of each individual in the population of the given evolutionary state.
     *
     * This function is central to the fitness evaluation phase of an evolutionary algorithm. During this phase,
     * each individual in the population is assessed for its fitness, which is a measure of how well it performs
     * in the given problem context. The fitness evaluation is crucial for guiding the evolutionary process as it
     * determines which individuals are more likely to survive and reproduce.
     *
     * ## Constraints:
     * - The size of the population in the state must match the expected population size. This ensures that the
     *   evaluation process is applied to a complete and consistent population set.
     * - After evaluation, all individuals in the population must have a fitness value assigned. This is crucial
     *   for the subsequent phases of the evolutionary algorithm, which rely on fitness values for decision-making.
     *
     * ## Process:
     * 1. **Evaluation Start Notification**: Notifies all registered listeners that the fitness evaluation phase has
     *   started.
     * 2. **Fitness Evaluation**: Executes the fitness evaluation process for each individual in the population using
     *   the configured evaluation executor.
     * 3. **Evaluation End Notification**: Informs all registered listeners that the fitness evaluation phase has
     *   concluded.
     *
     * ## Usage:
     * This method is generally invoked internally within the evolutionary cycle, specifically after the initialization
     * or alteration phases. The evaluated population is then used in selection and other genetic operations.
     *
     * ### Example:
     * ```kotlin
     * val engine = /* Create an instance of EvolutionEngine */
     * val currentState = EvolutionState(/* ... */)
     * val nextState = engine.evaluatePopulation(currentState)
     * ```
     * In this example, `evaluatePopulation` is called to assess the fitness of each individual within the
     * `currentState`. The resulting `nextState` includes the evaluated population, ready for further processing
     * in the evolutionary cycle.
     *
     * @param state The current [EvolutionState] representing the evolution's progress, including the population to be
     *   evaluated for fitness.
     * @return An [EvolutionState] after the fitness evaluation process, containing the evaluated population.
     */
    fun evaluatePopulation(state: EvolutionState<T, G>): EvolutionState<T, G> {
        // Validate the size of the population before evaluation
        Jakt.constraints {
            "Population size must be the same as the expected population size." {
                state.population must HaveSize(populationSize)
            }
        }
        // Notify listeners at the start of the evaluation phase
        listeners.forEach { it.onEvaluationStarted(state) }
        // Conduct the fitness evaluation process
        val evaluated = evaluator(state).apply {
            // Validate the population after evaluation
            Jakt.constraints {
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
     * Selects parents from the current population in the given evolutionary state.
     *
     * This function forms a critical part of the parent selection phase in an evolutionary algorithm. During this
     * phase, individuals from the current population are chosen to be parents for the next generation. The selection
     * is guided by a specified strategy, often based on fitness, to ensure the propagation of beneficial traits.
     *
     * ## Process:
     * 1. **Parent Selection Start Notification**: Notifies all registered listeners that the parent selection phase
     *   has started.
     * 2. **Parent Selection**: Executes the parent selection process using the configured parent selector. The number
     *   of parents chosen is determined by the complement of the survival rate and the total population size.
     * 3. **Parent Selection End Notification**: Informs all registered listeners that the parent selection phase has
     *   concluded.
     *
     * ## Usage:
     * This method is typically invoked within the evolutionary cycle, specifically after the survival phase and before
     * the recombination or crossover phase. It identifies the subset of the population that will contribute to the
     * creation of the next generation through genetic operations like crossover.
     *
     * ### Example:
     * ```kotlin
     * val engine = /* Create an instance of EvolutionEngine */
     * val currentState = EvolutionState(/* ... */)
     * val nextState = engine.selectParents(currentState)
     * ```
     * In this example, `selectParents` is used to choose individuals from `currentState` who will act as parents for
     * generating offspring in the next generation (`nextState`).
     *
     * @param state The current [EvolutionState] representing the evolution's progress, including the population from
     *   which parents need to be selected.
     * @return An [EvolutionState] after the parent selection process, containing the selected parents.
     */
    fun selectParents(state: EvolutionState<T, G>): EvolutionState<T, G> {
        // Notify listeners at the start of the parent selection phase
        listeners.forEach { it.onParentSelectionStarted(state) }
        // Conduct the parent selection process
        val selected = parentSelector(state, floor((1 - survivalRate) * populationSize).toInt())
        // Notify listeners at the end of the parent selection phase
        listeners.forEach { it.onParentSelectionEnded(selected) }
        return selected
    }

    /**
     * Selects survivors from the current population in the given evolutionary state.
     *
     * This function is integral to the survival phase of an evolutionary algorithm, where individuals from the current
     * population are chosen to continue into the next generation. The selection is based on a survival strategy,
     * typically favoring individuals with higher fitness, to ensure the propagation of advantageous traits.
     *
     * ## Process:
     * 1. **Survivor Selection Start Notification**: Notifies all registered listeners that the survivor selection
     *   phase has begun.
     * 2. **Survivor Selection**: Executes the survivor selection process using the configured survivor selector. The
     *   number of survivors is determined by the survival rate and the total population size.
     * 3. **Survivor Selection End Notification**: Informs all registered listeners that the survivor selection phase
     *   has concluded.
     *
     * ## Usage:
     * This method is commonly called within the evolutionary cycle after the offspring generation phase. It ensures
     * that only a subset of the current population, deemed fit for survival, proceeds to the next generation.
     *
     * ### Example:
     * ```kotlin
     * val engine = /* Create an instance of EvolutionEngine */
     * val currentState = EvolutionState(/* ... */)
     * val nextState = engine.selectSurvivors(currentState)
     * ```
     * In this example, `selectSurvivors` is used to choose individuals from `currentState` who will survive and be
     * part of the next generation (`nextState`).
     *
     * @param state The current [EvolutionState] representing the progress of the evolution, including the population
     *   from which survivors need to be selected.
     * @return An [EvolutionState] after the survivor selection process, containing the population that survived.
     */
    fun selectSurvivors(state: EvolutionState<T, G>): EvolutionState<T, G> {
        // Notify listeners at the start of the survivor selection phase
        listeners.forEach { it.onSurvivorSelectionStarted(state) }
        // Conduct the survivor selection process
        val selected = survivorSelector(state, ceil(survivalRate * populationSize).toInt())
        // Notify listeners at the end of the survivor selection phase
        listeners.forEach { it.onSurvivorSelectionEnded(selected) }
        return selected
    }

    /**
     * Applies genetic alterations to the offspring in the given evolution state.
     *
     * This function is a crucial part of the evolutionary process, where genetic operators such as mutation and
     * crossover are applied to the offspring. These operations introduce genetic diversity and are essential for the
     * evolution of the population.
     *
     * ## Process:
     * 1. **Alteration Start Notification**: Notifies all registered listeners that the alteration phase has started.
     * 2. **Application of Alterers**: Sequentially applies each registered alterer (genetic operator) to the current
     *   state. These alterers modify the offspring's genotypes, thereby implementing the genetic operations.
     * 3. **Alteration End Notification**: Notifies all registered listeners that the alteration phase has ended.
     *
     * ## Usage:
     * This method is typically invoked internally during the evolutionary cycle, specifically after parent
     * selection and before the next generation is formed. It ensures that the offspring have gone through the
     * necessary genetic modifications before being considered for the next generation.
     *
     * ### Example:
     * ```kotlin
     * val engine = /* Create an instance of EvolutionEngine */
     * val currentState = EvolutionState(/* ... */)
     * val nextState = engine.alterOffspring(currentState)
     * ```
     * In this example, `alterOffspring` is called to apply genetic alterations to the offspring within `currentState`.
     * The resulting `nextState` contains the modified offspring, ready for the subsequent phases of the evolution
     * process.
     *
     * @param state The current [EvolutionState] representing the evolution's progress, including the population of
     *   offspring to be altered.
     * @return An [EvolutionState] with the altered offspring, marking the completion of the genetic alteration phase.
     */
    fun alterOffspring(state: EvolutionState<T, G>): EvolutionState<T, G> {
        // Notify listeners that the alteration phase has started
        listeners.forEach { it.onAlterationStarted(state) }
        // Apply each alterer to the current state
        val altered = alterers.fold(state) { acc, alterer -> alterer(acc, state.population.size) }
        // Notify listeners that the alteration phase has ended
        listeners.forEach { it.onAlterationEnded(altered) }
        return altered
    }

    /**
     * A factory class for creating instances of [EvolutionEngine] with configurable parameters.
     *
     * This factory provides a flexible and customizable way to instantiate an [EvolutionEngine]. Various aspects of the
     * evolutionary process, such as population size, survival rate, selectors, and genetic operators, can be configured
     * using this factory.
     *
     * ## Usage:
     * Create an instance of this factory, set the desired parameters, and then call [make] to create an
     * [EvolutionEngine].
     *
     * ### Example:
     * ```kotlin
     * val factory = Factory<MyDataType, MyGene>(
     *     fitnessFunction = { /* Define fitness function */ },
     *     genotypeFactory = /* Define genotype factory */
     * ).apply {
     *     populationSize = 100
     *     survivalRate = 0.5
     *     parentSelector = TournamentSelector(3)
     *     survivorSelector = TournamentSelector(3)
     *     alterers += RandomMutator()
     *     limits += GenerationLimit(100)
     * }
     *
     * val evolutionEngine = factory.make()
     * ```
     * In this example, an [EvolutionEngine] is configured and created using the `Factory`. Various parameters such
     * as population size, survival rate, and selectors are set to customize the evolutionary process.
     *
     * @param T The type of data encapsulated by the genes within the individuals' genotypes.
     * @param G The type of gene in the individuals' genotypes.
     * @property fitnessFunction A function that computes the fitness of a genotype.
     * @property genotypeFactory A factory for creating genotypes.
     * @property populationSize The size of the population in each generation. Defaults to [DEFAULT_POPULATION_SIZE].
     * @property survivalRate The proportion of the population to survive each generation. Defaults to
     *   [DEFAULT_SURVIVAL_RATE].
     * @property parentSelector A [Selector] used to choose individuals for producing offspring.
     * @property survivorSelector A [Selector] used to choose individuals that will survive to the next generation.
     * @property alterers A list of [Alterer] instances that perform genetic operations like mutation and crossover.
     * @property limits A list of [Limit] instances that define termination conditions for the evolution process.
     * @property ranker An [IndividualRanker] used to rank individuals in the population based on fitness.
     * @property listeners A list of [EvolutionListener] instances for monitoring evolution events.
     * @property evaluator An [EvaluationExecutor] for evaluating individuals' fitness.
     * @property interceptor An [EvolutionInterceptor] for intercepting and modifying the evolution state.
     */
    class Factory<T, G>(
        val fitnessFunction: (Genotype<T, G>) -> Double,
        val genotypeFactory: Genotype.Factory<T, G>,
    ) where G : Gene<T, G> {

        @OptIn(ExperimentalJakt::class)
        var populationSize: Int = DEFAULT_POPULATION_SIZE
            set(value) = Jakt.constraints {
                "Population size ($value) must be positive."(::EngineException) { value must BePositive }
            }.let { field = value }

        @OptIn(ExperimentalJakt::class)
        var survivalRate: Double = DEFAULT_SURVIVAL_RATE
            set(value) = Jakt.constraints {
                "Survival rate ($value) must be between 0 and 1."(::EngineException) {
                    value must BeInRange(0.0..1.0)
                }
            }.let { field = value }

        var parentSelector: Selector<T, G> = defaultParentSelector()

        var survivorSelector: Selector<T, G> = defaultSurvivorSelector()

        var alterers: MutableList<Alterer<T, G>> = defaultAlterers()

        var limits: MutableList<Limit<T, G>> = defaultLimits()

        var ranker: IndividualRanker<T, G> = defaultRanker()

        var listeners: MutableList<EvolutionListener<T, G>> = defaultListeners()

        var evaluator: EvaluationExecutor.Factory<T, G> = defaultEvaluator()

        var interceptor: EvolutionInterceptor<T, G> = defaultInterceptor()

        /**
         * Constructs and returns an instance of [EvolutionEngine] based on the current configuration of the factory.
         *
         * This method creates a new [EvolutionEngine] using the parameters set in the [Factory]. It allows for the
         * customization of various components of the evolutionary algorithm, including population size, selection
         * strategies, genetic operators, and more. The created [EvolutionEngine] is ready to be used for running
         * evolutionary algorithms.
         *
         * See the [Factory] class documentation for more information on the available configuration options and usage
         * examples.
         *
         * @return An instance of [EvolutionEngine] configured according to the factory's settings.
         */
        fun make() = EvolutionEngine(
            populationConfig = PopulationConfig(genotypeFactory, populationSize),
            selectionConfig = SelectionConfig(survivalRate, parentSelector, survivorSelector),
            alterationConfig = AlterationConfig(alterers),
            evolutionConfig = EvolutionConfig(
                limits, ranker, listeners, evaluator.creator(fitnessFunction), interceptor
            )
        )

        companion object {
            /**
             * Default population size for an evolutionary algorithm. Set to 50.
             */
            const val DEFAULT_POPULATION_SIZE = 50

            /**
             * Default survival rate for an evolutionary algorithm. Set to 0.4.
             */
            const val DEFAULT_SURVIVAL_RATE = 0.4

            fun <T, G> defaultParentSelector() where G : Gene<T, G> = TournamentSelector<T, G>()
            fun <T, G> defaultSurvivorSelector() where G : Gene<T, G> = TournamentSelector<T, G>()

            fun <T, G> defaultAlterers() where G : Gene<T, G> = mutableListOf<Alterer<T, G>>()

            fun <T, G> defaultLimits() where G : Gene<T, G> = mutableListOf<Limit<T, G>>()

            fun <T, G> defaultRanker() where G : Gene<T, G> = FitnessMaxRanker<T, G>()

            fun <T, G> defaultListeners() where G : Gene<T, G> = mutableListOf<EvolutionListener<T, G>>()

            fun <T, G> defaultEvaluator() where G : Gene<T, G> = EvaluationExecutor.Factory<T, G>().apply {
                creator = { SequentialEvaluator(it) }
            }

            fun <T, G> defaultInterceptor() where G : Gene<T, G> = EvolutionInterceptor.identity<T, G>()
        }
    }
}