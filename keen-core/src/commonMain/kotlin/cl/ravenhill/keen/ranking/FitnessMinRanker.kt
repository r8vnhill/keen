/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.ranking

import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constraints.collections.BeMonotonicallyDecreasing
import cl.ravenhill.jakt.constraints.collections.BeMonotonicallyIncreasing
import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.utils.SortingStrategy

/**
 * A ranker for minimizing fitness in evolutionary algorithms.
 *
 * The `FitnessMinRanker` interface provides a mechanism for sorting individuals in a population based on their fitness
 * values, with the goal of minimizing the fitness. This ranker is particularly useful in scenarios where lower fitness
 * values are more desirable, such as in optimization problems where the objective is to minimize a cost function.
 *
 * @param T The type of value held by the genes in the individuals.
 * @param F The type of feature used in the individual's representation.
 * @param R The type of representation used by the individual.
 */
interface FitnessMinRanker<T, F, R> : IndividualRanker<T, F, R> where F : Feature<T, F>,
                                                                      R : Representation<T, F> {

    /**
     * Compares two individuals by their fitness, preferring the individual with the lower fitness value.
     *
     * @param first The first individual to compare.
     * @param second The second individual to compare.
     * @return A negative integer, zero, or a positive integer if the first individual's fitness is less than, equal to,
     *   or greater than the second individual's fitness, respectively.
     */
    override fun invoke(first: Individual<T, F, R>, second: Individual<T, F, R>) =
        second.fitness.compareTo(first.fitness)

    /**
     * Transforms a list of fitness values by inverting them relative to the sum of the fitness values.
     *
     * @param fitness The list of fitness values to transform.
     * @return A list of transformed fitness values, where each value is the difference between the sum of all fitness
     *   values and the original fitness value.
     */
    override fun fitnessTransform(fitness: List<Double>): List<Double> {
        val sum = fitness.sum()
        return fitness.map { sum - it }
    }

    companion object {
        /**
         * Creates a synchronous instance of `FitnessMinRanker`.
         *
         * @return A [SyncFitnessMinRanker] that ranks individuals by minimizing their fitness.
         */
        fun <T, F, R> sync()
                where F : Feature<T, F>,
                      R : Representation<T, F> = SyncFitnessMinRanker<T, F, R>()

        /**
         * Creates an asynchronous instance of `FitnessMinRanker`.
         *
         * @return An [AsyncFitnessMinRanker] that ranks individuals by minimizing their fitness. Default chunk size is
         * [DEFAULT_CHUNK_SIZE].
         */
        fun <T, F, R> async(chunkSize: Int = DEFAULT_CHUNK_SIZE)
                where F : Feature<T, F>,
                      R : Representation<T, F> = AsyncFitnessMinRanker<T, F, R>(chunkSize)

        /**
         * The default size of the chunks into which the population is divided for parallel sorting.
         */
        private const val DEFAULT_CHUNK_SIZE = 1000
    }

    /**
     * A synchronous ranker for minimizing fitness in evolutionary algorithms.
     *
     * The `SyncFitnessMinRanker` class implements both the [FitnessMinRanker] and [SyncRanker] interfaces, providing a
     * synchronous mechanism for sorting individuals in a population based on their fitness values with the goal of
     * minimizing fitness. This ranker is useful in scenarios where lower fitness values indicate better solutions, such as
     * in cost minimization problems.
     *
     * @param T The type of value held by the genes in the individuals.
     * @param F The type of feature used in the individual's representation.
     * @param R The type of representation used by the individual.
     */
    class SyncFitnessMinRanker<T, F, R> : FitnessMinRanker<T, F, R>, SyncRanker<T, F, R> where F : Feature<T, F>,
                                                                                               R : Representation<T, F>

    /**
     * An asynchronous ranker for minimizing fitness in evolutionary algorithms.
     *
     * The `AsyncFitnessMinRanker` class implements both the [FitnessMinRanker] and [AsyncRanker] interfaces, providing
     * an asynchronous mechanism for sorting individuals in a population based on their fitness values with the goal of
     * minimizing fitness. This ranker is useful in scenarios where lower fitness values indicate better solutions, such
     * as in cost minimization problems.
     *
     * @param T The type of value held by the genes in the individuals.
     * @param F The type of feature used in the individual's representation.
     * @param R The type of representation used by the individual.
     * @param chunkSize The size of the chunks into which the population is divided for parallel sorting. The default
     *   value is [DEFAULT_CHUNK_SIZE].
     */
    class AsyncFitnessMinRanker<T, F, R>(override val chunkSize: Int = DEFAULT_CHUNK_SIZE) : FitnessMinRanker<T, F, R>,
        AsyncRanker<T, F, R> where F : Feature<T, F>,
                                   R : Representation<T, F> {

        override fun checkIfSorted(sortedChunks: List<Population<T, F, R>>, sortOrder: SortingStrategy) {
            constrained {
                sortedChunks.forEach { chunk ->
                    if (sortOrder == SortingStrategy.ASCENDING) {
                        "Sorted chunk must be monotonically decreasing" {
                            chunk.map { it.fitness } must BeMonotonicallyDecreasing()
                        }
                    } else if (sortOrder == SortingStrategy.DESCENDING) {
                        "Sorted chunk must be monotonically increasing" {
                            chunk.map { it.fitness } must BeMonotonicallyIncreasing()
                        }
                    }
                }
            }.onLeft { throw it }
        }
    }
}
