/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.chromosomes.numeric

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.numeric.DoubleChromosome.Factory
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import cl.ravenhill.keen.utils.nextDoubleInRange


/**
 * Represents a chromosome composed of double-valued genes.
 *
 * The `DoubleChromosome` class encapsulates a sequence of genes where each gene holds a double value.
 * This chromosome type is particularly useful in evolutionary algorithms where genetic information is
 * expressed as floating-point numbers, such as in problems involving numerical optimization.
 *
 * ## Characteristics:
 * - **Gene Type**: Uses [DoubleGene], which allows representation of numerical values with double precision.
 * - **Flexibility**: Capable of representing a wide range of numerical values, subject to the constraints defined in
 *   the genes.
 * - **Customizable**: Through the associated [Factory], the chromosome can be customized with specific ranges and
 *   filters for its genes, tailoring it to various problem spaces.
 *
 * ## Usage:
 * `DoubleChromosome` is typically used in gene-centric evolutionary algorithms where numerical values are manipulated.
 * It can be employed in scenarios like optimization problems, where the solution space can be explored using
 * floating-point values.
 *
 * ### Example:
 * Creating a `DoubleChromosome` with a specific range and filter for its genes:
 * ```kotlin
 * val chromosome = DoubleChromosome(DoubleGene(0.0, 10.0, { it > 5.0 }), DoubleGene(0.0, 10.0, { it < 5.0 }))
 * // Equivalent to:
 * val chromosome = DoubleChromosome(listOf(DoubleGene(0.0, 10.0, { it > 5.0 }), DoubleGene(0.0, 10.0, { it < 5.0 })))
 * ```
 *
 * @property genes The list of [DoubleGene] instances forming the chromosome.
 * @constructor Creates a new [DoubleChromosome] instance with the specified list of genes
 */
data class DoubleChromosome(override val genes: List<DoubleGene>) : NumberChromosome<Double, DoubleGene> {

    /**
     * Secondary constructor for creating a [DoubleChromosome] instance using a vararg of [DoubleGene]s.
     *
     * This constructor provides a convenient way to create a [DoubleChromosome] instance by directly passing individual
     * [DoubleGene] objects, instead of a pre-constructed list. It is particularly useful when the number of genes is
     * known at compile-time or when creating a chromosome with a small number of genes for simplicity or testing
     * purposes.
     *
     * ## Usage:
     * The secondary constructor allows for the direct specification of genes without the need to create a list.
     * This can enhance code readability and ease of use, especially in scenarios where the genes are known and
     * static.
     *
     * ### Example:
     * ```
     * // Creating a DoubleChromosome with individual DoubleGene instances
     * val gene1 = DoubleGene(5.0, 0.0..10.0)
     * val gene2 = DoubleGene(7.5, 0.0..10.0)
     * val chromosome = DoubleChromosome(gene1, gene2)
     * ```
     * In this example, `DoubleChromosome` is constructed directly by passing individual `DoubleGene` objects as
     * arguments.
     * This makes the code more concise and clear, particularly for a small number of genes.
     *
     * @param genes Varargs of [DoubleGene] instances to be included in the chromosome.
     */
    constructor(vararg genes: DoubleGene) : this(genes.toList())

    /**
     * Creates a new [DoubleChromosome] instance with a specified list of [DoubleGene]s.
     *
     * This method is a key component of the gene-centric evolutionary algorithms' operations like crossover and
     * mutation, where a new chromosome is formed with a modified set of genes. The `duplicateWithGenes` method
     * allows for the creation of a new chromosome instance while preserving the integrity of the original
     * chromosome's structure.
     *
     * ## Functionality:
     * - The method takes a list of [DoubleGene] instances and creates a new [DoubleChromosome] with these genes.
     * - It ensures immutability of chromosomes, where modifications result in new instances rather than altering
     *   the original chromosome.
     *
     * ## Usage:
     * This method is typically used in genetic operations that require the creation of new chromosomes based on
     * existing genetic material, such as in crossover or mutation processes.
     *
     * ### Example:
     * ```
     * val originalChromosome = DoubleChromosome(DoubleGene(1.0, 0.0..2.0), DoubleGene(1.5, 0.0..2.0))
     * val mutatedGenes = listOf(DoubleGene(1.2, 0.0..2.0), DoubleGene(1.7, 0.0..2.0))
     * val newChromosome = originalChromosome.duplicateWithGenes(mutatedGenes)
     * ```
     * In this example, `duplicateWithGenes` is used to create a new `DoubleChromosome` instance based on a modified set
     * of genes, representing a mutation from the original chromosome.
     *
     * @param genes A list of [DoubleGene] instances to be included in the new chromosome.
     * @return A new [DoubleChromosome] instance containing the provided genes.
     */
    override fun duplicateWithGenes(genes: List<DoubleGene>) = DoubleChromosome(genes)

