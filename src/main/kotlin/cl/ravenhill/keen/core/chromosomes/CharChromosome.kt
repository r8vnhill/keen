/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.core.chromosomes

import cl.ravenhill.keen.core.genes.CharGene
import cl.ravenhill.keen.core.genes.Gene


class CharChromosome(override val genes: List<Gene<Char>>) : Chromosome<Char> {
    override fun copy(genes: List<Gene<Char>>) = CharChromosome(genes)

    constructor(genes: Int) : this(List(genes) { CharGene.create() })

    override fun toString(): String {
        return genes.joinToString("")
    }

    class Builder(private val size: Int) : Chromosome.Builder<Char> {
        override fun build() = CharChromosome(size)

        override fun toString() = "CharChromosome.Builder { size: $size }"
    }
}
