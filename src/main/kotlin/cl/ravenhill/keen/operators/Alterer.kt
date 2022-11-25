/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.util.validateProbability

interface Alterer<DNA> {
    val probability: Double

    operator fun invoke(population: List<Genotype<DNA>>): List<Genotype<DNA>>
}

abstract class AbstractAlterer<DNA>(final override val probability: Double) : Alterer<DNA> {
    init {
        probability.validateProbability()
    }
}