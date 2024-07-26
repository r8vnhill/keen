/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.selection

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.keen.evolution.states.State
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.mixins.FitnessEvaluable
import cl.ravenhill.keen.operators.Operator
import cl.ravenhill.keen.ranking.Ranker

interface Selector<T, F> : Operator<T, F> where F : Feature<T, F> {
    override operator fun <S, I> invoke(
        state: S,
        outputSize: Int,
        buildState: (List<I>) -> S
    ): S where S : State<T, F, I>, I : FitnessEvaluable {
        constraints {
            "Population must not be empty" {
                state.population mustNot BeEmpty
            }
            "Selection count ($outputSize) must not be negative" {
                outputSize mustNot BeNegative
            }
        }
        val selectedPopulation = select(state.population, outputSize, state.ranker)
        return buildState(selectedPopulation).apply {
            constraints {
                "Expected output size ($outputSize) must be equal to actual output size (${selectedPopulation.size})" {
                    selectedPopulation must HaveSize(outputSize)
                }
            }
        }
    }

    /**
     * Selects individuals from the population based on their fitness.
     *
     * This method performs the selection process, selecting a specified number of individuals from the population
     * based on their fitness and the provided ranker.
     *
     * @param I The type of the individuals in the population, which must extend [FitnessEvaluable].
     * @param population The population of individuals to select from.
     * @param count The number of individuals to select.
     * @param ranker The ranker used to evaluate individuals in the population.
     * @return The list of selected individuals.
     */
    fun <I> select(population: List<I>, count: Int, ranker: Ranker<T, F>): List<I> where I : FitnessEvaluable
}
