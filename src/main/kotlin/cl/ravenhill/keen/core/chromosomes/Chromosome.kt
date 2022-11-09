/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.core.chromosomes

import cl.ravenhill.keen.core.GeneticMaterial
import cl.ravenhill.keen.core.genes.Gene

/**
 * Sequence of genes.
 *
 * @param DNA   The type of the genes' values.
 *
 * @property genes  The genes of the chromosome.
 * @property size   The size of the chromosome.
 *
 * @author <a href="https://github.com/r8vnhill">R8V</a>
 */
interface Chromosome<DNA> : GeneticMaterial {

    val genes: List<Gene<DNA>>

    val size: Int
        get() = genes.size

    /// {@inheritDoc}
    override fun verify() = genes.isNotEmpty() && genes.all { it.verify() }

    /**
     * Returns the gene at the given ``index``.
     */
    operator fun get(index: Int) = genes[index]

    /**
     * Returns a new chromosome with the given ``genes``.
     */
    fun copy(genes: List<Gene<DNA>>): Chromosome<DNA>

    /**
     * Builder for [Chromosome]s.
     *
     * @param DNA   The type of the genes' values.
     */
    interface Builder<DNA> {

        /**
         * Builds a new chromosome.
         */
        fun build(): Chromosome<DNA>
    }
}