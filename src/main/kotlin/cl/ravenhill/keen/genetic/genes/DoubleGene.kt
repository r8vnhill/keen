/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Core


class DoubleGene(override val dna: Double, private val range: ClosedFloatingPointRange<Double>) :
        NumberGene<Double> {

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