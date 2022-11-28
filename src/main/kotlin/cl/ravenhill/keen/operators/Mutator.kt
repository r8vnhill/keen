/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene


class Mutator<DNA>(override val probability: Double) : Alterer<DNA> {
    override fun invoke(population: Population<DNA>): Population<DNA> {
        TODO("Not yet implemented")
    }

    override fun toString() = "Mutator { " +
            "probability: $probability }"
}