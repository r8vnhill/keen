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
import cl.ravenhill.keen.features.Representation
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Represents a genotype in an evolutionary algorithm.
 *
 * The `Genotype` class encapsulates a collection of chromosomes and provides various methods for manipulating
 * and querying the genotype. It implements the [Representation], [GeneticMaterial], and [Collection] interfaces,
 * allowing it to be used flexibly within evolutionary algorithms.
 *
 * ## Usage:
 * This class is used to manage the collection of chromosomes that make up an individual's genotype in evolutionary
 * algorithms. It provides methods for accessing, verifying, and manipulating the chromosomes.
 *
 * ### Example:
 * ```
 * val chromosome1 = Chromosome(...)
 * val chromosome2 = Chromosome(...)
 * val genotype = Genotype(chromosome1, chromosome2)
 * println(genotype.size) // Prints the number of chromosomes
 * ```
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @property chromosomes The list of chromosomes in the genotype.
 * @constructor Creates an instance of `Genotype` with the specified list of chromosomes.
 */
data class Genotype<T, G>(val chromosomes: List<Chromosome<T, G>>) :
    Representation<T, G>, GeneticMaterial<T, G>, Collection<Chromosome<T, G>> where G : Gene<T, G> {

    /**
     * Secondary constructor for creating a `Genotype` instance with a vararg of chromosomes.
     *
     * This constructor converts the vararg of chromosomes into a list.
     *
     * @param chromosomes Vararg of chromosomes to be included in the genotype.
     */
    constructor(vararg chromosomes: Chromosome<T, G>) : this(chromosomes.toList())

    /**
     * The number of chromosomes in the genotype.
     */
    override val size = chromosomes.size

    /**
     * Checks if the genotype is empty.
     *
     * @return `true` if there are no chromosomes, `false` otherwise.
     */
    override fun isEmpty() = chromosomes.isEmpty()

    /**
     * Checks if the genotype contains all the specified chromosomes.
     *
     * @param elements The collection of chromosomes to check.
     * @return `true` if all the specified chromosomes are contained in the genotype, `false` otherwise.
     */
    override fun containsAll(elements: Collection<Chromosome<T, G>>) = chromosomes.containsAll(elements)

    /**
     * Checks if the genotype contains the specified chromosome.
     *
     * @param element The chromosome to check.
     * @return `true` if the chromosome is contained in the genotype, `false` otherwise.
     */
    override fun contains(element: Chromosome<T, G>) = chromosomes.contains(element)

    /**
     * Verifies the integrity of the genotype.
     *
     * @return `true` if all chromosomes are valid, `false` otherwise.
     */
    override fun verify() = chromosomes.all { it.verify() }

    /**
     * Returns an iterator over the chromosomes in the genotype.
     *
     * @return An iterator over the chromosomes.
     */
    override fun iterator() = chromosomes.iterator()

    /**
     * Flattens the genotype into a list of values from all chromosomes.
     *
     * @return A flat list of values.
     */
    override fun flatten() = chromosomes.flatMap { it.flatten() }

    /**
     * Applies a transformation function to each element in the genotype and returns a list of the results.
     *
     * The `flatMap` method allows for applying a specified transformation function to each element contained within
     * the chromosomes of the genotype. The results are then flattened into a single list.
     *
     * ## Usage:
     * This method is useful for scenarios where you need to transform and flatten the elements of a genotype, such as
     * extracting specific properties or computing derived values from the features contained within the chromosomes.
     *
     * ### Example:
     * ```
     * val genotype = Genotype(listOf(chromosome1, chromosome2))
     * val transformedElements = genotype.flatMap { gene ->
     *     // Transformation logic, e.g., extracting gene values
     *     gene.value
     * }
     * ```
     * In this example, the `flatMap` method is used to extract and flatten the values of genes across all chromosomes
     * in the genotype.
     *
     * @param U The type of elements in the resulting list.
     * @param transform The transformation function to apply to each element.
     * @return A list of transformed elements.
     */
    override fun <U> flatMap(transform: (T) -> U): List<U> = chromosomes.flatMap { it.flatMap(transform) }

    /**
     * Accesses the chromosome at the specified index.
     *
     * @param index The index of the chromosome to access.
     * @return The chromosome at the specified index.
     * @throws CompositeException containing all the exceptions thrown by the constraints.
     * @throws InvalidIndexException if the index is out of range.
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
     * @return A string representation of the genotype based on the current [Domain.toStringMode].
     */
    override fun toString() = when (Domain.toStringMode) {
        ToStringMode.SIMPLE -> chromosomes.joinToString(separator = ", ", prefix = "[", postfix = "]")
        else -> "Genotype(chromosomes=$chromosomes)"
    }

    /**
     * Factory class for creating `Genotype` instances.
     *
     * The `Factory` class allows for the configuration and creation of genotypes with specified chromosomes.
     *
     * ### Example:
     * ```
     * val chromosomeFactory = Chromosome.Factory(...)
     * val genotypeFactory = Genotype.Factory<...>().apply {
     *     chromosomes += chromosomeFactory
     * }
     * val genotype = genotypeFactory.make()
     * ```
     *
     * @param T The type of the value held by the genes.
     * @param G The type of the gene, which must extend [Gene].
     */
    class Factory<T, G> where G : Gene<T, G> {

        /**
         * The list of chromosome factories used to create chromosomes for the genotype.
         */
        var chromosomes: MutableList<Chromosome.Factory<T, G>> = mutableListOf()

        /**
         * Creates a `Genotype` instance using the configured chromosome factories.
         *
         * @return A new `Genotype` instance.
         */
        fun make() = Genotype(chromosomes.map { it.make() })
    }
}
