/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.util.math.eq
import cl.ravenhill.keen.util.math.sub
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer
import kotlin.math.min


class RouletteWheelSelector<DNA>(
    sorted: Boolean = false
) : AbstractProbabilitySelector<DNA>(sorted) {

    override fun probabilities(
        population: List<Phenotype<DNA>>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA>
    ): DoubleArray {
        val fitness = population.fitness.let {
            it sub min(it.min(), 0.0)
        }
        val cums = fitness.reduce { acc, d -> acc + d }
        return if (cums eq 0.0) {
            List(population.size) { 1.0 / population.size }
        } else {
            fitness.map { it / cums }
        }.toDoubleArray()
    }

    override fun toString() = "RouletteWheelSelector { " +
            "sorted: $sorted }"
}

private val <DNA> List<Phenotype<DNA>>.fitness: List<Double>
    get() = this.map { it.fitness }
