/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.genetic.genes.numerical

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.numerical.DoubleChromosome
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.ComparableGene
import java.util.*

/**
 * A gene that stores a 64-bit floating point number value.
 *
 * This gene represents a value within a specified range, and can be used to model discrete
 * numerical parameters in genetic algorithms.
 *
 * @param dna The current value of this gene.
 * @param range The range of valid values for this gene represented as a pair of [Int] values.
 *     The first value represents the lower bound of the range (inclusive), and the second value
 *     represents the upper bound of the range (exclusive).
 * @param filter A predicate function that determines whether a number should be accepted as valid
 *      for this gene.
 *
 * @property start The lower bound of the range (inclusive).
 * @property end The upper bound of the range (exclusive).
 *
 * @see IntChromosome
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
class IntGene(
    override val dna: Int,
    val range: Pair<Int, Int>,
    override val filter: (Int) -> Boolean = { true }
) : NumberGene<Int>, ComparableGene<Int> {

    val start = range.first

    val end = range.second

    override fun average(genes: List<NumberGene<Int>>) =
        duplicate(genes.fold(dna.toDouble() / (genes.size + 1)) { acc, gene ->
            acc + gene.toDouble() / (genes.size + 1)
        }.toInt())

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
