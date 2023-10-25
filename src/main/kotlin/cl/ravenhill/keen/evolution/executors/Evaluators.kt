/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.executors

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.IntRequirement.BePositive
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene
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
 * An interface representing different strategies for evaluating the fitness functions of a given
 * population of DNA sequences.
 *
 * @param DNA The type of DNA sequence to evaluate.
 *
 * @since 1.0.0
 * @version 2.0.0
 */
interface EvaluationExecutor<DNA, G : Gene<DNA, G>> : KeenExecutor {

    /**
     * Evaluates the fitness function of the given population of DNA sequences.
     *
     * @param population The population of DNA sequences to evaluate.
     * @param force Whether to force the evaluation of fitness functions for all individuals in
     *  the population, even if they have already been evaluated.
     * @return The population of evaluated DNA sequences.
     */
    operator fun invoke(population: Population<DNA, G>, force: Boolean = false): Population<DNA, G>

    /**
     * A factory class for creating instances of the `Evaluator` interface.
     *
     * __Usage:__
     * ```
     * // Define a concrete Evaluator implementation
     * class MyEvaluator<DNA>(private val fitnessFunction: (Genotype<DNA>) -> Double) : Evaluator<DNA> {
     *     override fun invoke(population: Population<DNA>, force: Boolean): Population<DNA> {
     *         // Evaluate the fitness function for each individual in the population
     *         return population.map {
     *             if (force || it.fitness == null) {
     *                 val fitness = fitnessFunction(it.genotype)
     *                 it.copy(fitness = fitness)
     *             } else {
     *                 it
     *             }
     *         }
     *     }
     * }
     *
     * // Define a Factory implementation that creates instances of MyEvaluator
     * class MyEvaluatorFactory<DNA> : Evaluator.Factory<DNA>() {
     *     override lateinit var creator: ((Genotype<DNA>) -> Double) -> Evaluator<DNA>
     *         get() = { fitnessFunction -> MyEvaluator(fitnessFunction) }
     * }
     * ```
     *
     * @param DNA The type of DNA sequence to evaluate.
     * @property creator A function that creates an instance of the [EvaluationExecutor] interface.
     */
    open class Factory<DNA, G : Gene<DNA, G>> :
        KeenExecutor.Factory<((Genotype<DNA, G>) -> Double), EvaluationExecutor<DNA, G>> {
        override lateinit var creator: ((Genotype<DNA, G>) -> Double) -> EvaluationExecutor<DNA, G>
    }
}

/**
 * A class that implements the [EvaluationExecutor] interface and provides sequential fitness evaluation for
 * a given [Population] using a given fitness function.
 *
 * @param DNA The type of DNA sequence to evaluate.
 * @property function the fitness function to be used in the evaluation.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
class SequentialEvaluator<DNA, G : Gene<DNA, G>>(
    private val function: (Genotype<DNA, G>) -> Double
) : EvaluationExecutor<DNA, G> {

    // Inherit documentation from Evaluator
    override fun invoke(population: Population<DNA, G>, force: Boolean) =
        evaluateAndAddToPopulation(
            selectAndCreateEvaluators(population, function, force),
            population
        ) { evaluators -> evaluators.forEach { it.evaluate() } }
}

/**
 * A concurrent evaluator that uses coroutines to evaluate fitness functions for [Individual]
 * instances in a [Population].
 *
 * @param function The fitness function that evaluates a [Genotype] and returns a [Double].
 * @param dispatcher The [CoroutineDispatcher] used to dispatch the coroutines for parallel
 * evaluation. Defaults to [Dispatchers.Default].
 * @param chunkSize The number of [PhenotypeEvaluator] instances to evaluate in each coroutine.
 *  Larger values will require more memory, but may improve performance for some use cases.
 *  Defaults to 100.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
class CoroutineEvaluator<DNA, G : Gene<DNA, G>>(
    private val function: (Genotype<DNA, G>) -> Double,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val chunkSize: Int = 100
) : EvaluationExecutor<DNA, G>, CoroutineScope {

    /**
     * The [Job] instance used to manage the coroutines created by this instance.
     */
    private lateinit var job: Job

    // Inherit documentation from CoroutineScope
    override val coroutineContext: CoroutineContext
        get() = dispatcher + job

    // Inherit documentation from Evaluator
    override fun invoke(population: Population<DNA, G>, force: Boolean): Population<DNA, G> {
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

    /**
     * A factory for creating instances of [CoroutineEvaluator].
     *
     * @param DNA The type of DNA sequence to evaluate.
     * @property dispatcher The [CoroutineDispatcher] used to dispatch the coroutines for parallel
     *  evaluation.
     *  Defaults to [Dispatchers.Default].
     * @property chunkSize The number of [PhenotypeEvaluator] instances to evaluate in each
     *  coroutine.
     *  Larger values will require more memory, but may improve performance for some use cases.
     *  Defaults to 100.
     */
    class Factory<DNA, G : Gene<DNA, G>> : EvaluationExecutor.Factory<DNA, G>() {
        var dispatcher: CoroutineDispatcher = Dispatchers.Default
        var chunkSize: Int = 100
            set(value) {
                enforce {
                    "The chunk size [$value] must be a positive integer." {
                        value must BePositive
                    }
                }
                field = value
            }

        override var creator: ((Genotype<DNA, G>) -> Double) -> EvaluationExecutor<DNA, G> =
            { function -> CoroutineEvaluator(function, dispatcher, chunkSize) }
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
private fun <DNA, G : Gene<DNA, G>> selectAndCreateEvaluators(
    population: Population<DNA, G>,
    function: (Genotype<DNA, G>) -> Double,
    force: Boolean = false
): List<PhenotypeEvaluator<DNA, G>> = if (force) {
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
private fun <DNA, G : Gene<DNA, G>> evaluateAndAddToPopulation(
    toEvaluate: List<PhenotypeEvaluator<DNA, G>>,
    population: Population<DNA, G>,
    evaluationStrategy: (List<PhenotypeEvaluator<DNA, G>>) -> Unit
) = if (toEvaluate.isNotEmpty()) {
    evaluationStrategy(toEvaluate)
    // If all individuals in the population were evaluated, return a new population with the
    // evaluated individuals.
    if (toEvaluate.size == population.size) {
        toEvaluate.map { it.individual }
    } else {
        // Otherwise, add the evaluated individuals to the population and return a new
        // population.
        population.filter { it.isEvaluated() }.toMutableList().apply {
            addAll(toEvaluate.map { it.individual })
        }
    }
} else {
    // If no individuals were selected for evaluation, return the original population.
    population
}

/**
 * A helper class that encapsulates the evaluation of a [Individual] instance using a fitness
 * [function].
 *
 * @property individual The [Individual] instance to be evaluated.
 * @property function The fitness function used to calculate the fitness value of the [individual].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
private class PhenotypeEvaluator<DNA, G : Gene<DNA, G>>(
    individual: Individual<DNA, G>,
    private val function: (Genotype<DNA, G>) -> Double
) {
    /**
     * The fitness value of the [individual] calculated by the [function].
     */
    private var fitness = Double.NaN

    /**
     * Backing field for the [individual] property.
     */
    private val _individual = individual

    /**
     * Returns a [Individual] instance with the calculated fitness value.
     */
    val individual: Individual<DNA, G>
        get() = _individual.withFitness(fitness)

    /**
     * Evaluates the [individual] using the provided [function] to calculate the fitness value.
     * This method updates the [fitness] property of this instance.
     */
    fun evaluate() {
        fitness = function(individual.genotype)
    }
}
