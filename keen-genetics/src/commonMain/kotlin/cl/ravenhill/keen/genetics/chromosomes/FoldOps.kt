/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics.chromosomes

import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.mixins.Foldable

/**
 * Provides folding operations for genes in a chromosome.
 *
 * The `FoldOps` class implements the [Foldable] interface, offering methods to fold (reduce) the values of genes within
 * a chromosome. These folding operations are performed from left to right or right to left, allowing for accumulation
 * of results across the chromosome's genes. This is useful for various operations, such as summing values, building
 * strings, or aggregating results based on the gene values.
 *
 * @param T The type of value held by the genes.
 * @param G The type of gene in the chromosome, which must extend [Gene].
 * @property genes The list of genes within the chromosome.
 */
interface FoldOps<T, G> : Foldable<T> where G : Gene<T, G> {

    val genes: List<G>

    /**
     * Folds the genes in the chromosome from left to right, accumulating a result.
     *
     * This method starts with an initial value and processes each gene in the chromosome from left to right, applying the
     * given binary operation to the accumulator and each gene's value. The result is accumulated step by step until the
     * final result is obtained.
     *
     * ### Example: Calculating the Sum of Gene Values
     * Suppose you have a chromosome where each gene holds an integer value, and you want to calculate the sum of these
     * values:
     * ```kotlin
     * // Chromosome: [1, 2, 3, 4, 5]
     * val chromosome: Chromosome<Int, MyGene> = // obtain a chromosome instance
     * val sumOfGeneValues = chromosome.fold(0) { acc, value -> acc + value }
     * println(sumOfGeneValues) // Output: 15
     * ```
     *
     * @param R The type of the result produced by the fold operation.
     * @param initial The initial value to start the accumulation with.
     * @param operation The binary operation to apply to the accumulator and each gene's value.
     * @return The final accumulated result after processing all genes from left to right.
     */
    override fun <R> fold(initial: R, operation: (R, T) -> R): R =
        genes.fold(initial) { acc, gene -> operation(acc, gene.value) }

    /**
     * Folds the genes in the chromosome from right to left, accumulating a result.
     *
     * This method starts with an initial value and processes each gene in the chromosome from right to left, applying the
     * given binary operation to each gene's value and the accumulator. The result is accumulated step by step, starting
     * from the last gene and moving toward the first.
     *
     * ### Example: Building a String Representation of Gene Values in Reverse Order
     * Suppose you have a chromosome where each gene holds a character, and you want to build a string that represents
     * the gene values in reverse order:
     * ```kotlin
     * val chromosome: Chromosome<Char, MyGene> = // obtain a chromosome instance
     * val reversedGeneString = chromosome.foldRight("") { value, acc -> value + acc }
     * println(reversedGeneString) // Output: the gene values concatenated in reverse order
     * ```
     *
     * ## Efficiency Considerations:
     * - **Folding Left (`fold`)**: Efficient when the order of operations naturally follows the structure's sequence.
     *   For example, summing values or processing genes in their natural order.
     * - **Folding Right (`foldRight`)**: More efficient for right-associative operations, such as when building results
     *   from the last element or constructing a data structure that depends on the order starting from the end.
     *
     * @param R The type of the result produced by the fold operation.
     * @param initial The initial value to start the accumulation with.
     * @param operation The binary operation to apply to each gene's value and the accumulator.
     * @return The final accumulated result after processing all genes from right to left.
     */
    override fun <R> foldRight(initial: R, operation: (T, R) -> R): R =
        genes.foldRight(initial) { gene, acc -> operation(gene.value, acc) }
}
