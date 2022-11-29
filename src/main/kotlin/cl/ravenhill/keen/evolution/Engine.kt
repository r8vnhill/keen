/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.constraints.Constraint
import cl.ravenhill.keen.constraints.RetryConstraint
import cl.ravenhill.keen.evolution.streams.EvolutionStream
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
import kotlin.properties.Delegates


/**
 * Fundamental class of the library. It is the engine that will run the evolution process.
 *
 * @param DNA   The type of the DNA of the Genotype
 * @property genotype           The genotype that will be used to create the population
 * @property populationSize     The size of the population
 * @property selector           The selector that will be used to select the individuals
 * @property alterers           The alterers that will be used to alter the population
 * @property generation         The current generation
 * @property limits             The limits that will be used to stop the evolution
 * @property steadyGenerations  The number of generations that the fitness has not changed
 * @property fittest            The fittest individual of the current population
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
    private val optimizer: PhenotypeOptimizer,
    val statistics: List<Statistic<DNA>>,
    private val executor: Executor,
    val evaluator: Evaluator<DNA>,
    private val interceptor: EvolutionInterceptor<DNA>
) : Evolver<DNA> {

    init {
        // We need to set the genotype's fitness function to evolve the population
        genotype.fitnessFunction = fitnessFunction
    }

    // region : PROPERTIES  ------------------------------------------------------------------------
    var generation: Int by Delegates.observable(0) { _, _, new ->
//        statistics.stream().parallel().forEach { it.generation = new }
    }
        private set

    var steadyGenerations by Delegates.observable(0) { _, _, new ->
        statistics.stream().parallel().forEach { it.steadyGenerations = new }
    }
        private set

    var bestFitness: Double by Delegates.observable(0.0) { _, old, new ->
        if (old == new) {
            steadyGenerations++
        } else {
            steadyGenerations = 0
        }
//        statistics.stream().parallel().forEach { it.bestFitness = new }
    }

    private val clock = Clock.systemDefaultZone()
    // endregion    --------------------------------------------------------------------------------

    fun evolve() {
//        val evolutionStartTime = clock.millis()
//        createPopulation()
//        while (limits.none { it(this) }) { // While none of the limits are met
//            val initialTime = clock.millis()
//            population = select(populationSize)     // Select the population
//            population = alter(population)          // Alter the population
//            generation++                            // Increment the generation
//            bestFitness = fittest.fitness           // Update the best fitness
//            statistics.stream().parallel().forEach {
//                it.generationTimes.add(clock.millis() - initialTime)
//            }
//        }
//        statistics.stream().parallel()
//            .forEach { it.evolutionTime = clock.millis() - evolutionStartTime }
    }

    /**
     * The main method of the ``Engine``.
     *
     * This is the classical flow of a Genetic Algorithm (GA), the user is assumed to know the
     * basics of evolutionary programming to use this method, so no further explanation is provided
     * (but take note of the comments placed on the body of the method).
     *
     * This method implements a single step of the GA so the process of evolution can be tweaked and
     * monitored by external entities, such as the [EvolutionSpliterator] used to process the
     * evolution as a [Stream].
     *
     * @param start the starting state of the evolution at this generation.
     * @return  the result of advancing the population by one generation.
     *
     * @see EvolutionInterceptor.identity
     * @see Evolu
     */
    override fun evolve(start: EvolutionStart<DNA>): EvolutionResult<DNA> {
        // (1) The starting state of the evolution is pre-processed (if no method is hooked to
        // pre-process, it defaults to the identity function (EvolutionStart)
        val interceptedStart = interceptor.before(start)
        val evolution = evolutionStart(interceptedStart)
        val evaluatedPopulation = evaluate(evolution)
        val offspring = selectOffspring(evaluatedPopulation)
        val survivors = selectSurvivors(evaluatedPopulation)
        val alteredOffspring = alter(offspring, evolution)
        // TODO: Filter population

        val nextPopulation = survivors.thenCombineAsync(
            alteredOffspring,
            { s, o -> s + o.population },
            executor
        )
        val pop = nextPopulation.join()
        val evPop = evaluate(EvolutionStart(pop, generation++))
        val result = EvolutionResult(optimizer, pop, generation)
        return interceptor.after(result)
    }

    private fun evolutionStart(start: EvolutionStart<DNA>) = if (start.population.isEmpty()) {
        val generation = start.generation
        val stream = Stream.concat(
            start.population.stream(),
            Stream.generate { genotype.make() }
                .map { Phenotype(it, generation) })
        EvolutionStart(
            stream.limit(populationSize.toLong())
                .collect(Collectors.toList()),
            generation
        )
    } else {
        start
    }

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

    private fun selectOffspring(population: Population<DNA>) =
        asyncSelect {
            offspringSelector(
                population,
                (offspringFraction * populationSize).toInt(),
                optimizer
            )
        }

    private fun selectSurvivors(population: List<Phenotype<DNA>>) =
        asyncSelect {
            survivorSelector(
                population,
                ((1 - offspringFraction) * populationSize).toInt(),
                optimizer
            )
        }

    private fun asyncSelect(select: () -> Population<DNA>) = supplyAsync({
        select()
    }, executor)

    private fun alter(
        population: CompletableFuture<Population<DNA>>,
        evolution: EvolutionStart<DNA>
    ) = population.thenApplyAsync({
        alterer(it, evolution.generation)
    }, executor)

    fun stream() = stream { EvolutionStart.empty() }

    private fun stream(start: () -> EvolutionStart<DNA>) =
        EvolutionStream.ofEvolver(this) { evolutionStart(start()) }

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

        var optimizer: PhenotypeOptimizer = FitnessMaximizer()

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
