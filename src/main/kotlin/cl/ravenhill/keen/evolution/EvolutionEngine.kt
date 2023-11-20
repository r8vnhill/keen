/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.constraints.ints.BeEqualTo
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.evolution.executors.SequentialEvaluator
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.operators.Alterer
import cl.ravenhill.keen.operators.AltererResult
import cl.ravenhill.keen.operators.CompositeAlterer
import cl.ravenhill.keen.operators.selector.Selector
import cl.ravenhill.keen.operators.selector.TournamentSelector
import cl.ravenhill.keen.util.ceil
import cl.ravenhill.keen.util.floor
import cl.ravenhill.keen.util.listeners.EvolutionListener
import cl.ravenhill.keen.util.optimizer.FitnessMaximizer
import cl.ravenhill.keen.util.optimizer.IndividualOptimizer

/**
 * The core class that drives the evolutionary algorithm process.
 *
 * This class encapsulates the genetic algorithm's logic to evolve a population of genotypes
 * towards better solutions using genetic operators such as selection, crossover, and mutation.
 * It employs a variety of customizable components to tailor the evolutionary process to
 * specific problem domains.
 *
 * The evolutionary process consists of evaluating the population, selecting individuals for reproduction,
 * creating offspring, applying genetic operators, and selecting survivors to form the next generation.
 * Listeners and interceptors can be attached to monitor and influence the evolution.
 *
 * @param DNA The data type that represents the genetic information of an individual.
 * @param G The type of gene that holds the genetic information, extending from [Gene].
 * @param genotypeFactory Factory to produce genotypes for initial population and offspring generation.
 * @param populationSize The number of individuals in the population.
 * @param survivalRate The proportion of the population to be replaced by offspring each generation.
 * @param selector Selection mechanism to choose individuals for reproduction.
 * @param offspringSelector Selection mechanism to choose which offspring to keep.
 * @param alterer Genetic operator to modify genotypes (e.g., mutation, crossover).
 * @param limits Conditions to terminate the evolution (e.g., number of generations, fitness threshold).
 * @param survivorSelector Selection mechanism to choose which existing individuals to keep.
 * @param optimizer Strategy to compare and optimize individual fitness values.
 * @param listeners Observers that react to evolution events and collect statistics.
 * @param evaluator Executor responsible for evaluating the fitness of individuals.
 * @param interceptor Hook that allows custom operations before and after evolution stages.
 *
 * @property generation The current generation count in the evolutionary process.
 *
 * @constructor Initializes the engine with the provided parameters.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 1.0.0
 * @version 2.0.0
 */
