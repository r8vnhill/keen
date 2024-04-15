/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.genes


/**
 * An interface representing a gene with a comparable value in genetic algorithms.
 *
 * `ComparableGene` extends the [Gene] interface and is tailored for genes with values that can be compared.
 * This interface is especially useful in evolutionary algorithms where ranking, sorting, or any form of comparison
 * between genes is required. It enables genes to be directly compared based on their values, making it
 * suitable for scenarios where genetic material must be ordered or evaluated against a metric.
 *
 * ## Features:
 * - **Comparable Values**: Assumes that the gene's value type [T] implements [Comparable], allowing gene
 *   instances to be compared based on their values.
 * - **Natural Ordering**: Provides a natural ordering for genes, which can be leveraged in selection processes,
 *   sorting algorithms, or when determining the fittest individuals in a population.
 *
 * ## Usage:
 * Implement this interface in classes representing genes where comparison based on the gene's value is necessary.
 * This could be in optimization problems, sorting algorithms, or any scenario where gene ranking is relevant.
 *
 * ### Example:
 * Implementing a `ComparableGene` for a simple numeric value:
 * ```kotlin
 * class NumericGene(override val value: Int) : ComparableGene<Int, NumericGene> {
 *     // Other implementations...
 * }
 *
 * val gene1 = NumericGene(5)
 * val gene2 = NumericGene(10)
 * val comparisonResult = gene1.compareTo(gene2) // Will be negative as 5 < 10
 * ```
 * In this example, `NumericGene` implements `ComparableGene`, allowing instances of `NumericGene` to be
 * compared based on their integer values. The `compareTo` method facilitates the comparison.
 *
 * @param T The comparable type of the gene's value.
 * @param G The specific type of `ComparableGene`.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface ComparableGene<T, G> : Gene<T, G> where T : Comparable<T>, G : ComparableGene<T, G> {

    /**
     * Compares this gene with another gene based on their values.
     *
     * @param other The other [ComparableGene] to compare with.
     * @return A negative integer, zero, or a positive integer as this gene's value is less than, equal to, or greater
     *   than the specified gene's value.
     */
    operator fun compareTo(other: ComparableGene<T, G>) = value.compareTo(other.value)
}
