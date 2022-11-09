/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.core.chromosomes

import cl.ravenhill.keen.core.KeenCore
import cl.ravenhill.keen.core.genes.DoubleGene
import cl.ravenhill.keen.core.genes.Gene
import kotlin.random.asKotlinRandom


/**
 * A chromosome that contains a list of [DoubleGene]s.
 *
 * @param genes The list of genes that this chromosome will contain.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 */
class DoubleChromosome private constructor(
    genes: List<DoubleGene>
) : AbstractChromosome<Double>(genes) {

    private constructor(size: Int, range: ClosedFloatingPointRange<Double>) : this(
        (0 until size).map {
            DoubleGene(
                KeenCore.generator.asKotlinRandom().nextDouble(range.start, range.endInclusive),
                range
            )
        }
    )

    class Builder(private val size: Int, private val range: ClosedFloatingPointRange<Double>) :
        Chromosome.Builder<Double> {

        override fun build() = DoubleChromosome(size, range)

        override fun toString(): String {
            return "DoubleChromosome.Builder { " +
                    "size: $size, " +
                    "range: $range }"
        }
    }

    override fun verify() = genes.first().verify()

    @Suppress("UNCHECKED_CAST")
    override fun copy(genes: List<Gene<Double>>) =
        DoubleChromosome(genes as List<DoubleGene>)
}