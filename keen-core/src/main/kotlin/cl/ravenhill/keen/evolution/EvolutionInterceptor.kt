/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.genetic.genes.Gene
import java.util.*


/**
 * Represents an interceptor for evolution state transformations in an evolutionary algorithm.
 *
 * `EvolutionInterceptor` allows the insertion of custom logic before and after the evolution process at each
 * generation. It can be used to modify, log, or analyze the state of the evolution at various stages.
 *
 * ## Key Features:
 * - **Pre-Processing**: The `before` function is applied to the evolution state before any genetic
 *   operations are performed. It can be used for setting up or modifying the state.
 * - **Post-Processing**: The `after` function is applied to the evolution state after all genetic
 *   operations are completed. It can be used for cleanup, logging, or applying additional transformations.
 *
 * ## Usage:
 * Implement an `EvolutionInterceptor` to introduce custom behavior at different stages of the evolutionary
 * process. It can be particularly useful for debugging, monitoring, or applying custom modifications to the
 * evolution state.
 *
 * ### Example:
 * Creating an interceptor that logs the evolution state before and after each generation:
 * ```kotlin
 * class MyGene : Gene<Int, MyGene> { /* ... */ }
 *
 * val loggingInterceptor = EvolutionInterceptor<MyDataType, MyGene>(
 *     before = { state ->
 *         println("Before evolution: $state")
 *         state // Return the state unchanged
 *     },
 *     after = { state ->
 *         println("After evolution: $state")
 *         state // Return the state unchanged
 *     }
 * )
 * ```
 * In this example, `loggingInterceptor` logs the state of evolution before and after each generation,
 * allowing for monitoring of the evolutionary process.
 *
 * @param T The type of data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 * @property before A function to be applied to the [EvolutionState] before genetic operations.
 * @property after A function to be applied to the [EvolutionState] after genetic operations.
 *
 * @constructor Creates an [EvolutionInterceptor] with specified `before` and `after` functions.
 */
class EvolutionInterceptor<T, G>(
    val before: (EvolutionState<T, G>) -> EvolutionState<T, G>,
    val after: (EvolutionState<T, G>) -> EvolutionState<T, G>,
) where G : Gene<T, G> {

    override fun toString() = "EvolutionInterceptor(before=$before, after=$after)"

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is EvolutionInterceptor<*, *> -> false
        else -> before == other.before && after == other.after
    }

    override fun hashCode() = Objects.hash(EvolutionInterceptor::class, before, after)

    companion object {

        /**
         * Creates an [EvolutionInterceptor] that performs no modifications on the evolution state.
         *
         * This factory method is useful when an interceptor is required, but no actual transformation of the evolution
         * state is needed. Both `before` and `after` functions in this interceptor are identity functions, meaning they
         * return the state as-is without any changes.
         *
         * ## Usage:
         * Use this method to create a placeholder or default interceptor that maintains the original state of the
         * evolution process. This is particularly useful in scenarios where interceptors are required by the system's
         * design, but no specific logic needs to be executed.
         *
         * @return An instance of [EvolutionInterceptor] with no operational impact on the evolution state.
         */
        fun <T, G> identity() where G : Gene<T, G> = EvolutionInterceptor<T, G>(before = { it }, after = { it })


        /**
         * Creates an [EvolutionInterceptor] that applies a specified function after genetic operations.
         *
         * This factory method is designed to generate an interceptor that acts on the evolution state post-genetic
         * operations. The `after` function provided will be applied to the evolution state after genetic operations
         * are completed.
         *
         * ## Usage:
         * This method is particularly useful when there is a need to apply additional transformations or checks
         * on the evolution state after the primary genetic operations. It allows for the insertion of custom logic
         * into the evolutionary algorithm's workflow.
         *
         * @param function A lambda function to be applied to the [EvolutionState] after genetic operations.
         *                 The function takes an [EvolutionState] as input and returns an [EvolutionState].
         * @return An [EvolutionInterceptor] configured to apply the specified `after` function.
         */
        fun <T, G> after(
            function: (EvolutionState<T, G>) -> EvolutionState<T, G>,
        ) where G : Gene<T, G> = EvolutionInterceptor<T, G>(before = { it }, after = { function(it) })
    }
}
