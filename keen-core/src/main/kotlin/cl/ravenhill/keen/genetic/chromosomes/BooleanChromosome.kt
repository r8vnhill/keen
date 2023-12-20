/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.genetic.genes.BooleanGene
import cl.ravenhill.keen.utils.roundUpToMultipleOf
import kotlin.properties.Delegates

/**
 * The size of each chunk in the binary string representation of a `BooleanChromosome`.
 *
 * The value of `CHUNK_SIZE` is set to 4, meaning that every four genes will be grouped together in the string
 * representation, separated by spaces.
 */
private const val CHUNK_SIZE = 4

/**
 * Represents a chromosome consisting of a list of `BooleanGene` objects.
 *
 * This data class models a chromosome specifically for genetic algorithms that use Boolean genes.
 * It provides functionalities to duplicate the chromosome with a new set of genes and convert the chromosome
 * into string representations. `BooleanChromosome` is useful in scenarios where you need to model genetic information
 * using binary values, typically represented by `true` and `false`.
 *
 * ## Usage:
 * ```
 * // Create a list of BooleanGenes
 * val genes = listOf(BooleanGene.True, BooleanGene.False, BooleanGene.True)
 *
 * // Create a BooleanChromosome using the list of genes
 * val booleanChromosome = BooleanChromosome(genes)
 *
 * // Duplicate the chromosome with a new set of genes
 * val newGenes = listOf(BooleanGene.False, BooleanGene.True, BooleanGene.False)
 * val duplicatedChromosome = booleanChromosome.duplicateWithGenes(newGenes)
 * ```
 * In this example, a `BooleanChromosome` is created with an initial set of genes. Then, it is duplicated to create a
 * new `BooleanChromosome` with a different set of genes.
 *
 * @property genes The list of genes in this chromosome.
 */
data class BooleanChromosome(override val genes: List<BooleanGene>) : Chromosome<Boolean, BooleanGene> {

    /**
     * Creates a duplicate of the current `BooleanChromosome` instance with a new set of genes.
     *
     * This function allows for the creation of a new `BooleanChromosome` instance based on the current one, but with
     * a different set of `BooleanGene` objects. It is particularly useful in genetic algorithm operations such as
     * crossover and mutation, where new chromosomes are created based on existing ones but with variations in their
     * gene sequences.
     *
     * ## Usage:
     * ```
     * val originalChromosome = BooleanChromosome(listOf(BooleanGene.True, BooleanGene.False))
     * val newGenes = listOf(BooleanGene.False, BooleanGene.True)
     * val duplicatedChromosome = originalChromosome.duplicateWithGenes(newGenes)
     * ```
     * In this example, `duplicatedChromosome` is a new instance of `BooleanChromosome` created from
     * `originalChromosome` but with a different set of genes (`newGenes`).
     *
     * @param genes A list of `BooleanGene` objects to be used in the new chromosome. This list replaces the gene
     *   sequence of the current chromosome.
     * @return A new `BooleanChromosome` instance.
     */
    override fun duplicateWithGenes(genes: List<BooleanGene>) = copy(genes = genes)

    /**
     * Converts the chromosome to a simple string representation.
     *
     * This function provides a binary string representation of the `BooleanChromosome`, where each gene is represented
     * as "1" for `true` and "0" for `false`. The genes are grouped into chunks of a predefined size ([CHUNK_SIZE]), and
     * these chunks are separated by spaces for readability. If the total number of genes is not a multiple of
     * [CHUNK_SIZE], the string is left-padded with zeros to make up the difference.
     *
     * ## Usage:
     * ```
     * val genes = listOf(BooleanGene.True, BooleanGene.False, BooleanGene.True)
     * val booleanChromosome = BooleanChromosome(genes)
     * val simpleString = booleanChromosome.toSimpleString()
     * println(simpleString) // Prints "0101"
     * ```
     * In this example, `simpleString` provides a binary string representation of `booleanChromosome`, with genes
     * represented as "1" or "0" and grouped into chunks.
     *
     * @return A string where each gene is represented as "1" or "0", grouped into chunks of [CHUNK_SIZE] and separated
     *   by spaces.
     */
    override fun toSimpleString(): String {
        val stringSize = size roundUpToMultipleOf CHUNK_SIZE
        val paddingZeroes = "0".repeat(stringSize - size)
        return (paddingZeroes + joinToString("") { if (it.value) "1" else "0" })
            .chunked(CHUNK_SIZE).joinToString(" ")
    }

    /**
     * Provides a standard string representation of the `BooleanChromosome`.
     *
     * ## Example:
     * ```
     * val genes = listOf(BooleanGene.True, BooleanGene.False, BooleanGene.True)
     * val booleanChromosome = BooleanChromosome(genes)
     * println(booleanChromosome)
     * // Output: "BooleanChromosome(genes=[True, False, True])"
     * ```
     * In this example, the `toString` method provides a string representation of `booleanChromosome` showing each
     * gene as either "True" or "False".
     *
     * @return A string representation of the `BooleanChromosome`.
     */
    override fun toString() = "BooleanChromosome(genes=${map { it.toSimpleString() }})"

    /**
     * A factory class for creating instances of [BooleanChromosome].
     *
     * This `Factory` class extends [Chromosome.AbstractFactory] and is specialized for the creation of
     * [BooleanChromosome] instances.
     * It allows the customization of the chromosome creation process, particularly the probability with which each gene
     * in the chromosome is set to `true`. This probability is controlled by the [trueRate] property.
     *
     * ## Usage:
     * ```
     * val chromosomeFactory = BooleanChromosome.Factory().apply {
     *     size = 10 // Set the size of the chromosome
     *     trueRate = 0.5 // Set the probability of each gene being true
     * }
     * val chromosome = chromosomeFactory.make() // Creates a BooleanChromosome with specified parameters
     * ```
     * In this example, `chromosomeFactory` is configured to create `BooleanChromosome` instances with 10 genes, each
     * having a 50% chance of being `true`. The `make` method then generates a chromosome based on these specifications.
     *
     * @property trueRate A [Double] value representing the probability of each gene in the chromosome being `true`. It
     *   must be within the range [0.0, 1.0].
     */
    class Factory : Chromosome.AbstractFactory<Boolean, BooleanGene>() {
        var trueRate: Double by Delegates.notNull()

        /**
         * Creates and returns a new instance of [BooleanChromosome] with genes initialized based on the `trueRate`.
         *
         * This `make` function is responsible for generating a [BooleanChromosome] according to the defined constraints
         * and parameters of the factory. It first ensures that the [trueRate] property falls within the valid range
         * [0.0, 1.0].
         * Then, it initializes the genes of the chromosome, with each gene having a probability of being `true` equal
         * to the [trueRate]. The determination of each gene's value is based on a random draw from [Domain.random].
         *
         * ## Constraints:
         * - The [trueRate] must be within the range [0.0, 1.0]. This constraint ensures that the probability is valid
         *   for a Boolean value generation.
         *
         * ## Usage:
         * See the example in the documentation of [Factory].
         *
         * @return A newly created [BooleanChromosome] instance with genes that are randomly set to `true` or `false`
         *   based on the [trueRate] probability.
         */
        override fun make(): Chromosome<Boolean, BooleanGene> {
            constraints {
                "The probability of a gene being true must be in the range [0.0, 1.0]" {
                    trueRate must BeInRange(0.0..1.0)
                }
            }
            return BooleanChromosome(executor(size) {
                if (Domain.random.nextDouble() > trueRate) {
                    BooleanGene.False
                } else {
                    BooleanGene.True
                }
            })
        }
    }
}
