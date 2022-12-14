package cl.ravenhill.keen.genetic.genes.numerical

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.numerical.DoubleChromosome
import cl.ravenhill.keen.genetic.genes.ComparableGene
import java.util.Objects

/**
 * [NumberGene] which holds a 64 bit floating point number.
 *
 * @property dna The value of the gene.
 * @property range The range of the gene.
 *
 * @see DoubleChromosome
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 */
class DoubleGene(
    override val dna: Double,
    private val range: Pair<Double, Double>,
    override val filter: (Double) -> Boolean = { true }
) : NumberGene<Double>, ComparableGene<Double> {

    // https://hal.archives-ouvertes.fr/hal-00576641v1/document
    override fun mean(gene: NumberGene<Double>) =
        duplicate((dna - dna / 2) + (gene.dna - gene.dna / 2))

    private val start = range.first
    private val end = range.second

    override fun toDouble() = dna

    override fun toInt() = dna.toInt()

    override fun generator() = Core.rng.nextDouble() * (end - start) + start

    override fun duplicate(dna: Double) = DoubleGene(dna, range)

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