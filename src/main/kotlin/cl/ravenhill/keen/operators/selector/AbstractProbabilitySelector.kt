/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.util.optimizer.Optimizer
import cl.ravenhill.keen.util.validateSize


abstract class AbstractProbabilitySelector<DNA>(protected val sorted: Boolean) : Selector<DNA> {
    private val reverter = if (sorted) {
        { it: List<Double> -> it.reversed() }
    } else {
        { it: List<Double> -> it.sorted().reversed() }
    }

    abstract fun probabilities(population: List<Phenotype<DNA>>, count: Int): List<Double>

    override fun invoke(
        population: List<Phenotype<DNA>>,
        count: Int,
        optimizer: Optimizer
    ): List<Phenotype<DNA>> {
        count.validateSize(true to { "Selection count [$count] must be at least 0" })
        val selection = mutableListOf<Phenotype<DNA>>()
        val probabilities = probabilities(population, count)
        return selection
    }
}