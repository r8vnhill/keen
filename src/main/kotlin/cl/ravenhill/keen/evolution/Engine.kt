/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.EngineConfigurationException
import cl.ravenhill.keen.constraints.Constraint
import cl.ravenhill.keen.constraints.RetryConstraint
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.operators.Alterer
import cl.ravenhill.keen.operators.selector.Selector
import cl.ravenhill.keen.operators.selector.TournamentSelector
import cl.ravenhill.keen.util.optimizer.Maximizer
import cl.ravenhill.keen.util.optimizer.Optimizer
import cl.ravenhill.keen.util.parallelMap
import cl.ravenhill.keen.util.statistics.Statistic
import cl.ravenhill.keen.util.statistics.StatisticCollector
import kotlinx.coroutines.runBlocking
import java.time.Clock
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
 * @property population         The current population
 * @property limits             The limits that will be used to stop the evolution
 * @property steadyGenerations  The number of generations that the fitness has not changed
 * @property fittest            The fittest individual of the current population
 */
class Engine<DNA> private constructor(
    private val fitnessFunction: (Genotype<DNA>) -> Double,
    private val genotype: Genotype.Factory<DNA>,
    private val populationSize: Int,
    val selector: Selector<DNA>,
    private val alterers: List<Alterer<DNA>>,
    private val limits: List<Limit>,
    val survivorSelector: Selector<DNA>,
    private val survivors: Int,
    private val optimizer: Optimizer,
    val statistics: List<Statistic<DNA>>,
    val evaluator: Evaluator<DNA>,
    private val interceptor: EvolutionInterceptor<DNA>
) : Evolver<DNA> {

    init {
        // We need to set the genotype's fitness function to evolve the population
        genotype.fitnessFunction = fitnessFunction
    }

    // region : PROPERTIES  ------------------------------------------------------------------------
    var generation: Int by Delegates.observable(0) { _, _, new ->
        statistics.stream().parallel().forEach { it.generation = new }
    }
        private set

    var steadyGenerations by Delegates.observable(0) { _, _, new ->
        statistics.stream().parallel().forEach { it.steadyGenerations = new }
    }
        private set

    var population: List<Genotype<DNA>> = emptyList()
        private set

    val fittest: Genotype<DNA>
        get() {
            val fittest = population.reduce { acc, genotype ->
                if (optimizer(
                        genotype.fitness,
                        acc.fitness
                    )
                ) genotype else acc
            }
            statistics.stream().parallel().forEach { it.fittest = fittest }
            return fittest
        }

    var bestFitness: Double by Delegates.observable(0.0) { _, old, new ->
        if (old == new) {
            steadyGenerations++
        } else {
            steadyGenerations = 0
        }
        statistics.stream().parallel().forEach { it.bestFitness = new }
    }

    private val clock = Clock.systemDefaultZone()
    // endregion    --------------------------------------------------------------------------------

    fun evolve() {
        val evolutionStartTime = clock.millis()
        createPopulation()
        while (limits.none { it(this) }) { // While none of the limits are met
            val initialTime = clock.millis()
            population = select(populationSize)     // Select the population
            population = alter(population)          // Alter the population
            generation++                            // Increment the generation
            bestFitness = fittest.fitness           // Update the best fitness
            statistics.stream().parallel().forEach {
                it.generationTimes.add(clock.millis() - initialTime)
            }
        }
        statistics.stream().parallel()
            .forEach { it.evolutionTime = clock.millis() - evolutionStartTime }
    }

    private fun alter(population: List<Genotype<DNA>>): List<Genotype<DNA>> {
        val initialTime = clock.millis()
        var alteredPopulation = population.toMutableList()
        alterers.forEach { alterer ->
            alteredPopulation = alterer(alteredPopulation).filter { it.verify() }.toMutableList()
        }
        if (alteredPopulation.size != populationSize) {
            alteredPopulation.addAll(population.take(populationSize - alteredPopulation.size))
        }
        statistics.stream().parallel()
            .forEach { it.alterTime.add(clock.millis() - initialTime) }
        return alteredPopulation
    }

    internal fun createPopulation() {
        runBlocking {
            population =
                (0 until populationSize).parallelMap { genotype.make() }
        }
        bestFitness = fittest.fitness
    }

    internal fun select(n: Int): List<Genotype<DNA>> {
        val initialTime = clock.millis()
        val newPopulation = survivorSelector(population, survivors, optimizer)
        population = newPopulation + selector(population, n - survivors, optimizer)
        statistics.stream().parallel()
            .forEach { it.selectionTime.add(clock.millis() - initialTime) }
        return population
    }

    override fun evolve(next: EvolutionStart<DNA>): EvolutionResult<DNA> {
        val interceptedStart = interceptor.before(next)
        val evolution = if (interceptedStart.population.isEmpty()) {
            evolutionStart(interceptedStart)
        } else {
            interceptedStart
        }
        val evaluatedPopulation = if (evolution.isDirty) {
            evaluate(evolution.population)
        } else {
            evolution.population
        }
        TODO("Not yet implemented")
    }

    private fun evaluate(population: List<Phenotype<DNA>>): List<Phenotype<DNA>> {
        val evaluated = evaluator.invoke(population)
        TODO("Not yet implemented")
    }

    fun stream() = stream { EvolutionStart.empty() }

    private fun stream(start: () -> EvolutionStart<DNA>) =
        EvolutionStream.ofEvolver(this) { evolutionStart(start()) }

    private fun evolutionStart(start: EvolutionStart<DNA>): EvolutionStart<DNA> {
        val population = start.population
        val generation = start.generation

        val stream = Stream.concat(
            population.stream(),
            Stream.generate { genotype.make() }
                .map { Phenotype(it, generation) })

        return EvolutionStart(
            stream.limit(populationSize.toLong())
                .collect(Collectors.toList()),
            generation
        )
    }

    override fun toString() =
        "Engine { " +
                "populationSize: $populationSize, " +
                "genotype: $genotype, " +
                "selector: $selector, " +
                "alterers: $alterers, " +
                "optimizer: $optimizer, " +
                "survivors: $survivors, " +
                "survivorSelector: $survivorSelector " +
                "}"

    /**
     * Engine builder.
     *
     * @param DNA   The type of the DNA of the Genotype.
     *
     * @property populationSize The size of the population.
     *                          It must be greater than 0.
     *                          Default value is 50.
     * @property selector       The selector that will be used to select the individuals.
     *                          Default value is ``TournamentSelector(3)``.
     * @property alterers       The alterers that will be used to alter the population.
     *                          Default value is an empty list.
     */
    class Builder<DNA>(
        private val fitnessFunction: (Genotype<DNA>) -> Double,
        private val genotype: Genotype.Factory<DNA>
    ) {

        // region : PROPERTIES  --------------------------------------------------------------------
        var limits: List<Limit> = listOf(GenerationCount(100))

        var alterers: List<Alterer<DNA>> = emptyList()

        var selector: Selector<DNA> = TournamentSelector(3)

        var survivorSelector: Selector<DNA> = selector

        var survivors: Int = 20

        var populationSize: Int = 50
            set(value) = if (value > 0) {
                field = value
            } else {
                throw EngineConfigurationException { "Population size must be positive" }
            }

        var optimizer: Optimizer = Maximizer()

        var statistics: List<Statistic<DNA>> = listOf(StatisticCollector())

        var evaluator: Evaluator<DNA> = ConcurrentEvaluator(fitnessFunction, commonPool())

        var constraint: Constraint<DNA> = RetryConstraint(genotype)

        private val interceptor = EvolutionInterceptor.identity<DNA>()
        // endregion    ----------------------------------------------------------------------------

        fun build() = Engine(
            fitnessFunction,
            genotype,
            populationSize,
            selector,
            alterers,
            limits,
            survivorSelector,
            survivors,
            optimizer,
            statistics,
            evaluator,
            interceptor
        )
    }

}
