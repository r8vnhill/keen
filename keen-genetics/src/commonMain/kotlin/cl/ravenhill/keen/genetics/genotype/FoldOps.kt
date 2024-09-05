/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics.genotype

import cl.ravenhill.keen.genetics.chromosomes.Chromosome
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.mixins.Foldable

/**
 * Provides folding operations for a collection of chromosomes in an evolutionary algorithm.
 *
 * The `FoldOps` class implements the [Foldable] interface and provides methods to fold (reduce) the values of genes
 * across multiple chromosomes. These operations allow processing all genes within a genotype by applying a binary
 * operation to an initial value and each gene's value, either from left to right or right to left. This is useful in
 * evolutionary algorithms where results need to be aggregated from the genetic structure of individuals.
 *
 * @param T The type of value held by the genes.
 * @param G The type of gene within the chromosomes, which must extend [Gene].
 * @property chromosomes The list of chromosomes that contain the genes to be folded.
 */
internal class FoldOps<T, G>(private val chromosomes: List<Chromosome<T, G>>) : Foldable<T> where G : Gene<T, G> {

    /**
     * Folds the values of all genes in the genotype from left to right, accumulating a result.
     *
     * The `fold` function allows you to reduce the entire genotype to a single value by applying a binary operation
     * to an initial value and each gene's value within each chromosome, processing elements sequentially from the
     * first gene in the first chromosome to the last gene in the last chromosome. This is particularly useful for
     * operations that require aggregating or combining values across all genes in the genotype.
     *
     * ### Example: Summing Gene Values in the Genotype
     * Suppose you want to calculate the sum of all gene values across all chromosomes in the genotype:
     * ```kotlin
     * val chromosome1 = IntChromosome(IntGene(1), IntGene(2))
     * val chromosome2 = IntChromosome(IntGene(3), IntGene(4))
     * val genotype = Genotype(chromosome1, chromosome2)
     * val totalGeneValue = genotype.fold(0) { acc, value -> acc + value }
     * println(totalGeneValue) // Output: 10
     * ```
     *
     * @param R The type of the result produced by the fold operation.
     * @param initial The initial value to start the accumulation with.
     * @param operation The binary operation to apply to the accumulator and each gene's value.
     * @return The final accumulated result after processing all genes from left to right.
     */
    override fun <R> fold(initial: R, operation: (R, T) -> R): R =
        chromosomes.fold(initial) { acc, chromosome -> chromosome.fold(acc, operation) }

    /**
     * Folds the values of all genes in the genotype from right to left, accumulating a result.
     *
     * The `foldRight` function allows you to reduce the entire genotype to a single value by applying a binary
     * operation to each gene's value within each chromosome and an initial value, processing elements from the last
     * gene in the last chromosome to the first gene in the first chromosome. This is particularly useful for operations
     * where the order of processing should start from the end of the genotype and move towards the beginning.
     *
     * ### Example: Creating a String Representation of Genes in Reverse Order
     * Suppose you want to build a string that represents the values of all genes across all chromosomes in reverse
     * order:
     * ```kotlin
     * val chromosome1 = CharChromosome(CharGene('A'), CharGene('B'))
     * val chromosome2 = CharChromosome(CharGene('C'), CharGene('D'))
     * val genotype = Genotype(chromosome1, chromosome2)
     * val reversedGeneString = genotype.foldRight("") { value, acc -> value + acc }
     * println(reversedGeneString) // Output: "DCBA"
     * ```
     *
     * ## Efficiency Considerations:
     * - **Folding Left (`fold`)**: Efficient for operations where the accumulation order follows the sequence of
     *   genes from first to last. Ideal for summing values or combining results in the natural order of the genotype.
     * - **Folding Right (`foldRight`)**: More efficient for operations where accumulation should start from the last
     *   gene and proceed to the first, such as when building results that depend on the reverse order of the genes.
     *
     * @param R The type of the result produced by the fold operation.
     * @param initial The initial value to start the accumulation with.
     * @param operation The binary operation to apply to each gene's value and the accumulator.
     * @return The final accumulated result after processing all genes from right to left.
     */
    override fun <R> foldRight(initial: R, operation: (T, R) -> R): R =
        chromosomes.foldRight(initial) { chromosome, acc -> chromosome.foldRight(acc, operation) }
}
