/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.plotter

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mixins.GenerationListener
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * A listener for plotting the evolution of fitness values over generations in an evolutionary algorithm.
 *
 * The `EvolutionPlotter` class serves as a specialized listener that tracks the progress of the evolutionary
 * algorithm and generates visual plots of the fitness values across generations. This class integrates with the
 * evolution process by listening to generation events and producing visual representations of the results.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 * @property configuration The listener configuration that contains the necessary settings and components for tracking
 *   and plotting the evolution process.
 */
class EvolutionPlotter<T, F, R, S> private constructor(private val configuration: ListenerConfiguration<T, F, R>) :
    GenerationListener<T, F, R, S> by GenerationPlotterListener(configuration)
        where F : Feature<T, F>,
              R : Representation<T, F>,
              S : EvolutionState<T, F, R, S> {

    /**
     * Displays the evolution plot based on the collected data.
     *
     * This method generates a plot that visually represents the evolution of fitness values over the course of the
     * evolutionary process. It uses the provided configuration's evolution record and ranker to create the plot.
     */
    override suspend fun display() = plot(configuration.evolution, configuration.ranker)

    /**
     * Creates a copy of this `EvolutionPlotter` instance with the same configuration.
     *
     * @return A new instance of `EvolutionPlotter` with the same configuration.
     */
    override fun copy() = EvolutionPlotter<T, F, R, S>(configuration)

    /**
     * A companion object that provides a factory method for creating instances of `EvolutionPlotter`.
     *
     * @see invoke
     */
    companion object {
        /**
         * Factory method for creating an `EvolutionPlotter` instance.
         *
         * This method returns a function that, when invoked with a `ListenerConfiguration`, creates an instance of
         * `EvolutionPlotter`.
         *
         * @return A function that creates an `EvolutionPlotter` instance with the provided configuration.
         */
        operator fun <T, F, R> invoke(): (
            ListenerConfiguration<T, F, R>
        ) -> EvolutionPlotter<T, F, R, out EvolutionState<T, F, R, *>>
                where F : Feature<T, F>,
                      R : Representation<T, F> =
            { configuration -> EvolutionPlotter(configuration) }
    }
}

/**
 * Expected function for plotting the fitness progression over generations in an evolutionary algorithm. Currently,
 * only the JVM platform is supported for plotting.
 *
 * This function is an expected declaration that is platform-specific, allowing for the generation of visual plots
 * that track the progression of fitness values across generations in an evolutionary algorithm. The actual
 * implementation of this function will vary depending on the platform (e.g., JVM, JS, Native).
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 */
internal expect suspend fun <T, F, R> plot(
    evolution: EvolutionRecord<T, F, R>,
    ranker: IndividualRanker<T, F, R>
) where F : Feature<T, F>,
        R : Representation<T, F>
