/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes.numeric

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.numeric.IntChromosome.Factory
import cl.ravenhill.keen.genetic.genes.numeric.IntGene

/**
 * Represents a chromosome composed of integer-valued genes.
 *
 * The `IntChromosome` class encapsulates a sequence of genes where each gene holds an integer value. This chromosome
 * type is especially useful in evolutionary algorithms where genetic information is expressed as integers, such as in
 * combinatorial optimization problems.
 *
 * ## Characteristics:
 * - **Gene Type**: Uses [IntGene], allowing representation of numerical values as integers.
 * - **Flexibility**: Capable of representing a broad spectrum of numerical values, adhering to constraints defined in
 *   the genes.
 * - **Customizable**: Through the associated [Factory], the chromosome can be tailored with specific ranges
 *   and filters for its genes, making it adaptable to various problem domains.
 *
 * ## Usage:
 * `IntChromosome` is typically utilized in gene-centric evolutionary algorithms where integer values are manipulated.
 * It's suitable for scenarios like discrete optimization problems, where the solution space is explored using integer
 * values.
 *
 * ### Example:
 * Creating an `IntChromosome` with specific range and filter for its genes:
 * ```kotlin
 * val chromosome = IntChromosome(IntGene(1, 0..10, { it > 5 }), IntGene(5, 0..10, { it < 5 }))
 * // Equivalent to:
 * val chromosome = IntChromosome(listOf(IntGene(1, 0..10, { it > 5 }), IntGene(5, 0..10, { it < 5 })))
 * ```
 *
 * @property genes The list of [IntGene] instances forming the chromosome.
 * @constructor Creates a new [IntChromosome] instance with the specified list of genes.
 */
data class IntChromosome(override val genes: List<IntGene>) : NumberChromosome<Int, IntGene> {

    /**
     * Secondary constructor for creating an [IntChromosome] instance using a vararg of [IntGene]s.
     *
     * This constructor simplifies the creation of an [IntChromosome] by allowing the direct input of [IntGene]
     * objects, rather than requiring a pre-constructed list. It is particularly helpful when the number of genes is
     * predetermined or in cases of creating a chromosome with a minimal number of genes for simplicity or testing
     * purposes.
     *
     * ## Usage:
     * Enables direct specification of genes without needing to construct a list, enhancing code clarity and usability,
     * especially when the genes are predetermined and static.
     *
     * ### Example:
     * ```
     * // Creating an IntChromosome with individual IntGene instances
     * val gene1 = IntGene(3, 0..5)
     * val gene2 = IntGene(4, 0..5)
     * val chromosome = IntChromosome(gene1, gene2)
     * ```
     * Here, `IntChromosome` is directly instantiated with individual `IntGene` objects, offering a more concise and
     * straightforward approach, especially for a small set of genes.
     *
     * @param genes Varargs of [IntGene] instances to be included in the chromosome.
     */
    constructor(vararg genes: IntGene) : this(genes.toList())

    /**
     * Creates a new [IntChromosome] instance with specified list of [IntGene]s.
     *
     * Essential for evolutionary algorithm operations like crossover and mutation, this method forms a new chromosome
     * with an altered set of genes. `duplicateWithGenes` ensures the original chromosome's structure remains intact
     * while allowing for genetic variations.
     *
     * ## Functionality:
     * - Takes a list of [IntGene]s and constructs a new [IntChromosome] with these genes.
     * - Maintains chromosome immutability, where modifications result in new instances rather than altering
     *   the existing one.
     *
     * ## Usage:
     * Commonly used in genetic operations requiring new chromosomes based on existing genetic material, such as during
     * crossover or mutation processes.
     *
     * ### Example:
     * ```
     * val originalChromosome = IntChromosome(IntGene(2, 0..3), IntGene(3, 0..3))
     * val mutatedGenes = listOf(IntGene(2, 0..3), IntGene(1, 0..3))
     * val newChromosome = originalChromosome.duplicateWithGenes(mutatedGenes)
     * ```
     * This example demonstrates using `duplicateWithGenes` to create a new `IntChromosome` with a modified
     * set of genes, representing a mutation from the original chromosome.
     *
     * @param genes A list of [IntGene]s to include in the new chromosome.
     * @return A new [IntChromosome] instance containing the provided genes.
     */
    override fun duplicateWithGenes(genes: List<IntGene>) = IntChromosome(genes)

