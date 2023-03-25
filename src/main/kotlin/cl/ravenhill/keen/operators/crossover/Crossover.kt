/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.MutablePopulation
import cl.ravenhill.keen.operators.Alterer

interface Crossover<DNA> : Alterer<DNA> {
    fun crossover(population: MutablePopulation<DNA>, indices: List<Int>): Int
}