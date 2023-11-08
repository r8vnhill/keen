/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.genes.numerical

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.ComparableGene
import cl.ravenhill.keen.util.Ranged
import cl.ravenhill.keen.util.nextIntInRange
import cl.ravenhill.keen.util.IntToInt
import cl.ravenhill.keen.util.toRange
import java.util.*

/**
 * A gene that stores a 32-bit floating point number value.
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
data class IntGene(
    override val dna: Int,
    override val range: ClosedRange<Int> = Int.MIN_VALUE..Int.MAX_VALUE,
    override val filter: (Int) -> Boolean = { true },
) : NumberGene<Int, IntGene>, ComparableGene<Int, IntGene>, Ranged<Int> {

    // region : Properties
    val start = range.start

    val end = range.endInclusive
    // endregion

    // region : NumberGene Interface Implementation
    /* Documentation inherited from [NumberGene]    */
    override fun average(genes: List<IntGene>) = withDna(
        genes.fold(dna.toDouble() / (genes.size + 1)) { acc, gene ->
            acc + gene.toDouble() / (genes.size + 1)
        }.toInt()
    )

    /* Documentation inherited from [NumberGene]    */
    override fun toDouble() = dna.toDouble()

    /* Documentation inherited from [NumberGene]    */
    override fun toInt() = dna
    // endregion

    // region : Gene Interface Implementation
    /* Documentation inherited from [Gene]  */
    override fun generator() = Core.random.nextIntInRange(range)

    /* Documentation inherited from [Gene]  */
    override fun withDna(dna: Int) = IntGene(dna, range, filter)
    // endregion

    // region : Verifiable Interface Implementation
    /* Documentation inherited from [Verifiable]    */
    override fun verify() = dna in range && filter(dna)
    // endregion

    override fun toSimpleString() = dna.toString()

    override fun equals(other: Any?) = when {
        other !is IntGene -> false
        other::class != IntGene::class -> false
        other.dna != dna -> false
        other.range.start != range.start -> false
        other.range.endInclusive != range.endInclusive -> false
        else -> true
    }

    override fun hashCode() =
        Objects.hash(IntGene::class, dna, range.start, range.endInclusive)
}
