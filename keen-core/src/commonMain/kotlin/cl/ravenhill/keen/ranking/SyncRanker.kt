/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.utils.SortingStrategy

/**
 * A synchronous implementation of an [IndividualRanker] for sorting individuals in an evolutionary algorithm.
 *
 * The `SyncRanker` interface extends the `IndividualRanker` interface, providing a default synchronous implementation
 * for sorting a population of individuals based on their fitness. This interface is useful when the sorting operation
 * does not require any asynchronous operations and can be performed synchronously.
 *
 * @param T The type of value held by the genes in the individuals.
 * @param F The type of feature used in the individual's representation.
 * @param R The type of representation used by the individual.
 */
interface SyncRanker<T, F, R> : IndividualRanker<T, F, R> where F : Feature<T, F>,
                                                                R : Representation<T, F> {
    /**
     * Sorts the population of individuals based on their fitness, according to the specified sorting strategy.
     *
     * This method sorts the population synchronously using the comparator obtained from the [getComparator] method,
     * which orders individuals according to the provided [SortingStrategy].
     *
     * @param population The list of individuals to be sorted.
     * @param sortOrder The strategy to use for sorting (ascending, descending, or unsorted).
     * @return A list of individuals sorted according to the specified strategy.
     */
    override suspend fun sort(
        population: List<Individual<T, F, R>>,
        sortOrder: SortingStrategy
    ) = population.sortedWith(getComparator(sortOrder))
}
