package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Phenotype
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

/***************************************************************************************************
 * This code defines three classes: Evaluator, SequentialEvaluator, and ConcurrentEvaluator, as well
 * as a private helper class PhenotypeEvaluator.
 * Evaluator is an interface that defines the structure of an object that can evaluate a population
 * of DNA sequences using a specific fitness function.
 * SequentialEvaluator is a class that implements the Evaluator interface and evaluates the fitness
 * of a population of DNA sequences sequentially.
 * ConcurrentEvaluator is another class that implements the Evaluator interface and uses coroutines
 * to evaluate the fitness of a population of DNA sequences concurrently.
 * Finally, PhenotypeEvaluator is a private helper class that encapsulates the evaluation of a
 * single Phenotype instance using a fitness function.
 **************************************************************************************************/

/**
 * An interface for defining different strategies for evaluating the fitness functions of a given
 * population of DNA sequences.
 *
 * @param DNA The type of DNA sequence to evaluate.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
interface Evaluator<DNA> {

    /**
     * Evaluates the fitness function of the given population of DNA sequences.
     *
     * @param population The population to evaluate.
     * @param force Whether to force a fitness evaluation of all individuals, even if they have
     *  already been evaluated.
     * @return The evaluated population of DNA sequences.
     */
    operator fun invoke(population: Population<DNA>, force: Boolean = false): Population<DNA>
}

/**
 * A class that implements the [Evaluator] interface and provides sequential fitness evaluation for
 * a given [Population] using a given fitness function.
 *
 * @param DNA The type of DNA sequence to evaluate.
 * @property function the fitness function to be used in the evaluation.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
class SequentialEvaluator<DNA>(
    private val function: (Genotype<DNA>) -> Double
) : Evaluator<DNA> {

    // Inherit documentation from Evaluator
    override fun invoke(population: Population<DNA>, force: Boolean): Population<DNA> {
        // Select individuals to evaluate based on whether they have already been evaluated or not.
        val toEvaluate = if (force) {
            population
        } else {
            population.filter { it.isNotEvaluated() }
        }.map { PhenotypeEvaluator(it, function) }
        // Evaluate the selected individuals.
        return if (toEvaluate.isNotEmpty()) {
            toEvaluate.forEach { it.evaluate() }
            // If all individuals in the population were evaluated, return a new population with the
            // evaluated individuals.
            if (toEvaluate.size == population.size) {
                toEvaluate.map { it.phenotype }
            } else {
                // Otherwise, add the evaluated individuals to the population and return a new
                // population.
                population.filter { it.isEvaluated() }.toMutableList().apply {
                    addAll(toEvaluate.map { it.phenotype })
                }
            }
        } else {
            // If no individuals were selected for evaluation, return the original population.
            population
        }
    }
}

/**
 * A concurrent evaluator that uses coroutines to evaluate fitness functions for [Phenotype]
 * instances in a [Population].
 *
 * @param function The fitness function that evaluates a [Genotype] and returns a [Double].
 * @param dispatcher The [CoroutineDispatcher] used to dispatch the coroutines for parallel
 *  evaluation.
 * @param chunkSize The number of [PhenotypeEvaluator] instances to evaluate in each coroutine.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
class CoroutineEvaluator<DNA>(
    private val function: (Genotype<DNA>) -> Double,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val chunkSize: Int = 100
) : Evaluator<DNA>, CoroutineScope {

    /**
     * The [Job] instance used to manage the coroutines created by this instance.
     */
    private lateinit var job: Job

    // Inherit documentation from CoroutineScope
    override val coroutineContext: CoroutineContext
        get() = dispatcher + job

    // Inherit documentation from Evaluator
    override fun invoke(population: Population<DNA>, force: Boolean): Population<DNA> {
        // Initialize the job instance with a new Job
        job = Job()
        // Create a list of PhenotypeEvaluator instances that need to be evaluated
        val notEvaluated = if (force) {
            population
        } else {
            population.filter { it.isNotEvaluated() }
        }.map { PhenotypeEvaluator(it, function) }
        // If there are any PhenotypeEvaluator instances that need to be evaluated, run them
        // concurrently
        return if (notEvaluated.isNotEmpty()) {
            // Run the evaluations concurrently using coroutines
            runBlocking {
                // Split the list of PhenotypeEvaluator instances into chunks of size `chunkSize`
                notEvaluated.chunked(chunkSize).map { chunk ->
                    // Create a new coroutine to evaluate each chunk of PhenotypeEvaluator instances
                    async {
                        chunk.forEach { it.evaluate() }
                    }
                }.awaitAll() // Wait for all coroutines to complete
            }
            // Update the population with the evaluated Phenotype instances
            if (notEvaluated.size == population.size) {
                notEvaluated.map { it.phenotype }
            } else {
                population.filter { it.isEvaluated() }.toMutableList().apply {
                    addAll(notEvaluated.map { it.phenotype })
                }
            }
        } else {
            // If all Phenotype instances have already been evaluated, return the original
            // population
            population
        }
    }
}

/**
 * A helper class that encapsulates the evaluation of a [Phenotype] instance using a fitness
 * [function].
 *
 * @property phenotype The [Phenotype] instance to be evaluated.
 * @property function The fitness function used to calculate the fitness value of the [phenotype].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
private class PhenotypeEvaluator<DNA>(
    phenotype: Phenotype<DNA>,
    private val function: (Genotype<DNA>) -> Double
) {
    /**
     * The fitness value of the [phenotype] calculated by the [function].
     */
    private var fitness = Double.NaN

    /**
     * Backing field for the [phenotype] property.
     */
    private val _individual = phenotype

    /**
     * Returns a [Phenotype] instance with the calculated fitness value.
     */
    val phenotype: Phenotype<DNA>
        get() = _individual.withFitness(fitness)

    /**
     * Evaluates the [phenotype] using the provided [function] to calculate the fitness value.
     * This method updates the [fitness] property of this instance.
     */
    fun evaluate() {
        fitness = function(phenotype.genotype)
    }
}