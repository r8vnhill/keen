/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.executors.evaluation

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Evaluates the fitness of an individual in an evolutionary algorithm.
 *
 * The `IndividualEvaluator` class is responsible for calculating the fitness of an [Individual] using a provided
 * fitness function. Fitness evaluation is a critical step in evolutionary algorithms, as it determines how well an
 * individual performs relative to others in the population, guiding the selection process in subsequent generations.
 *
 * @param T The type of value held by the features in the representation.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param individual The individual whose fitness is to be evaluated.
 * @param fitnessFunction The function used to calculate the fitness of the individual, based on its representation.
 * @property fitness The calculated fitness of the individual. Initially set to `Double.NaN` until evaluated.
 * @property individual A copy of the individual with the evaluated fitness value.
 */
internal class IndividualEvaluator<T, F, R>(
    individual: Individual<T, F, R>,
    private val fitnessFunction: (R) -> Double
) where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * The calculated fitness of the individual. Initially set to `Double.NaN` until evaluated.
     */
    private var fitness = Double.NaN

    /**
     * A copy of the individual with the evaluated fitness value.
     */
    val individual = individual
        get() = field.copy(fitness = fitness)

    /**
     * Evaluates the fitness of the individual using the provided fitness function.
     *
     * This method calculates the fitness by applying the `fitnessFunction` to the individual's representation.
     * The calculated fitness is then stored internally, and the individual can be accessed with this fitness value
     * through the `individual` property.
     *
     * @return The individual with the evaluated fitness value.
     */
    operator fun invoke(): Individual<T, F, R> {
        fitness = fitnessFunction(individual.representation)
        return individual
    }
}
