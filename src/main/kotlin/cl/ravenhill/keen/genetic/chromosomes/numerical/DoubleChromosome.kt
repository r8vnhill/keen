/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.InvalidStateException
import cl.ravenhill.keen.genetic.chromosomes.AbstractChromosome
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import java.util.Objects
import kotlin.properties.Delegates


/**
 * A chromosome that contains a list of [DoubleGene]s.
 *
 * @param genes The list of genes that this chromosome will contain.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 */
class DoubleChromosome private constructor(
    genes: List<DoubleGene>,
    val range: Pair<Double, Double>
) : AbstractChromosome<Double>(genes) {

    private constructor(size: Int, range: Pair<Double, Double>) : this(
        (0 until size).map {
            DoubleGene(Core.random.nextDouble(range.first, range.second), range)
        }, range
    )


    override fun verify() = genes.all { it.verify() }

    @Suppress("UNCHECKED_CAST")
    override fun duplicate(genes: List<Gene<Double>>) =
        DoubleChromosome(genes as List<DoubleGene>, range)

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is DoubleChromosome -> false
        else -> genes == other.genes
    }

    override fun hashCode() = Objects.hash(DoubleChromosome::class, genes, range)

    class Factory : Chromosome.Factory<Double> {

        lateinit var range: Pair<Double, Double>
        var size by Delegates.notNull<Int>()

        override fun make() = when {
            size < 1 -> throw InvalidStateException("size") {
                "The size of a chromosome ($size) must be greater than 0."
            }

            range.first.isNaN() || range.second.isNaN() -> DoubleChromosome((0 until size).map {
                DoubleGene(Double.NaN, range)
            }, range)

            (!range.first.isFinite())
                    || (!range.second.isFinite()) -> {
                throw InvalidStateException("range") {
                    "The range of a chromosome ([${range.first}, ${range.second})) " +
                            "must be finite."
                }
            }

            range.first == range.second -> {
                throw InvalidStateException("range") {
                    "The range of a chromosome ([${range.first}, ${range.second})) " +
                            "must not be empty."
                }
            }

            range.first > range.second -> {
                throw InvalidStateException("range") {
                    "The range of a chromosome ([${range.first}, ${range.second})) " +
                            "must be ordered."
                }
            }

            else -> DoubleChromosome(size, range)
        }

        override fun toString(): String {
            return "DoubleChromosome.Builder { " +
                    "size: $size, " +
                    "range: $range }"
        }
    }
}