/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.selection

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.evolution.states.State
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.Operator
import cl.ravenhill.keen.ranking.FitnessRanker


interface Selector<T, F> : Operator<T, F> where F : Feature<T, F> {

    override fun invoke(state: State<T, F>, outputSize: Int): State<T, F> {
        constraints {
            "Population must not be empty" {
                state.population mustNot BeEmpty
            }
            "Selection count ($outputSize) must not be negative" {
                outputSize mustNot BeNegative
            }
        }
        val selectedPopulation = select(state.population, outputSize, state.ranker)
        return GeneticEvolutionState(
            state.generation,
            state.ranker,
            selectedPopulation
        ).apply {
            constraints {
                "Expected output size ($outputSize) must be equal to actual output size (${selectedPopulation.size})" {
                    selectedPopulation must HaveSize(outputSize)
                }
            }
        }
    }

    /**
     * Selects a subset of individuals from a population based on specific criteria.
     *
     * See [Selector] for more information.
     *
     * @param population The population from which to select individuals.
     * @param count The number of individuals to select.
     * @param ranker The [FitnessRanker] used to rank individuals in the population.
     * @return A [Population] consisting of the selected individuals.
     */
    fun select(population: Population<T, F>, count: Int, ranker: FitnessRanker<T, F>): Population<T, F>
}
