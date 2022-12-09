/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.genetic.genes.BoolGene
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.math.BitArray
import cl.ravenhill.keen.util.math.bitArrayOf
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

    constructor(genes: BitArray, truesProbability: Double) : this(
        genes.toBoolGeneList(),
        truesProbability
    )

    /// {@inheritDoc}
    override fun verify() = genes.isNotEmpty()

    /**
     * Returns the number of true genes in this chromosome.
     */
    fun trues() = genes.count { it == BoolGene.True }

    /// {@inheritDoc}
    @Suppress("UNCHECKED_CAST")
    override fun duplicate(genes: List<Gene<Boolean>>) =
        BoolChromosome(genes as List<BoolGene>, truesProbability)

    /// {@inheritDoc}
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is BoolChromosome -> false
        other::class != BoolChromosome::class -> false
        genes != other.genes -> false
        else -> true
    }

    /// {@inheritDoc}
    override fun hashCode() = Objects.hash(BoolChromosome::class, genes)

    /// {@inheritDoc}
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
    class Factory(private val size: Int, private val truesProbability: Double) :
            Chromosome.Factory<Boolean> {

        /// {@inheritDoc}
        override fun make() = BoolChromosome(bitArrayOf(size, truesProbability), truesProbability)

        /// {@inheritDoc}
        override fun toString() =
            "BoolChromosome.Builder { size: $size, truesProbability: $truesProbability }"
    }
}
