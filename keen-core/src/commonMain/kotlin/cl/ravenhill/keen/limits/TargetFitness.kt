package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Limit condition based on achieving a target fitness in an evolutionary algorithm.
 *
 * The `TargetFitness` class represents a termination condition for an evolutionary algorithm, where the algorithm stops
 * when any individual in the population achieves a fitness value greater than or equal to a specified target. This is a
 * common stopping criterion in evolutionary computation, particularly in optimization problems where the goal is to
 * reach a certain fitness threshold.
 *
 * ## Usage:
 * The `TargetFitness` class is typically used in evolutionary algorithms to define a fitness-based stopping condition.
 * It is recommended to use the curried equivalent function [targetFitness] to create instances of this class, as it
 * allows for more flexible and modular configuration of the limit conditions.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 * @param targetFitness The fitness value that, when reached or exceeded by any individual in the population,
 *   will cause the evolutionary process to stop.
 */
class TargetFitness<T, F, R, S>(
    val targetFitness: Double
) : Limit<T, F, R, S, TargetFitnessListener<T, F, R, S>>(
    TargetFitnessListener(),
    { state -> state.population.any { it.fitness >= targetFitness } }
) where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R>

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
    EvolutionListener<T, F, R, S> where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R>

/**
 * Factory function to create a [TargetFitness] limit condition for an evolutionary algorithm.
 *
 * The `targetFitness` function provides a convenient way to create instances of the `TargetFitness` class, which
 * serves as a termination condition based on achieving a specified fitness level within the population. This function
 * simplifies the process of defining a fitness-based limit in evolutionary algorithms, improving code readability and
 * reducing the potential for errors.
 *
 * ## Usage:
 * This factory function is curried, meaning it returns a partially-applied function that can be used to build the
 * `TargetFitness` limit incrementally. This design allows for greater flexibility, particularly when configuring
 * listeners or other parameters separately from the limit condition itself.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 * @param targetFitness The fitness threshold that must be reached by any individual in the population to terminate the
 *   evolutionary process.
 * @return A function that returns a [TargetFitness] instance when invoked, allowing for partial application and
 *   flexible configuration.
 */
fun <T, F, R, S> targetFitness(
    targetFitness: Double
): (ListenerConfiguration<T, F, R>) -> TargetFitness<T, F, R, S> where
        F : Feature<T, F>,
        R : Representation<T, F>,
        S : EvolutionState<T, F, R> =
    { TargetFitness(targetFitness) }
