/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.Core.Dice
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.evolution.executors.ConstructorExecutor
import cl.ravenhill.keen.genetic.genes.BoolGene
import cl.ravenhill.keen.probability
import cl.ravenhill.keen.requirements.DoubleRequirement.BeInRange
import cl.ravenhill.keen.requirements.IntRequirement.BePositive
import cl.ravenhill.keen.util.math.roundUpToMultipleOf
import java.util.Objects

/**
 * A chromosome representing a binary sequence of genes, with each gene being either `true` or
 * `false`.
 * The probability of a gene being `true` can be specified during construction.
 *
 * @param genes The list of genes in the chromosome.
 * @property truesProbability The probability of a gene being `true`.
 * This value should be a number between 0 and 1, inclusive.
 *
 * @constructor Creates a new `BoolChromosome` with the specified ``genes`` and [truesProbability].
 *
 * @see [BoolGene]
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
class BoolChromosome(
    genes: List<BoolGene>,
    private val truesProbability: Double
) : AbstractChromosome<Boolean, BoolGene>(genes) {

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
        constructorExecutor(size) {
            if (Dice.probability() < truesProbability) BoolGene.True else BoolGene.False
        }, truesProbability
    )

    // Documentation inherited from [Verifiable].
    override fun verify() = genes.isNotEmpty()

    /**
     * Returns the number of genes that are [BoolGene.True].
     */
    fun trues() = genes.count { it == BoolGene.True }

    // Documentation inherited from [Chromosome].
    override fun withGenes(genes: List<BoolGene>) = BoolChromosome(genes, truesProbability)

    // Documentation inherited from [Any].
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is BoolChromosome -> false
        other::class != BoolChromosome::class -> false
        genes != other.genes -> false
        else -> true
    }

    // Documentation inherited from [Any].
    override fun hashCode() = Objects.hash(BoolChromosome::class, genes)

    // Documentation inherited from [Any].
    override fun toString(): String {
        var str = ""
        genes.forEach { str += if (it == BoolGene.True) "1" else "0" }
        return str.chunked(4).joinToString("|")
            .padStart(genes.size.roundUpToMultipleOf(8), '0')
    }

    /**
     * Builder for [BoolChromosome]s.
     *
     * @property size The size of the chromosome to build.
     * @property truesProbability The probability of a gene being true.
     */
    class Factory : Chromosome.AbstractFactory<Boolean, BoolGene>() {

        var truesProbability: Double = Double.NaN

        var size: Int = 0

        // Documentation inherited from [Chromosome.Factory].
        override fun make(): BoolChromosome {
            enforce {
                size should BePositive()
                truesProbability should BeInRange(0.0..1.0)
            }
            return BoolChromosome(size, truesProbability, executor)
        }

        // Documentation inherited from [Any].
        override fun toString() =
            "BoolChromosome.Factory { size: $size, truesProbability: $truesProbability }"
    }
}

