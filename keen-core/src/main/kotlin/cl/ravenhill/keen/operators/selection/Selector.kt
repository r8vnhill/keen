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

/**
 * Represents a selector in an evolutionary algorithm.
 *
 * The `Selector` interface extends the `Operator` interface and defines the basic structure and operations for
 * selecting individuals from a population based on their fitness. This includes a method for performing the selection
 * process and an override of the operator function to apply the selector to the current state and produce a new state
 * with a specified output size.
 *
 * ## Usage:
 * Implement this interface in classes that perform specific selection operations in the evolutionary algorithm, such as
 * tournament selection or roulette wheel selection. Provide the logic to select individuals from the population based
 * on their fitness.
 *
 * ### Example:
 * ```kotlin
 * class TournamentSelector<T, F>(
 *     val tournamentSize: Int = DEFAULT_SIZE,
 *     override val stateBuilder: StateBuilder<T, F, S>
 * ) : Selector<T, F, S> where F : Feature<T, F>, S : State<T, F> {
 *
 *     override fun <I> select(population: List<I>, count: Int, ranker: FitnessRanker<T, F>): List<I>
 *         where I : FitnessEvaluable {
 *         // Implementation of tournament selection logic
 *     }
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 */
interface Selector<T, F> : Operator<T, F> where F : Feature<T, F> {

    /**
     * Applies the selector to the given state and produces a new state with the specified output size.
     *
     * This method performs the selection process, ensuring the population is not empty and the output size is not
     * negative. It then selects individuals from the population based on their fitness and returns a new state with
     * the selected individuals.
     *
     * @param S The type of the state.
     * @param I The type of the individuals in the state, which must extend [FitnessEvaluable].
     * @param state The current state of the evolutionary process.
     * @param outputSize The size of the output state to be produced.
     * @param buildState The function used to create the new state from the modified list of individuals.
     * @return The new state after applying the selector.
     */
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
