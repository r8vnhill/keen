/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.constraints.ints.BeEqualTo
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.evolution.executors.SequentialEvaluator
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.Population
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
import kotlinx.coroutines.runBlocking
import kotlin.properties.Delegates

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
 * @param genotype Factory to produce genotypes for initial population and offspring generation.
 * @param populationSize The number of individuals in the population.
 * @param offspringFraction The proportion of the population to be replaced by offspring each generation.
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
 * @property population The current population of individuals in the genetic algorithm.
 * @property generation The current generation count in the evolutionary process.
 * @property _steadyGenerations The number of generations without significant fitness improvement.
 * @property _bestFitness The fitness value of the best individual in the current generation.
 * @property fittest The individual with the highest fitness in the current generation.
 *
 * @constructor Initializes the engine with the provided parameters.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 1.0.0
 * @version 2.0.0
 */
class Engine<DNA, G : Gene<DNA, G>>(
    val genotype: Genotype.Factory<DNA, G>,
    val populationSize: Int,
    val offspringFraction: Double,
    val selector: Selector<DNA, G>,
    val offspringSelector: Selector<DNA, G>,
    val alterer: Alterer<DNA, G>,
    val limits: List<Limit>,
    val survivorSelector: Selector<DNA, G>,
    val optimizer: IndividualOptimizer<DNA, G>,
    val listeners: List<EvolutionListener<DNA, G>>,
    val evaluator: EvaluationExecutor<DNA, G>,
    val interceptor: EvolutionInterceptor<DNA, G>,
) : Evolver<DNA, G> {

    // region : PROPERTIES  ------------------------------------------------------------------------
    var population: Population<DNA, G> by Delegates.observable(
        initialValue = listOf(),
        onChange = { _, _, _ -> listeners.forEach { it.population = population } })
        private set

    // TODO: Records para poder hacer un mejor seguimiento de la evolución [R8V]
    private var evolutionResult: EvolutionResult<DNA, G> by Delegates.observable(
        initialValue = EvolutionResult(optimizer, listOf(), 0),
        onChange = { _, _, new -> listeners.forEach { it.evolutionResult = new } })
    private var _generation: Int by Delegates.observable(
        initialValue = 0,
        onChange = { prop, old, new ->
            listeners.forEach { it.onGenerationShift(prop, old, new) }
        }
    )

    override val generation: Int get() = _generation

    private var _steadyGenerations by Delegates.observable(
        initialValue = 0,
        onChange = { _, _, new -> listeners.forEach { it.steadyGenerations = new } })

    override val steadyGenerations: Int get() = _steadyGenerations

    private var _bestFitness: Double by Delegates.observable(
        initialValue = Double.NaN,
        onChange = { _, old, new ->
            if (old == new) {
                _steadyGenerations++
            } else {
                _steadyGenerations = 0
            }
        })

    override val bestFitness: Double get() = _bestFitness

    /**
     * The fittest individual of the current generation.
     */
    private
    var fittest: Individual<DNA, G>? by Delegates.observable(null) { _, _, _ ->
    }
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
        while (limits.none { it(this) }) { // While none of the limits are met
            result = evolve(evolution)
            _bestFitness = result.best.fitness
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
    fun evolve(start: EvolutionState<DNA, G>) = runBlocking {
        listeners.forEach { it.onGenerationStarted(generation, listOf<Individual<DNA, G>>()) }
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
        val alteredOffspring = alter(offspring, evolution)
        // (7) The altered offspring is merged with the survivors
        val nextPopulation = survivors + alteredOffspring.population
        // (8) The next population is evaluated
        val pop = evaluate(EvolutionState(nextPopulation, generation), true)
        evolutionResult = EvolutionResult(optimizer, pop, ++_generation)
        fittest = evolutionResult.best
        // (9) The result of the evolution is post-processed
        val afterResult = interceptor.after(evolutionResult)
        listeners.forEach { it.onGenerationFinished(pop) }
        afterResult
    }

    /**
     * Creates the initial population of the evolution.
     *
     * @param state the starting state of the evolution at this generation.
     * @return the initial population of the evolution.
     */
    fun startEvolution(state: EvolutionState<DNA, G>): EvolutionState<DNA, G> {
        return if (state.population.isEmpty()) {
            listeners.forEach { it.onInitializationStarted() }
            val generation = state.generation
            val individuals =
                state.population.asSequence() + generateSequence { genotype.make() }
                    .map { Individual(it) }
            EvolutionState(
                individuals.take(populationSize).toList(),
                generation
            ).also {
                listeners.forEach { it.onInitializationFinished() }
            }
        } else {
            state
        }
    }

    /**
     * Evaluates the fitness of the population.
     *
     * @param evolution the current state of the evolution.
     * @param force if true, the fitness will be evaluated even if it has already been evaluated.
     * @return the evaluated population.
     */
    fun evaluate(
        evolution: EvolutionState<DNA, G>,
        force: Boolean = false,
    ): Population<DNA, G> {
        listeners.forEach { it.onEvaluationStarted() }
        return evaluator(evolution.population, force).also {
            listeners.forEach { it.onEvaluationFinished() }
            constraints {
                "Evaluated population size [${it.size}] doesn't match expected population size [$populationSize]" {
                    populationSize must BeEqualTo(it.size)
                }
                "There are unevaluated individuals" {
                    constraint { it.all { individual -> individual.isEvaluated() } }
                }
            }
        }
    }


    /**
     * Selects (asynchronously) the offspring from the evaluated population.
     *
     * @param population the evaluated population.
     * @return the offspring.
     */
    fun selectOffspring(population: Population<DNA, G>): Population<DNA, G> {
        listeners.forEach { it.onOffspringSelectionStarted() }
        return offspringSelector(
            population,
            (offspringFraction * populationSize).ceil(),
            optimizer
        ).also {
            listeners.forEach { it.onOffspringSelectionFinished() }
        }
    }

    /**
     * Selects (asynchronously) the survivors from the evaluated population.
     *
     * @param population the evaluated population.
     * @return the survivors.
     */
    fun selectSurvivors(population: List<Individual<DNA, G>>): Population<DNA, G> {
        listeners.forEach { it.onSurvivorSelectionStarted() }
        return survivorSelector(
            population,
            ((1 - offspringFraction) * populationSize).floor(),
            optimizer
        ).also {
            listeners.forEach {
                it.onSurvivorSelectionFinished()
            }
        }
    }

    /**
     * Alters a population of individuals.
     *
     * @param population the population to alter.
     * @param evolution the current state of the evolution.
     * @return the altered population.
     */
    fun alter(
        population: Population<DNA, G>,
        evolution: EvolutionState<DNA, G>,
    ): AltererResult<DNA, G> {
        listeners.forEach { it.onAlterationStarted() }
        return alterer(population, evolution.generation)
            .also {
                listeners.forEach { it.onAlterationFinished() }
            }

    }

    /**
     * Builder for the [Engine] class.
     *
     * @param DNA The type of the DNA of the Genotype.
     * @property fitnessFunction the fitness function used to evaluate the fitness of the
     * population.
     * @property genotype the genotype factory used to create the initial population.
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
     * @property offspringFraction The fraction of the population that will be used to create
     * the offspring.
     * Default value is 0.6.
     * @property alterers The alterers that will be used to alter the population.
     * Default value is an empty list.
     * @property listeners The statistics collectors used to collect data during the evolution.
     */
    class Builder<DNA, G : Gene<DNA, G>>(
        private val fitnessFunction: (Genotype<DNA, G>) -> Double,
        private val genotype: Genotype.Factory<DNA, G>,
    ) {
        // region : Evolution parameters -----------------------------------------------------------
        var populationSize = 50
            set(value) = constraints {
                "Population size must be greater than 0" { value must BePositive }
            }.let { field = value }

        var limits: List<Limit> = listOf(GenerationCount(100))
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
                field = value
            }

        var survivorSelector = selector

        var offspringSelector = selector

        var offspringFraction = 0.6
            set(value) = constraints {
                "Offspring fraction must be in range [0, 1]" { value must BeInRange(0.0..1.0) }
            }.let { field = value }
        // endregion    ----------------------------------------------------------------------------

        var listeners = mutableListOf<EvolutionListener<DNA, G>>()

        fun build() = Engine(
            genotype = genotype,
            populationSize = populationSize,
            offspringFraction = offspringFraction,
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
