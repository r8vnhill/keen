/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.repr.Representation


/**
 * A class that intercepts the evolution process in an evolutionary algorithm.
 *
 * The `EvolutionInterceptor` class provides mechanisms to perform actions before and after each step of the evolution
 * process. This allows for custom behavior to be executed at specific points in the evolutionary cycle, enabling
 * more flexible and controlled evolution processes.
 *
 * ## Usage:
 * Use this class to define actions that should occur before and after the evolution process. This can be useful for
 * logging, modifying state, or implementing custom constraints or behaviors.
 *
 * ### Example 1: Using an Interceptor to Log Before and After Evolution Steps
 * ```kotlin
 * val interceptor = EvolutionInterceptor<MyType, MyFeature, MyRepresentation, MyState>(
 *     before = { state ->
 *         println("Before evolution step: $state")
 *         state
 *     },
 *     after = { state ->
 *         println("After evolution step: $state")
 *         state
 *     }
 * )
 * ```
 *
 * ### Example 2: Using an Interceptor to Modify State Before Evolution Steps
 * ```kotlin
 * val interceptor = EvolutionInterceptor<MyType, MyFeature, MyRepresentation, MyState>(
 *     before = { state ->
 *         // Modify state before the evolution step
 *         state.copy(generation = state.generation + 1)
 *     },
 *     after = { state ->
 *         // No modifications after the evolution step
 *         state
 *     }
 * )
 * ```
 *
 * ### Example 3: Using an Interceptor to Apply a Constraint After Evolution Steps
 * ```kotlin
 * val interceptor = EvolutionInterceptor<MyType, MyFeature, MyRepresentation, MyState>(
 *     before = { state ->
 *         // No modifications before the evolution step
 *         state
 *     },
 *     after = { state ->
 *         // Apply a constraint after the evolution step
 *         constraints {
 *             "Population size must not exceed 100" { state.population.size must BeAtMost(100) }
 *         }
 *         state
 *     }
 * )
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 * @property before A function to be executed before each evolution step.
 * @property after A function to be executed after each evolution step.
 * @constructor Creates an instance of `EvolutionInterceptor` with the specified before and after functions.
 */
class EvolutionInterceptor<T, F, R, S>(
    val before: (S) -> S,
    val after: (S) -> S
) where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> {

    companion object {

        /**
         * Creates an identity interceptor that performs no operations.
         *
         * This function returns an `EvolutionInterceptor` that does nothing in the before and after steps.
         *
         * @return An identity `EvolutionInterceptor`.
         */
        fun <T, F, R, S> identity() where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> =
            EvolutionInterceptor<T, F, R, S>(before = { it }, after = { it })

        /**
         * Creates an interceptor with a specified after function.
         *
         * This function returns an `EvolutionInterceptor` that performs the specified operation after each evolution
         * step.
         *
         * ### Example:
         * ```kotlin
         * val afterInterceptor = EvolutionInterceptor.after<MyType, MyFeature, MyRepresentation, MyState> { state ->
         *     // Custom operation to be performed after each evolution step
         *     state.copy(...)
         * }
         * ```
         *
         * @param function The function to be executed after each evolution step.
         * @return An `EvolutionInterceptor` with the specified after function.
         */
        fun <T, F, R, S> after(
            function: (S) -> S,
        ) where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> =
            EvolutionInterceptor<T, F, R, S>(before = { it }, after = function)

        /**
         * Creates an interceptor with a specified before function.
         *
         * This function returns an `EvolutionInterceptor` that performs the specified operation before each evolution
         * step.
         *
         * ### Example:
         * ```kotlin
         * val beforeInterceptor = EvolutionInterceptor.before<MyType, MyFeature, MyRepresentation, MyState> { state ->
         *     // Custom operation to be performed before each evolution step
         *     state.copy(...)
         * }
         * ```
         *
         * @param function The function to be executed before each evolution step.
         * @return An `EvolutionInterceptor` with the specified before function.
         */
        fun <T, F, R, S> before(
            function: (S) -> S,
        ) where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> =
            EvolutionInterceptor<T, F, R, S>(before = function, after = { it })
    }
}
