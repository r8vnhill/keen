/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.ranking

import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constraints.collections.BeMonotonicallyDecreasing
import cl.ravenhill.jakt.constraints.collections.BeMonotonicallyIncreasing
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
 * Represents a ranker for evaluating and comparing the fitness of individuals in an evolutionary algorithm.
 *
 * The `Ranker` interface provides methods for comparing individuals based on their fitness, sorting populations,
 * and transforming fitness values. Implementations of this interface define how to compare fitness values and rank
 * individuals accordingly.
 *
 * ## Usage:
 * Use this interface to define custom ranking strategies for evolutionary algorithms. The ranker can be used to sort
 * populations, compare individuals, and apply transformations to fitness values.
 *
 * ### Example:
 * Implementing a simple ranker:
 * ```kotlin
 * class SimpleRanker<T, F, R> :  IndividualRanker<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {
 *     override fun invoke(first: Individual<T, F, R>, second: Individual<T, F, R>) =
 *         first.fitness.compareTo(second.fitness)
 * }
 * val ranker = SimpleRanker<MyType, MyFeature, MyRepresentation>()
 * val sortedPopulation = ranker.sort(population)
 * val transformedFitness = ranker.fitnessTransform(fitnessValues)
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @property comparator The comparator used for comparing two individuals based on their fitness.
 */
interface IndividualRanker<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {
    /**
     * The comparator used for comparing two individuals based on their fitness.
     */
    val comparator
        get() = Comparator(::invoke)

    /**
     * Compares two individuals based on their fitness.
     *
     * @param first The first individual to compare.
     * @param second The second individual to compare.
     * @return A negative integer, zero, or a positive integer if the first individual's fitness is less than,
     *   equal to, or greater than the second individual's fitness, respectively.
     */
    operator fun invoke(first: Individual<T, F, R>, second: Individual<T, F, R>): Int

    /**
     * Asynchronously sorts a population of individuals in chunks using the specified sorting strategy.
     *
     * The `sort` function sorts a large population of individuals by dividing it into smaller chunks, sorting each
     * chunk concurrently, and then merging the sorted chunks into a final sorted list. This approach is efficient for
     * handling large populations, leveraging Kotlin coroutines to perform the sorting in parallel.
     *
     * ## Example:
     * ```kotlin
     * val sortedPopulation = sort(population, chunkSize = 500, sortOrder = SortingStrategy.DESCENDING)
     * println(sortedPopulation)
     * ```
     *
     * ## Implementation Details:
     * - The population is first split into chunks of the specified size.
     * - Each chunk is then sorted asynchronously in parallel using the specified sorting strategy.
     * - Finally, the sorted chunks are merged into a single, fully sorted list using the [mergeSortedChunks] function.
     *
     * @param population The list of [Individual] elements to be sorted.
     * @param chunkSize The size of each chunk that the population will be divided into for sorting. Default is `1000`.
     * @param sortOrder The [SortingStrategy] that determines the order of sorting. Default is
     *   [SortingStrategy.ASCENDING].
     * @return A sorted list of [Individual] elements, ordered according to the specified [SortingStrategy].
     */
    suspend fun sort(
        population: List<Individual<T, F, R>>,
        chunkSize: Int = 1000,
        sortOrder: SortingStrategy = SortingStrategy.ASCENDING
    ): List<Individual<T, F, R>> = withContext(Domain.dispatcher) {
        val chunks = population.chunked(chunkSize)
        val sortedChunks = coroutineScope {
            chunks.map { chunk ->
                async { chunk.sortedWith(getComparator(sortOrder)) }
            }.awaitAll()
        }
        mergeSortedChunks(sortedChunks, sortOrder)
    }

    /**
     * Returns a comparator for sorting individuals based on the specified sorting strategy.
     *
     * @param sortOrder The [SortingStrategy] that determines the sorting order.
     * @return A [Comparator] for sorting [Individual] elements according to the specified sorting strategy.
     */
    private fun getComparator(sortOrder: SortingStrategy): Comparator<Individual<T, F, R>> =
        when (sortOrder) {
            SortingStrategy.ASCENDING -> comparator
            SortingStrategy.DESCENDING -> comparator.reversed()
            SortingStrategy.UNSORTED -> Comparator { _, _ -> 0 } // Neutral comparator for UNSORTED
        }

    /**
     * Merges multiple sorted lists of individuals into a single sorted list according to the specified sorting
     * strategy.
     *
     * The `mergeSortedChunks` function takes a list of sorted chunks (lists of individuals) and merges them into a
     * single list that is also sorted according to the specified [SortingStrategy]. Before merging, the function
     * validates that each chunk is sorted correctly using the [checkIfSorted] function. The merge operation is
     * performed in a way that maintains the overall sort order of the final merged list.
     *
     * @param sortedChunks A list of sorted lists of [Individual] elements. Each list should be sorted according to the
     *   specified [SortingStrategy].
     * @param sortOrder The [SortingStrategy] that determines the order of the final merged list.
     * @return A list of [Individual] elements merged from the sorted chunks and sorted according to the specified strategy.
     * @throws CompositeException if any chunk is not sorted correctly according to the specified sorting strategy.
     */
    private fun mergeSortedChunks(
        sortedChunks: List<List<Individual<T, F, R>>>,
        sortOrder: SortingStrategy
    ): List<Individual<T, F, R>> {
        checkIfSorted(sortedChunks, sortOrder)
        val iterators = sortedChunks.map { it.iterator() }.toMutableList()
        return buildList {
            while (iterators.isNotEmpty()) {
                // Find the extreme item (min or max) from the current set of items according to the sort order
                val extremeItem = iterators.asSequence()
                    .mapNotNull { it.nextOrNull() }
                    .let { items ->
                        when (sortOrder) {
                            SortingStrategy.ASCENDING -> items.minByOrNull { item -> comparator.compare(item, item) }
                            SortingStrategy.DESCENDING -> items.maxByOrNull { item -> comparator.compare(item, item) }
                            SortingStrategy.UNSORTED -> null // No sorting applied for UNSORTED strategy
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
     * Validates that each chunk in a list of sorted populations is correctly ordered according to the specified sorting
     * strategy.
     *
     * @param sortedChunks A list of population chunks, where each chunk is a [Population] of individuals. Each chunk is
     *   expected to be sorted according to the provided sorting strategy.
     * @param sortOrder The [SortingStrategy] used to determine the expected order of the fitness values in each chunk.
     *   The strategy can be either [SortingStrategy.ASCENDING] or [SortingStrategy.DESCENDING].
     * @throws CompositeException if any chunk is not sorted according to the specified sorting strategy.
     */
    private fun checkIfSorted(sortedChunks: List<Population<T, F, R>>, sortOrder: SortingStrategy) {
        constrained {
            sortedChunks.forEach { chunk ->
                if (sortOrder == SortingStrategy.ASCENDING) {
                    "Sorted chunk must be monotonically increasing" {
                        chunk.map { it.fitness } must BeMonotonicallyIncreasing()
                    }
                } else {
                    "Sorted chunk must be monotonically decreasing" {
                        chunk.map { it.fitness } must BeMonotonicallyDecreasing()
                    }
                }
            }
        }.onLeft { throw it }
    }

    /**
     * Safely retrieves the next element from the iterator or returns `null` if no more elements are available.
     *
     * @receiver An iterator of type `T`.
     * @return The next element in the iteration if available, or `null` if the iterator has no more elements.
     */
    private fun <T> Iterator<T>.nextOrNull(): T? = if (hasNext()) next() else null

    /**
     * Transforms a list of fitness values.
     *
     * @param fitness The list of fitness values to transform.
     * @return The transformed list of fitness values.
     */
    fun fitnessTransform(fitness: List<Double>) = fitness
}
