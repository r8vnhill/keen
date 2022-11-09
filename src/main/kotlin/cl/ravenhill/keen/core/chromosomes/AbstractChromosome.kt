/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.core.chromosomes

import cl.ravenhill.keen.core.genes.Gene


/**
 * Abstract collection of genes.
 *
 * This class defines a basic string representation of a generic chromosome.
 */
abstract class AbstractChromosome<DNA>(override val genes: List<Gene<DNA>>) : Chromosome<DNA> {
    override fun toString() = genes.joinToString(separator = ", ", prefix = "[", postfix = "]")
}