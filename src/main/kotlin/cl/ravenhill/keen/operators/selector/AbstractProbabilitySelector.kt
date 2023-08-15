/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.DoubleRequirement.BeEqualTo
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.incremental
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer

/**
 * The threshold array size below which a linear search will be used instead of binary search.
 */
private const val SERIAL_INDEX_THRESHOLD = 35

/**
 * An abstract base class for selection operators that select individuals based on a probability
 * distribution.
 *
 * This class provides a common implementation for selecting individuals based on their probability
 * of selection, which is determined by a concrete implementation of the `probabilities` method.
 *
 * Subclasses of this class must implement the `probabilities` method to compute the probabilities
 * of selection for each individual in a population. The probabilities are then used to select
 * individuals for the next generation.
 *
 * @param sorted flag that determines whether the input population should be sorted by fitness
 * before selecting individuals.
 * Sorting can improve performance in some cases.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.3.0
 * @version 2.0.0
 */
abstract class AbstractProbabilitySelector<DNA, G : Gene<DNA, G>>(protected val sorted: Boolean) :
    AbstractSelector<DNA, G>() {

    /* Documentation inherited from [Selector] */
    override fun select(
        population: Population<DNA, G>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA, G>,
    ): Population<DNA, G> {
        // Sort the population if necessary
        val pop = if (sorted) {
            optimizer.sort(population)
        } else {
            population
        }
        // Calculate the probabilities for each phenotype
        val probabilities = probabilities(population, count, optimizer)
        // Check that the probabilities sum to 1.0
        enforce {
            "Probabilities must sum 1.0" { probabilities.sum() must BeEqualTo(1.0) }
        }
        // Correct any rounding errors in the probabilities
        checkAndCorrectProbabilities(probabilities)
        // Convert the probabilities to incremental probabilities
        probabilities.incremental()
        // Select the phenotypes using fitness-proportionate selection
        return List(count) { pop[indexOf(probabilities)] }
    }

    /**
     * Calculates the probabilities of selecting each member of the population for reproduction,
     * based on their fitness.
     *
     * @param population the population to calculate probabilities for
     * @param count the number of probabilities to calculate
     * @param optimizer the phenotype optimizer used to calculate the fitness of the population
     * @return an array of probabilities, one for each member of the population
     */
    abstract fun probabilities(
        population: Population<DNA, G>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA, G>,
    ): DoubleArray

    /**
     * Check and corrects an array of probabilities to ensure that it contains only finite values,
     * and that the sum of its elements equals 1.0.
     *
     * @param probabilities The array of probabilities to be checked and corrected.
     */
    private fun checkAndCorrectProbabilities(probabilities: DoubleArray) {
        if (probabilities.any { !it.isFinite() }) {
            probabilities.fill(1.0 / probabilities.size)
        }
        val sum = probabilities.sum()
        if (sum != 1.0) {
            probabilities.indices.forEach { index ->
                probabilities[index] /= sum
            }
        }
    }

    /**
     * Finds the index of a random value between 0.0 (inclusive) and 1.0 (exclusive) in a sorted
     * array of increasing values.
     * If the length of the array is less than or equal to [SERIAL_INDEX_THRESHOLD], it uses a
     * linear search.
     * Otherwise, it uses a binary search.
     *
     * @param incr the sorted array of increasing values to search
     * @return the index of the random value
     */
    private fun indexOf(incr: DoubleArray) = if (incr.size <= SERIAL_INDEX_THRESHOLD) {
        serialSearchIndex(incr)
    } else {
        binarySearchIndex(incr)
    }

    /**
     * Finds the index of a random value between 0.0 (inclusive) and 1.0 (exclusive)
     * in a sorted array of increasing values.
     *
     * @param incr the sorted array of increasing values to search
     * @return the index of the random value
     */
    private fun binarySearchIndex(incr: DoubleArray): Int {
        var (lo, hi) = 0 to incr.size
        var index = -1
        val v = Core.random.nextDouble()
        // Loop while the search range isn't empty and the index hasn't been found
        while (lo < hi && index == -1) {
            val mid = (lo + hi) ushr 1 // The index of the midpoint in the search range
            // If the midpoint is the index we're looking for
            if (mid == 0 || (incr[mid] >= v && incr[mid - 1] < v)) {
                index = mid
            } else if (incr[mid] < v) { // If the midpoint is less than the random value
                lo = mid + 1 // Search the right half of the search range
            } else { // If the midpoint is greater than the random value
                hi = mid // Search the left half of the search range
            }
        }
        return index
    }

    /**
     * Finds the index of the first element in a sorted array of increasing values
     * that is greater than or equal to a random value between 0.0 (inclusive) and 1.0 (exclusive).
     *
     * @param incr the sorted array of increasing values to search
     * @return the index of the first element that is greater than or equal to the random value
     */
    private fun serialSearchIndex(incr: DoubleArray): Int {
        var index = -1
        incr.forEachIndexed { i, value ->
            if (value >= Core.random.nextDouble()) {
                index = i
                return@forEachIndexed
            }
        }
        return index
    }
}
