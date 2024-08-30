/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines

import arrow.core.Either
import arrow.core.getOrElse
import cl.ravenhill.keen.evolution.config.EvolutionConfiguration
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.exceptions.EvolutionException
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.listeners.mixins.GenerationListener
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Abstract base class for implementing evolutionary algorithms.
 *
 * The `AbstractEvolver` class serves as a foundational framework for creating evolutionary algorithms, encapsulating
 * the core structure and processes needed to evolve a population of individuals over successive generations. It manages
 * the lifecycle of the evolutionary process, including the invocation of listeners at key stages and the enforcement of
 * evolutionary limits.
 *
 * ## Usage:
 * This class is designed to be extended by specific evolutionary algorithm implementations. It provides a concrete
 * implementation of the [Evolver] interface, handling common tasks such as managing listeners and applying evolutionary
 * limits. Subclasses are required to implement the [iterateGeneration] method, which defines the specific logic for
 * advancing the evolutionary state through each generation.
 *
 * ### Example: Implementing a Custom Evolver
 * ```kotlin
 * class MyEvolver<T, F, R, S>(
 *     evolutionConfiguration: EvolutionConfiguration<T, F, R, S>
 * ) : AbstractEvolver<T, F, R, S>(evolutionConfiguration)
 *         where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {
 *
 *     override var state: S = // initialize the state here
 *
 *     override suspend fun iterateGeneration(state: S): Either<EvolutionException, S> {
 *         // Define the logic for advancing the evolutionary process by one generation
 *         return updatedState.right() // Right indicates a successful operation
 *     }
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 * @property evolutionConfiguration The configuration settings for the evolutionary process, including listeners and
 *   limits.
 * @property publicListeners A list of listeners that will be notified of events during the evolution process. The
 *   listeners are provided as copies to prevent direct modifications, preserving the integrity of the evolution
 *   process.
 */
abstract class AbstractEvolver<T, F, R, S>(
    private val evolutionConfiguration: EvolutionConfiguration<T, F, R, S>
) : Evolver<T, F, R, S> where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {

    /**
     * The current evolutionary state.
     */
    protected abstract var state: S

    /**
     * The limits that control the evolution process.
     */
    private val limits = evolutionConfiguration.limits

    /**
     * The listeners that are common to all evolutionary processes.
     */
    protected val listeners = evolutionConfiguration.listeners + limits.map { it.listener }

    /**
     * The listeners that are specific to the evolutionary process.
     */
    private val evolutionListeners = evolutionConfiguration.listeners.filterIsInstance<EvolutionListener<*, *, *, S>>()

    /**
     * The listeners that are specific to each generation.
     */
    private val generationListeners =
        evolutionConfiguration.listeners.filterIsInstance<GenerationListener<*, *, *, S>>()

    final override val publicListeners
        get() = evolutionConfiguration.listeners.map { it.copy() } + limits.map { it.listener.copy() }

    /**
     * Executes the evolutionary process.
     *
     * This method manages the main loop of the evolutionary algorithm, invoking lifecycle listeners at the start and
     * end of the evolution, as well as at the start and end of each generation. The process continues until one of the
     * configured limits is met, at which point the final state is returned.
     *
     * @return The final evolutionary state after the process is complete.
     */
    override suspend fun evolve(): S {
        evolutionListeners.forEach { it.onEvolutionStart() }
        do {
            generationListeners.forEach { it.onGenerationStart(state) }
            state = iterateGeneration(state)
                .getOrElse { throw it } // Re-throw the exception to halt the evolution process
            generationListeners.forEach { it.onGenerationEnd(state) }
        } while (limits.none { it(state) })
        evolutionListeners.forEach { it.onEvolutionEnd(state) }
        return state
    }

    /**
     * Advances the evolutionary state by one generation.
     *
     * Subclasses must implement this method to define the specific logic for updating the evolutionary state in each
     * generation.
     *
     * The method should return an [Either] value, where the left side indicates an error condition and the right side
     * indicates a successful operation. If an error occurs, the method should return an [Either.Left] value containing
     * an [EvolutionException] describing the error. If the operation is successful, the method should return an
     * [Either.Right] value containing the updated evolutionary state.
     *
     * @param state The current evolutionary state.
     * @return The updated evolutionary state after one generation.
     */
    abstract suspend fun iterateGeneration(state: S): Either<EvolutionException, S>
}
