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
import cl.ravenhill.keen.util.validateAtLeast

interface Selector<DNA> {

    operator fun invoke(
        population: List<Phenotype<DNA>>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA>
    ): List<Phenotype<DNA>>
}

abstract class AbstractSelector<DNA> : Selector<DNA> {
    final override operator fun invoke(
        population: List<Phenotype<DNA>>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA>
    ): List<Phenotype<DNA>> {
        count.validateAtLeast(0) { "Selection count [$count] must be at least 0" }
        return select(population, count, optimizer)
    }

    protected abstract fun select(
        population: List<Phenotype<DNA>>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA>
    ): List<Phenotype<DNA>>

}