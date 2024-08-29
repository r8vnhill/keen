/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.config.EvolutionConfiguration
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.listeners.mixins.GenerationListener
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Abstract base class for implementing evolutionary algorithms.
 *
 * The `AbstractEvolver` class provides a flexible foundation for creating evolutionary algorithms by defining the core
 * structure and processes required for evolving a population of individuals over successive generations. It manages the
 * lifecycle of the evolutionary process, including invoking listeners at key points and enforcing evolution limits.
 *
 * ## Usage:
 * This class is intended to be extended by specific implementations of evolutionary algorithms. It provides a concrete
 * implementation of the [Evolver] interface, handling common operations such as managing listeners and applying limits.
 * Subclasses are required to implement the [iterateGeneration] method, which defines the specific logic for advancing
 * the evolutionary state through generations.
 *
 * ### Example: Implementing a Custom Evolver
 * ```kotlin
 * class MyEvolver<T, F, R, S>(
 *     evolutionConfiguration: EvolutionConfiguration<T, F, R, S>
 * ) : AbstractEvolver<T, F, R, S>(evolutionConfiguration)
 *         where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> {
 *
 *     override var state: S = // initialize state here
 *
 *     override fun iterateGeneration(state: S): S {
 *         // Define the logic for advancing the evolutionary process by one generation
 *         return updatedState
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
) : Evolver<T, F, R, S> where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> {

    /**
     * The current evolutionary state.
     */
    protected abstract var state: S

    /**
     * The limits that control the evolution process.
     */
    private val limits = evolutionConfiguration.limits

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
     * end of the evolution and at the start and end of each generation. The process continues until one of the
     * configured limits is met, at which point the final state is returned.
     *
     * @return The final evolutionary state after the process is complete.
     */
    override suspend fun evolve(): S {
        evolutionListeners.forEach { it.onEvolutionStart() }
        do {
            generationListeners.forEach { it.onGenerationStart(state) }
            state = iterateGeneration(state)
            generationListeners.forEach { it.onGenerationEnd(state) }
        } while (limits.none { it(state) })
        evolutionListeners.forEach { it.onEvolutionEnd(state) }
        return state
    }

    /**
     * Advances the evolutionary state by one generation.
     *
     * Subclasses must implement this method to define the specific logic for updating the evolutionary state
     * in each generation.
     *
     * @param state The current evolutionary state.
     * @return The updated evolutionary state after one generation.
     */
    abstract fun iterateGeneration(state: S): S
}
