package cl.ravenhill.keen.genetic.genes.numerical

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.numerical.DoubleChromosome
import cl.ravenhill.keen.genetic.genes.ComparableGene
import java.util.Objects

/**
 * A gene that stores a 64-bit floating point number value.
 *
 * This gene represents a value within a specified range, and can be used to model continuous
 * numerical parameters in genetic algorithms.
 *
 * @param dna The current value of this gene.
 * @param range The range of valid values for this gene represented as a pair of [Double] values.
 *     The first value represents the lower bound of the range (inclusive), and the second value
 *     represents the upper bound of the range (exclusive).
 * @param filter A predicate function that determines whether a number should be accepted as valid
 *      for this gene.
 *
 * @see DoubleChromosome
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
class DoubleGene(
    override val dna: Double,
    val range: Pair<Double, Double>,
    override val filter: (Double) -> Boolean = { true }
) : NumberGene<Double, DoubleGene>, ComparableGene<Double, DoubleGene> {

    override fun average(genes: List<DoubleGene>) =
        withDna(genes.fold(dna / (genes.size + 1)) { acc, gene -> acc + gene.dna / (genes.size + 1) })

    private val start = range.first

    private val end = range.second

    override fun toDouble() = dna

    override fun toInt() = dna.toInt()

    override fun generator() = Core.random.nextDouble(start, end)

    override fun withDna(dna: Double) = DoubleGene(dna, range)

    override fun verify() = dna < range.second && dna >= range.first

    override fun toString() = "$dna"

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is DoubleGene -> false
        other::class != this::class -> false
        else -> dna == other.dna
                && range == other.range
    }

    override fun hashCode() = Objects.hash(DoubleGene::class, dna, range)
}