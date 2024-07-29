/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Represents an evolver in an evolutionary algorithm.
 *
 * The `Evolver` interface defines the basic structure and operations for an entity that manages the evolution process.
 * This includes properties for the population size and survival rate, as well as a method to perform the evolution.
 *
 * ## Usage:
 * Implement this interface in classes that perform the overall evolution process in an evolutionary algorithm.
 * Provide the logic to manage the population and apply evolutionary operations.
 *
 * ### Example:
 * ```kotlin
 * class MyEvolver<T, F, R, S> : Evolver<T, F, R, S>
 *     where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> {
 *
 *     override fun evolve(): S {
 *         // Implementation of the evolution logic
 *     }
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolution state, which must extend [EvolutionState].
 */
interface Evolver<T, F, R, S> where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> {

    /**
     * Performs the evolution process and returns the resulting state.
     *
     * This method manages the overall evolution process, applying evolutionary operations to the population
     * and returning the resulting state.
     *
     * @return The new state after performing the evolution.
     */
    fun evolve(): S
}
