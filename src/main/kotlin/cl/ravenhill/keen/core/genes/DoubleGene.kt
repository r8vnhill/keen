/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.core.genes

import cl.ravenhill.keen.core.KeenCore


class DoubleGene(override val dna: Double, private val range: ClosedFloatingPointRange<Double>) :
        Gene<Double> {

    override fun mutate(): DoubleGene {
        val max = range.endInclusive
        val min = range.start
        return DoubleGene(dna + (KeenCore.generator.nextDouble() * (max - min) + min), range)
    }

    override fun copy(dna: Double) = DoubleGene(dna, range)

    override fun verify() = dna in range

    override fun toString() = "$dna"
}