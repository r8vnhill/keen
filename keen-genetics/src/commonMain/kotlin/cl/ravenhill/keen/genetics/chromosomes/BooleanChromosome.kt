/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics.chromosomes

import cl.ravenhill.keen.genetics.genes.BooleanGene

/**
 * Represents a chromosome composed of boolean genes in an evolutionary algorithm.
 *
 * The `BooleanChromosome` class is a concrete implementation of the `Chromosome` interface, specifically designed
 * to hold a list of `BooleanGene` instances. Each `BooleanChromosome` represents a sequence of binary genetic
 * information, with each gene being either `True` or `False`. This structure is commonly used in genetic algorithms
 * where binary encoding is employed, such as in genetic optimization problems.
 *
 * ## Usage:
 * The `BooleanChromosome` class can be used in evolutionary algorithms to represent individuals that have binary
 * traits. It provides methods to duplicate the chromosome with a new set of genes, which is essential for operations
 * like mutation, crossover, and selection within the genetic algorithm.
 *
 * ### Example 1: Creating a Boolean Chromosome
 * ```kotlin
 * val gene1 = BooleanGene.True
 * val gene2 = BooleanGene.False
 * val chromosome = BooleanChromosome(listOf(gene1, gene2))
 * println(chromosome.genes) // Output: [True, False]
 * ```
 *
 * ### Example 2: Duplicating a Chromosome with New Genes
 * ```kotlin
 * val chromosome = BooleanChromosome(listOf(BooleanGene.True, BooleanGene.False))
 * val newGenes = listOf(BooleanGene.False, BooleanGene.True)
 * val newChromosome = chromosome.duplicateWithGenes(newGenes)
 * println(newChromosome.genes) // Output: [False, True]
 * ```
 *
 * @param genes The list of boolean genes that make up the chromosome. Each gene in the list is either `True` or
 *   `False`, representing binary genetic information.
 */
data class BooleanChromosome(override val genes: List<BooleanGene>) : Chromosome<Boolean, BooleanGene> {

    /**
     * Creates a copy of the chromosome with a new list of genes.
     *
     * The `duplicateWithGenes` method is used to generate a new `BooleanChromosome` with a specified set of genes.
     * This method is crucial in genetic algorithms for operations like crossover, where a new chromosome is
     * created by combining genes from parent chromosomes.
     *
     * ### Example:
     * ```kotlin
     * val chromosome = BooleanChromosome(listOf(BooleanGene.True, BooleanGene.False))
     * val newGenes = listOf(BooleanGene.False, BooleanGene.True)
     * val newChromosome = chromosome.duplicateWithGenes(newGenes)
     * println(newChromosome.genes) // Output: [False, True]
     * ```
     *
     * @param newGenes The new list of `BooleanGene` instances to replace the current genes in the chromosome.
     * @return A new `BooleanChromosome` instance with the specified genes.
     */
    override fun duplicateWithGenes(newGenes: List<BooleanGene>) = copy(genes = newGenes)
}
