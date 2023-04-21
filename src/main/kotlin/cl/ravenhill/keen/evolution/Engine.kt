/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.Core.EvolutionLogger.debug
import cl.ravenhill.keen.Core.EvolutionLogger.info
import cl.ravenhill.keen.Core.EvolutionLogger.trace
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.evolution.executors.ConstructorExecutor
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.evolution.executors.SequentialConstructor
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
import cl.ravenhill.keen.requirements.CollectionRequirement.NotBeEmpty
import cl.ravenhill.keen.requirements.DoubleRequirement.BeInRange
import cl.ravenhill.keen.requirements.IntRequirement
import cl.ravenhill.keen.requirements.IntRequirement.BePositive
import cl.ravenhill.keen.util.optimizer.FitnessMaximizer
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer
import cl.ravenhill.keen.util.statistics.Statistic
import cl.ravenhill.keen.util.statistics.StatisticCollector
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.runBlocking
import java.time.Clock
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.supplyAsync
import java.util.concurrent.Executor
import java.util.concurrent.ForkJoinPool.commonPool
import kotlin.math.roundToInt
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
    val statistics: List<Statistic<DNA, G>>,
    val executor: Executor,
    val evaluator: EvaluationExecutor<DNA, G>,
    val interceptor: EvolutionInterceptor<DNA, G>
) : Evolver<DNA, G> {

    // region : PROPERTIES  ------------------------------------------------------------------------
    var population: Population<DNA, G> by Delegates.observable(listOf()) { _, _, _ ->
        runBlocking { statistics.asFlow().collect { it.population = population } }
    }
    private var evolutionResult: EvolutionResult<DNA, G>
            by Delegates.observable(EvolutionResult(optimizer, listOf(), 0)) { _, _, new ->
                runBlocking { statistics.asFlow().collect { it.evolutionResult = new } }
            }

    var generation: Int = 0
        private set

    var steadyGenerations by Delegates.observable(0) { _, _, new ->
        runBlocking { statistics.asFlow().collect { it.steadyGenerations = new } }
    }
        private set

    var bestFitness: Double by Delegates.observable(Double.NaN) { _, old, new ->
        if (old == new) {
            steadyGenerations++
        } else {
            steadyGenerations = 0
        }
    }
        private set

    /**
     * The fittest individual of the current generation.
     */
    private var fittest: Phenotype<DNA, G>? by Delegates.observable(null) { _, _, _ ->
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
            EvolutionStart.empty<DNA, G>().apply { debug { "Started an empty evolution." } }
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
        statistics.stream().parallel()
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
     * @see evolutionStart
     * @see evaluate
     * @see selectOffspring
     * @see selectSurvivors
     * @see alter
     * @see EvolutionResult
     */
    fun evolve(start: EvolutionStart<DNA, G>) = runBlocking {
        val initTime = clock.millis()
        // (1) The starting state of the evolution is pre-processed (if no method is hooked to
        // pre-process, it defaults to the identity function (EvolutionStart)
        trace { "Pre-processing evolution start." }
        val interceptedStart = interceptor.before(start)
        // (2) The population is created from the starting state
        trace { "Creating population." }
        val evolution = evolutionStart(interceptedStart)
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
        val pop = evaluate(EvolutionStart(nextPopulation, generation), true)
        evolutionResult = EvolutionResult(optimizer, pop, ++generation)
        fittest = evolutionResult.best
        // (9) The result of the evolution is post-processed
        trace { "Post-processing evolution result." }
        val afterResult = interceptor.after(evolutionResult)
        statistics.asFlow().collect { it.generationTimes.add(clock.millis() - initTime) }
        afterResult
    }

    /**
     * Creates the initial population of the evolution.
     *
     * @param start the starting state of the evolution at this generation.
     * @return the initial population of the evolution.
     */
    private fun evolutionStart(start: EvolutionStart<DNA, G>) =
        if (start.population.isEmpty()) {
            info { "Initial population is empty, creating a new one." }
            val generation = start.generation
            val individuals =
                start.population.asSequence() + generateSequence { genotype.make() }
                    .map { Phenotype(it, generation) }
            EvolutionStart(
                individuals.take(populationSize).toList(),
                generation
            ).also {
                info { "Created a new population." }
                debug { "Generation: ${it.generation}" }
            }
        } else {
            debug { "Initial population is not empty, using it." }
            start
        }

    /**
     * Evaluates the fitness of the population.
     *
     * @param evolution the current state of the evolution.
     * @param force if true, the fitness will be evaluated even if it has already been evaluated.
     * @return the evaluated population.
     */
    private fun evaluate(evolution: EvolutionStart<DNA, G>, force: Boolean = false) =
        evaluator(evolution.population, force).also {
            enforce {
                "Evaluated population size [${it.size}] doesn't " +
                        "match expected population size [$populationSize]" {
                            populationSize should IntRequirement.BeEqualTo(it.size)
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
            (offspringFraction * populationSize).roundToInt(),
            optimizer
        ).also {
            statistics.stream().parallel()
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
            ((1 - offspringFraction) * populationSize).roundToInt(),
            optimizer
        ).also {
            statistics.stream().parallel()
                .forEach { it.survivorSelectionTime.add(clock.millis() - initTime) }
        }
    }

    /**
     * Selects individuals from the population asynchronously.
     *
     * @param select the function that selects the individuals.
     * @return the selected individuals.
     *
     * @see supplyAsync
     */
    private fun asyncSelect(select: () -> Population<DNA, G>) = supplyAsync({
        select()
    }, executor)

    /**
     * Alters a population of individuals.
     *
     * @param population the population to alter.
     * @param evolution the current state of the evolution.
     * @return the altered population.
     *
     * @see CompletableFuture.thenApplyAsync
     */
    private fun alter(
        population: Population<DNA, G>,
        evolution: EvolutionStart<DNA, G>
    ): AltererResult<DNA, G> {
        debug { "Altering offspring." }
        val initTime = clock.millis()
        return alterer(population, evolution.generation)
            .also {
                statistics.stream().parallel()
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
                "survivorSelector: $survivorSelector " +
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
     * @property executor The executor used to run the evolution.
     * Default value is ``ForkJoinPool.commonPool()``.
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
     * @property statistics The statistics collectors used to collect data during the evolution.
     * @property constructorExecutor The [ConstructorExecutor] used to create individuals.
     */
    class Builder<DNA, G : Gene<DNA, G>>(
        private val fitnessFunction: (Genotype<DNA, G>) -> Double,
        private val genotype: Genotype.Factory<DNA, G>
    ) {
        // region : Evolution parameters -----------------------------------------------------------
        var populationSize = 50
            set(value) = enforce {
                "Population size must be greater than 0" { value should BePositive }
            }.let { field = value }

        var limits: List<Limit> = listOf(GenerationCount(100))
            set(value) = enforce {
                "Limits cannot be empty" { value should NotBeEmpty }
            }.let { field = value }

        var optimizer: PhenotypeOptimizer<DNA, G> = FitnessMaximizer()

        val interceptor = EvolutionInterceptor.identity<DNA, G>()
        // endregion    ----------------------------------------------------------------------------

        // region : Execution -----------------------------------------------------------------------
        var executor: Executor = commonPool()

        var evaluator =
            EvaluationExecutor.Factory<DNA, G>().apply { creator = { SequentialEvaluator(it) } }

        var constructorExecutor: ConstructorExecutor<DNA> = SequentialConstructor()
        // endregion    ----------------------------------------------------------------------------

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
                "Offspring fraction must be in range [0, 1]" { value should BeInRange(0.0..1.0) }
            }.let { field = value }
        // endregion    ----------------------------------------------------------------------------

        var statistics: List<Statistic<DNA, G>> = listOf(StatisticCollector())

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
            statistics = statistics,
            executor = executor,
            evaluator = evaluator.creator(fitnessFunction),
            interceptor = interceptor
        )
    }
}
