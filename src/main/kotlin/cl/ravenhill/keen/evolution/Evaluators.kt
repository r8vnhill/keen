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
 * This code defines classes and interfaces for evaluating the fitness of a population of DNA
 * sequences using a specific fitness function.
 * The Evaluator interface defines the structure of an object that can evaluate a population of DNA
 * sequences.
 * The SequentialEvaluator class implements the Evaluator interface and evaluates the fitness of a
 * population of DNA sequences sequentially using a given fitness function.
 * The CoroutineEvaluator class is another implementation of the Evaluator interface that uses
 * coroutines to evaluate the fitness of a population of DNA sequences concurrently.
 * Finally, the PhenotypeEvaluator class is a private helper class that encapsulates the evaluation
 * of a single Phenotype instance using a fitness function.
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
    override fun invoke(population: Population<DNA>, force: Boolean) = evaluateAndAddToPopulation(
        selectAndCreateEvaluators(population, function, force),
        population
    ) { evaluators -> evaluators.forEach { it.evaluate() } }
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
        return evaluateAndAddToPopulation(
            selectAndCreateEvaluators(population, function, force),
            population
        ) { evaluators ->
            runBlocking {
                evaluators.chunked(chunkSize).map { chunk ->
                    async {
                        chunk.forEach { it.evaluate() }
                    }
                }.awaitAll()
            }
        }
    }
}

/**
 * Selects the un-evaluated individuals from a population and creates a list of corresponding
 * [PhenotypeEvaluator] objects for evaluating their fitness.
 *
 * @param population the population of individuals to evaluate
 * @param function the fitness function to use for evaluating individuals
 * @param force whether to force evaluation of all individuals in the population regardless of
 * their evaluation status (default is false)
 * @return a list of [PhenotypeEvaluator] objects corresponding to the un-evaluated individuals
 * in the population (or all individuals if [force] is true)
 */
private fun <DNA> selectAndCreateEvaluators(
    population: Population<DNA>,
    function: (Genotype<DNA>) -> Double,
    force: Boolean = false
): List<PhenotypeEvaluator<DNA>> = if (force) {
    // Evaluate all individuals in the population if force is true
    population
} else {
    // Select un-evaluated individuals and create corresponding PhenotypeEvaluator objects
    population.filter { it.isNotEvaluated() }
}.map { PhenotypeEvaluator(it, function) }

/**
 * Evaluates a list of phenotype evaluators and adds the resulting phenotypes to the given
 * population.
 *
 * @param toEvaluate a list of phenotype evaluators to evaluate and add to the population.
 * @param population the original population to add the evaluated individuals to.
 * @param evaluationStrategy a function that evaluates the list of phenotype evaluators.
 * @return a new population that includes the evaluated individuals. If no individuals were selected
 *  for evaluation, the original population is returned.
 *  If all individuals in the population were evaluated, only the evaluated individuals are
 *  returned.
 */
private fun <DNA> evaluateAndAddToPopulation(
    toEvaluate: List<PhenotypeEvaluator<DNA>>,
    population: Population<DNA>,
    evaluationStrategy: (List<PhenotypeEvaluator<DNA>>) -> Unit
) = if (toEvaluate.isNotEmpty()) {
    evaluationStrategy(toEvaluate)
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