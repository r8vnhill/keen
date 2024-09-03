/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.ranking

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.utils.SortingStrategy
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/**
 * An asynchronous implementation of the `IndividualRanker` interface for ranking individuals in evolutionary
 * algorithms.
 *
 * The `AsyncRanker` interface extends the `IndividualRanker` interface, providing an asynchronous approach to ranking
 * individuals within a population based on their fitness. This interface is designed to handle large populations
 * efficiently by sorting individuals in parallel, which can significantly reduce the time required for ranking in
 * scenarios where the population size is large.
 *
 * ### Sorting Process:
 * 1. **Chunking**: The population is divided into smaller chunks based on the [chunkSize].
 * 2. **Parallel Sorting**: Each chunk is sorted concurrently using coroutines.
 * 3. **Merging**: The sorted chunks are merged back into a single list, maintaining the overall sort order.
 *
 * @param T The type of value held by the genes in the individuals.
 * @param F The type of feature used in the individual's representation.
 * @param R The type of representation used by the individual.
 * @property chunkSize The size of the chunks into which the population is divided for parallel sorting.
 */
interface AsyncRanker<T, F, R> : IndividualRanker<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    val chunkSize: Int
        get() = 1000

    /**
     * Asynchronously sorts the population based on the specified sorting strategy.
     *
     * @param population The list of individuals to be sorted.
     * @param sortOrder The sorting strategy to apply (e.g., ascending, descending, or unsorted).
     * @return A list of individuals sorted according to the specified sort order.
     */
    override suspend fun sort(
        population: List<Individual<T, F, R>>,
        sortOrder: SortingStrategy
    ): List<Individual<T, F, R>> = if (sortOrder == SortingStrategy.UNSORTED) {
        population
    } else {
        withContext(Domain.dispatcher) {
            val chunks = population.chunked(chunkSize)
            val sortedChunks = coroutineScope {
                chunks.map { chunk ->
                    async { chunk.sortedWith(getComparator(sortOrder)) }
                }.awaitAll()
            }
            mergeSortedChunks(sortedChunks, sortOrder)
        }
    }

    /**
     * Merges sorted chunks of individuals into a single sorted list.
     *
     * @param sortedChunks A list of sorted chunks of individuals.
     * @param sortOrder The sorting strategy used to sort the chunks.
     * @return A single list of individuals sorted according to the specified sort order.
     */
    private fun mergeSortedChunks(
        sortedChunks: List<List<Individual<T, F, R>>>,
        sortOrder: SortingStrategy
    ): List<Individual<T, F, R>> {
        checkIfSorted(sortedChunks, sortOrder)
        val iterators = sortedChunks.map { it.iterator() }.toMutableList()
        return buildList {
            while (iterators.isNotEmpty()) {
                val extremeItem = iterators.asSequence()
                    .mapNotNull { it.nextOrNull() }
                    .let { items ->
                        when (sortOrder) {
                            SortingStrategy.ASCENDING -> items.minByOrNull { item -> comparator.compare(item, item) }
                            SortingStrategy.DESCENDING -> items.maxByOrNull { item -> comparator.compare(item, item) }
                            SortingStrategy.UNSORTED -> null    // Should never happen
                        }
                    }
                extremeItem?.let { item ->
                    add(item)
                    iterators.removeAll { !it.hasNext() }
                }
            }
        }
    }

    /**
     * Checks if the sorted chunks adhere to the required sorting constraints.
     *
     * @param sortedChunks A list of sorted chunks of individuals.
     * @param sortOrder The sorting strategy used to sort the chunks.
     * @throws CompositeException if the sorted chunks do not meet the required sorting constraints.
     */
    fun checkIfSorted(sortedChunks: List<List<Individual<T, F, R>>>, sortOrder: SortingStrategy)

    /**
     * Safely retrieves the next item from an iterator or returns `null` if no items are available.
     */
    private fun <T> Iterator<T>.nextOrNull(): T? = if (hasNext()) next() else null
}
