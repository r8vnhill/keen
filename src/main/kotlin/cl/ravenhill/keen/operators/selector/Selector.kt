/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.enforcer.requirements.IntRequirement.BeAtLeast
import cl.ravenhill.enforcer.requirements.IntRequirement.BePositive
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
                population.size must BePositive
            }
            "Selection count [$count] must be at least 0" { count must BeAtLeast(0) }
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