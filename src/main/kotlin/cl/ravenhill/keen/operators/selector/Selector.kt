/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.IntConstraint.BeAtLeast
import cl.ravenhill.jakt.constraints.IntConstraint.BeEqualTo
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.optimizer.IndividualOptimizer

/**
 * A selector is an operator that selects a subset of the population to be used in the next generation.
 * The selection is based on the fitness of the individuals.
 *
 * @param DNA The type of the DNA of the individuals.
 */
interface Selector<DNA, G> where G : Gene<DNA, G> {

    /**
     * Selects a specified number of individuals from a population based on the given optimizer.
     *
     * @param population The input population of individuals.
     * @param count The number of individuals to select.
     * @param optimizer The optimizer used to evaluate and compare individuals.
     *
     * @return The selected individuals, which form a new population.
     */
    fun select(
        population: Population<DNA, G>,
        count: Int,
        optimizer: IndividualOptimizer<DNA, G>,
    ): Population<DNA, G>

    /**
     * Selects a subset of the population to be used in the next generation.
     *
     * @param population The population to select from.
     * @param count The number of individuals to select.
     * @param optimizer The optimizer that is using this selector.
     * @return The selected individuals.
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
 * @param DNA The type of the DNA of the individuals.
 */
abstract class AbstractSelector<DNA, G : Gene<DNA, G>> : Selector<DNA, G> {
    final override operator fun invoke(
        population: Population<DNA, G>,
        count: Int,
        optimizer: IndividualOptimizer<DNA, G>
    ): Population<DNA, G> {
        constraints {
            "Population size [${population.size}] must be at least 1" {
                population mustNot BeEmpty
            }
            "Selection count [$count] must be at least 0" { count must BeAtLeast(0) }
        }
        return select(population, count, optimizer).apply {
            constraints {
                "Selected population size [$size] must match selection count [$count]." {
                    size must BeEqualTo(count)
                }
            }
        }
    }
}
