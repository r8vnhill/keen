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
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.probability
import cl.ravenhill.keen.requirements.DoubleRequirement.BeInRange
import cl.ravenhill.keen.requirements.IntRequirement.BePositive
import cl.ravenhill.keen.util.math.roundUpToMultipleOf
import java.util.Objects

/**
 * A chromosome of [BoolGene]s.
 *
 * @param genes The genes of this chromosome.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 */
class BoolChromosome private constructor(
    genes: List<BoolGene>,
    private val truesProbability: Double
) : AbstractChromosome<Boolean>(genes) {

    constructor(
        size: Int,
        truesProbability: Double,
        constructorExecutor: ConstructorExecutor<BoolGene>
    ) : this(
        constructorExecutor(size) {
            if (Dice.probability() < truesProbability) BoolGene.True else BoolGene.False
        }, truesProbability
    )

    /// Documentation inherited from [Verifiable].
    override fun verify() = genes.isNotEmpty()

    fun trues() = genes.count { it == BoolGene.True }

    /// Documentation inherited from [Chromosome].
    @Suppress("UNCHECKED_CAST")
    override fun withGenes(genes: List<Gene<Boolean>>) =
        BoolChromosome(genes as List<BoolGene>, truesProbability)

    /// Documentation inherited from [Any].
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is BoolChromosome -> false
        other::class != BoolChromosome::class -> false
        genes != other.genes -> false
        else -> true
    }

    /// Documentation inherited from [Any].
    override fun hashCode() = Objects.hash(BoolChromosome::class, genes)

    /// Documentation inherited from [Any].
    override fun toString(): String {
        var str = ""
        genes.forEach { str += if (it == BoolGene.True) "1" else "0" }
        return str.chunked(4).joinToString("|")
            .padStart(genes.size.roundUpToMultipleOf(8), '0')
    }

    /**
     * Builder for [BoolChromosome]s.
     *
     * @property size               The size of the chromosome to build.
     * @property truesProbability   The probability of a gene being true.
     *
     * @constructor Creates a new builder for [BoolChromosome]s.
     */
    class Factory : Chromosome.AbstractFactory<Boolean, BoolGene>() {

        var truesProbability: Double = Double.NaN

        var size: Int = 0

        /// Documentation inherited from [Chromosome.Factory].
        override fun make(): BoolChromosome {
            enforce {
                size should BePositive()
                truesProbability should BeInRange(0.0..1.0)
            }
            return BoolChromosome(size, truesProbability, executor)
        }

        /// Documentation inherited from [Any].
        override fun toString() =
            "BoolChromosome.Factory { size: $size, truesProbability: $truesProbability }"
    }
}
