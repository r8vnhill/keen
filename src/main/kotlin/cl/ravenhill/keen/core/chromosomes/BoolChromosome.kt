/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.core.chromosomes

import cl.ravenhill.keen.core.KeenCore
import cl.ravenhill.keen.core.genes.BoolGene
import cl.ravenhill.keen.core.genes.Gene
import java.util.Objects

/**
 * A chromosome of [BoolGene]s.
 *
 * @param genes The genes of this chromosome.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 */
class BoolChromosome private constructor(genes: List<BoolGene>) :
        AbstractChromosome<Boolean>(genes) {

    /**
     * Creates a new chromosome of a given ``size`` with random genes following the given
     * ``truesProbability`` (how often a gene is true).
     */
    constructor(size: Int, truesProbability: Double) : this(
        List(size) {
            if (KeenCore.generator.nextDouble() < truesProbability) {
                BoolGene.True
            } else {
                BoolGene.False
            }
        }
    )

    /// {@inheritDoc}
    override fun verify() = genes.isNotEmpty()

    /**
     * Returns the number of true genes in this chromosome.
     */
    fun trues() = genes.count { it == BoolGene.True }

    /// {@inheritDoc}
    @Suppress("UNCHECKED_CAST")
    override fun copy(genes: List<Gene<Boolean>>) =
        BoolChromosome(genes as List<BoolGene>)

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
    override fun toString() =
        genes.map { if (it == BoolGene.True) "1" else "0" }.chunked(8)
            .joinToString("|") { it.joinToString("") }

    /**
     * Builder for [BoolChromosome]s.
     *
     * @property size               The size of the chromosome to build.
     * @property truesProbability   The probability of a gene being true.
     *
     * @constructor Creates a new builder for [BoolChromosome]s.
     */
    class Builder(private val size: Int, private val truesProbability: Double) :
        Chromosome.Builder<Boolean> {

        /// {@inheritDoc}
        override fun build() = BoolChromosome(size, truesProbability)

        /// {@inheritDoc}
        override fun toString() =
            "BoolChromosome.Builder { size: $size, truesProbability: $truesProbability }"
    }
}
