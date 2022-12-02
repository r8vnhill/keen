/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.genetic.genes.Gene

class AnyChromosome<DNA>(override val genes: List<Gene<DNA>>) : Chromosome<DNA> {
    override fun duplicate(genes: List<Gene<DNA>>) = AnyChromosome(genes)

    class Factory<DNA>(private val genes: List<Gene<DNA>>) : Chromosome.Factory<DNA> {
        constructor(
            size: Int,
            geneFactory: (Int) -> Gene<DNA>
        ) : this((0 until size).map { geneFactory(it) })

        override fun make() = AnyChromosome(genes)
    }
}
