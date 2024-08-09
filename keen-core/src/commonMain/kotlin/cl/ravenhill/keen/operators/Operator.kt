/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Represents a generic operator in an evolutionary algorithm.
 *
 * The `Operator` interface defines a structure for applying operations to states within an evolutionary algorithm.
 * It uses a transformation function to process a state and produce an output state of a specified size.
 *
 * ## Usage:
 * Implement this interface to create custom operators for evolutionary algorithms, such as selection, mutation,
 * and crossover operators.
 *
 * ### Example:
 * Implementing a simple operator:
 * ```kotlin
 * class SimpleOperator<T, F, R> : Operator<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {
 *     override fun <S> invoke(state: S, outputSize: Int, buildState: (List<Individual<T, F, R>>) -> S): S {
 *         val selected = state.population.take(outputSize)
 *         return buildState(selected)
 *     }
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 */
interface Operator<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * Applies the operator to the given state and produces an output state.
     *
     * @param S The type of the state.
     * @param state The input state to process.
     * @param outputSize The desired size of the output state.
     * @param buildState A function to build the output state from the selected individuals.
     * @return The output state.
     */
    operator fun <S> invoke(
        state: S,
        outputSize: Int,
        buildState: (List<Individual<T, F, R>>) -> S
    ): S where S : EvolutionState<T, F, R>
}
