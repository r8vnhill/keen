/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.mixins

/**
 * Represents a structure that can be folded or reduced to a single value in evolutionary algorithms.
 *
 * The `Foldable` interface defines operations for folding a collection of elements into a single value by iteratively
 * applying a binary operation. This is particularly useful in evolutionary algorithms for aggregating or processing
 * data, such as summing the values of genes in a chromosome or combining results of genetic operations.
 *
 * ## Usage:
 * Implement this interface for data structures in evolutionary algorithms that need to be folded, such as chromosomes
 * or genotypes. The `fold` and `foldRight` methods allow you to reduce these structures to a single value, starting
 * from an initial value and processing each element according to a specified operation.
 *
 * ### Example 1: Folding to Calculate Sum of Gene Values
 * ```kotlin
 * interface Chromosome<T, G> : Foldable<G> where G : Gene<T, G> {
 *
 *     val genes: List<G>
 *
 *     override fun <R> fold(initial: R, operation: (R, G) -> R): R {
 *         var result = initial
 *         for (gene in genes) {
 *             result = operation(result, gene)
 *         }
 *         return result
 *     }
 *
 *     override fun <R> foldRight(initial: R, operation: (G, R) -> R): R {
 *         var result = initial
 *         for (gene in genes.reversed()) {
 *             result = operation(gene, result)
 *         }
 *         return result
 *     }
 *     // ... other methods and properties ...
 * }
 *
 * // Example usage
 * val chromosome = object : Chromosome<Int, IntGene>
 * val sumOfGeneValues = chromosome.fold(0) { acc, gene -> acc + gene.value }  // Left fold, sum = 6
 * val sumOfGeneValuesRight = chromosome.foldRight(0) { gene, acc -> gene.value + acc }  // Right fold, sum = 6
 * ```
 *
 * ### Example 2: Folding to Combine Genetic Information
 * ```kotlin
 * val combinedString = chromosome.fold("") { acc, gene -> acc + gene.toString() }  // Left fold, combine gene strings
 * val combinedStringRight = chromosome.foldRight("") { gene, acc -> gene.toString() + acc }  // Right fold
 * ```
 *
 * ## Efficiency Considerations:
 * - **Folding Left (`fold`)**: Efficient for left-associative operations and when processing elements in the order they
 *   appear. Ideal for linear data structures like lists when accumulation starts from the beginning.
 * - **Folding Right (`foldRight`)**: More efficient when combining results in a right-associative manner, such as when
 *   processing elements in reverse order. This can be beneficial when working with operations that depend on the last
 *   element or when building up results from the end of a structure.
 *
 * @param T The type of elements contained in the structure.
 */
interface Foldable<T> {

    /**
     * Folds the elements of the structure from left to right, accumulating a result.
     *
     * This method starts with an initial value and processes each element of the structure from left to right (i.e., in
     * the order they appear), applying the given binary operation to the current accumulator and each element.
     *
     * ## Usage:
     * Folding to the left is typically used when the order of operations follows the structure's natural order. For
     * example, summing gene values in a chromosome where the order of genes matters.
     *
     * @param R The type of the result produced by the fold operation.
     * @param initial The initial value to start the accumulation with.
     * @param operation The binary operation to apply to the accumulator and each element of the structure.
     * @return The final accumulated result.
     */
    fun <R> fold(initial: R, operation: (R, T) -> R): R

    /**
     * Folds the elements of the structure from right to left, accumulating a result.
     *
     * This method starts with an initial value and processes each element of the structure from right to left (i.e., in
     * reverse order), applying the given binary operation to the current element and the accumulator.
     *
     * ## Usage:
     * Folding to the right is useful when the order of operations should start from the end of the structure. For
     * instance, in scenarios where the last element has more significance, or when building results in a
     * right-associative manner, such as constructing a string representation of genes in reverse order.
     *
     * @param R The type of the result produced by the fold operation.
     * @param initial The initial value to start the accumulation with.
     * @param operation The binary operation to apply to each element and the accumulator.
     * @return The final accumulated result.
     */
    fun <R> foldRight(initial: R, operation: (T, R) -> R): R
}
