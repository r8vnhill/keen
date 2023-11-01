/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.CollectionRequirement.BeEmpty
import cl.ravenhill.enforcer.requirements.IntRequirement.BeAtLeast
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.optimizer.IndividualOptimizer

/**
 * A selector is an operator that selects a subset of the population to be used in the next generation.
 * The selection is based on the fitness of the phenotypes.
 *
 * @param DNA The type of the DNA of the phenotypes.
 */
interface Selector<DNA, G> where G : Gene<DNA, G> {

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
        optimizer: IndividualOptimizer<DNA, G>
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
        optimizer: IndividualOptimizer<DNA, G>
    ): Population<DNA, G> {
        enforce {
            "Population size [${population.size}] must be at least 1" {
                population mustNot BeEmpty
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
        optimizer: IndividualOptimizer<DNA, G>
    ): Population<DNA, G>
}
