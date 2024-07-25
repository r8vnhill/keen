package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.states.State
import cl.ravenhill.keen.features.Feature

/**
 * Represents an evolver in an evolutionary algorithm.
 *
 * The `Evolver` interface defines the basic structure and operations for an entity that manages the evolutionary
 * process. This includes maintaining the population size and providing a method to start and run the evolution process.
 *
 * ## Usage:
 * This interface is intended to be implemented by classes that manage the evolutionary process. Implementing classes
 * should provide the logic to evolve the population through various evolutionary steps.
 *
 * ### Example:
 * ```kotlin
 * class MyEvolver<T, F>(
 *     override val populationSize: Int
 * ) : Evolver<T, F> where F : Feature<T, F> {
 *
 *     override fun evolve(): State<T, F> {
 *         // Implementation of the evolution process
 *         return MyState()
 *     }
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @property populationSize The size of the population managed by the evolver.
 */
interface Evolver<T, F> where F : Feature<T, F> {

    /**
     * The size of the population managed by the evolver.
     */
    val populationSize: Int

    /**
     * Starts and runs the evolution process.
     *
     * @return The final state of the evolutionary process.
     */
    fun evolve(): State<T, F>
}
