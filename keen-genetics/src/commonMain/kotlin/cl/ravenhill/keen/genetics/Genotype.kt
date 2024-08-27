/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BeInRange
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ToStringMode
import cl.ravenhill.keen.exceptions.InvalidIndexException
import cl.ravenhill.keen.genetics.chromosomes.Chromosome
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.repr.Representation

/**
 * Represents a genotype in the evolutionary computation framework.
 *
 * The `Genotype` class encapsulates a collection of chromosomes, providing functionalities to access and manipulate
 * the genetic information of an individual. It implements the [Representation] and [Collection] interfaces, allowing
 * it to be used in various evolutionary operations.
 *
 * ## Usage:
 * The `Genotype` class is typically used in evolutionary algorithms to represent the structure and genetic composition
 * of individuals in a population.
 *
 * ### Example 1: Creating a Genotype with a List of Chromosomes
 * ```kotlin
 * val chromosome1 = Chromosome(Gene(1), Gene(2), Gene(3))
 * val chromosome2 = Chromosome(Gene(4), Gene(5), Gene(6))
 * val genotype = Genotype(listOf(chromosome1, chromosome2))
 * ```
 *
 * ### Example 2: Creating a Genotype with Vararg Chromosomes
 * ```kotlin
 * val chromosome1 = Chromosome(listOf(Gene(1), Gene(2), Gene(3)))
 * val chromosome2 = Chromosome(listOf(Gene(4), Gene(5), Gene(6)))
 * val genotype = Genotype(chromosome1, chromosome2)
 * ```
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @property chromosomes The list of chromosomes that make up the genotype.
 * @constructor Creates a `Genotype` instance with the specified list of chromosomes.
 */
data class Genotype<T, G>(val chromosomes: List<Chromosome<T, G>>) : Representation<T, G>,
    Collection<Chromosome<T, G>> where G : Gene<T, G> {

    /**
     * Secondary constructor for creating a `Genotype` instance using a vararg of chromosomes.
     *
     * @param chromosomes The chromosomes that make up the genotype.
     */
    constructor(vararg chromosomes: Chromosome<T, G>) : this(chromosomes.toList())

    /**
     * Flattens the genotype by collecting the value of all genes in all chromosomes.
     *
     * @return A list of all the values in the genotype.
     */
    override fun flatten(): List<T> = chromosomes.flatMap { it.flatten() }

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

    /**
     * The number of chromosomes in the genotype.
     */
    override val size = chromosomes.size

    /**
     * Checks if the genotype is empty (i.e., contains no chromosomes).
     *
     * @return `true` if the genotype is empty, `false` otherwise.
     */
    override fun isEmpty() = chromosomes.isEmpty()

    /**
     * Provides an iterator over the chromosomes in the genotype.
     *
     * The `iterator` method allows for iterating over each chromosome within the genotype. This is useful for
     * traversing the genetic structure of an individual, enabling operations that require sequential access to each
     * chromosome.
     *
     * ## Usage:
     * This method can be used in loops and other iterative constructs to process each chromosome individually.
     *
     * ### Example:
     * ```kotlin
     * val chromosome1 = MyChromosome(MyGene(1), MyGene(2))
     * val chromosome2 = MyChromosome(MyGene(3), MyGene(4))
     * val genotype = Genotype(chromosome1, chromosome2)
     * for (chromosome in genotype) {
     *     println(chromosome)
     * }
     * // Output will be:
     * // Chromosome(genes=[Gene(1), Gene(2)])
     * // Chromosome(genes=[Gene(3), Gene(4)])
     * ```
     *
     * @return An iterator over the chromosomes in the genotype.
     */
    override fun iterator() = chromosomes.iterator()

    /**
     * Checks if the genotype contains all the specified chromosomes.
     *
     * @param elements The collection of chromosomes to check for containment.
     * @return `true` if the genotype contains all the specified chromosomes, `false` otherwise.
     */
    override fun containsAll(elements: Collection<Chromosome<T, G>>) = chromosomes.containsAll(elements)

    /**
     * Checks if the genotype contains the specified chromosome.
     *
     * @param element The chromosome to check for containment.
     * @return `true` if the genotype contains the specified chromosome, `false` otherwise.
     */
    override fun contains(element: Chromosome<T, G>) = chromosomes.contains(element)

    /**
     * Verifies the validity of the genotype by checking all its chromosomes.
     *
     * @return `true` if all chromosomes in the genotype are valid, `false` otherwise.
     */
    override fun verify() = chromosomes.all { it.verify() }

    /**
     * Retrieves the chromosome at the specified index.
     *
     * @param index The index of the chromosome to retrieve.
     * @return The chromosome at the specified index.
     * @throws InvalidIndexException If the index is out of range.
     */
    operator fun get(index: Int): Chromosome<T, G> {
        constraints {
            "The index ($index) must be in the range [0, $size)"(::InvalidIndexException) {
                index must BeInRange(this@Genotype.indices)
            }
        }
        return chromosomes[index]
    }

    /**
     * Returns a string representation of the genotype.
     *
     * @return A string representation of the genotype.
     */
    override fun toString() = when (Domain.toStringMode) {
        ToStringMode.SIMPLE -> chromosomes.joinToString(separator = ", ", prefix = "[", postfix = "]")
        else -> "Genotype(chromosomes=$chromosomes)"
    }
}
