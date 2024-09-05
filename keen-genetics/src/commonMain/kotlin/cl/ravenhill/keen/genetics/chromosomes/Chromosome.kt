/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics.chromosomes

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constraints.ints.BeInRange
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.exceptions.InvalidIndexException
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.mixins.FlatMappable
import cl.ravenhill.keen.mixins.Foldable
import cl.ravenhill.keen.repr.Representation

/**
 * Represents a chromosome in the Keen evolutionary computation framework.
 *
 * The `Chromosome` interface defines the structure and behavior of a chromosome, which is a collection of genes that
 * encode a solution or candidate in evolutionary algorithms. It extends the [Representation], [Collection], and
 * [FlatMappable] interfaces, providing additional functionality specific to chromosomes, such as verification,
 * duplication, and flattening.
 *
 * ## Usage:
 * Implement this interface to define the behavior and properties of chromosomes in evolutionary algorithms. Classes
 * implementing this interface must provide the `genes` property, which represents the sequence of genes
 * that make up the chromosome. Implementing classes can also override methods like `verify` and `flatten` to
 * customize the chromosome's behavior.
 *
 * ### Example 1: Implementing a Chromosome
 * ```kotlin
 * data class MyChromosome(
 *     override val genes: List<MyGene>
 * ) : Chromosome<Int, MyGene> {
 *     override fun copyWithGenes(newGenes: List<MyGene>) = copy(genes = newGenes)
 * }
 * ```
 *
 * ### Example 2: Verifying a Chromosome
 * ```kotlin
 * val chromosome = MyChromosome(listOf(MyGene(1) { random -> random.nextInt(0, 10) }))
 * val isValid = chromosome.verify()
 * ```
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @property genes The list of genes that make up the chromosome.
 */
interface Chromosome<T, G> : Representation<T, G>, Collection<G>, FlatMappable<T>, ContainOps<T, G>, FoldOps<T, G>
        where G : Gene<T, G> {

    /**
     * The list of genes that make up the chromosome.
     */
    override val genes: List<G>

    /**
     * The size of the chromosome, representing the number of genes it contains.
     */
    override val size: Int
        get() = genes.size

    /**
     * Creates a new chromosome with a specified list of genes.
     *
     * This method returns a new instance of the chromosome with the provided list of genes, preserving the other
     * properties of the chromosome.
     *
     * @param newGenes The list of genes to use in the new chromosome.
     * @return A new `Chromosome` instance with the specified genes.
     */
    fun copyWithGenes(newGenes: List<G>): Chromosome<T, G>

    /**
     * Checks if the chromosome is empty, meaning it contains no genes.
     *
     * @return `true` if the chromosome is empty, `false` otherwise.
     */
    override fun isEmpty() = genes.isEmpty()

    /**
     * Provides an iterator over the genes in the chromosome.
     *
     * This method allows for sequential traversal of the genes within the chromosome.
     *
     * ## Usage:
     * The iterator can be used in a `for` loop or other iterative constructs to process each gene in the chromosome.
     *
     * ### Example 1: Iterating Over Genes
     * ```kotlin
     * val chromosome: Chromosome<Int, MyGene> = // obtain a chromosome instance
     * for (gene in chromosome) {
     *     println(gene)
     * }
     * ```
     *
     * ### Example 2: Using the Iterator Directly
     * ```kotlin
     * val chromosome: Chromosome<Int, MyGene> = // obtain a chromosome instance
     * val iterator = chromosome.iterator()
     * while (iterator.hasNext()) {
     *     val gene = iterator.next()
     *     println(gene)
     * }
     * ```
     *
     * @return An iterator over the genes in the chromosome.
     */
    override fun iterator() = genes.iterator()

    /**
     * Flattens the chromosome to a list containing the values of all its genes.
     *
     * This method collects the values from each gene in the chromosome and returns them as a flat list.
     *
     * @return A list containing the values of all the genes in the chromosome.
     */
    override fun flatten() = genes.flatMap { it.toList() }

    /**
     * Verifies the correctness or validity of the chromosome.
     *
     * This method checks the validity of each gene in the chromosome, ensuring that all genes meet the required
     * conditions.
     *
     * @return `true` if all genes in the chromosome are verified, `false` otherwise.
     */
    override fun verify(): Boolean = genes.all { it.verify() }

    /**
     * Retrieves the gene at the specified index within the chromosome.
     *
     * The `get` operator function provides safe access to the genes within a chromosome by validating the provided
     * index before attempting to retrieve the gene. If the index is within bounds, the function returns the gene
     * wrapped in an [Either.Right]. If the index is out of bounds, the function returns an [Either.Left] containing a
     * [CompositeException] that describes the error.
     *
     * @param index The index of the gene to be retrieved from the chromosome.
     * @return An `Either<CompositeException, G>` where [G] is the type of the gene:
     * - `Either.Right<G>` containing the gene if the index is valid.
     * - `Either.Left<CompositeException>` containing an error if the index is invalid.
     */
    operator fun get(index: Int): Either<CompositeException, G> = constrained {
        "Index ($index) must be within the bounds of the chromosome [0, ${size - 1}]"(::InvalidIndexException) {
            index must BeInRange(this@Chromosome.indices)
        }
    }.fold(
        ifLeft = { it.left() }, // If the index is invalid, return the error
        ifRight = { genes[index].right() } // If the index is valid, return the gene
    )

    /**
     * Transforms the genes in the chromosome by applying a given function to each gene's value.
     *
     * The `map` function creates a new chromosome by applying the provided transformation function to each gene in the
     * current chromosome. It produces a new chromosome instance with the transformed genes, preserving the structure of
     * the original chromosome. This function is typically used to apply a uniform operation to each gene, such as
     * scaling, shifting, or otherwise modifying the gene values.
     *
     * @param transform The transformation function to apply to each gene's value.
     * @return A new chromosome with the transformed gene values.
     */
    override fun map(transform: (T) -> T) = copyWithGenes(genes.map { it.map(transform) })

    override fun drop(n: Int): Either<CompositeException, Representation<T, G>> {
        constrained {
            "Number of elements to drop ($n) must be non-negative" { n must BeInRange(0..size) }
        }.onLeft { return it.left() }
        return copyWithGenes(genes.drop(n)).right()
    }

    override fun take(n: Int): Either<Exception, Representation<T, G>> {
        constrained {
            "Number of elements to take ($n) must be non-negative" { n must BeInRange(0..size) }
        }.onLeft { return it.left() }
        return copyWithGenes(genes.take(n)).right()
    }
}
