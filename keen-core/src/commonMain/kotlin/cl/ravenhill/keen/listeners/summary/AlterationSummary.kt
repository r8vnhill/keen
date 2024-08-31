/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.summary

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mixins.AlterationListener
import cl.ravenhill.keen.listeners.records.applyToGeneration
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * A listener that tracks and summarizes the alteration phase in each generation of an evolutionary algorithm.
 *
 * The `AlterationSummary` class is responsible for capturing key metrics related to the alteration phase within each
 * generation of the evolutionary process. This includes recording the start time and duration of the alteration phase,
 * which is critical for understanding the performance and efficiency of the genetic operations applied to the
 * population, such as mutation and crossover.
 *
 * @param T The type of the value held by the features.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param S The type of evolutionary state, which must extend [EvolutionState].
 * @property timeSource The source of time used to measure durations.
 * @property currentGeneration A reference to the current generation record being processed.
 * @property withPrecision A lambda function that provides the desired precision for time measurements.
 */
internal class AlterationSummary<T, F, R, S>(
    configuration: ListenerConfiguration<T, F, R>
) : AlterationListener<T, F, R, S> where F : Feature<T, F>,
                                         R : Representation<T, F>,
                                         S : EvolutionState<T, F, R, S> {

    private val timeSource = configuration.timeSource
    private val currentGeneration = configuration.currentGeneration
    private val withPrecision = configuration.precision.withPrecision

    /**
     * Called at the start of the alteration phase.
     *
     * This method records the start time of the alteration phase using the provided time source.
     *
     * @param state The current state of the evolutionary algorithm.
     */
    override fun onAlterationStart(state: S) = applyToGeneration(currentGeneration) {
        alteration.startTime = timeSource.markNow()
    }

    /**
     * Called at the end of the alteration phase.
     *
     * This method calculates and records the duration of the alteration phase by measuring the elapsed time since the
     * start time.
     *
     * @param state The current state of the evolutionary algorithm.
     */
    override fun onAlterationEnd(state: S) = applyToGeneration(currentGeneration) {
        alteration.duration = alteration.startTime.elapsedNow().withPrecision()
    }
}
