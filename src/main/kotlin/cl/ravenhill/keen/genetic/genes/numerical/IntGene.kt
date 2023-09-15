/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.genes.numerical

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.ComparableGene
import cl.ravenhill.utils.IntToInt
import java.util.Objects

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
class IntGene(
    override val dna: Int,
    val range: IntToInt = Int.MIN_VALUE to Int.MAX_VALUE,
    override val filter: (Int) -> Boolean = { true }
) : NumberGene<Int, IntGene>, ComparableGene<Int, IntGene> {
    // region : Properties
    val start = range.first

    val end = range.second
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
    override fun generator() = Core.random.nextInt(start, end)

    /* Documentation inherited from [Gene]  */
    override fun withDna(dna: Int) = IntGene(dna, start to end, filter)
    // endregion

    // region : Verifiable Interface Implementation
    /* Documentation inherited from [Verifiable]    */
    @Suppress("ConvertTwoComparisonsToRangeCheck")
    override fun verify() = dna >= start && dna < end && filter(dna)
    // endregion

    // region : Any Interface Implementation
    /* Documentation inherited from [Any]   */
    override fun toString() = "$dna"

    /* Documentation inherited from [Any]   */
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is IntGene -> false
        other::class != IntGene::class -> false
        else -> dna == other.dna
    }

    /* Documentation inherited from [Any]   */
    override fun hashCode() = Objects.hash(IntGene::class, dna)
    // endregion
}