    /**
     * Factory class for creating [IntChromosome] instances.
     *
     * Extends [Chromosome.AbstractFactory] and implements [NumberChromosome.Factory], specializing in generating
     * [IntChromosome] instances. It enables custom chromosome configurations, particularly in terms of gene ranges and
     * filtering criteria.
     *
     * ## Key Features:
     * - **Range Customization**: Defines the value range for each gene in the chromosome via [ranges].
     * - **Filter Customization**: Allows specifying custom filter functions for genes through [filters].
     * - **Dynamic Range and Filter Adjustment**: Ensures consistency between the number of genes and their
     *   corresponding ranges or filters, adjusting them based on the chromosome's size.
     *
     * ## Usage:
     * Utilized to create [IntChromosome] instances with specific characteristics. Configuring [ranges] and
     * [filters] tailors the chromosome to particular needs.
     *
     * ### Example:
     * ```
     * val chromosomeFactory = IntChromosome.Factory().apply {
     *     size = 4
     *     ranges += 0..10  // Apply this range to all genes
     *     filters += { it >= 0 } // Apply this filter to all genes
     * }
     * val chromosome = chromosomeFactory.make() // Creates an IntChromosome with the specified configuration
     * ```
     * Here, an [IntChromosome] factory is set up with a specific range and filter for all genes. The `make`
     * method then produces a chromosome instance based on these settings.
     *
     * @property ranges Mutable list of [ClosedRange]<[Int]> specifying the value range for each gene.
     * @property filters Mutable list of filter functions, each accepting an [Int] and returning a [Boolean].
     * @property defaultRange The default range to use when no other range is specified. Defaults to the entire range of
     *   [Int].
     */
    class Factory : Chromosome.AbstractFactory<Int, IntGene>(), NumberChromosome.Factory<Int, IntGene> {

        override var ranges = mutableListOf<ClosedRange<Int>>()

        override var filters = mutableListOf<(Int) -> Boolean>()

        override val defaultRange = Int.MIN_VALUE..Int.MAX_VALUE


        /**
         * Creates an [IntChromosome] instance using the current configuration.
         *
         * Constructs an [IntChromosome] by generating a list of [IntGene]s based on the specified ranges,
         * filters, and executor. Each gene in the chromosome is crafted using the range and filter at its
         * respective index, while the executor is responsible for instantiating each gene, ensuring adherence
         * to the set constraints.
         *
         * ## Process:
         * - Iterates through each index up to the chromosome's specified size.
         * - At each index, an [IntGene] is created within the corresponding range and tested against the
         *   respective filter.
         * - These genes form an [IntChromosome] using the [executor].
         *
         * ## Assumptions:
         * - [ranges] and [filters] lists are pre-adjusted to align with the chromosome size, as per the
         *   [adjustRangesAndFilters] method.
         *
         * @return A newly created [IntChromosome] with genes conforming to the set constraints.
         */
        override fun createChromosome(): IntChromosome {
            constraints {
                val emptyRanges = ranges.mapIndexed { index, range -> index to range }
                    .filter { it.second.start >= it.second.endInclusive }
                "The ranges cannot be empty at indices: $emptyRanges" {
                    emptyRanges must BeEmpty
                }
            }
            return IntChromosome(
                executor(size) { index ->
                    IntGene(
                        Domain.random.nextInt(ranges[index].start, ranges[index].endInclusive + 1),
                        ranges[index],
                        filters[index]
                    )
                }
            )
        }

        override fun toString() = "IntChromosome.Factory(ranges=$ranges)"
    }
}
