/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException

/**
 * Utility object for handling common operations related to chromosome configuration in gene-centric evolutionary
 * computation.
 *
 * `ChromosomeUtils` provides utility functions for enforcing constraints on chromosomes and adjusting their gene ranges
 * and filters. These functions are generic and can be used with any type of chromosome that requires such
 * configurations.
 */
object ChromosomeUtils {

    /**
     * Enforces constraints on the size of ranges and filters lists to match the size of the chromosome.
     *
     * This function ensures that if there are multiple ranges or filters, their count should match the number of genes
     * in the chromosome. This validation is crucial to maintain consistency in chromosomes where each gene might have
     * different range or filter criteria.
     *
     * @param ranges A list of closed ranges applicable to the genes.
     * @param filters A list of filter functions applicable to the genes.
     * @param size The expected size of the chromosome (i.e., number of genes).
     * @throws CompositeException containing all the exceptions thrown by the constraints.
     * @throws CollectionConstraintException if the size of the ranges or filters lists does not match the size of the
     *   chromosome.
     */
    fun <T> enforceConstraints(
        ranges: List<ClosedRange<T>>,
        filters: List<(T) -> Boolean>,
        size: Int,
    ) where T : Comparable<T> {
        constraints {
            if (ranges.size > 1) {
                "Chromosome with multiple ranges must have the same number of genes as ranges" {
                    ranges must HaveSize(size)
                }
            }
            if (filters.size > 1) {
                "Chromosome with multiple filters must have the same number of genes as filters" {
                    filters must HaveSize(size)
                }
            }
        }
    }

    /**
     * Adjusts the ranges and filters lists to match the size of the chromosome.
     *
     * This function provides a way to ensure that the lists of ranges and filters are of the correct size to match
     * the chromosome's gene count. If there are no ranges or filters, or only one of each, it expands them to match
     * the chromosome size. Otherwise, it retains the original lists.
     *
     * @param size The size of the chromosome (i.e., number of genes).
     * @param ranges A mutable list of closed ranges; adjusted to match the chromosome size.
     * @param defaultRange The default range to apply if no ranges are specified.
     * @param filters A mutable list of filter functions; adjusted to match the chromosome size.
     * @param defaultFilter The default filter function to apply if no filters are specified.
     * @return A pair of adjusted lists for ranges and filters.
     */
    fun <T> adjustRangesAndFilters(
        size: Int,
        ranges: MutableList<ClosedRange<T>>,
        defaultRange: ClosedRange<T>,
        filters: MutableList<(T) -> Boolean>,
        defaultFilter: (T) -> Boolean,
    ) where T : Comparable<T> = when (ranges.size) {
        0 -> MutableList(size) { defaultRange }
        1 -> MutableList(size) { ranges.first() }
        else -> ranges
    } to when (filters.size) {
        0 -> MutableList(size) { defaultFilter }
        1 -> MutableList(size) { filters.first() }
        else -> filters
    }
}