class EvolutionEngine<DNA, G : Gene<DNA, G>>(
    val genotypeFactory: Genotype.Factory<DNA, G>,
    val populationSize: Int,
    val survivalRate: Double,
    val selector: Selector<DNA, G>,
    val offspringSelector: Selector<DNA, G>,
    val alterer: Alterer<DNA, G>,
    val limits: List<Limit<DNA, G>>,
    val survivorSelector: Selector<DNA, G>,
    val optimizer: IndividualOptimizer<DNA, G>,
    override val listeners: MutableList<EvolutionListener<DNA, G>>,
    val evaluator: EvaluationExecutor<DNA, G>,
    val interceptor: EvolutionInterceptor<DNA, G>,
) : Evolver<DNA, G> {

    init {
        limits.forEach { it.engine = this }
    }

    // region : PROPERTIES  ------------------------------------------------------------------------

    private var evolutionResult: EvolutionResult<DNA, G> = EvolutionResult(optimizer, listOf(), 0)

    private var _generation: Int = 0

    override val generation: Int get() = _generation

    // endregion    --------------------------------------------------------------------------------

    /**
     * The entry point of the evolution process.
     *
     * @return an [EvolutionResult] containing the last generation of the evolution process.
     * @see [evolve]
     */
    override fun evolve(): EvolutionResult<DNA, G> {
        listeners.forEach { it.onEvolutionStart() }
        var evolution = EvolutionState.empty<DNA, G>()
        var result = EvolutionResult(optimizer, evolution.population, generation)
        while (limits.none { it(result.generation) }) { // While none of the limits are met
            result = evolve(evolution)
            evolution = result.next()
        }
        return result.also {
            listeners.forEach { it.onEvolutionFinished() }
        }
    }

    /**
     * The main method of the ``Engine``.
     *
     * This is the classical flow of a Genetic Algorithm (GA), the user is assumed to know the
     * basics of evolutionary programming to use this method, so no further explanation is provided
     * (but take note of the comments placed on the body of the method).
     *
     * @param start the starting state of the evolution at this generation.
     * @return  the result of advancing the population by one generation.
     *
     * @see startEvolution
     * @see evaluate
     * @see selectOffspring
     * @see selectSurvivors
     * @see alter
     * @see EvolutionResult
     */
    fun evolve(start: EvolutionState<DNA, G>): EvolutionResult<DNA, G> {
        listeners.forEach { it.onGenerationStarted(start.population) }
        // (1) The starting state of the evolution is pre-processed (if no method is hooked to
        // pre-process, it defaults to the identity function (EvolutionStart)
        val interceptedStart = interceptor.before(start)
        // (2) The population is created from the starting state
        val evolution = startEvolution(interceptedStart)
        // (3) The population's fitness is evaluated
        val evaluatedPopulation = evaluate(evolution)
        // (4) The offspring is selected from the evaluated population
        val offspring = selectOffspring(evaluatedPopulation)
        // (5) The survivors are selected from the evaluated population
        val survivors = selectSurvivors(evaluatedPopulation)
        // (6) The offspring is altered
        val alteredOffspring = alter(offspring)
        // (7) The altered offspring is merged with the survivors
        val nextPopulation = survivors.population + alteredOffspring.population
        // (8) The next population is evaluated
        val pop = evaluate(EvolutionState(generation, nextPopulation), true)
        evolutionResult = EvolutionResult(optimizer, pop.population, ++_generation)
        // (9) The result of the evolution is post-processed
        val afterResult = interceptor.after(evolutionResult)
        listeners.forEach { it.onGenerationFinished(pop.population) }
        return afterResult
    }

    /**
     * Initializes or continues the evolutionary process based on the given state.
     *
     * This function either starts the evolution from scratch or continues from an existing state.
     * If the population in the provided state is empty, it indicates the start of the evolution.
     * In this case, the function generates an initial population and commences the evolutionary process.
     * Otherwise, the evolution continues from the given state.
     *
     * ## Behavior:
     * - **Starting Evolution**: When the population in the state is empty, this function creates
     *   an initial population of individuals using the genotype factory. The newly created population
     *   is then used to form a new [EvolutionState], marking the start of the evolution process.
     *   During this process, it notifies listeners about the initialization start and completion.
     * - **Continuing Evolution**: If the state already contains a population, it implies the
     *   evolution is already underway, and the function simply returns the provided state without
     *   modifications.
     *
     * ## Usage:
     * This function is typically called within the evolutionary engine to either initiate or
     * continue the evolutionary process. It's an integral part of the overall evolution cycle.
     *
     * @param state The current state of the evolution. It contains the existing population and
     *   the current generation number.
     * @return An [EvolutionState] representing either the newly initialized state (with the
     *   initial population and the same generation number as provided) or the original
     *   state if the evolution is already underway.
     */
    fun startEvolution(state: EvolutionState<DNA, G>) = if (state.population.isEmpty()) {
        listeners.forEach { it.onInitializationStarted() }
        val generation = state.generation
        val individuals = generateSequence { genotypeFactory.make() }
            .map { Individual(it) }
            .take(populationSize)
            .toList()
        EvolutionState(
            generation,
            individuals
        ).also {
            listeners.forEach { it.onInitializationFinished() }
        }
    } else {
        state
    }


    /**
     * Evaluates the fitness of the population in the given evolutionary state.
     *
     * This function assesses the fitness of each individual in the population, which is a critical
     * aspect of the evolutionary process. The fitness evaluation is vital for subsequent selection,
     * crossover, and mutation processes as it determines the suitability of each individual to the problem.
     *
     * ## Behavior:
     * - **Population Size Constraint**: Ensures that the population size in the given state matches the
     *   expected population size, maintaining consistency in the evolutionary process.
     * - **Evaluation Process**: Conducts the fitness evaluation of the population. The evaluation is
     *   performed on all individuals if `force` is true; otherwise, it evaluates only the unevaluated individuals.
     * - **Post-Evaluation Constraints**: After evaluation, checks that the size of the evaluated
     *   population matches the expected size and that all individuals have been evaluated.
     *
     * ## Usage:
     * This function is generally invoked within the evolutionary engine in each generation of the evolutionary
     * process. It plays a key role in providing the necessary fitness data that guides the generation and
     * selection of new populations.
     *
     * @param state The current state of the evolution, containing the population to be evaluated.
     * @param force A boolean flag indicating whether to force re-evaluation of the entire population.
     *   If `false`, only unevaluated individuals are evaluated. If `true`, all individuals
     *   are re-evaluated.
     * @return An updated [EvolutionState] with the evaluated population and the current generation number.
     * @throws CompositeException If constraints regarding population size or individual evaluation are not met.
     *
     * @see EvolutionState The state encapsulating the current population and generation number.
     * @see Individual.isEvaluated Checks if an individual has already been evaluated.
     * @see CollectionConstraintException The exception stored in the [CompositeException] thrown by this function.
     */
    @Throws(CompositeException::class)
    fun evaluate(
        state: EvolutionState<DNA, G>,
        force: Boolean = false,
    ): EvolutionState<DNA, G> {
        constraints {
            "Population size must be the same as the expected population size" {
                state.population must HaveSize(populationSize)
            }
        }
        listeners.forEach { it.onEvaluationStarted() }
        val evaluated = evaluator(state.population, force).apply {
            constraints {
                "Evaluated population size [${size}] doesn't match expected population size [$populationSize]" {
                    populationSize must BeEqualTo(size)
                }
                "There are unevaluated individuals" {
                    constraint { all { individual -> individual.isEvaluated() } }
                }
            }
        }
        return EvolutionState(state.generation, evaluated)
    }

    /**
     * Selects offspring from the current population in a given evolutionary state.
     *
     * This function is pivotal in the genetic algorithm's process, as it determines which individuals
     * from the current population will contribute to the next generation. The selection is based on the
     * offspring selector ([offspringSelector]) configured in the engine, which typically selects individuals based
     * on their fitness, thereby influencing the genetic traits passed on to the next generation.
     *
     * ## Behavior:
     * - **Offspring Selection Notification**: Notifies all listeners that the offspring selection process
     *   has started, marking the beginning of the selection phase.
     * - **Offspring Selection Process**: Delegates the task of selecting offspring to the configured
     *   offspring selector. The number of offspring to be selected is calculated based on the survival rate
     *   and the total population size. This selection process is crucial for ensuring that desirable traits
     *   are passed on to the next generation.
     * - **Completion Notification**: Upon completing the selection process, listeners are notified that the
     *   offspring selection phase has finished.
     *
     * ## Usage:
     * The function is typically called within the evolutionary engine's loop to select the parents for the
     * next generation. This selection step is vital for the genetic algorithm's progression, as it influences
     * the direction and speed of evolution by determining which individuals will reproduce.
     *
     * @param state The current evolutionary state containing the population from which offspring are to be selected.
     * @return An [EvolutionState] representing the state after offspring selection, containing the selected
     *   offspring and maintaining the same generation number.
     * @see EvolutionEngine.offspringSelector The selector responsible for choosing offspring based on configured criteria.
     */
    fun selectOffspring(state: EvolutionState<DNA, G>): EvolutionState<DNA, G> {
        listeners.forEach { it.onOffspringSelectionStarted() }
        val selected = offspringSelector(state.population, ((1 - survivalRate) * populationSize).floor(), optimizer)
        listeners.forEach { it.onOffspringSelectionFinished() }
        return EvolutionState(state.generation, selected)
    }

    /**
     * Selects survivors from the current population within a given evolutionary state.
     *
     * This function is integral to the genetic algorithm's process, as it determines which individuals
     * from the current generation will survive to the next. Survivor selection is a critical step in
     * maintaining a healthy and fit population, ensuring the persistence of advantageous traits.
     * The selection is carried out based on the survivor selector ([survivorSelector]) configured in the engine,
     * which typically selects individuals based on their fitness and other criteria.
     *
     * ## Behavior:
     * - **Survivor Selection Notification**: Notifies all listeners that the survivor selection process
     *   has commenced, marking the start of this phase.
     * - **Survivor Selection Process**: The task of selecting survivors is delegated to the configured
     *   survivor selector. The number of survivors is calculated based on the survival rate and the total
     *   population size. This process is crucial to ensure that only the most fit individuals are retained
     *   for the next generation.
     * - **Completion Notification**: After the selection process, listeners are informed that the survivor
     *   selection phase has concluded.
     *
     * ## Usage:
     * This function is generally invoked within the evolutionary engine during the generational transition
     * phase. It plays a key role in determining which individuals will continue to the next generation,
     * influencing the genetic diversity and fitness of future populations.
     *
     * @param state The current evolutionary state, containing the population from which survivors are to be selected.
     * @return An [EvolutionState] representing the state after survivor selection, containing the selected
     *   survivors and maintaining the same generation number.
     * @see EvolutionEngine.survivorSelector The selector used for choosing survivors based on configured criteria.
     */
    fun selectSurvivors(state: EvolutionState<DNA, G>): EvolutionState<DNA, G> {
        listeners.forEach { it.onSurvivorSelectionStarted() }
        val selected = survivorSelector(state.population, (survivalRate * populationSize).ceil(), optimizer)
        listeners.forEach { it.onSurvivorSelectionFinished() }
        return EvolutionState(state.generation, selected)
    }

    /**
     * Alters a population of individuals.
     *
     * @param population the population to alter.
     * @param evolution the current state of the evolution.
     * @return the altered population.
     */
    fun alter(
        evolution: EvolutionState<DNA, G>,
    ): AltererResult<DNA, G> {
        listeners.forEach { it.onAlterationStarted() }
        val altered = alterer(evolution.population, evolution.generation)
        listeners.forEach { it.onAlterationFinished() }
        return altered
    }

    /**
     * Builder for the [EvolutionEngine] class.
     *
     * @param DNA The type of the DNA of the Genotype.
     * @property fitnessFunction the fitness function used to evaluate the fitness of the
     * population.
     * @property genotypeFactory the genotype factory used to create the initial population.
     * @property populationSize The size of the population.
     * It must be greater than 0.
     * Default value is 50.
     * @property limits The limits that will be used to stop the evolution.
     * Default value is ``listOf(GenerationCount(100))``.
     * @property optimizer The optimization strategy used to compare the fitness of the population.
     * @property evaluator The evaluator used to evaluate the fitness of the population.
     * Default value is ``ConcurrentEvaluator(fitnessFunction, executor)``.
     * @property interceptor The interceptor used to intercept the evolution process.
     * Default value is ``EvolutionInterceptor.identity()``.
     * @property selector The selector that will be used to select the individuals.
     * Default value is ``TournamentSelector(3)``.
     * @property offspringSelector The selector that will be used to select the offspring.
     * Default value is the same as the ``selector``.
     * @property survivorSelector The selector that will be used to select the survivors.
     * Default value is the same as the ``selector``.
     * @property survivalRate The fraction of the population that will be used to create
     * the offspring.
     * Default value is 0.6.
     * @property alterers The alterers that will be used to alter the population.
     * Default value is an empty list.
     * @property listeners The statistics collectors used to collect data during the evolution.
     */
    class Factory<DNA, G : Gene<DNA, G>>(
        val fitnessFunction: (Genotype<DNA, G>) -> Double,
        val genotypeFactory: Genotype.Factory<DNA, G>,
    ) {
        // region : Evolution parameters -----------------------------------------------------------
        var populationSize = 50
            set(value) = constraints {
                "Population size [$value] must be greater than 0" { value must BePositive }
            }.let { field = value }

        var limits: List<Limit<DNA, G>> = listOf(GenerationCount(100))
            set(value) = constraints {
                "Limits cannot be empty" { value mustNot BeEmpty }
            }.let { field = value }

        var optimizer: IndividualOptimizer<DNA, G> = FitnessMaximizer()

        var interceptor = EvolutionInterceptor.identity<DNA, G>()
        // endregion    ----------------------------------------------------------------------------

        // region : -== EXECUTION ==-
        var evaluator =
            EvaluationExecutor.Factory<DNA, G>()
                .apply { creator = { SequentialEvaluator(it) } }
        // endregion EXECUTION

        // region : Alterers -----------------------------------------------------------------------
        var alterers: List<Alterer<DNA, G>> = emptyList()

        /**
         * The "main" alterer, by default it is a [CompositeAlterer] that contains all the alterers
         * added to the builder.
         */
        private val alterer: Alterer<DNA, G>
            get() = CompositeAlterer(alterers)
        // endregion    ----------------------------------------------------------------------------

        // region : Selection ----------------------------------------------------------------------
        var selector: Selector<DNA, G> = TournamentSelector(3)
            set(value) {
                offspringSelector = value
                survivorSelector = value
                field = value
            }

        var survivorSelector = selector

        var offspringSelector = selector

        var survivalRate = 0.4
            set(value) = constraints {
                "Survival rate [$value] must be a valid probability" { value must BeInRange(0.0..1.0) }
            }.let { field = value }
        // endregion    ----------------------------------------------------------------------------

        var listeners = mutableListOf<EvolutionListener<DNA, G>>()

        fun make() = EvolutionEngine(
            genotypeFactory = genotypeFactory,
            populationSize = populationSize,
            survivalRate = survivalRate,
            selector = selector,
            offspringSelector = offspringSelector,
            alterer = alterer,
            limits = limits,
            survivorSelector = survivorSelector,
            optimizer = optimizer,
            listeners = listeners,
            evaluator = evaluator.creator(fitnessFunction),
            interceptor = interceptor
        )
    }
}
