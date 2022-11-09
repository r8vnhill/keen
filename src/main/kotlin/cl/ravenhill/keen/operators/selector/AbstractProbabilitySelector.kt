/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.core.Genotype
import cl.ravenhill.keen.util.Optimizer


abstract class AbstractProbabilitySelector<DNA>(protected val sorted: Boolean) : Selector<DNA> {
    abstract fun probabilities(population: List<Genotype<DNA>>, count: Int): List<Double>

    override fun invoke(
        population: List<Genotype<DNA>>,
        count: Int,
        optimizer: Optimizer
    ): List<Genotype<DNA>> {
        val probabilities = probabilities(population, count)
        val selected = mutableListOf<Genotype<DNA>>()
        for (i in 0 until count) {
            val random = Math.random()
            var sum = 0.0
            for (j in population.indices) {
                sum += probabilities[j]
                if (random <= sum) {
                    selected.add(population[j])
                    break
                }
            }
        }
        return if (sorted) selected.sortedByDescending { it.fitness } else selected
    }
}