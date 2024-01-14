/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.evolution.Evolver
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * An interface representing a limit condition in an evolutionary algorithm.
 *
 * A `Limit` in the context of evolutionary algorithms is a condition or a set of conditions that determine whether the
 * evolutionary process should continue or terminate. It is used to define stopping criteria based on various factors
 * such as the number of generations, fitness thresholds, or other custom conditions.
 *
 * ## Key Concepts:
 * - **Termination Criteria**: The primary role of a `Limit` is to provide a mechanism to decide when the
 *   evolutionary process should stop. This decision is typically based on the current state of the evolution.
 * - **State Evaluation**: Each `Limit` evaluates the [EvolutionState] to determine if the specified criteria
 *   for termination are met.
 *
 * ## Usage:
 * Implement this interface to define custom termination conditions for an evolutionary algorithm. The specific
 * criteria can be tailored to the requirements of the problem being solved or the objectives of the algorithm.
 *
 * ### Example:
 * Implementing a generation limit:
 * ```kotlin
 * class GenerationLimit<T, G>(private val maxGenerations: Int) : Limit<T, G> where G : Gene<T, G> {
 *     override fun invoke(state: EvolutionState<T, G>): Boolean {
 *         return state.generation >= maxGenerations
 *     }
 * }
 *
 * // Usage in an evolutionary algorithm
 * val limit: Limit<MyDataType, MyGene> = GenerationLimit(100)
 * val shouldTerminate = limit(currentState)
 * ```
 * In this example, `GenerationLimit` implements the `Limit` interface, providing a stopping condition when
 * the number of generations reaches or exceeds a specified maximum.
 *
 * @param T The type of data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 * @property engine The [Evolver] instance that is executing the evolutionary process.
 */
interface Limit<T, G> where G : Gene<T, G> {

    var engine: Evolver<T, G>?

    /**
     * Evaluates the given [EvolutionState] and determines whether the evolutionary process should terminate.
     *
     * @param state The current state of the evolution process.
     * @return `true` if the termination criteria are met, `false` otherwise.
     */
    operator fun invoke(state: EvolutionState<T, G>): Boolean
}
