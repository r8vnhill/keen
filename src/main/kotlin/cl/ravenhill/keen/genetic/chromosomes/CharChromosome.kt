/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.genetic.genes.CharGene
import cl.ravenhill.keen.genetic.genes.Gene
import java.util.*


class CharChromosome(
    override val genes: List<Gene<Char>>,
    private val filter: (Char) -> Boolean = { true }
) : Chromosome<Char> {

    override fun duplicate(genes: List<Gene<Char>>) = CharChromosome(genes)

    constructor(
        size: Int,
        filter: (Char) -> Boolean = { true }
    ) : this(List(size) { CharGene.create(filter) })

    override fun verify() = genes.all { filter(it.dna) }

    override fun toString(): String {
        return genes.joinToString("")
    }

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is CharChromosome -> false
        other::class != this::class -> false
        else -> genes == other.genes
    }

    override fun hashCode() = Objects.hash(CharChromosome::class, genes)

    class Factory : Chromosome.Factory<Char> {
        var size = 0
        var filter: (Char) -> Boolean = { true }
        override fun make() = CharChromosome(size, filter)

        override fun toString() = "CharChromosome.Builder { size: $size }"
    }
}
