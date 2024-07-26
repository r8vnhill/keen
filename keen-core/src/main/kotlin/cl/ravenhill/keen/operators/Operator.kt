/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators

import cl.ravenhill.keen.evolution.states.State
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.mixins.FitnessEvaluable

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
 *     override fun <S, I> invoke(
 *         state: S,
 *         outputSize: Int,
 *         buildState: (List<I>) -> S
 *     ): S where S : State<T, F, I>, I : FitnessEvaluable {
 *         // Implementation of the operator logic
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
     * This method performs the operation on the state, ensuring the transformation is applied correctly and
     * constructs a new state with the modified population.
     *
     * @param S The type of the state.
     * @param I The type of the individuals in the state, which must extend [FitnessEvaluable].
     * @param state The current state of the evolutionary process.
     * @param outputSize The size of the output state to be produced.
     * @param buildState The function used to create the new state from the modified list of individuals.
     * @return The new state after applying the operator.
     */
    operator fun <S, I> invoke(
        state: S,
        outputSize: Int,
        buildState: (List<I>) -> S
    ): S where S : State<T, F, I>, I : FitnessEvaluable
}
