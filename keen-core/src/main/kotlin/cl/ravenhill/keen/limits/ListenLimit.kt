/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.evolution.Evolver
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.EvolutionListener


/**
 * A limit implementation in an evolutionary algorithm that triggers based on a specified condition and involves an
 * [EvolutionListener].
 *
 * `ListenLimit` integrates an [EvolutionListener] into the limit checking mechanism of an evolutionary algorithm. It
 * evaluates a condition (defined as a predicate) on the current evolutionary state and determines whether the
 * evolutionary process should be halted.
 *
 * ## Functionality:
 * - **Listener Integration**: Incorporates an [EvolutionListener] into the limit evaluation process. This listener
 *   can be used to monitor and react to the evolutionary process, providing additional flexibility in defining
 *   the termination condition.
 * - **Conditional Check**: Utilizes a custom predicate that takes the current [EvolutionState] and evaluates
 *   whether a specified condition has been met. If the condition returns `true`, it indicates that the
 *   evolutionary process should be terminated.
 *
 * ## Usage:
 * `ListenLimit` is particularly useful in scenarios where the termination condition is complex or requires
 * monitoring specific aspects of the evolutionary process, such as population diversity, convergence rate,
 * or external criteria.
 *
 * ### Example:
 * ```kotlin
 * val listener: EvolutionListener<MyDataType, MyGene> = /* Define your listener */
 * val limit = ListenLimit(listener) { state ->
 *     // Define the condition for termination based on the state
 *     state.generation > 100 || isConverged(listener)
 * }
 * ```
 * In this example, `ListenLimit` is created with a listener and a predicate that checks if the number of
 * generations has exceeded 100 or if the population has converged. The evolutionary process will stop when
 * either of these conditions is met.
 *
 * See [SteadyGenerations] for an example of a `ListenLimit` implementation.
 *
 * @param T The type of data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 * @param listener The [EvolutionListener] that is integrated into the limit checking process.
 * @param predicate A function associated with the listener that takes an [EvolutionState] and returns
 *   a `Boolean` indicating whether the evolutionary process should be terminated.
 * @property engine The [Evolver] instance that is executing the evolutionary process.
 */
open class ListenLimit<T, G>(
    private val listener: EvolutionListener<T, G>,
    private val predicate: EvolutionListener<T, G>.(EvolutionState<T, G>) -> Boolean
) : Limit<T, G> where G : Gene<T, G> {
    override var engine: Evolver<T, G>? = null
        set(value) {
            value?.listeners?.add(listener)
            field = value
        }

    /**
     * Evaluates the termination condition for the evolutionary process using the provided listener predicate.
     *
     * This method is an implementation of the `invoke` function from the [Limit] interface. It is called during the
     * evolutionary process to check whether the specified condition (defined in the [predicate]) is met, based on
     * the current [EvolutionState]. If the condition is satisfied, it indicates that the evolutionary process should
     * be halted.
     *
     * ## Functionality:
     * - **Condition Evaluation**: Utilizes the [predicate] function, which is part of the [listener], to evaluate
     *   whether the termination condition is met.
     * - **State Analysis**: The current [EvolutionState] is passed to the predicate function, allowing it to
     *   analyze various aspects of the state, such as the generation number, population fitness, diversity, or
     *   any other relevant metric.
     *
     * ## Usage:
     * This method is automatically invoked by the evolutionary algorithm's control mechanism at each generation or
     * evolutionary step. It is not typically called directly by the user.
     *
     * @param state The current [EvolutionState] of the evolutionary process. This state is used by the predicate to
     *   determine whether the termination condition is satisfied.
     * @return `true` if the termination condition is met and the evolutionary process should be halted, `false`
     *   otherwise.
     */
    override fun invoke(state: EvolutionState<T, G>) = listener.predicate(state)
}