    /**
     * Factory class for creating instances of [DoubleChromosome].
     *
     * This factory extends [Chromosome.AbstractFactory] and implements [NumberChromosome.Factory], specializing in
     * the creation of [DoubleChromosome] instances. It allows for custom configurations of chromosome creation,
     * particularly in terms of gene ranges and filtering criteria.
     *
     * ## Key Features:
     * - **Range Customization**: Allows defining the range of values for each gene in the chromosome through [ranges].
     * - **Filter Customization**: Provides the ability to specify custom filter functions for genes via [filters].
     * - **Dynamic Range and Filter Adjustment**: Adjusts ranges and filters based on the size of the chromosome,
     *   ensuring consistency between the number of genes and corresponding ranges or filters.
     *
     * ## Usage:
     * This factory is used to create [DoubleChromosome] instances with specific characteristics. The [ranges] and
     * [filters] lists can be configured to tailor the chromosome to specific requirements.
     *
     * ### Example:
     * ```
     * val chromosomeFactory = DoubleChromosome.Factory().apply {
     *     size = 5
     *     ranges += 0.0..10.0  // Apply this range to all genes
     *     filters += { it >= 0 } // Apply this filter to all genes
     * }
     * val chromosome = chromosomeFactory.make() // Creates a DoubleChromosome with the specified configuration
     * ```
     * In this example, a [DoubleChromosome] factory is configured with a specific range and filter for all genes. The
     * `make` method then creates a chromosome instance based on these configurations.
     *
     * @property ranges Mutable list of [ClosedRange]<[Double]> specifying the value range for each gene.
     * @property filters Mutable list of filter functions, each accepting a [Double] and returning a [Boolean].
     */
    class Factory : Chromosome.AbstractFactory<Double, DoubleGene>(), NumberChromosome.Factory<Double, DoubleGene> {

        override var ranges = mutableListOf<ClosedRange<Double>>()

        override var filters = mutableListOf<(Double) -> Boolean>()

        override val defaultRange = -Double.MAX_VALUE..Double.MAX_VALUE


        /**
         * Creates an instance of [DoubleChromosome] using the current configuration.
         *
         * This method constructs a [DoubleChromosome] by generating a list of [DoubleGene]s based on the specified
         * ranges, filters, and executor. Each gene in the chromosome is created using the range and filter at the
         * corresponding index. The executor is used to instantiate each gene in the chromosome, ensuring that they
         * adhere to the specified constraints.
         *
         * ## Process:
         * - Iterates through each index up to the specified size of the chromosome.
         * - For each index, a [DoubleGene] is created with a random value within the corresponding range and validated
         *   against the corresponding filter.
         * - These genes are then combined to form a [DoubleChromosome] using the [executor].
         *
         * ## Assumptions:
         * - The [ranges] and [filters] lists have been previously adjusted to match the chromosome size using
         *   the [adjustRangesAndFilters] method.
         *
         * @return A newly created [DoubleChromosome] with genes conforming to the specified constraints.
         */
        override fun createChromosome(): DoubleChromosome {
            constraints {
                val emptyRanges = ranges.mapIndexed { index, range -> index to range }
                    .filter { it.second.start >= it.second.endInclusive }
                "The ranges cannot be empty at indices: $emptyRanges" {
                    emptyRanges must BeEmpty
                }
            }
            return DoubleChromosome(
                executor(size) { index ->
                    DoubleGene(
                        Domain.random.nextDoubleInRange(ranges[index]),
                        ranges[index],
                        filters[index]
                    )
                }
            )
        }

        override fun toString() = "DoubleChromosome.Factory(ranges=$ranges)"
    }
}
