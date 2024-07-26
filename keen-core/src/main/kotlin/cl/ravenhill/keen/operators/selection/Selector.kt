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
import cl.ravenhill.keen.ranking.FitnessRanker


/**
 * Represents a selector in an evolutionary algorithm.
 *
 * The `Selector` interface extends the `Operator` interface and defines the basic structure and operations for
 * selecting individuals from a population based on their fitness. This includes a state builder for creating new
 * states and a method for performing the selection process.
 *
 * ## Usage:
 * This interface is intended to be implemented by classes that perform specific selection operations on the state of
 * the evolutionary algorithm, such as tournament selection or roulette wheel selection. Implementing classes should
 * provide the logic to select individuals from the population based on their fitness.
 *
 * ### Example:
 * ```kotlin
 * class TournamentSelector<T, F, S>(
 *     override val stateBuilder: (Int, FitnessRanker<T, F>, List<FitnessEvaluable>) -> S
 * ) : Selector<T, F> where F : Feature<T, F>, S : State<T, F> {
 *
 *     override fun select(population: List<FitnessEvaluable>, count: Int, ranker: FitnessRanker<T, F>):
 *         List<FitnessEvaluable> {
 *         // Implementation of tournament selection logic
 *     }
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @property stateBuilder The state builder function for creating new states. This function is used to create new states
 *  with the specified generation, ranker, and selected population.
 */
interface Selector<T, F, S> : Operator<T, F, S> where F : Feature<T, F>, S : State<T, F> {

    val stateBuilder: (Int, FitnessRanker<T, F>, List<FitnessEvaluable>) -> S

    /**
     * Applies the selector to the given state and produces a new state with the specified output size.
     *
     * This method performs the selection process, ensuring the population is not empty and the output size is not
     * negative. It then selects individuals from the population based on their fitness and returns a new state with
     * the selected individuals.
     *
     * @param state The current state of the evolutionary process.
     * @param outputSize The size of the output state to be produced.
     * @return The new state after applying the selector.
     */
    override fun invoke(state: S, outputSize: Int): S {
        constraints {
            "Population must not be empty" {
                state.population mustNot BeEmpty
            }
            "Selection count ($outputSize) must not be negative" {
                outputSize mustNot BeNegative
            }
        }
        val selectedPopulation = select(state.population, outputSize, state.ranker)
        return stateBuilder(state.generation, state.ranker, selectedPopulation).apply {
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
     * @param population The population of individuals to select from.
     * @param count The number of individuals to select.
     * @param ranker The ranker used to evaluate individuals in the population.
     * @return The list of selected individuals.
     */
    fun select(population: List<FitnessEvaluable>, count: Int, ranker: FitnessRanker<T, F>): List<FitnessEvaluable>
}
