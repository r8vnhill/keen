/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.plotter

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mixins.GenerationListener
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord
import cl.ravenhill.keen.listeners.records.applyToGeneration
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Listener for plotting generation data in an evolutionary algorithm.
 *
 * The `GenerationPlotterListener` class implements the `GenerationListener` interface and is responsible for
 * capturing and storing data about each generation during the evolutionary process. This data can later be used
 * for visualization or analysis of the evolution over time.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 */
class GenerationPlotterListener<T, F, R, S>(private val configuration: ListenerConfiguration<T, F, R>) :
    GenerationListener<T, F, R, S> where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {

    /**
     * Reference to the current generation record.
     *
     * This property is updated at the start of each generation and holds data such as the generation number and the
     * individuals in the population at the end of the generation.
     */
    private val currentGeneration = configuration.currentGeneration

    /**
     * Reference to the evolution record.
     *
     * This property holds the record of the evolutionary process, which includes detailed information about the
     * generations, populations, and individuals.
     */
    private val evolution = configuration.evolution

    /**
     * Called at the start of a generation.
     *
     * This method initializes the `currentGeneration` with the current generation number, preparing it for data
     * collection during the generation.
     *
     * @param state The current evolutionary state, providing context for the generation.
     */
    override fun onGenerationStart(state: S) {
        currentGeneration.value = GenerationRecord(state.generation)
        evolution.generations += currentGeneration.value!!  // !! is safe because it's initialized in onGenerationStart
    }

    /**
     * Called at the end of a generation.
     *
     * This method records the offspring from the population into the `currentGeneration` record, capturing the
     * outcomes of the generation for later analysis or visualization.
     *
     * @param state The current evolutionary state, containing the population and other relevant data.
     */
    override suspend fun onGenerationEnd(state: S) = applyToGeneration(currentGeneration) {
        population.offspring = List(state.size) {
            IndividualRecord.fromIndividual(state.population[it])
        }
    }

    /**
     * Creates a copy of this listener.
     *
     * This method returns a new instance of `GenerationPlotterListener` with the same configuration, allowing the
     * listener to be reused or applied in different evolutionary processes.
     *
     * @return A new `GenerationPlotterListener` instance with the same configuration.
     */
    override fun copy() = GenerationPlotterListener<T, F, R, S>(configuration)
}
