package cl.ravenhill.keen.genetic.genes.numerical

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.numerical.DoubleChromosome
import cl.ravenhill.keen.genetic.genes.ComparableGene

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
class DoubleGene(override val dna: Double, private val range: ClosedFloatingPointRange<Double>) :
        NumberGene<Double>, ComparableGene<Double> {

    // https://hal.archives-ouvertes.fr/hal-00576641v1/document
    override fun mean(gene: NumberGene<Double>) =
        duplicate((dna - dna / 2) + (gene.dna - gene.dna / 2))

    override fun mutate(): DoubleGene {
        val max = range.endInclusive
        val min = range.start
        return DoubleGene(dna + (Core.rng.nextDouble() * (max - min) + min), range)
    }

    override fun duplicate(dna: Double) = DoubleGene(dna, range)

    override fun verify() = dna in range

    override fun toString() = "$dna"
}