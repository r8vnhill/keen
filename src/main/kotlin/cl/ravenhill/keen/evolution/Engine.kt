/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.evolution

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.CollectionRequirement.BeEmpty
import cl.ravenhill.enforcer.requirements.DoubleRequirement.BeInRange
import cl.ravenhill.enforcer.requirements.IntRequirement
import cl.ravenhill.enforcer.requirements.IntRequirement.BePositive
import cl.ravenhill.keen.Core.EvolutionLogger.debug
import cl.ravenhill.keen.Core.EvolutionLogger.info
import cl.ravenhill.keen.Core.EvolutionLogger.trace
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.evolution.executors.SequentialEvaluator
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.operators.Alterer
import cl.ravenhill.keen.operators.AltererResult
import cl.ravenhill.keen.operators.CompositeAlterer
import cl.ravenhill.keen.operators.selector.Selector
import cl.ravenhill.keen.operators.selector.TournamentSelector
import cl.ravenhill.keen.util.Pretty
import cl.ravenhill.keen.util.ceil
import cl.ravenhill.keen.util.floor
import cl.ravenhill.keen.util.listeners.EvolutionListener
import cl.ravenhill.keen.util.listeners.EvolutionSummary
import cl.ravenhill.keen.util.optimizer.FitnessMaximizer
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.runBlocking
import java.time.Clock
import kotlin.properties.Delegates

