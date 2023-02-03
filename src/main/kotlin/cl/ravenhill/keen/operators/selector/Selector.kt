/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.IntRequirement.BeAtLeast
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer

/**
 * A selector is an operator that selects a subset of the population to be used in the next generation.
 * The selection is based on the fitness of the phenotypes.
 *
 * @param DNA The type of the DNA of the phenotypes.
 */
interface Selector<DNA> {

    /**
     * Selects a subset of the population to be used in the next generation.
     *
     * @param population The population to select from.
     * @param count The number of phenotypes to select.
     * @param optimizer The optimizer that is using this selector.
     * @return The selected phenotypes.
     */
    operator fun invoke(
        population: List<Phenotype<DNA>>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA>
    ): List<Phenotype<DNA>>
}

/**
 * An abstract implementation of [Selector] that validates the parameters and delegates the selection
 * to the [select] method.
 *
 * @param DNA The type of the DNA of the phenotypes.
 */
abstract class AbstractSelector<DNA> : Selector<DNA> {
    final override operator fun invoke(
        population: List<Phenotype<DNA>>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA>
    ): List<Phenotype<DNA>> {
        enforce {
            population.size should BeAtLeast(1) {
                "Population size [${population.size}] must be at least 1"
            }
            count should BeAtLeast(0) { "Selection count [$count] must be at least 0" }
        }
        return select(population, count, optimizer)
    }

    /**
     * Selects a subset of the population to be used in the next generation.
     *
     * @param population The population to select from.
     * @param count The number of phenotypes to select.
     * @param optimizer The optimizer that is using this selector.
     * @return The selected phenotypes.
     */
    protected abstract fun select(
        population: List<Phenotype<DNA>>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA>
    ): List<Phenotype<DNA>>

}