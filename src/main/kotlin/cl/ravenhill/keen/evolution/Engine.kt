/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.operators.Alterer
import cl.ravenhill.keen.operators.CompositeAlterer
import cl.ravenhill.keen.operators.selector.Selector
import cl.ravenhill.keen.operators.selector.TournamentSelector
import cl.ravenhill.keen.util.optimizer.FitnessMaximizer
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer
import cl.ravenhill.keen.util.statistics.Statistic
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.validateAtLeast
import cl.ravenhill.keen.util.validateNotEmpty
import cl.ravenhill.keen.util.validatePredicate
import cl.ravenhill.keen.util.validateRange
import java.time.Clock
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.supplyAsync
import java.util.concurrent.Executor
import java.util.concurrent.ForkJoinPool.commonPool
import java.util.stream.Collectors
import java.util.stream.Stream
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
class Engine<DNA> private constructor(
    private val fitnessFunction: (Genotype<DNA>) -> Double,
    private val genotype: Genotype.Factory<DNA>,
    private val populationSize: Int,
    private val offspringFraction: Double,
    val selector: Selector<DNA>,
    private val offspringSelector: Selector<DNA>,
    private val alterer: Alterer<DNA>,
    private val limits: List<Limit>,
    val survivorSelector: Selector<DNA>,
    private val optimizer: PhenotypeOptimizer<DNA>,
    val statistics: List<Statistic<DNA>>,
    private val executor: Executor,
    val evaluator: Evaluator<DNA>,
    private val interceptor: EvolutionInterceptor<DNA>
) : Evolver<DNA> {

    // region : PROPERTIES  ------------------------------------------------------------------------
    var generation: Int by Delegates.observable(0) { _, _, new ->
        statistics.stream().parallel().forEach { it.generation = new }
    }
        private set

    var steadyGenerations by Delegates.observable(0) { _, _, new ->
        // TODO:
        statistics.stream().parallel().forEach { it.steadyGenerations = new }
    }
        private set

    var bestFitness: Double by Delegates.observable(Double.NaN) { _, old, new ->
        if (old == new) {
            steadyGenerations++
        } else {
            steadyGenerations = 0
        }
        statistics.stream().parallel().forEach { it.bestFitness = new }
    }
        private set

    /**
     * The fittest individual of the current generation.
     */
    private var fittest: Phenotype<DNA>? by Delegates.observable(null) { _, _, new ->
        statistics.stream().parallel().forEach { it.fittest = new }
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
    fun run(): EvolutionResult<DNA> {
        val initTime = clock.millis()
        var evolution = EvolutionStart.empty<DNA>()
        var result = EvolutionResult(optimizer, evolution.population, generation)
        while (limits.none { it(this) }) { // While none of the limits are met
            result = evolve(evolution)
            bestFitness = result.best?.fitness ?: Double.NaN
            evolution = result.next()
        }
        statistics.stream().parallel().forEach { it.evolutionTime = clock.millis() - initTime }
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
    override fun evolve(start: EvolutionStart<DNA>): EvolutionResult<DNA> {
        val initTime = clock.millis()
        // (1) The starting state of the evolution is pre-processed (if no method is hooked to
        // pre-process, it defaults to the identity function (EvolutionStart)
        val interceptedStart = interceptor.before(start)
        // (2) The population is created from the starting state
        val evolution = evolutionStart(interceptedStart)
        // (3) The population's fitness is evaluated
        val evaluatedPopulation = evaluate(evolution)
        // (4) The offspring is selected from the evaluated population
        val offspring = selectOffspring(evaluatedPopulation)
        // (5) The survivors are selected from the evaluated population
        val survivors = selectSurvivors(evaluatedPopulation)
        // (6) The offspring is altered
        val alteredOffspring = alter(offspring, evolution)
        // TODO: Filter population
        // (7) The altered offspring is merged with the survivors
        val nextPopulation = survivors.thenCombineAsync(
            alteredOffspring,
            { s, o -> s + o.population },
            executor
        )
        // (8) The next population is evaluated
        val pop = evaluate(EvolutionStart(nextPopulation.join(), generation++), true)
        val result = EvolutionResult(optimizer, pop, generation)
        fittest = result.best
        // (9) The result of the evolution is post-processed
        val afterResult = interceptor.after(result)
        statistics.stream().parallel().forEach { it.generationTimes.add(clock.millis() - initTime) }
        return afterResult
    }

    /**
     * Creates the initial population of the evolution.
     *
     * @param start the starting state of the evolution at this generation.
     * @return the initial population of the evolution.
     */
    private fun evolutionStart(start: EvolutionStart<DNA>) = if (start.population.isEmpty()) {
        val generation = start.generation
        val stream =
            Stream.concat(
                start.population.stream(),
                Stream.generate { genotype.make() }
                    .map { Phenotype(it, generation) }
            )
        EvolutionStart(
            stream.limit(populationSize.toLong())
                .collect(Collectors.toList()),
            generation
        )
    } else {
        start
    }

    /**
     * Evaluates the fitness of the population.
     *
     * @param evolution the current state of the evolution.
     * @param force if true, the fitness will be evaluated even if it has already been evaluated.
     * @return the evaluated population.
     */
    private fun evaluate(evolution: EvolutionStart<DNA>, force: Boolean = false) =
        if (force || evolution.isDirty) {
            evaluator(evolution.population).also {
                validatePredicate({ populationSize == it.size }) {
                    "Evaluated population size [${it.size}] doesn't match expected population " +
                            "size [$populationSize]"
                }
                validatePredicate({ it.all { phenotype -> phenotype.isEvaluated() } }) {
                    "There are unevaluated phenotypes"
                }
            }
        } else {
            evolution.population
        }

    /**
     * Selects (asynchronously) the offspring from the evaluated population.
     *
     * @param population the evaluated population.
     * @return the offspring.
     */
    private fun selectOffspring(population: Population<DNA>) =
        asyncSelect {
            val initTime = clock.millis()
            offspringSelector(
                population,
                (offspringFraction * populationSize).roundToInt(),
                optimizer
            ).also {
                statistics.stream().parallel()
                    .forEach { it.offspringSelectionTime.add(clock.millis() - initTime) }
            }
        }

    /**
     * Selects (asynchronously) the survivors from the evaluated population.
     *
     * @param population the evaluated population.
     * @return the survivors.
     */
    private fun selectSurvivors(population: List<Phenotype<DNA>>) =
        asyncSelect {
            val initTime = clock.millis()
            survivorSelector(
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
    private fun asyncSelect(select: () -> Population<DNA>) = supplyAsync({
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
        population: CompletableFuture<Population<DNA>>,
        evolution: EvolutionStart<DNA>
    ) = population.thenApplyAsync({
        val initTime = clock.millis()
        alterer(it, evolution.generation)
            .also {
                statistics.stream().parallel()
                    .forEach { stat -> stat.alterTime.add(clock.millis() - initTime) }
            }
    }, executor)

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
     *
     * @property fitnessFunction the fitness function used to evaluate the fitness of the
     *      population.
     * @property genotype the genotype factory used to create the initial population.
     *
     * @property populationSize The size of the population.
     *      It must be greater than 0.
     *      Default value is 50.
     * @property limits The limits that will be used to stop the evolution.
     *      Default value is ``listOf(GenerationCount(100))``.
     * @property optimizer The optimization strategy used to compare the fitness of the population.
     * @property executor The executor used to run the evolution.
     *     Default value is ``ForkJoinPool.commonPool()``.
     * @property evaluator The evaluator used to evaluate the fitness of the population.
     *      Default value is ``ConcurrentEvaluator(fitnessFunction, executor)``.
     * @property interceptor The interceptor used to intercept the evolution process.
     *     Default value is ``EvolutionInterceptor.identity()``.
     *
     * @property selector The selector that will be used to select the individuals.
     *      Default value is ``TournamentSelector(3)``.
     * @property offspringSelector The selector that will be used to select the offspring.
     *      Default value is the same as the ``selector``.
     * @property survivorSelector The selector that will be used to select the survivors.
     *      Default value is the same as the ``selector``.
     * @property offspringFraction The fraction of the population that will be used to create
     *     the offspring.
     *     Default value is 0.6.
     *
     * @property alterers The alterers that will be used to alter the population.
     *      Default value is an empty list.
     *
     * @property statistics The statistics collectors used to collect data during the evolution.
     */
    class Builder<DNA>(
        private val fitnessFunction: (Genotype<DNA>) -> Double,
        private val genotype: Genotype.Factory<DNA>,
    ) {
        // region : Evolution parameters -----------------------------------------------------------
        var populationSize = 50
            set(value) = value.validateAtLeast(1, "Population size").let { field = it }

        var limits: List<Limit> = listOf(GenerationCount(100))
            set(value) = value.validateNotEmpty { "Limits must be a non-empty list" }
                .let { field = it }

        var optimizer: PhenotypeOptimizer<DNA> = FitnessMaximizer()

        var executor: Executor = commonPool()

        var evaluator: Evaluator<DNA> = ConcurrentEvaluator(fitnessFunction, executor)

        val interceptor = EvolutionInterceptor.identity<DNA>()
        // endregion    ----------------------------------------------------------------------------

        // region : Alterers -----------------------------------------------------------------------
        var alterers: List<Alterer<DNA>> = emptyList()

        /**
         * The "main" alterer, by default it is a [CompositeAlterer] that contains all the alterers
         * added to the builder.
         */
        private val alterer: Alterer<DNA>
            get() = CompositeAlterer(alterers)
        // endregion    ----------------------------------------------------------------------------

        // region : Selection ----------------------------------------------------------------------
        var selector: Selector<DNA> = TournamentSelector(3)
            set(value) {
                offspringSelector = value
                field = value
            }

        var survivorSelector = selector

        var offspringSelector = selector

        var offspringFraction = 0.6
            set(value) = value.validateRange(0.0..1.0, "Offspring fraction").let { field = it }
        // endregion    ----------------------------------------------------------------------------

        var statistics: List<Statistic<DNA>> = listOf(StatisticCollector())

        fun build() = Engine(
            fitnessFunction = fitnessFunction,
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
            evaluator = evaluator,
            interceptor = interceptor
        )
    }
}