/**
 * Fundamental class of the library. It is the engine that will run the evolution process.
 *
 * @param DNA   The type of the DNA of the Genotype
 * @property genotype           The genotype that will be used to create the population
 * @property populationSize     The size of the population
 * @property selector           The selector that will be used to select the individuals
 * @property generation         The current generation
 * @property limits             The limits that will be used to stop the evolution
 * @property steadyGenerations  The number of generations that the fitness has not changed
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
    val optimizer: PhenotypeOptimizer<DNA, G>,
    val listeners: List<EvolutionListener<DNA, G>>,
    val evaluator: EvaluationExecutor<DNA, G>,
    val interceptor: EvolutionInterceptor<DNA, G>,
) : Evolver<DNA, G>, Pretty {

    // region : PROPERTIES  ------------------------------------------------------------------------
    var population: Population<DNA, G> by Delegates.observable(
        initialValue = listOf(),
        onChange = { _, _, _ -> listeners.forEach { it.population = population } })
        private set
    // TODO: Records para poder hacer un mejor seguimiento de la evolución [R8V]
    private var evolutionResult: EvolutionResult<DNA, G> by Delegates.observable(
        initialValue = EvolutionResult(optimizer, listOf(), 0),
        onChange = { _, _, new -> listeners.forEach { it.evolutionResult = new } })
    var generation: Int by Delegates.observable(
        initialValue = 0,
        onChange = { prop, old, new ->
            listeners.forEach { it.onGenerationShift(prop, old, new) }
        })
        private set

    var steadyGenerations by Delegates.observable(
        initialValue = 0,
        onChange = { _, _, new -> listeners.forEach { it.steadyGenerations = new } })
        private set

    var bestFitness: Double by Delegates.observable(
        initialValue = Double.NaN,
        onChange = { _, old, new ->
            if (old == new) {
                steadyGenerations++
            } else {
                steadyGenerations = 0
            }
        })
        private set

    /**
     * The fittest individual of the current generation.
     */
    private
    var fittest: Phenotype<DNA, G>? by Delegates.observable(null) { _, _, _ ->
    }

    /**
     * The clock that will be used to measure the time of the evolution.
     */
    private val clock = Clock.systemDefaultZone()
    // endregion    --------------------------------------------------------------------------------

    /**
     * The entry point of the evolution process.
     *
     * @return an [EvolutionResult] containing the last generation of the evolution process.
     * @see [evolve]
     */
    override fun evolve(): EvolutionResult<DNA, G> {
        val initTime = clock.millis()
        info { "Starting evolution process." }
        var evolution =
            EvolutionState.empty<DNA, G>().apply { debug { "Started an empty evolution." } }
        var result = EvolutionResult(optimizer, evolution.population, generation)
        debug { "Optimizer: ${result.optimizer}" }
//        debug { "Best: ${result.best}" }
        while (limits.none { it(this) }) { // While none of the limits are met
            result = evolve(evolution).apply {
                debug { "Generation: $generation" }
                debug { "Best: $best" }
            }
            bestFitness = result.best.fitness
            evolution = result.next()
        }
        listeners.stream().parallel()
            .forEach { it.evolutionTime = clock.millis() - initTime }
        info { "Evolution process finished" }
        return result
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
        listeners.forEach { it.onGenerationStarted(generation) }
        val initTime = clock.millis()
        // (1) The starting state of the evolution is pre-processed (if no method is hooked to
        // pre-process, it defaults to the identity function (EvolutionStart)
        trace { "Pre-processing evolution start." }
        val interceptedStart = interceptor.before(start)
        // (2) The population is created from the starting state
        trace { "Creating population." }
        val evolution = startEvolution(interceptedStart)
        // (3) The population's fitness is evaluated
        trace { "Evaluating population." }
        val evaluatedPopulation = evaluate(evolution)
        // (4) The offspring is selected from the evaluated population
        trace { "Selecting offspring." }
        val offspring = selectOffspring(evaluatedPopulation)
        // (5) The survivors are selected from the evaluated population
        trace { "Selecting survivors." }
        val survivors = selectSurvivors(evaluatedPopulation)
        // (6) The offspring is altered
        trace { "Altering offspring." }
        val alteredOffspring = alter(offspring, evolution)
        // (7) The altered offspring is merged with the survivors
        trace { "Merging offspring and survivors." }
        val nextPopulation = survivors + alteredOffspring.population
        // (8) The next population is evaluated
        trace { "Evaluating next population." }
        val pop = evaluate(EvolutionState(nextPopulation, generation), true)
        evolutionResult = EvolutionResult(optimizer, pop, ++generation)
        fittest = evolutionResult.best
        // (9) The result of the evolution is post-processed
        trace { "Post-processing evolution result." }
        val afterResult = interceptor.after(evolutionResult)
        listeners.asFlow().collect { it.generationTimes.add(clock.millis() - initTime) }
        listeners.forEach { it.onGenerationFinished() }
        afterResult
    }

    /**
     * Creates the initial population of the evolution.
     *
     * @param state the starting state of the evolution at this generation.
     * @return the initial population of the evolution.
     */
    private fun startEvolution(state: EvolutionState<DNA, G>) =
        if (state.population.isEmpty()) {
            info { "Initial population is empty, creating a new one." }
            val generation = state.generation
            val individuals =
                state.population.asSequence() + generateSequence { genotype.make() }
                    .map { Phenotype(it, generation) }
            EvolutionState(
                individuals.take(populationSize).toList(),
                generation
            ).also {
                info { "Created a new population." }
                debug { "Generation: ${it.generation}" }
            }
        } else {
            debug { "Initial population is not empty, using it." }
            state
        }

    /**
     * Evaluates the fitness of the population.
     *
     * @param evolution the current state of the evolution.
     * @param force if true, the fitness will be evaluated even if it has already been evaluated.
     * @return the evaluated population.
     */
    private fun evaluate(evolution: EvolutionState<DNA, G>, force: Boolean = false) =
        evaluator(evolution.population, force).also {
            enforce {
                "Evaluated population size [${it.size}] doesn't match expected population size [$populationSize]" {
                    populationSize must IntRequirement.BeEqualTo(it.size)
                }
                "There are unevaluated phenotypes" {
                    requirement { it.all { phenotype -> phenotype.isEvaluated() } }
                }
            }
        }


    /**
     * Selects (asynchronously) the offspring from the evaluated population.
     *
     * @param population the evaluated population.
     * @return the offspring.
     */
    private fun selectOffspring(population: Population<DNA, G>): Population<DNA, G> {
        debug { "Selecting offspring." }
        val initTime = clock.millis()
        return offspringSelector(
            population,
            (offspringFraction * populationSize).ceil(),
            optimizer
        ).also {
            listeners.stream().parallel()
                .forEach { it.offspringSelectionTime.add(clock.millis() - initTime) }
            debug { "Selected offspring." }
        }
    }

    /**
     * Selects (asynchronously) the survivors from the evaluated population.
     *
     * @param population the evaluated population.
     * @return the survivors.
     */
    private fun selectSurvivors(population: List<Phenotype<DNA, G>>): Population<DNA, G> {
        debug { "Selecting survivors." }
        val initTime = clock.millis()
        return survivorSelector(
            population,
            ((1 - offspringFraction) * populationSize).floor(),
            optimizer
        ).also {
            listeners.stream().parallel()
                .forEach { it.survivorSelectionTime.add(clock.millis() - initTime) }
        }
    }

    /**
     * Alters a population of individuals.
     *
     * @param population the population to alter.
     * @param evolution the current state of the evolution.
     * @return the altered population.
     */
    private fun alter(
        population: Population<DNA, G>,
        evolution: EvolutionState<DNA, G>,
    ): AltererResult<DNA, G> {
        debug { "Altering offspring." }
        val initTime = clock.millis()
        return alterer(population, evolution.generation)
            .also {
                listeners.stream().parallel()
                    .forEach { stat -> stat.alterTime.add(clock.millis() - initTime) }
            }

    }

    override fun toString() =
        "Engine { " +
                "populationSize: $populationSize, " +
                "genotype: $genotype, " +
                "selector: $selector, " +
                "alterer: $alterer, " +
                "optimizer: $optimizer, " +
                "survivorSelector: $survivorSelector, " +
                "evaluator: $evaluator, " +
                "interceptor: $interceptor, " +
                "limits: $limits " +
                "}"

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
            set(value) = enforce {
                "Population size must be greater than 0" { value must BePositive }
            }.let { field = value }

        var limits: List<Limit> = listOf(GenerationCount(100))
            set(value) = enforce {
                "Limits cannot be empty" { value mustNot BeEmpty }
            }.let { field = value }

        var optimizer: PhenotypeOptimizer<DNA, G> = FitnessMaximizer()

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
            set(value) = enforce {
                "Offspring fraction must be in range [0, 1]" { value must BeInRange(0.0..1.0) }
            }.let { field = value }
        // endregion    ----------------------------------------------------------------------------

        var listeners: List<EvolutionListener<DNA, G>> = listOf(EvolutionSummary())

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
