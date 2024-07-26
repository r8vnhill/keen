package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.states.State
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.mixins.FitnessEvaluable

/**
 * Represents an evolver in an evolutionary algorithm.
 *
 * The `Evolver` interface defines the basic structure and operations for an entity that manages the evolution process.
 * This includes properties for the population size and survival rate, as well as a method to perform the evolution.
 *
 * ## Usage:
 * Implement this interface in classes that perform the overall evolution process in an evolutionary algorithm.
 * Provide the logic to manage the population and apply evolutionary operations.
 *
 * ### Example:
 * ```kotlin
 * class MyEvolver<T, F, I>(
 *     override val populationSize: Int,
 *     override val survivalRate: Double
 * ) : Evolver<T, F, I> where F : Feature<T, F>, I : FitnessEvaluable {
 *
 *     override fun evolve(): State<T, F, I> {
 *         // Implementation of the evolution logic
 *     }
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param I The type of the individuals in the state, which must extend [FitnessEvaluable].
 * @property populationSize The size of the population.
 * @property survivalRate The rate at which individuals survive to the next generation.
 */
interface Evolver<T, F, I> where F : Feature<T, F>, I : FitnessEvaluable {

    val populationSize: Int

    val survivalRate: Double

    /**
     * Performs the evolution process and returns the resulting state.
     *
     * This method manages the overall evolution process, applying evolutionary operations to the population
     * and returning the resulting state.
     *
     * @return The new state after performing the evolution.
     */
    fun evolve(): State<T, F, I>
}
