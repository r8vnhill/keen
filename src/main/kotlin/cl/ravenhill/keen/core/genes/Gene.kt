/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.core.genes

import cl.ravenhill.keen.core.GeneticMaterial

/**
 * Atomic unit of a chromosome.
 *
 * @param DNA   The type of the gene's value.
 * @property dna        The gene's value.
 */
interface Gene<DNA> : GeneticMaterial {

    val dna: DNA

    /**
     * Creates a new gene with a mutated value.
     */
    fun mutate(): Gene<DNA>

    /**
     * Creates a new gene with the given value.
     */
    fun copy(dna: DNA): Gene<DNA>
}