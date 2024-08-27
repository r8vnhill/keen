/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution

/**
 * An interceptor for evolutionary processes that allows customization of actions to be performed before and after each
 * evolutionary step.
 *
 * The `EvolutionInterceptor` class enables you to insert custom logic before and after the evolutionary process
 * progresses. This can be useful for tasks such as logging, modifying the evolutionary state, or injecting additional
 * behaviors at specific stages.
 *
 * ## Usage:
 * You can use this class to define actions that should occur before and after the evolutionary algorithm advances to a
 * new state. The `before` function is applied to the state before the evolutionary step, and the `after` function is
 * applied after the step.
 *
 * ### Example:
 * Implementing an interceptor that logs the state before and after each evolutionary step:
 * ```kotlin
 * val loggingInterceptor = EvolutionInterceptor<MyType, MyFeature, MyRepresentation, MyState>(
 *     before = { state ->
 *         println("Before evolution: $state")
 *         state
 *     },
 *     after = { state ->
 *         println("After evolution: $state")
 *         state
 *     }
 * )
 * ```
 *
 * ### Identity Interceptor:
 * The `identity` interceptor does nothing and simply returns the state unchanged.
 * ```kotlin
 * val identityInterceptor = EvolutionInterceptor.identity<MyType, MyFeature, MyRepresentation, MyState>()
 * ```
 *
 * @param T The type of the value held by the features in the representation.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state.
 * @property before A function applied to the state before the evolutionary step.
 * @property after A function applied to the state after the evolutionary step.
 */
class EvolutionInterceptor<T, F, R, S>(
    val before: (S) -> S,
    val after: (S) -> S
) {
    companion object {
        /**
         * Returns an `EvolutionInterceptor` that applies no changes to the state before or after the evolutionary step.
         *
         * This can be used as a default or no-op interceptor.
         */
        fun <T, F, R, S> identity() = EvolutionInterceptor<T, F, R, S>(before = { it }, after = { it })

        /**
         * Returns an `EvolutionInterceptor` that only applies a given `before` function, leaving the `after` function
         * as a no-op.
         *
         * @param before The function to apply to the state before the evolutionary step.
         */
        fun <T, F, R, S> before(before: (S) -> S) = EvolutionInterceptor<T, F, R, S>(before, after = { it })

        /**
         * Returns an `EvolutionInterceptor` that only applies a given `after` function, leaving the `before` function
         * as a no-op.
         *
         * @param after The function to apply to the state after the evolutionary step.
         */
        fun <T, F, R, S> after(after: (S) -> S) = EvolutionInterceptor<T, F, R, S>(before = { it }, after)
    }
}
