/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.genetic.genes.NothingGene

/**
 * Represents a chromosome composed entirely of [NothingGene], a theoretical gene type.
 *
 * `NothingChromosome` is a data structure in the genetic algorithm framework, consisting of a list of `NothingGene`
 * objects. Like `NothingGene`, this chromosome is a conceptual construct, primarily used for testing and demonstrating
 * the robustness of the chromosome type system in a theoretical context. It does not serve a practical purpose in
 * actual evolutionary algorithms.
 *
 * ## Characteristics:
 * - **Genes**: The `genes` property is a list of `NothingGene` objects. Interaction with these genes follows the same
 *   theoretical constraints as `NothingGene`, where any practical operation is conceptually absurd.
 * - **Duplication**: The `duplicateWithGenes` function allows for the duplication of `NothingChromosome`, but since the
 *   underlying genes are of type `NothingGene`, this operation remains within the theoretical realm.
 *
 * ## Usage:
 * This class is not intended for use in practical applications of genetic algorithms. It exists to complete the
 * theoretical framework of gene and chromosome types and serves as an edge case in testing environments.
 *
 * ### Example:
 * ```kotlin
 * // Theoretical usage for testing or demonstration purposes
 * val chromosome = NothingChromosome.Factory().apply { size = 10 }.make()
 * // chromosome is an instance of NothingChromosome with theoretical genes
 * ```
 * In this example, `chromosome` is an instance of `NothingChromosome` containing `NothingGene` objects, highlighting
 * the theoretical and non-functional nature of these constructs.
 */
data class NothingChromosome(override val genes: List<NothingGene>) : Chromosome<Nothing, NothingGene> {

    /**
     * Creates a duplicate of the current `NothingChromosome` instance with the specified list of `NothingGene` genes.
     *
     * This method overrides the `duplicateWithGenes` function from the `Chromosome` interface. It allows for the
     * creation of a new `NothingChromosome` instance, where the list of `NothingGene` objects can be specified. Since
     * `NothingChromosome` is a theoretical construct primarily used for testing and demonstration, this method follows
     * the same conceptual nature.
     *
     * ## Usage:
     * This method is not intended for practical use in evolutionary algorithms. It is part of the theoretical framework
     * designed to demonstrate the completeness of the chromosome and gene type system in a genetic algorithm context.
     *
     * ### Example:
     * ```kotlin
     * // Theoretical usage, not meant for practical implementation
     * val originalChromosome = NothingChromosome(listOf(NothingGene))
     * val duplicatedChromosome = originalChromosome.duplicateWithGenes(listOf(NothingGene))
     * // duplicatedChromosome is a new instance of NothingChromosome
     * ```
     * In this example, `duplicatedChromosome` is a new instance of `NothingChromosome` created theoretically using
     * `NothingGene` objects.
     *
     * @param genes A list of `NothingGene` objects to be used in the duplicated chromosome. Given the theoretical
     *   nature of `NothingGene`, this list is conceptually absurd but necessary to satisfy the interface contract.
     * @return A new `NothingChromosome` instance with the specified list of `NothingGene` genes.
     */
    override fun duplicateWithGenes(genes: List<NothingGene>) = copy(genes = genes)

    /**
     * A factory class for creating instances of `NothingChromosome`.
     *
     * This class extends `Chromosome.AbstractFactory<Nothing, NothingGene>` and provides an implementation for
     * creating `NothingChromosome` instances. It adheres to the abstract factory design pattern, encapsulating the
     * instantiation logic for `NothingChromosome`. The factory is aligned with the theoretical nature of
     * `NothingChromosome`, serving primarily for testing and demonstration purposes within the genetic algorithm
     * framework.
     *
     * ## Usage:
     * While the `Factory` class is not intended for use in practical applications of genetic algorithms, it plays a
     * significant role in creating test scenarios or demonstrating the theoretical aspects of the chromosome and gene
     * type system.
     *
     * ### Example:
     * See [NothingChromosome] for an example of the `Factory` class in use.
     */
    class Factory : Chromosome.AbstractFactory<Nothing, NothingGene>() {

        /**
         * Overrides the `make` method to create a `NothingChromosome` instance.
         *
         * This method is a part of the `Factory` class within `NothingChromosome`. It provides the logic for
         * creating a new `NothingChromosome` instance. The chromosome is composed of a list of `NothingGene` objects,
         * with the list's size determined by the `size` property of the factory. Each gene in the list is an instance
         * of `NothingGene`, adhering to the theoretical nature of this chromosome type.
         *
         * ## Functionality:
         * - The `make` method constructs a `NothingChromosome` with a predefined number of genes (`size`), where each
         *   gene is a `NothingGene`.
         *
         * ## Usage:
         * This method is primarily used for testing or demonstrating the chromosome creation process in a theoretical
         * and conceptual context. It is not intended for practical use in real-world genetic algorithms.
         *
         * See [NothingChromosome] for an example of the `make` method in use.
         */
        override fun make() = NothingChromosome(List(size) { NothingGene })
    }
}

