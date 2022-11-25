/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.util.parallelMap
import kotlinx.coroutines.runBlocking

class RouletteWheelSelector<DNA>(
    sorted: Boolean = false
) : AbstractProbabilitySelector<DNA>(sorted) {

    override fun probabilities(population: List<Genotype<DNA>>, count: Int): List<Double> {
        var fitness: List<Double>
        runBlocking {
            val rawFitness = population.parallelMap { it.fitness }
            fitness = rawFitness.parallelMap { (it - rawFitness.min()) / rawFitness.sum() }
        }
        return fitness
    }

    override fun toString() = "RouletteWheelSelector { " +
            "sorted: $sorted }"
}