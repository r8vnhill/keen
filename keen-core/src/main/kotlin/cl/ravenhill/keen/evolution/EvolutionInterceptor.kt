/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.features.Representation


/**
 * Intercepts and modifies the evolution state before and after certain operations in an evolutionary algorithm.
 *
 * The `EvolutionInterceptor` class provides hooks for modifying the evolution state before and after specific
 * operations. This can be useful for tasks such as logging, monitoring, or applying additional transformations
 * to the evolution state at different stages of the evolutionary process.
 *
 * ## Usage:
 * This class can be used to create interceptors that apply custom functions to the evolution state. The `before`
 * function is applied before the main operation, and the `after` function is applied after the main operation.
 * The `identity` companion object function creates an interceptor that performs no modifications.
 *
 * ### Example:
 * ```
 * val interceptor = EvolutionInterceptor.before<MyType, MyFeature, MyRepresentation> {
 *     // Modify the state before the operation
 *     it
 * }
 *
 * val anotherInterceptor = EvolutionInterceptor.after<MyType, MyFeature, MyRepresentation> {
 *     // Modify the state after the operation
 *     it
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @property before The function to be applied before the main operation.
 * @property after The function to be applied after the main operation.
 * @constructor Creates an instance of `EvolutionInterceptor` with the specified before and after functions.
 */
class EvolutionInterceptor<T, F, R>(
    val before: (EvolutionState<T, F, R>) -> EvolutionState<T, F, R>,
    val after: (EvolutionState<T, F, R>) -> EvolutionState<T, F, R>,
) where F : Feature<T, F>, R : Representation<T, F> {

    companion object {

        /**
         * Creates an identity interceptor that performs no modifications.
         *
         * @return An identity interceptor.
         */
        fun <T, F, R> identity() where F : Feature<T, F>, R : Representation<T, F> =
            EvolutionInterceptor<T, F, R>(before = { it }, after = { it })

        /**
         * Creates an interceptor that applies a function before the main operation.
         *
         * @param function The function to be applied before the main operation.
         * @return An interceptor that applies the specified function before the main operation.
         */
        fun <T, F, R> before(
            function: (EvolutionState<T, F, R>) -> EvolutionState<T, F, R>,
        ) where F : Feature<T, F>, R : Representation<T, F> =
            EvolutionInterceptor(before = function, after = { it })

        /**
         * Creates an interceptor that applies a function after the main operation.
         *
         * @param function The function to be applied after the main operation.
         * @return An interceptor that applies the specified function after the main operation.
         */
        fun <T, F, R> after(
            function: (EvolutionState<T, F, R>) -> EvolutionState<T, F, R>,
        ) where F : Feature<T, F>, R : Representation<T, F> =
            EvolutionInterceptor(before = { it }, after = function)
    }
}
