/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners.mixins

import cl.ravenhill.keen.evolution.states.State
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.mixins.FitnessEvaluable
import cl.ravenhill.keen.ranking.FitnessRanker
import cl.ravenhill.keen.utils.isNotNaN


/**
 * Represents an evolution listener in an evolutionary algorithm.
 *
 * The `EvolutionListener` interface extends multiple listener interfaces to provide a comprehensive event handling
 * mechanism for various stages of the evolutionary process. This includes handling events for generation,
 * initialization, evaluation, parent selection, survivor selection, and alteration.
 *
 * ## Usage:
 * Implement this interface in classes that need to listen and respond to different stages of the evolutionary process.
 * The methods provided allow for custom actions to be performed at each stage of the process.
 *
 * ### Example:
 * ```kotlin
 * class MyEvolutionListener<T, F, I> : EvolutionListener<T, F, I> where F : Feature<T, F>, I : FitnessEvaluable {
 *
 *     override fun onEvolutionStarted(state: State<T, F, I>) {
 *         println("Evolution started with state: $state")
 *     }
 *
 *     override fun onEvolutionEnded(state: State<T, F, I>) {
 *         println("Evolution ended with state: $state")
 *     }
 *
 *     override fun onGenerationStart(generation: Int) {
 *         println("Generation $generation started")
 *     }
 *
 *     // Implement other event handling methods as needed
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param I The type of the individuals in the state, which must extend [FitnessEvaluable].
 */
interface EvolutionListener<T, F, I> :
    GenerationListener<T, F, I>,
    InitializationListener<T, F, I>,
    EvaluationListener<T, F, I>,
    ParentSelectionListener<T, F, I>,
    SurvivorSelectionListener<T, F, I>,
    AlterationListener<T, F, I>
        where F : Feature<T, F>, I : FitnessEvaluable {

    /**
     * Called when the evolution process starts.
     *
     * @param state The current state of the evolutionary process.
     */
    fun onEvolutionStarted(state: State<T, F, I>) = Unit

    /**
     * Called when the evolution process ends.
     *
     * @param state The current state of the evolutionary process.
     */
    fun onEvolutionEnded(state: State<T, F, I>) = Unit

    /**
     * Displays the listener's state by printing its string representation.
     */
    fun display() = println(toString())

    /**
     * Combines this listener with another listener into a list.
     *
     * @param other The other listener to combine with this listener.
     * @return A list containing this listener and the specified listener.
     */
    operator fun plus(other: EvolutionListener<T, F, I>): List<EvolutionListener<T, F, I>> = listOf(this, other)

    companion object {

        /**
         * Computes the number of steady generations in the evolutionary process.
         *
         * This method calculates how many generations in a row have had the same fittest individual based on the
         * provided ranker and evolution record.
         *
         * @param ranker The ranker used to evaluate individuals.
         * @param evolution The record of the evolutionary process.
         * @return The number of steady generations.
         */
        fun <T, F> computeSteadyGenerations(
            ranker: FitnessRanker<T, F>,
            evolution: EvolutionRecord<T, F>,
        ): Int where F : Feature<T, F> {
            var steady = 0
            for (i in evolution.generations.size - 1 downTo 1) {
                val last = evolution.generations[i - 1]
                val current = evolution.generations[i]
                val lastFittest = last.population.offspring
                    .filter { it.fitness.isNotNaN() }
                    .maxOfWith(ranker.comparator) { it.toIndividual() }
                val currentFittest = current.population.offspring
                    .filter { it.fitness.isNotNaN() }
                    .maxOfWith(ranker.comparator) { it.toIndividual() }
                if (lastFittest.fitness == currentFittest.fitness) {
                    steady++
                } else {
                    break
                }
            }
            return steady
        }
    }
}
