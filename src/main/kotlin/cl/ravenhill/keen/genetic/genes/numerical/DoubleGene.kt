/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.genes.numerical

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.CollectionRequirement.BeEmpty
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.numerical.DoubleChromosome
import cl.ravenhill.keen.genetic.genes.ComparableGene
import cl.ravenhill.keen.util.Ranged
import cl.ravenhill.keen.util.nextDoubleInRange
import cl.ravenhill.utils.DoubleToDouble
import cl.ravenhill.utils.toRange
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
    override val range: ClosedFloatingPointRange<Double> = -Double.MAX_VALUE..Double.MAX_VALUE,
    override val filter: (Double) -> Boolean = { true },
) : NumberGene<Double, DoubleGene>, ComparableGene<Double, DoubleGene>, Ranged<Double> {

    val start = range.start
    val end = range.endInclusive

    init {
        enforce {
//            "The range [$range] must be ordered" { range must BeStrictlyOrdered() }
//            "The range [$range] must be finite" { range must BeFinite }
//            "The value [$dna] must be in range [$range]" { dna must BeInRange(range) }
        }
    }

    @Deprecated(
        "Use the constructor that receives a range instead",
        ReplaceWith("DoubleGene(dna, range.toRange(), filter)")
    )
    constructor(dna: Double, range: DoubleToDouble, filter: (Double) -> Boolean = { true }) :
        this(dna, range.toRange(), filter = filter)

    // / Documentation inherited from [NumberGene]
    override fun average(genes: List<DoubleGene>): DoubleGene {
        enforce { "The list of genes must not be empty" { genes mustNot BeEmpty } }
        return withDna((dna + genes.sumOf { it.dna }) / (genes.size + 1))
    }

    // / Documentation inherited from [NumberGene]
    override fun toDouble() = dna

    // / Documentation inherited from [NumberGene]
    override fun toInt() = dna.toInt()

    // / Documentation inherited from [Gene]
    override fun generator() = Core.random.nextDoubleInRange(range)

    // / Documentation inherited from [Gene]
    override fun withDna(dna: Double) = DoubleGene(dna, range)

    // / Documentation inherited from [Verifiable]
    override fun verify() = dna in range && filter(dna)

    // / Documentation inherited from [Any]
    override fun toString() = "DoubleGene(dna=$dna, range=$range)"

    // / Documentation inherited from [Any]
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is DoubleGene -> false
        other::class != this::class -> false
        else ->
            dna == other.dna &&
                range == other.range
    }

    // / Documentation inherited from [Any]
    override fun hashCode() = Objects.hash(DoubleGene::class, dna, range)
}
