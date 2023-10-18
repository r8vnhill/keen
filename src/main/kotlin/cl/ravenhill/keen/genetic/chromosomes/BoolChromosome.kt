/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.DoubleRequirement.BeInRange
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.Dice
import cl.ravenhill.keen.evolution.executors.ConstructorExecutor
import cl.ravenhill.keen.genetic.genes.BoolGene
import cl.ravenhill.keen.probability
import cl.ravenhill.keen.util.roundUpToMultipleOf
import kotlin.properties.Delegates

/**
 * A chromosome representing a binary sequence of genes, with each gene being either `true` or
 * `false`.
 * The probability of a gene being `true` can be specified during construction.
 *
 * @param genes The list of genes in the chromosome.
 *
 * @constructor Creates a new `BoolChromosome` with the specified [genes].
 *
 * @see [BoolGene]
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 1.0.0
 * @version 2.0.0
 */
data class BoolChromosome(
    override val genes: List<BoolGene>,
) : AbstractChromosome<Boolean, BoolGene>(genes) {

    /**
     * A secondary constructor that creates a `BoolChromosome` with a specific size.
     * The genes are initialized as random boolean values based on the given probability.
     *
     * @param size The number of genes in the chromosome.
     * @param truesProbability The probability that a gene will be `true`.
     */
    constructor(size: Int, truesProbability: Double) : this(
        List(size) {
            if (Core.random.nextDouble() > truesProbability) {
                BoolGene.True
            } else {
                BoolGene.False
            }
        }
    ) {
        enforce {
            "The probability of a gene being true must be in the range [0.0, 1.0]" {
                truesProbability must BeInRange(0.0..1.0)
            }
        }
    }

    /**
     * Secondary constructor for creating a new [BoolChromosome] of the specified size.
     *
     * Each gene in the chromosome is randomly generated according to the specified
     * [truesProbability].
     *
     * @param size The size of the chromosome to create.
     * @param truesProbability The probability of each gene being `true`.
     * This value should be a number between 0 and 1, inclusive.
     * @param constructorExecutor The executor to use for creating the genes.
     *
     * @return A new `BoolChromosome` with randomly generated genes.
     */
    constructor(
        size: Int,
        truesProbability: Double,
        constructorExecutor: ConstructorExecutor<BoolGene>
    ) : this(
        constructorExecutor(
            enforce {
                "The trues probability [$truesProbability] must be between 0 and 1, inclusive." {
                    truesProbability must BeInRange(0.0..1.0)
                }
            }.let {
                size
            }
        ) {
            if (Dice.probability() < truesProbability) BoolGene.True else BoolGene.False
        }
    )

    // Documentation inherited from [Verifiable].
    override fun verify() = genes.isNotEmpty()

    // Documentation inherited from [Chromosome].
    override fun withGenes(genes: List<BoolGene>) = BoolChromosome(genes)

    /**
     * Returns a string representation of the chromosome in binary format.
     * Each gene is represented by a bit: [BoolGene.True] is represented as '1' and [BoolGene.False]
     * as '0'.
     * The genes are grouped in sets of 4 bits, separated by '|'.
     * The string is padded with '0's to make its length a multiple of 8.
     *
     * @return a string representation of the chromosome in binary format.
     */
    fun toBinaryString(): String {
        // Calculate the required size, rounding up to a multiple of 8.
        val size = genes.size.roundUpToMultipleOf(8)

        // Create padding zeros to ensure the size is a multiple of 8.
        val paddingZeros = List(size - genes.size) { "0" }

        // Convert genes to binary string and chunk them in groups of 8.
        return (paddingZeros + genes.map { if (it == BoolGene.True) "1" else "0" })
            .chunked(8) { it.joinToString(separator = "") }
            .joinToString(separator = "|")
    }

    /**
     * Factory for [BoolChromosome]s.
     *
     * @property size The size of the chromosome to build.
     * @property truesProbability The probability of a gene being true.
     */
    class Factory : Chromosome.AbstractFactory<Boolean, BoolGene>() {

        var truesProbability: Double by Delegates.notNull()

        // Documentation inherited from [Chromosome.Factory].
        override fun make() = BoolChromosome(size, truesProbability, executor)

        // Documentation inherited from [Any].
        override fun toString() =
            "BoolChromosome.Factory { size: $size, truesProbability: $truesProbability }"
    }
}
