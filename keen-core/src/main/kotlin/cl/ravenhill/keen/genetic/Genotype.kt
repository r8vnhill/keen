/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BeInRange
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ToStringMode
import cl.ravenhill.keen.exceptions.InvalidIndexException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.repr.Representation


/**
 * Represents a genotype in an evolutionary algorithm, consisting of a list of chromosomes.
 *
 * The `Genotype` class encapsulates a collection of chromosomes that represent an individual's genotype in an
 * evolutionary algorithm. It implements the [Representation] and [Collection] interfaces, providing methods for
 * verification, flattening, and collection operations.
 *
 * It is **recommended** to use the [Factory] class to create instances of `Genotype` to ensure proper configuration.
 *
 * ## Usage:
 * Use this class to represent the genotype of individuals in an evolutionary algorithm. The genotype can be verified
 * for consistency, flattened into a list of genes, and accessed by index.
 *
 * ### Example:
 * ```kotlin
 * val gene1 = IntGene(1, 0..10)
 * val gene2 = IntGene(2, 0..10)
 * val chromosome1 = IntChromosome(gene1)
 * val chromosome2 = IntChromosome(gene2)
 * val genotype = Genotype(chromosome1, chromosome2)
 *
 * println(genotype) // Output: [IntChromosome([IntGene(1)]), IntChromosome([IntGene(2)])]
 * println(genotype.verify()) // Output: true
 * println(genotype.flatten()) // Output: [1, 2]
 * println(genotype[0]) // Output: IntChromosome([IntGene(1)])
 * ```
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @property chromosomes The list of chromosomes that make up the genotype.
 * @constructor Creates an instance of `Genotype` with the specified list of chromosomes.
 */
data class Genotype<T, G>(val chromosomes: List<Chromosome<T, G>>) :
    Representation<T, G>, Collection<Chromosome<T, G>> where G : Gene<T, G> {

    /**
     * Secondary constructor for creating a `Genotype` instance using a vararg of chromosomes.
     *
     * This constructor allows for the creation of a `Genotype` by directly passing the chromosomes as varargs,
     * simplifying the initialization process.
     *
     * @param chromosomes The chromosomes to include in the genotype.
     */
    constructor(vararg chromosomes: Chromosome<T, G>) : this(chromosomes.toList())

    override val size = chromosomes.size

    override fun isEmpty() = chromosomes.isEmpty()

    override fun containsAll(elements: Collection<Chromosome<T, G>>) = chromosomes.containsAll(elements)

    override fun contains(element: Chromosome<T, G>) = chromosomes.contains(element)

    /**
     * Verifies the consistency and validity of the genotype.
     *
     * @return `true` if all chromosomes in the genotype are valid, `false` otherwise.
     */
    override fun verify() = chromosomes.all { it.verify() }

    override fun iterator() = chromosomes.iterator()

    /**
     * Flattens the genotype into a list of elements by flattening each chromosome.
     *
     * @return A list of elements from the flattened genotype.
     */
    override fun flatten() = chromosomes.flatMap { it.flatten() }

    /**
     * Gets the chromosome at the specified index, with bounds checking.
     *
     * @param index The index of the chromosome to retrieve.
     * @return The chromosome at the specified index.
     * @throws CompositeException if any constraints are violated.
     * @throws InvalidIndexException if the index is out of bounds.
     */
    operator fun get(index: Int): Chromosome<T, G> {
        constraints {
            "The index [$index] must be in the range [0, $size)"(::InvalidIndexException) {
                index must BeInRange(0..chromosomes.lastIndex)
            }
        }
        return chromosomes[index]
    }

    /**
     * Returns a string representation of the genotype.
     *
     * @return A string representation based on the current domain's toString mode.
     */
    override fun toString() = when (Domain.toStringMode) {
        ToStringMode.SIMPLE -> chromosomes.joinToString(separator = ", ", prefix = "[", postfix = "]")
        else -> "Genotype(chromosomes=$chromosomes)"
    }

    /**
     * Factory class for creating `Genotype` instances.
     *
     * The `Factory` class allows for the creation of `Genotype` instances with specified configurations for the
     * chromosomes.
     *
     * ### Example:
     * ```kotlin
     * val chromosomeFactory = IntChromosome.Factory()
     * val genotypeFactory = Genotype.Factory<Int, IntGene>().apply {
     *     chromosomes += chromosomeFactory
     * }
     * val genotype = genotypeFactory.make()
     * ```
     */
    class Factory<T, G> where G : Gene<T, G> {

        /**
         * The list of chromosome factories used to create the chromosomes in the genotype.
         */
        var chromosomes: MutableList<Chromosome.Factory<T, G>> = mutableListOf()

        /**
         * Creates a `Genotype` instance using the configured chromosome factories.
         *
         * @return A new `Genotype` instance with the created chromosomes.
         */
        fun make() = Genotype(chromosomes.map { it.make() })
    }
}
