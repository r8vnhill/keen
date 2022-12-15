/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.AbstractChromosome
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.keen.genetic.genes.Gene
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

    private constructor(size: Int, range: Pair<Double, Double>) : this(
        (0 until size).map {
            DoubleGene(
                Core.rng.nextDouble(range.first, range.second),
                range
            )
        }
    )

    class Builder(private val size: Int, private val range: Pair<Double, Double>) :
            Chromosome.Factory<Double> {

        override fun make() = DoubleChromosome(size, range)

        override fun toString(): String {
            return "DoubleChromosome.Builder { " +
                    "size: $size, " +
                    "range: $range }"
        }
    }

    override fun verify() = genes.first().verify()

    @Suppress("UNCHECKED_CAST")
    override fun duplicate(genes: List<Gene<Double>>) =
        DoubleChromosome(genes as List<DoubleGene>)
}