/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.summary

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mixins.SurvivorSelectorListener
import cl.ravenhill.keen.listeners.records.applyToGeneration
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * A listener that records and summarizes the survivor selection phase in each generation of an evolutionary algorithm.
 *
 * The `SurvivorSelectionSummary` class is responsible for tracking key metrics related to the survivor selection phase
 * within each generation of the evolutionary process. This includes recording the start time and duration of the
 * survivor selection phase, which is crucial for understanding the time spent on selecting individuals that will
 * survive to the next generation.
 *
 * @param T The type of the value held by the features.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param S The type of evolutionary state, which must extend [EvolutionState].
 * @property timeSource The source of time used to measure durations.
 * @property currentGeneration A reference to the current generation record being processed.
 * @property withPrecision A lambda function that provides the desired precision for time measurements.
 */
internal class SurvivorSelectionSummary<T, F, R, S>(
    private val configuration: ListenerConfiguration<T, F, R>
) : SurvivorSelectorListener<T, F, R, S> where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {

    private val timeSource = configuration.timeSource
    private val currentGeneration = configuration.currentGeneration
    private val withPrecision = configuration.precision.withPrecision

    /**
     * Called at the start of the survivor selection phase.
     *
     * This method records the start time of the survivor selection phase using the provided time source.
     *
     * @param state The current state of the evolutionary algorithm.
     */
    override fun onSurvivorSelectionStart(state: S) = applyToGeneration(currentGeneration) {
        survivorSelection.startTime = timeSource.markNow()
    }

    /**
     * Called at the end of the survivor selection phase.
     *
     * This method calculates and records the duration of the survivor selection phase by measuring the elapsed time
     * since the start time.
     *
     * @param state The current state of the evolutionary algorithm.
     */
    override fun onSurvivorSelectionEnd(state: S) = applyToGeneration(currentGeneration) {
        survivorSelection.duration = survivorSelection.startTime.elapsedNow().withPrecision()
    }

    /**
     * Creates a copy of this `SurvivorSelectionSummary` listener.
     *
     * @return A new instance of `SurvivorSelectionSummary` with the same configuration.
     */
    override fun copy() = SurvivorSelectionSummary<T, F, R, S>(configuration)
}
