/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.summary

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.Listener
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mixins.InitializationListener
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * A listener that summarizes and records the initialization phase of the evolutionary process.
 *
 * The `InitializationSummary` class is responsible for capturing key metrics related to the initialization phase
 * of an evolutionary algorithm. This includes recording the start time and duration of the initialization process.
 * This listener is useful for monitoring how long the initialization phase takes, which can be critical for
 * performance analysis in evolutionary algorithms.
 *
 * @param T The type of the value held by the features.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param S The type of evolutionary state, which must extend [EvolutionState].
 * @property configuration The listener configuration that provides necessary dependencies such as the evolution record,
 *   time source, and precision.
 */
internal class InitializationSummary<T, F, R, S>(private val configuration: ListenerConfiguration<T, F, R>) :
    InitializationListener<T, F, R, S> where F : Feature<T, F>,
                                             R : Representation<T, F>,
                                             S : EvolutionState<T, F, R, S> {

    private val evolution = configuration.evolution
    private val timeSource = configuration.timeSource
    private val withPrecision = configuration.precision.withPrecision

    /**
     * Called at the start of the initialization phase.
     *
     * This method records the start time of the initialization phase using the provided time source.
     *
     * @param state The current state of the evolutionary algorithm.
     */
    override fun onInitializationStart(state: S) {
        evolution.initialization.startTime = timeSource.markNow()
    }

    /**
     * Called at the end of the initialization phase.
     *
     * This method calculates and records the duration of the initialization phase by measuring the elapsed time
     * since the start time.
     *
     * @param state The current state of the evolutionary algorithm.
     */
    override fun onInitializationEnd(state: S) {
        evolution.initialization.duration = evolution.initialization.startTime.elapsedNow().withPrecision()
    }

    /**
     * Creates a copy of this `InitializationSummary` listener.
     *
     * @return A new instance of `InitializationSummary` with the same configuration.
     */
    override fun copy() = InitializationSummary<T, F, R, S>(configuration)
}
