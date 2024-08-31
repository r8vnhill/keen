/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.summary

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mixins.ParentSelectionListener
import cl.ravenhill.keen.listeners.records.mapGeneration
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * A listener that records and summarizes the parent selection phase in each generation of an evolutionary algorithm.
 *
 * The `ParentSelectionSummary` class is responsible for tracking key metrics related to the parent selection phase
 * within each generation of the evolutionary process. This includes recording the start time and duration of the
 * parent selection phase, which is critical for understanding the time spent on selecting parents for reproduction.
 *
 * @param T The type of the value held by the features.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param S The type of evolutionary state, which must extend [EvolutionState].
 * @property timeSource The source of time used to measure durations.
 * @property currentGeneration A reference to the current generation record being processed.
 * @property withPrecision A lambda function that provides the desired precision for time measurements.
 */
internal class ParentSelectionSummary<T, F, R, S>(private val configuration: ListenerConfiguration<T, F, R>) :
    ParentSelectionListener<T, F, R, S> where F : Feature<T, F>,
                                              R : Representation<T, F>,
                                              S : EvolutionState<T, F, R, S> {

    private val timeSource = configuration.timeSource
    private val currentGeneration = configuration.currentGeneration
    private val withPrecision = configuration.precision.withPrecision

    /**
     * Creates a copy of this `ParentSelectionSummary` listener.
     *
     * @return A new instance of `ParentSelectionSummary` with the same configuration.
     */
    override fun copy() = ParentSelectionSummary<T, F, R, S>(configuration)

    /**
     * Called at the start of the parent selection phase.
     *
     * This method records the start time of the parent selection phase using the provided time source.
     *
     * @param state The current state of the evolutionary algorithm.
     */
    override fun onParentSelectionStart(state: S) = mapGeneration(currentGeneration) {
        parentSelection.startTime = timeSource.markNow()
    }

    /**
     * Called at the end of the parent selection phase.
     *
     * This method calculates and records the duration of the parent selection phase by measuring the elapsed time since
     * the start time.
     *
     * @param state The current state of the evolutionary algorithm.
     */
    override fun onParentSelectionEnd(state: S) = mapGeneration(currentGeneration) {
        parentSelection.duration = parentSelection.startTime.elapsedNow().withPrecision()
    }
}
