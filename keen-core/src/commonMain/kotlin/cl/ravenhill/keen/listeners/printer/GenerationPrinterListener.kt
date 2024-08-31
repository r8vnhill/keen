/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.printer

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.Listener
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mixins.GenerationListener
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord
import cl.ravenhill.keen.listeners.records.mapGeneration
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * A listener that records and prints details of each generation during the evolutionary process.
 *
 * The `GenerationPrinterListener` class is designed to capture and store information about each generation in the
 * evolutionary algorithm as it progresses. This listener is particularly useful for monitoring and analyzing the
 * performance of the algorithm over time, providing insights into how the population evolves from one generation to
 * the next.
 *
 * @param T The type of value held by the features.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param S The type of evolutionary state, which must extend [EvolutionState].
 * @property currentGeneration A mutable reference to the current generation's record, updated at the start of each
 *   generation.
 * @property timeSource The source of time used to mark the start and end times of each generation.
 * @property withPrecision A function that applies precision to the measured duration of the generation.
 * @property evolution The overall record of the evolutionary process, where each generation's data is stored.
 */
class GenerationPrinterListener<T, F, R, S>(
    private val configuration: ListenerConfiguration<T, F, R>
) : GenerationListener<T, F, R, S> where F : Feature<T, F>,
                                         R : Representation<T, F>,
                                         S : EvolutionState<T, F, R, S> {

    private val currentGeneration by lazy { configuration.currentGeneration.toMutable() }

    private val timeSource by lazy { configuration.timeSource }

    private val withPrecision by lazy { configuration.precision.withPrecision }

    private val evolution by lazy { configuration.evolution }

    /**
     * Called at the start of each generation.
     *
     * This method initializes the current generation's record, marking the start time of the generation.
     *
     * @param state The current evolutionary state at the start of the generation.
     */
    override fun onGenerationStart(state: S) {
        currentGeneration.value = GenerationRecord<T, F, R>(state.generation)
            .apply { startTime = timeSource.markNow() }
    }

    /**
     * Called at the end of each generation.
     *
     * This method records the duration of the generation, updates the evolution record with the current population and
     * its offspring, and calculates the number of steady generations.
     *
     * @param state The current evolutionary state at the end of the generation.
     */
    override suspend fun onGenerationEnd(state: S) {
        mapGeneration(currentGeneration) {
            duration = startTime.elapsedNow().withPrecision()
            evolution.generations += this
            population.offspring = List(state.size) {
                IndividualRecord(
                    state.population[it].representation,
                    state.population[it].fitness
                )
            }
            steady = Listener.computeSteadyGenerations(state.ranker, evolution)
        }
    }

    /**
     * Creates a copy of the `GenerationPrinterListener` with the same configuration.
     *
     * This method overrides the `copy` function to return a new instance of `GenerationPrinterListener` using the
     * current configuration.  It is useful for creating independent instances that share the same initial settings.
     *
     * @return A new `GenerationPrinterListener` instance with identical configuration.
     */
    override fun copy() = GenerationPrinterListener<_, _, _, S>(configuration)
}
