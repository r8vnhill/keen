/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.genetic.genes.numerical

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.ComparableGene
import java.util.*
import java.util.stream.IntStream

/**
 * [NumberGene] which holds a 32-bit integer number.
 *
 * @property dna The value of the gene.
 * @property range The range of the gene.
 * @property filter A filter function to apply to the gene's value.
 *
 * @see IntChromosome
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 */
class IntGene(
    override val dna: Int,
    val range: Pair<Int, Int>,
    override val filter: (Int) -> Boolean = { true }
) : NumberGene<Int>, ComparableGene<Int> {
    val start = range.first
    val end = range.second

    /**
     * Calculates the mean of this gene and the given one.
     *
     * @param gene NumberGene<Int>
     * @return IntGene
     */
    override fun mean(gene: NumberGene<Int>) =
        duplicate(((gene.dna.toLong() + dna.toLong()) / 2).toInt())

    override fun toDouble() = dna.toDouble()

    override fun toInt() = dna

    override fun generator() = Core.random.nextInt(start, end)

    override fun duplicate(dna: Int) = IntGene(dna, start to end, filter)

    @Suppress("ConvertTwoComparisonsToRangeCheck")
    override fun verify() = dna >= start && dna < end && filter(dna)

    override fun toString() = "$dna"

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is IntGene -> false
        other::class != IntGene::class -> false
        else -> dna == other.dna
    }

    override fun hashCode() = Objects.hash(IntGene::class, dna)
}
