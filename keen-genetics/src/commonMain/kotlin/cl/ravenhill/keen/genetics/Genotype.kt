/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics

import cl.ravenhill.jakt.Jakt.constraints
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
                index in this@Genotype.indices
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
