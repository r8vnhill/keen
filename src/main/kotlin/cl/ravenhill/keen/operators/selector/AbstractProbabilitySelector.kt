/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer
import cl.ravenhill.keen.util.validateSize


abstract class AbstractProbabilitySelector<DNA>(protected val sorted: Boolean) : Selector<DNA> {
    private val reverter = if (sorted) {
        { it: List<Double> -> it.reversed() }
    } else {
        { it: List<Double> -> it.sorted().reversed() }
    }

    abstract fun probabilities(
        population: List<Phenotype<DNA>>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA>
    ): List<Double>

    override operator fun invoke(
        population: List<Phenotype<DNA>>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA>
    ): List<Phenotype<DNA>> {
        count.validateSize(true to { "Selection count [$count] must be at least 0" })
        val pop = if (sorted) {
            optimizer.sort(population)
        } else {
            population
        }
        val probabilities = probabilities(population, count, optimizer)
        checkAnCorrect(probabilities)
        incremental(probabilities)
        return List(count) { pop[indexOf(probabilities)] }
    }

    private fun incremental(probabilities: List<Double>) {
        TODO("Not yet implemented")
    }

    private fun checkAnCorrect(probabilities: List<Double>) {
        TODO("Not yet implemented")
    }

    private fun indexOf(probabilities: List<Double>): Int {
        TODO("Not yet implemented")
    }
}