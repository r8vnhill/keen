/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics.chromosomes

import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.mixins.FlatMappable
import cl.ravenhill.keen.repr.Representation

/**
 * Interface representing a chromosome in the Keen evolutionary computation framework.
 *
 * The `Chromosome` interface defines the basic structure and behavior of a chromosome, which is a collection of genes.
 * It extends the [Representation], [Collection], and [FlatMappable] interfaces, providing additional functionality
 * specific to chromosomes, such as verification and flattening.
 *
 * ## Usage:
 * This interface is used to define the behavior and properties of chromosomes in evolutionary algorithms. Implementing
 * classes must provide the `genes` property and may override the `verify` and `flatten` methods to customize the
 * chromosome's behavior.
 *
 * ### Example 1: Implementing a Chromosome
 * ```kotlin
 * data class MyChromosome(
 *     override val genes: List<MyGene>
 * ) : Chromosome<Int, MyGene> {
 *     override fun duplicateWithGenes(newGenes: List<MyGene>) = copy(genes = newGenes)
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
interface Chromosome<T, G> : Representation<T, G>, Collection<G>, FlatMappable<T> where G : Gene<T, G> {

    /**
     * The list of genes that make up the chromosome.
     */
    val genes: List<G>

    /**
     * The size of the chromosome, which is the number of genes it contains.
     */
    override val size: Int
        get() = genes.size

    /**
     * Checks if the chromosome is empty, i.e., contains no genes.
     *
     * @return true if the chromosome is empty, false otherwise.
     */
    override fun isEmpty() = genes.isEmpty()

    /**
     * Provides an iterator over the genes in the chromosome.
     *
     * This method returns an iterator that allows for iterating through the genes contained within the chromosome.
     * It provides a way to traverse the chromosome in a sequential manner, accessing each gene in turn.
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
     * ### Example 2: Using Iterator Directly
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
     * Checks if the chromosome contains all the specified genes.
     *
     * @param elements The genes to check for.
     * @return true if the chromosome contains all the specified genes, false otherwise.
     */
    override fun containsAll(elements: Collection<G>) = genes.containsAll(elements)

    /**
     * Checks if the chromosome contains the specified gene.
     *
     * @param element The gene to check for.
     * @return true if the chromosome contains the specified gene, false otherwise.
     */
    override fun contains(element: G) = genes.contains(element)

    /**
     * Flattens the chromosome to a list containing the values of all its genes.
     *
     * @return A list containing the values of all the genes in the chromosome.
     */
    override fun flatten() = genes.flatMap { it.flatten() }

    /**
     * Verifies the correctness or validity of the chromosome.
     *
     * @return true if all genes in the chromosome are verified, false otherwise.
     */
    override fun verify(): Boolean = genes.all { it.verify() }
}
