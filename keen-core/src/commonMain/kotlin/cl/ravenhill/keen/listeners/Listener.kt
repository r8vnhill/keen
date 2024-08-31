/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.utils.isNotNaN

/**
 * A read-only listener interface for monitoring and displaying information.
 *
 * The `Listener` interface defines a minimal contract for listeners that are intended to observe and potentially
 * display information in an application. It provides a method to display the listener's state and a method to create a
 * copy of the listener. This interface is particularly useful in scenarios where listeners are used to monitor events
 * or states but should not be modified directly.
 *
 * ## Recommendations:
 * - The `copy` method should return a new instance or a deep copy of the listener to ensure that changes to the copy do
 *   not affect the original listener.
 */
interface Listener {

    /**
     * Displays the listener's state.
     *
     * This method outputs the result of the `toString` method to the console. Implementing classes should override
     * this method to provide a custom display format for the listener's state. This could be through a formatted
     * string, a graphical representation, or any other suitable output.
     */
    suspend fun display() = println(toString())

    /**
     * Creates and returns a copy of the listener.
     *
     * This method is intended to return a copy of the listener, which can be used in contexts where the listener needs
     * to be duplicated without modifying the original instance. The implementation of this method should ensure that
     * the copied listener is independent of the original.
     *
     * @return A new instance of the listener or a deep copy, depending on the implementation.
     */
    fun copy(): Listener

    companion object {
        /**
         * Computes the number of steady generations in an evolutionary process.
         *
         * The `computeSteadyGenerations` function calculates how many consecutive generations in an evolutionary
         * process have produced the fittest individual with the same fitness value. This can be used to determine if
         * the evolutionary process has reached a point of stagnation, where the population is no longer improving in
         * fitness.
         *
         * The function iterates through the generations in reverse order, comparing the fittest individual of each
         * generation with the fittest individual of the previous generation. If the fitness values are equal, the
         * function increments the `steady` counter. The process continues until a difference in fitness is found or all
         * generations are checked.
         *
         * @param ranker The [IndividualRanker] used to evaluate and compare individuals within the population.
         * @param evolution The [EvolutionRecord] containing the history of generations to be analyzed.
         * @return The number of steady generations, i.e., generations where the fittest individual has the same fitness
         *   value.
         * @param T The type of value held by the features.
         * @param F The type of feature, which must extend [Feature].
         * @param R The type of representation, which must extend [Representation].
         */
        fun <T, F, R> computeSteadyGenerations(
            ranker: IndividualRanker<T, F, R>,
            evolution: EvolutionRecord<T, F, R>
        ): Int where F : Feature<T, F>,
                     R : Representation<T, F> {
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
