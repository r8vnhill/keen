/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.engines.Evolver
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.mixins.FitnessEvaluable


/**
 * Represents a limit in an evolutionary algorithm.
 *
 * The `Limit` interface defines the basic structure and operations for a limit that determines whether the evolutionary
 * process should stop. This includes a deprecated property for the engine and a method to evaluate the limit condition
 * based on the current state.
 *
 * ## Usage:
 * Implement this interface in classes that define specific stopping conditions for the evolutionary algorithm, such as
 * reaching a certain number of generations or achieving a target fitness level. Provide the logic to evaluate the limit
 * condition.
 *
 * ### Example:
 * ```kotlin
 * class GenerationLimit<T, F, I>(
 *     private val maxGenerations: Int
 * ) : Limit<T, F, I> where F : Feature<T, F>, I : FitnessEvaluable {
 *
 *     @Deprecated("This property will be removed in future versions.")
 *     override var engine: Evolver<T, F, I>? = null
 *
 *     override fun invoke(state: State<T, F, I>) = state.generation >= maxGenerations
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param I The type of the individuals in the state, which must extend [FitnessEvaluable].
 */
interface Limit<T, F, I> where F : Feature<T, F>, I : FitnessEvaluable {

    /**
     * The engine associated with this limit.
     *
     * This property is deprecated and will be removed in future versions.
     */
    @Deprecated("This property will be removed in future versions.")
    var engine: Evolver<T, F, I>?

    /**
     * Evaluates the limit condition based on the current state.
     *
     * This method determines whether the evolutionary process should stop based on the specified limit condition.
     *
     * @param state The current state of the evolutionary process.
     * @return `true` if the limit condition is met and the process should stop, `false` otherwise.
     */
    operator fun invoke(state: EvolutionState<T, F, I>): Boolean
}
