/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.genetic.Phenotype

private val <DNA> List<Phenotype<DNA>>.fitness: List<Double>
    get() = this.map { it.fitness }

class RouletteWheelSelector<DNA>(
    sorted: Boolean = false
) : AbstractProbabilitySelector<DNA>(sorted) {

    override fun probabilities(population: List<Phenotype<DNA>>, count: Int): List<Double> {
        val fitness = population.fitness
        TODO("Complete implementation")
        return fitness
    }

    override fun toString() = "RouletteWheelSelector { " +
            "sorted: $sorted }"
}