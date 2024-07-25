/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators

import cl.ravenhill.keen.evolution.states.State
import cl.ravenhill.keen.features.Feature

/**
 * Represents an operator in an evolutionary algorithm.
 *
 * The `Operator` interface defines the basic structure and operations for an entity that modifies the state of the
 * evolutionary process. This includes a method that applies the operator to the current state and produces a new state
 * with a specified output size.
 *
 * ## Usage:
 * This interface is intended to be implemented by classes that perform specific operations on the state of the
 * evolutionary algorithm, such as selection, crossover, or mutation. Implementing classes should provide the logic
 * to transform the state accordingly.
 *
 * ### Example:
 * ```kotlin
 * class MyOperator<T, F> : Operator<T, F> where F : Feature<T, F> {
 *
 *     override fun invoke(state: State<T, F>, outputSize: Int): State<T, F> {
 *         // Implementation of the operator logic
 *         return transformedState
 *     }
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 */
interface Operator<T, F> where F : Feature<T, F> {

    /**
     * Applies the operator to the given state and produces a new state with the specified output size.
     *
     * @param state The current state of the evolutionary process.
     * @param outputSize The size of the output state to be produced.
     * @return The new state after applying the operator.
     */
    operator fun invoke(state: State<T, F>, outputSize: Int): State<T, F>
}
