/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.chromosomes.numeric

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.numeric.NumberChromosome.Factory
import cl.ravenhill.keen.genetic.genes.numeric.NumberGene
import cl.ravenhill.keen.mixins.FilterMutableListContainer
import cl.ravenhill.keen.mixins.RangeMutableListContainer


/**
 * Represents a chromosome comprised of numeric genes in gene-centric evolutionary algorithms.
 *
 * This interface specializes the [Chromosome] for numeric genes, encapsulating genes where the type [T] is a numeric
 * and comparable value. It extends [Chromosome], ensuring that all the basic chromosome functionalities are preserved
 * while adding specific features suitable for numeric genes.
 *
 * ## Features:
 * - The chromosome is designed to work with genes of numeric types (e.g., Int, Double) that are comparable.
 * - Suitable for scenarios in gene-centric evolutionary algorithms where numeric optimization or calculations are
 *   required.
 *
 * ## Nested Factory Interface:
 * `Factory` within `NumberChromosome` is an interface for creating instances of [NumberChromosome]. It extends
 * [Chromosome.Factory] and integrates two additional interfaces, [RangeMutableListContainer] and
 * [FilterMutableListContainer], offering extended capabilities for configuring numeric gene ranges and filters.
 *
 * ### Factory Features:
 * - **Range Configuration**: Allows setting multiple ranges for genes, ensuring each gene in the chromosome adheres
 *   to its respective range.
 * - **Filter Configuration**: Enables applying different filter criteria to each gene, providing flexibility in
 *   gene validation and selection.
 * - **Constraint Enforcement**: Ensures that the chromosome's configuration is consistent, particularly in terms
 *   of matching the count of genes with ranges and filters.
 *
 * ### Usage:
 * Implement this factory interface to create customized numeric chromosomes with specific range and filter settings.
 * The `enforceConstraints` method must be invoked to validate the chromosome configuration.
 *
 * ## Example:
 * ```kotlin
 * data class MyNumberChromosome(override val genes: List<MyNumberGene>) : NumberChromosome<Double, MyNumberGene> {
 *   override fun duplicateWithGenes(genes: List<MyNumberGene>) = MyNumberChromosome(genes)
 *   // Implement chromosome methods...
 * }
 * ```
 * In this example, `MyNumberChromosome` represents a chromosome with numeric genes of type `MyNumberGene`. The
 * chromosome implements the [NumberChromosome] interface, specifying the type of the gene's value (`Double`) and
 * the specific type of gene (`MyNumberGene`). The `duplicateWithGenes` method is implemented to return a new instance
 * of `MyNumberChromosome` with the given genes.
 *
 * It is recommended to use a [Factory] interface to create instances of `MyNumberChromosome`.
 *
 * @param T The numeric and comparable type of the gene's value.
 * @param G The specific type of [NumberGene] used in the chromosome.
 */
interface NumberChromosome<T, G> : Chromosome<T, G> where T : Number, T : Comparable<T>, G : NumberGene<T, G> {

    /**
     * Factory interface for creating instances of [NumberChromosome].
     * Extends [Chromosome.Factory] and integrates range and filter list containers.
     *
     * ## Factory Features:
     * - **Range Configuration**: Allows setting multiple ranges for genes, ensuring each gene in the chromosome adheres
     *   to its respective range.
     * - **Filter Configuration**: Enables applying different filter criteria to each gene, providing flexibility in
     *   gene validation and selection.
     * - **Constraint Enforcement**: Ensures that the chromosome's configuration is consistent, particularly in terms
     *   of matching the count of genes with ranges and filters.
     *
     * ### Usage:
     * Implement this factory interface to create customized numeric chromosomes with specific range and filter
     * settings. The `enforceConstraints` method must be invoked to validate the chromosome configuration.
     *
     * ## Example:
     * ```kotlin
     * class MyNumberChromosomeFactory : NumberChromosome.Factory<Double, MyNumberGene> {
     *     init {
     *         // Configure ranges, filters, and other settings
     *         enforceConstraints() // Ensure the configuration is valid
     *     }
     *     // Implement factory methods...
     * }
     * ```
     * In this example, `MyNumberChromosomeFactory` provides the logic for creating instances of a numeric chromosome,
     * including range and filter configurations.
     *
     * @param T The numeric and comparable type of the gene's value.
     * @param G The specific type of [NumberGene].
     */
    interface Factory<T, G> : Chromosome.Factory<T, G>, RangeMutableListContainer<T>, FilterMutableListContainer<T>
          where T : Number, T : Comparable<T>, G : NumberGene<T, G> {

        val defaultRange: ClosedRange<T>

        override fun make(): Chromosome<T, G> {
            enforceConstraints()
            adjustRangesAndFilters(
                size,
                ranges,
                defaultRange,
                filters
            ) { true }.let {
                ranges = it.first
                filters = it.second
            }
            return createChromosome()
        }

        fun createChromosome(): Chromosome<T, G>

        /**
         * Validates and enforces constraints on the chromosome configuration. Ensures consistency between the number of
         * genes, ranges, and filters.
         */
        fun enforceConstraints() {
            constraints {
                if (ranges.size > 1) {
                    "Chromosome with multiple ranges must have the same number of genes as ranges" {
                        ranges must HaveSize(size)
                    }
                }
                if (filters.size > 1) {
                    "Chromosome with multiple filters must have the same number of genes as filters" {
                        filters must HaveSize(size)
                    }
                }
            }
        }

        fun adjustRangesAndFilters(
            size: Int,
            ranges: MutableList<ClosedRange<T>>,
            defaultRange: ClosedRange<T>,
            filters: MutableList<(T) -> Boolean>,
            defaultFilter: (T) -> Boolean,
        ): Pair<MutableList<ClosedRange<T>>, MutableList<(T) -> Boolean>> {
            return when (ranges.size) {
                0 -> MutableList(size) { defaultRange }
                1 -> MutableList(size) { ranges.first() }
                else -> ranges
            } to when (filters.size) {
                0 -> MutableList(size) { { _: T -> true } }
                1 -> MutableList(size) { filters.first() }
                else -> filters
            }
        }
    }
}
