/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.requirements.IntRequirement.BePositive
import cl.ravenhill.keen.requirements.PairRequirement.Finite
import cl.ravenhill.keen.requirements.PairRequirement.StrictlyOrdered
import cl.ravenhill.keen.genetic.chromosomes.AbstractChromosome
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.keen.util.Filterable
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.properties.Delegates


/**
 * A chromosome that contains a list of [DoubleGene]s.
 *
 * This class constructors are private, see the [Factory] class for examples on how to
 * create a new instance of this class.
 *
 * @param genes The list of genes that this chromosome will contain.
 * @property range A pair of [Double]s that represents the range of the genes
 *                  (``a to b``).
 * @property predicate The filter to apply to the genes.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 */
class DoubleChromosome private constructor(
    genes: List<DoubleGene>,
    val range: Pair<Double, Double>,
    override val predicate: (Double) -> Boolean
) : AbstractChromosome<Double>(genes), Filterable<Double> {

    private constructor(
        size: Int,
        range: Pair<Double, Double>,
        predicate: (Double) -> Boolean
    ) : this(
        runBlocking {
            List(size) {
                DoubleGene(
                    sequence {
                        while (true) {
                            yield(Core.random.nextDouble(range.first, range.second))
                        }
                    }.filter(predicate).first(),
                    range
                )
            }
        }, range, predicate
    )

    override fun verify() = genes.all { it.verify() }

    @Suppress("UNCHECKED_CAST")
    override fun withGenes(genes: List<Gene<Double>>) =
        DoubleChromosome(genes as List<DoubleGene>, range, predicate)

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is DoubleChromosome -> false
        else -> genes == other.genes
    }

    override fun hashCode() = Objects.hash(DoubleChromosome::class, genes, range)

    /**
     * Abstract Factory Pattern implementation for [DoubleChromosome]s.
     *
     * This class is used to create [DoubleChromosome]s more easily.
     * It allows the creation of [Chromosome]s with a Kotlin-like syntax.
     *
     * @property range Pair<Double, Double>
     * @property size Int
     * @property filter Function1<Double, Boolean>
     */
    class Factory : Chromosome.AbstractFactory<Double, DoubleGene>() {

        lateinit var range: Pair<Double, Double>
        var size by Delegates.notNull<Int>()
        var filter: (Double) -> Boolean = { true }

        override fun make(): DoubleChromosome {
            enforce {
                size should BePositive()
                range should StrictlyOrdered()
                range should Finite()
            }
            return when {
                range.first.isNaN() || range.second.isNaN() -> DoubleChromosome((0 until size).map {
                    DoubleGene(Double.NaN, range)
                }, range, filter)

                else -> DoubleChromosome(size, range, filter)
            }
        }

        override fun toString(): String {
            return "DoubleChromosome.Builder { " +
                    "size: $size, " +
                    "range: $range }"
        }
    }
}