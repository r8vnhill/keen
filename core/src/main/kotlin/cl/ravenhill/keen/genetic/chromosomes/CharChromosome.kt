/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.genetic.genes.CharGene
import cl.ravenhill.keen.mixins.FilterMutableListContainer
import cl.ravenhill.keen.mixins.RangeMutableListContainer
import cl.ravenhill.keen.utils.nextChar


/**
 * Represents a chromosome consisting of a list of `CharGene` objects in a genetic algorithm.
 *
 * This data class models a chromosome specifically for genetic algorithms that use character genes. It provides
 * functionalities for duplicating the chromosome with a new set of genes and converting the chromosome into a
 * simple string representation. `CharChromosome` is used in scenarios where genetic information is best represented
 * using characters, such as in text-based optimization problems or in scenarios where characters can encode specific
 * traits or behaviors.
 *
 * ## Usage:
 * ### Creating a CharChromosome
 * ```
 * val genes = listOf(CharGene('A'), CharGene('B'), CharGene('C'))
 * val charChromosome = CharChromosome(genes)
 * ```
 *
 * @property genes The list of `CharGene` objects that make up the chromosome.
 */
data class CharChromosome(override val genes: List<CharGene>) : Chromosome<Char, CharGene> {

    /**
     * Creates a new instance of `CharChromosome` with the specified `CharGene` objects.
     *
     * This constructor allows for the creation of a new `CharChromosome` instance using a variable number of `CharGene`
     * objects. It takes a list of `CharGene` objects as varargs and initializes the class with that list.
     *
     * ## Usage:
     * ```
     * val gene1 = CharGene('A')
     * val gene2 = CharGene('B')
     * val gene3 = CharGene('C')
     * val chromosome = CharChromosome(gene1, gene2, gene3)
     * ```
     * In this example, `chromosome` will be a new instance of `CharChromosome` created with the provided genes `gene1`,
     * `gene2`, and `gene3`.
     *
     * @param genes The `CharGene` objects to be used in the chromosome.
     */
    constructor(vararg genes: CharGene) : this(genes.toList())

    /**
     * Creates a duplicate of the current `CharChromosome` instance with a specified list of `CharGene` objects.
     *
     * This method allows for the creation of a new `CharChromosome` instance using a different set of genes.
     * It is particularly useful in genetic algorithm operations such as mutation and crossover, where a new chromosome
     * instance needs to be created based on an existing chromosome but with variations in the gene sequence.
     *
     * ## Usage:
     * ```
     * val originalChromosome = CharChromosome(listOf(CharGene('A'), CharGene('B'), CharGene('C')))
     * val newGenes = listOf(CharGene('X'), CharGene('Y'), CharGene('Z'))
     * val duplicatedChromosome = originalChromosome.duplicateWithGenes(newGenes)
     * ```
     * In this example, `duplicatedChromosome` is a new instance of `CharChromosome` created from `originalChromosome`
     * but with a different set of genes specified by `newGenes`.
     *
     * Prefer using [copy] instead of this method when possible.
     *
     * @param genes The new list of [CharGene] objects to be used in the duplicated chromosome.
     * @return A new `CharChromosome` instance with the specified list of genes.
     */
    override fun duplicateWithGenes(genes: List<CharGene>) = copy(genes = genes)

    /**
     * Converts the chromosome to a simple string representation.
     *
     * ## Usage:
     * ```
     * val genes = listOf(CharGene('H'), CharGene('e'), CharGene('l'), CharGene('l'), CharGene('o'))
     * val charChromosome = CharChromosome(genes)
     * val chromosomeString = charChromosome.toSimpleString() // Returns "Hello"
     * ```
     * In this example, `chromosomeString` will be "Hello", reflecting the concatenated string values of the genes in
     * `charChromosome`.
     *
     * @return A string representation of the chromosome, composed of the string values of its genes.
     */
    override fun toSimpleString() = genes.joinToString("") { it.toSimpleString() }

    /**
     * Factory class for creating instances of [CharChromosome].
     *
     * This factory class extends [Chromosome.AbstractFactory] and is specialized for the creation of `CharChromosome`
     * instances.
     * It allows customization of chromosome creation by providing mechanisms to define specific ranges and filters for
     * each gene in the chromosome. This flexibility is crucial for scenarios where character genes need to adhere to
     * certain constraints or patterns.
     *
     * ## Usage:
     * ```
     * val charChromosomeFactory = CharChromosome.Factory().apply {
     *     size = 5 // Size of the chromosome
     *     ranges += 'a'..'z' // Range for each gene
     *     filters += { it.isLowerCase() } // Filter for each gene
     * }
     * val charChromosome = charChromosomeFactory.make() // Creates a CharChromosome
     * ```
     * In this example, `charChromosome` is created using the factory with a size of 5 genes, each gene having a range
     * of 'a' to 'z' and a filter that ensures each character is lowercase.
     *
     * @property filters Mutable list of lambda functions for character gene filtering.
     * @property ranges Mutable list of closed ranges defining valid character ranges for each gene.
     */
    class Factory : Chromosome.AbstractFactory<Char, CharGene>(),
        RangeMutableListContainer<Char>,
        FilterMutableListContainer<Char> {

        override var filters = mutableListOf<(Char) -> Boolean>()
        override var ranges = mutableListOf<ClosedRange<Char>>()

        /**
         * Creates and returns a new instance of [CharChromosome] with genes initialized based on the configured ranges
         * and filters.
         *
         * This method orchestrates the creation of a `CharChromosome` by applying defined constraints, adjusting ranges
         * and filters, and then generating a list of [CharGene] objects accordingly. It ensures that each gene in the
         * chromosome adheres to the specified range and filter criteria. This method is crucial for creating
         * chromosomes that meet specific requirements or follow certain patterns in a genetic algorithm.
         *
         * The method first enforces constraints to ensure the validity of ranges and filters. It then adjusts these
         * ranges and filters to match the chromosome size, if necessary. Finally, it creates a new `CharChromosome`
         * instance with a list of `CharGene` objects generated based on these configurations.
         *
         * ## Usage:
         * See [Factory] for an example of how to use this method.
         *
         * @return A new instance of [CharChromosome] with genes conforming to the specified ranges and filters.
         */
        override fun make(): Chromosome<Char, CharGene> {
            ChromosomeUtils.enforceConstraints(ranges, filters, size)
            ChromosomeUtils.adjustRangesAndFilters(
                size,
                ranges,
                Char.MIN_VALUE..Char.MAX_VALUE,
                filters
            ) { true }.let {
                ranges = it.first
                filters = it.second
            }
            constraints {
                val emptyRanges = ranges.mapIndexed { index, range ->
                    index to range
                }.filter { it.second.start >= it.second.endInclusive }
                "The ranges cannot be empty at indices: $emptyRanges" {
                    emptyRanges must BeEmpty
                }
            }
            return CharChromosome(
                MutableList(size) { index ->
                    CharGene(
                        Domain.random.nextChar(
                            ranges[index],
                            filters[index]
                        ),
                        ranges[index],
                        filters[index]
                    )
                }
            )
        }
    }
}