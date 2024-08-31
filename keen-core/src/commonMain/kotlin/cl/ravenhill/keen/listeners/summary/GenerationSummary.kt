/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.summary

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.Listener.Companion.computeSteadyGenerations
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mixins.GenerationListener
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord
import cl.ravenhill.keen.listeners.records.applyToGeneration
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import kotlin.time.Duration

/**
 * A listener that records and summarizes information about each generation during the evolutionary process.
 *
 * The `GenerationSummary` class is responsible for capturing and recording key metrics and information at the start and
 * end of each generation in an evolutionary algorithm. This information is stored in a `GenerationRecord` and includes
 * details such as the start time, duration, population of parents and offspring, and the number of steady generations.
 *
 * @param T The type of the value held by the features.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param S The type of evolutionary state, which must extend [EvolutionState].
 * @property configuration The listener configuration that provides necessary dependencies such as the evolution record,
 *   time source, ranker, precision, and current generation.
 */
internal class GenerationSummary<T, F, R, S>(
    private val configuration: ListenerConfiguration<T, F, R> = ListenerConfiguration()
) : GenerationListener<T, F, R, S> where F : Feature<T, F>,
                                         R : Representation<T, F>,
                                         S : EvolutionState<T, F, R, S> {

    private val evolution = configuration.evolution
    private val timeSource = configuration.timeSource
    private val ranker = configuration.ranker
    private val withPrecision: Duration.() -> Long = configuration.precision.withPrecision
    private val currentGeneration = configuration.currentGeneration

    /**
     * Called at the start of each generation.
     *
     * This method initializes a new `GenerationRecord`, capturing the start time of the generation and the initial
     * population of parents. It also increments the generation count in the evolution record.
     *
     * @param state The current state of the evolutionary algorithm.
     */
    override fun onGenerationStart(state: S) {
        currentGeneration.value = GenerationRecord<T, F, R>(evolution.generations.size + 1).apply {
            startTime = timeSource.markNow()
            this.population.parents = List(state.population.size) {
                IndividualRecord(state.population[it].representation, state.population[it].fitness)
            }
        }
        evolution.generations += currentGeneration.value!!
    }

    /**
     * Called at the end of each generation.
     *
     * This method updates the `GenerationRecord` with the duration of the generation, the population of offspring,
     * and the number of steady generations (generations without improvement in the best fitness).
     *
     * @param state The current state of the evolutionary algorithm.
     */
    override suspend fun onGenerationEnd(state: S) = applyToGeneration(currentGeneration) {
        duration = startTime.elapsedNow().withPrecision()
        population.offspring = List(state.population.size) { index ->
            IndividualRecord(state.population[index].representation, state.population[index].fitness)
        }
        steady = computeSteadyGenerations(ranker, evolution)
    }

    /**
     * Creates a copy of this `GenerationSummary` listener.
     *
     * @return A new instance of `GenerationSummary` with the same configuration.
     */
    override fun copy() = GenerationSummary<T, F, R, S>(configuration)
}
