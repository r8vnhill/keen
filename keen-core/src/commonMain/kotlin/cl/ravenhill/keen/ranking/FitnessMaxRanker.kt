/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.ranking

import arrow.core.getOrElse
import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constraints.collections.BeMonotonicallyDecreasing
import cl.ravenhill.jakt.constraints.collections.BeMonotonicallyIncreasing
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.ranking.FitnessMaxRanker.AsyncFitnessMaxRanker.Companion.DEFAULT_CHUNK_SIZE
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.utils.SortingStrategy

/**
 * An `IndividualRanker` that ranks individuals based on maximizing their fitness.
 *
 * The `FitnessMaxRanker` interface provides a mechanism for ranking individuals in an evolutionary algorithm by
 * maximizing their fitness values. This ranker compares two individuals and orders them based on their fitness, with
 * higher fitness values being favored. It is commonly used in evolutionary algorithms where the goal is to evolve the
 * population toward the highest possible fitness.
 *
 * @param T The type of value held by the genes in the individuals.
 * @param F The type of feature used in the individual's representation.
 * @param R The type of representation used by the individual.
 */
interface FitnessMaxRanker<T, F, R> : IndividualRanker<T, F, R> where F : Feature<T, F>,
                                                                      R : Representation<T, F> {

    /**
     * Compares two individuals based on their fitness.
     *
     * @param first The first individual to compare.
     * @param second The second individual to compare.
     * @return A negative integer, zero, or a positive integer if the first individual's fitness is less than, equal to,
     *   or greater than the second individual's fitness, respectively.
     */
    override fun invoke(first: Individual<T, F, R>, second: Individual<T, F, R>) =
        first.fitness.compareTo(second.fitness)

    companion object {

        /**
         * Creates a synchronous instance of `FitnessMaxRanker`.
         *
         * @return A [SyncFitnessMaxRanker] that ranks individuals by maximizing their fitness.
         */
        fun <T, F, R> sync() where F : Feature<T, F>, R : Representation<T, F> =
            SyncFitnessMaxRanker<T, F, R>()

        /**
         * Creates an asynchronous instance of `FitnessMaxRanker`.
         *
         * @return An [AsyncFitnessMaxRanker] that ranks individuals by maximizing their fitness.
         */
        fun <T, F, R> async(chunkSize: Int = DEFAULT_CHUNK_SIZE)
                where F : Feature<T, F>,
                      R : Representation<T, F> = AsyncFitnessMaxRanker<T, F, R>(chunkSize)
    }

    /**
     * A synchronous implementation of the `FitnessMaxRanker` for ranking individuals by maximizing their fitness.
     *
     * The `SyncFitnessMaxRanker` class combines the functionalities of [FitnessMaxRanker] and [SyncRanker], providing a
     * mechanism to rank individuals in an evolutionary algorithm based on their fitness values. This class operates
     * synchronously, meaning that the sorting and ranking operations are performed in a blocking manner. It is suitable
     * for use in environments where synchronous operations are preferred or required.
     *
     * @param T The type of value held by the genes in the individuals.
     * @param F The type of feature used in the individual's representation.
     * @param R The type of representation used by the individual.
     */
    class SyncFitnessMaxRanker<T, F, R> : FitnessMaxRanker<T, F, R>, SyncRanker<T, F, R>
            where F : Feature<T, F>,
                  R : Representation<T, F> {
        override fun toString() = "SyncFitnessMaxRanker"
    }

    /**
     * An asynchronous ranker for maximizing fitness in evolutionary algorithms.
     *
     * The `AsyncFitnessMaxRanker` class is an implementation of the [FitnessMaxRanker] interface, designed to sort
     * individuals in a population by maximizing their fitness values. This class leverages asynchronous processing to
     * handle large populations efficiently by sorting them in parallel, making it suitable for scenarios where
     * population sizes are large and performance is critical.
     *
     * @param T The type of value held by the genes in the individuals.
     * @param F The type of feature used in the individual's representation.
     * @param R The type of representation used by the individual.
     * @param chunkSize The size of the chunks into which the population is divided for parallel sorting. The default
     *   value is [DEFAULT_CHUNK_SIZE].
     */
    class AsyncFitnessMaxRanker<T, F, R>(override val chunkSize: Int = DEFAULT_CHUNK_SIZE) : FitnessMaxRanker<T, F, R>,
        AsyncRanker<T, F, R> where F : Feature<T, F>,
                                   R : Representation<T, F> {

        override fun toString() = "AsyncFitnessMaxRanker(chunkSize=$chunkSize)"

        /**
         * Checks whether the sorted chunks are correctly sorted according to the specified sorting strategy.
         *
         * The `checkIfSorted` method ensures that each chunk of the population, after being sorted, adheres to the
         * expected order defined by the [sortOrder]. If the sorting strategy is ascending, it verifies that each chunk
         * is monotonically increasing. If the strategy is descending, it verifies that each chunk is monotonically
         * decreasing. If any chunk does not meet the required criteria, an exception is thrown.
         *
         * @param sortedChunks The list of sorted population chunks to validate.
         * @param sortOrder The sorting strategy applied to the population (ascending, descending, or unsorted).
         * @throws CompositeException if any chunk does not meet the monotonicity condition according to the sorting
         *   strategy.
         */
        override fun checkIfSorted(
            sortedChunks: List<List<Individual<T, F, R>>>,
            sortOrder: SortingStrategy
        ) = constrained {
            sortedChunks.forEach { chunk ->
                if (sortOrder == SortingStrategy.ASCENDING) {
                    "Sorted chunk must be monotonically increasing" {
                        chunk.map { it.fitness } must BeMonotonicallyIncreasing()
                    }
                } else if (sortOrder == SortingStrategy.DESCENDING) {
                    "Sorted chunk must be monotonically decreasing" {
                        chunk.map { it.fitness } must BeMonotonicallyDecreasing()
                    }
                }
            }
        }.getOrElse { throw it }

        companion object {
            /**
             * The default chunk size for parallel sorting. Set to 1000.
             */
            internal const val DEFAULT_CHUNK_SIZE = 1000
        }
    }
}
