/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.keen.genetic.chromosomes.AbstractChromosome
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene

/**
 * A chromosome that contains a list of [IntGene]s.
 *
 * @param genes The list of genes that this chromosome will contain.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 */
class IntChromosome private constructor(
    genes: List<IntGene>
) : AbstractChromosome<Int>(genes) {

    /**
     * Creates a new [IntChromosome] from a given [size], [range] and a [filter]
     *
     * @param size The size of the chromosome.
     * @param range The range of the genes.
     * @param filter The filter to apply to the genes.
     */
    private constructor(size: Int, range: IntRange, filter: (Int) -> Boolean) : this(
        (0 until size).map {
            IntGene(range.random(), range, filter)
        }
    )

    @Suppress("UNCHECKED_CAST")
    override fun duplicate(genes: List<Gene<Int>>) =
        IntChromosome(genes as List<IntGene>)


    override fun toString() = "${genes.map { it.dna }}"

    /**
     * A [Chromosome.Factory] for [IntChromosome]s.
     *
     * @property size The size of the chromosome.
     * @property range The range of the genes.
     * @property filter The filter to apply to the genes.
     *
     * @constructor Creates a new [IntChromosome.Factory].
     */
    class Factory(
        private var size: Int,
        private var range: IntRange,
        private var filter: (Int) -> Boolean = { true }
    ) : Chromosome.Factory<Int> {

        override fun make() = IntChromosome(size, range, filter)

        override fun toString(): String {
            return "IntChromosome.Builder { " +
                    "size: $size, " +
                    "range: $range," +
                    "filter: $filter }"
        }
    }
}