/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selection

import cl.ravenhill.jakt.Jakt
import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.exceptions.SelectionException
import cl.ravenhill.keen.operators.Operator
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Represents a selector operator in an evolutionary algorithm.
 *
 * The `Selector` interface extends the `Operator` interface and defines the structure for selecting individuals
 * from a population based on their fitness. Implementations of this interface define how to select individuals
 * and build the output state.
 *
 * ## Usage:
 * Implement this interface to create custom selection operators for evolutionary algorithms, such as tournament
 * selection, roulette wheel selection, and rank-based selection.
 *
 * ### Example:
 * Implementing a simple selector:
 * ```kotlin
 * class SimpleSelector<T, F, R> : Selector<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {
 *     override fun select(
 *         population: Population<T, F, R>, count: Int, ranker: IndividualRanker<T, F, R>
 *     ) = population.sortedWith(ranker.comparator).take(count)
 * }
 * val selector = SimpleSelector<MyType, MyFeature, MyRepresentation>()
 * val selectedState = selector(state, 10) { selectedPopulation ->
 *     state.copy(population = selectedPopulation)
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 */
interface Selector<T, F, R> : Operator<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * Applies the selector to the given state and produces an output state.
     *
     * @param S The type of the state.
     * @param state The input state to process.
     * @param outputSize The desired size of the output state.
     * @param buildState A function to build the output state from the selected individuals.
     * @return The output state.
     * @throws CompositeException If any of the constraints are violated.
     * @throws SelectionException If the selection process fails and [Jakt.shortCircuit] is set to `true`.
     */
    override operator fun <S> invoke(
        state: S,
        outputSize: Int,
        buildState: (List<Individual<T, F, R>>) -> S
    ): S where S : EvolutionState<T, F, R> {
        constraints {
            "Population must not be empty"(::SelectionException) {
                state.population mustNot BeEmpty
            }
            "Selection count ($outputSize) must not be negative"(::SelectionException) {
                outputSize mustNot BeNegative
            }
        }
        val selectedPopulation = select(state.population, outputSize, state.ranker)
        return buildState(selectedPopulation).apply {
            constraints {
                "Expected output size ($outputSize) must be equal to actual output size (${selectedPopulation.size})"(
                    ::SelectionException
                ) {
                    selectedPopulation must HaveSize(outputSize)
                }
            }
        }
    }

    /**
     * Selects a subset of individuals from the population based on their fitness.
     *
     * @param population The population to select from.
     * @param count The number of individuals to select.
     * @param ranker The ranker used to evaluate and compare individuals.
     * @return The selected subset of individuals.
     */
    fun select(population: Population<T, F, R>, count: Int, ranker: IndividualRanker<T, F, R>): Population<T, F, R>
}
