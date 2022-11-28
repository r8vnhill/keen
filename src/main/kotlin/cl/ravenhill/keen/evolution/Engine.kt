/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.EngineConfigurationException
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.constraints.Constraint
import cl.ravenhill.keen.constraints.RetryConstraint
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
import cl.ravenhill.keen.util.validatePredicate
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
    private val numberOfSurvivors: Int,
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
        statistics.stream().parallel().forEach { it.generation = new }
    }
        private set

    var steadyGenerations by Delegates.observable(0) { _, _, new ->
        statistics.stream().parallel().forEach { it.steadyGenerations = new }
    }
        private set

//    val fittest: Genotype<DNA>
//        get() {
//            val fittest = population.reduce { acc, genotype ->
//                if (optimizer(
//                        genotype.fitness,
//                        acc.fitness
//                    )
//                ) genotype else acc
//            }
//            statistics.stream().parallel().forEach { it.fittest = fittest }
//            return fittest
//        }

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

    private fun alter(population: List<Phenotype<DNA>>): List<Genotype<DNA>> {
        TODO()
//        val initialTime = clock.millis()
//        var alteredPopulation = population.toMutableList()
//        alterers.forEach { alterer ->
//            alteredPopulation = alterer(alteredPopulation).filter { it.verify() }.toMutableList()
//        }
//        if (alteredPopulation.size != populationSize) {
//            alteredPopulation.addAll(population.take(populationSize - alteredPopulation.size))
//        }
//        statistics.stream().parallel()
//            .forEach { it.alterTime.add(clock.millis() - initialTime) }
//        return alteredPopulation
    }

    internal fun createPopulation() {
//        runBlocking {
//            population =
//                (0 until populationSize).parallelMap { genotype.make() }
//        }
//        bestFitness = fittest.fitness
    }

    internal fun select(n: Int): List<Genotype<DNA>> {
//        val initialTime = clock.millis()
//        val newPopulation = survivorSelector(population, numberOfSurvivors, optimizer)
//        population = newPopulation + selector(population, n - numberOfSurvivors, optimizer)
//        statistics.stream().parallel()
//            .forEach { it.selectionTime.add(clock.millis() - initialTime) }
//        return population
        TODO("Remove")
    }

    override fun evolve(next: EvolutionStart<DNA>): EvolutionResult<DNA> {
        val interceptedStart = interceptor.before(next)
        val evolution = evolutionStart(interceptedStart)
        val evaluatedPopulation = evaluate(evolution)
        val offspring = selectOffspring(evaluatedPopulation)
        val survivors = selectSurvivors(evaluatedPopulation)
        val alteredOffspring = alter(offspring, evolution)
        // TODO: Filter population

        val nextPopulation = survivors.thenCombineAsync(
            offspring,
            { s, o -> s + o },
            executor
        )
        val pop = nextPopulation.join()
        TODO("Select survivors")
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

    private fun evaluate(evolution: EvolutionStart<DNA>) = if (evolution.isDirty) {
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
        alterer(it)
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
                "survivors: $numberOfSurvivors, " +
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

        private val alterer: Alterer<DNA>
            get() = CompositeAlterer(alterers)

        var selector: Selector<DNA> = TournamentSelector(3)
            set(value) {
                offspringSelector = value
                field = value
            }

        var survivorSelector: Selector<DNA> = selector

        var offspringSelector = selector

        var survivors: Int = 20

        var populationSize: Int = 50
            set(value) = if (value > 0) {
                field = value
            } else {
                throw EngineConfigurationException { "Population size must be positive" }
            }

        var offspringFraction = 0.6

        var optimizer: PhenotypeOptimizer = FitnessMaximizer()

        var statistics: List<Statistic<DNA>> = listOf(StatisticCollector())

        var executor: Executor = commonPool()

        var evaluator: Evaluator<DNA> = ConcurrentEvaluator(fitnessFunction, executor)

        var constraint: Constraint<DNA> = RetryConstraint(genotype)

        private val interceptor = EvolutionInterceptor.identity<DNA>()
        // endregion    ----------------------------------------------------------------------------

        fun build() = Engine(
            fitnessFunction = fitnessFunction,
            genotype = genotype,
            populationSize = populationSize,
            offspringFraction = offspringFraction,
            selector = selector,
            alterer = alterer,
            limits = limits,
            survivorSelector = survivorSelector,
            numberOfSurvivors = survivors,
            optimizer = optimizer,
            statistics = statistics,
            evaluator = evaluator,
            interceptor = interceptor,
            offspringSelector = offspringSelector,
            executor = executor
        )
    }

}
