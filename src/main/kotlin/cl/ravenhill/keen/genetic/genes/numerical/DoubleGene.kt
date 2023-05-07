package cl.ravenhill.keen.genetic.genes.numerical

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.genetic.chromosomes.numerical.DoubleChromosome
import cl.ravenhill.keen.genetic.genes.ComparableGene
import cl.ravenhill.keen.requirements.CollectionRequirement.NotBeEmpty
import cl.ravenhill.keen.requirements.DoubleRequirement.BeInRange
import cl.ravenhill.keen.requirements.PairRequirement.BeFinite
import cl.ravenhill.keen.requirements.PairRequirement.BeStrictlyOrdered
import cl.ravenhill.keen.util.DoubleToDouble
import java.util.Objects
import kotlin.random.asJavaRandom

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
    val range: DoubleToDouble,
    override val filter: (Double) -> Boolean = { true }
) : NumberGene<Double, DoubleGene>, ComparableGene<Double, DoubleGene> {

    init {
        enforce {
            "The range [$range] must be ordered" { range must BeStrictlyOrdered() }
            "The range [$range] must be finite" { range must BeFinite }
            "The value [$dna] must be in range [$range]" { dna must BeInRange(range) }
        }
    }

    /**
     * The lower bound of the range (inclusive).
     */
    private val start = range.first

    /**
     * The upper bound of the range (exclusive).
     */
    private val end = range.second

    /// Documentation inherited from [NumberGene]
    override fun average(genes: List<DoubleGene>): DoubleGene {
        enforce { "The list of genes must not be empty" { genes must NotBeEmpty } }
        return withDna((dna + genes.sumOf { it.dna }) / (genes.size + 1))
    }

    /// Documentation inherited from [NumberGene]
    override fun toDouble() = dna

    /// Documentation inherited from [NumberGene]
    override fun toInt() = dna.toInt()

    /// Documentation inherited from [Gene]
    override fun generator() = Core.random.asJavaRandom().nextDouble() * (end - start) + start

    /// Documentation inherited from [Gene]
    override fun withDna(dna: Double) = DoubleGene(dna, range)

    /// Documentation inherited from [Verifiable]
    override fun verify() = dna < range.second && dna >= range.first

    /// Documentation inherited from [Any]
    override fun toString() = "$dna"

    /// Documentation inherited from [Any]
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is DoubleGene -> false
        other::class != this::class -> false
        else -> dna == other.dna
                && range == other.range
    }

    /// Documentation inherited from [Any]
    override fun hashCode() = Objects.hash(DoubleGene::class, dna, range)
}
