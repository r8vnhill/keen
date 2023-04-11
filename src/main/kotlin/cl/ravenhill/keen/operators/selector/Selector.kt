/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.requirements.IntRequirement.BeAtLeast
import cl.ravenhill.keen.requirements.IntRequirement.BePositive
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer

/**
 * A selector is an operator that selects a subset of the population to be used in the next generation.
 * The selection is based on the fitness of the phenotypes.
 *
 * @param DNA The type of the DNA of the phenotypes.
 */
interface Selector<DNA, G : Gene<DNA, G>> {

    /**
     * Selects a subset of the population to be used in the next generation.
     *
     * @param population The population to select from.
     * @param count The number of phenotypes to select.
     * @param optimizer The optimizer that is using this selector.
     * @return The selected phenotypes.
     */
    operator fun invoke(
        population: Population<DNA, G>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA, G>
    ): Population<DNA, G>
}

/**
 * An abstract implementation of [Selector] that validates the parameters and delegates the selection
 * to the [select] method.
 *
 * @param DNA The type of the DNA of the phenotypes.
 */
abstract class AbstractSelector<DNA, G : Gene<DNA, G>> : Selector<DNA, G> {
    final override operator fun invoke(
        population: Population<DNA, G>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA, G>
    ): Population<DNA, G> {
        enforce {
            "Population size [${population.size}] must be at least 1" {
                population.size should BePositive
            }
            "Selection count [$count] must be at least 0" { count should BeAtLeast(0) }
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
        population: Population<DNA, G>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA, G>
    ): Population<DNA, G>
}