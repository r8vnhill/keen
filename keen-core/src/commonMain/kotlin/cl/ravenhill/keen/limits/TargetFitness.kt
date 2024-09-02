/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.Listener
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Termination condition based on achieving a target fitness in an evolutionary algorithm.
 *
 * The `TargetFitness` class represents a termination condition used in evolutionary algorithms where the algorithm
 * stops when any individual in the population achieves a fitness value greater than or equal to a specified target.
 * This stopping criterion is particularly useful in optimization problems where the objective is to reach or exceed
 * a predefined fitness threshold.
 *
 * ## Description:
 * The `TargetFitness` class monitors the fitness of individuals within the population and triggers the termination of
 * the evolutionary process once the target fitness is met by any individual. This can help in preventing unnecessary
 * computational effort once an optimal or satisfactory solution has been found.
 *
 * The class can be instantiated with a specific target fitness value or a custom condition expressed as a lambda
 * function. The default behavior stops the algorithm when the fitness of any individual is greater than or equal to the
 * specified target fitness value.
 *
 * ### Example 1: Stopping at a Specific Fitness Value
 * ```kotlin
 * val targetFitnessCondition = TargetFitness<Double, MyFeature, MyRepresentation, MyEvolutionState>(50.0)
 * ```
 *
 * In this example, the evolutionary process will stop when any individual achieves a fitness of 50.0 or more.
 *
 * ### Example 2: Custom Termination Condition
 * ```kotlin
 * val customCondition = TargetFitness<Double, MyFeature, MyRepresentation, MyEvolutionState> { fitness ->
 *     fitness >= 50.0 && fitness < 100.0
 * }
 * ```
 *
 * Here, the algorithm stops when an individual's fitness is between 50.0 and 100.0.
 *
 * ## Usage:
 * The `TargetFitness` condition is typically used in conjunction with evolutionary algorithms to ensure that the
 * algorithm terminates once an acceptable solution has been found, avoiding unnecessary additional generations. The
 * class provides flexibility to define what constitutes an acceptable solution through the use of different constructors.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 * @param targetFitness The fitness value or condition that, when met or exceeded by any individual in the population,
 *   will cause the evolutionary process to stop.
 */
class TargetFitness<T, F, R, S> private constructor(
    private val targetFitness: (Double) -> Boolean
) : Limit<T, F, R, S, TargetFitnessListener<T, F, R, S>>(
    TargetFitnessListener(),
    { state -> state.population.any { targetFitness(it.fitness) } }
) where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {

    // Secondary constructor for a specific target fitness value.
    private constructor(targetFitness: Double) : this({ it >= targetFitness })

    companion object {
        /**
         * Creates a `TargetFitness` limit condition based on a specific fitness value.
         *
         * This function returns a `TargetFitness` instance that will terminate the evolutionary process when the
         * fitness of any individual in the population is greater than or equal to the specified [targetFitness] value.
         *
         * @param targetFitness The specific fitness value that triggers termination.
         * @return A `TargetFitness` instance.
         */
        operator fun <T, F, R, S> invoke(targetFitness: Double)
                where F : Feature<T, F>,
                      R : Representation<T, F>,
                      S : EvolutionState<T, F, R, S> = { _: ListenerConfiguration<T, F, R> ->
            TargetFitness<_, _, _, S>(targetFitness)
        }

        /**
         * Creates a `TargetFitness` limit condition based on a custom fitness condition.
         *
         * This function returns a `TargetFitness` instance that will terminate the evolutionary process when the
         * fitness of any individual in the population satisfies the provided [targetFitness] condition.
         *
         * @param targetFitness A lambda function that defines the fitness condition for termination.
         * @return A `TargetFitness` instance.
         */
        operator fun <T, F, R, S> invoke(targetFitness: (Double) -> Boolean)
                where F : Feature<T, F>,
                      R : Representation<T, F>,
                      S : EvolutionState<T, F, R, S> = { _: ListenerConfiguration<T, F, R> ->
            TargetFitness<_, _, _, S>(targetFitness)
        }
    }
}

/**
 * A placeholder listener for the `TargetFitness` limit condition in an evolutionary algorithm.
 *
 * The `TargetFitnessListener` class serves as a required placeholder for the `TargetFitness` limit condition. While the
 * `TargetFitness` limit operates purely by evaluating the evolutionary process against a target fitness threshold,
 * this listener class is necessary to conform to the general design pattern of having a listener associated with each
 * limit condition in the framework.
 *
 * @param T The type of value held by the features.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param S The type of evolutionary state, which must extend [EvolutionState].
 */
class TargetFitnessListener<T, F, R, S> :
        Listener where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {
    override fun copy() = TargetFitnessListener<T, F, R, S>()
}

