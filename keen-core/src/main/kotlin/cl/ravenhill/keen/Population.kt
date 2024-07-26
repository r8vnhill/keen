/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Represents a population of individuals in an evolutionary algorithm.
 *
 * The `Population` type alias defines a population as a list of individuals. This type alias simplifies the usage of
 * populations within evolutionary algorithms by providing a clear and concise way to represent collections of
 * individuals.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 */
typealias Population<T, F, R> = List<Individual<T, F, R>>

/**
 * Extension property to get the fitness values of the individuals in the population.
 *
 * This extension property provides a convenient way to retrieve the fitness values of all individuals in a population.
 * The fitness values are computed by mapping over the population and extracting the fitness of each individual.
 *
 * ## Usage:
 * Use this property to quickly access the fitness values of all individuals in a population, which is useful for
 * analysis, selection, and ranking processes in evolutionary algorithms.
 *
 * ### Example:
 * ```kotlin
 * val population: Population<MyType, MyFeature, MyRepresentation> = // initialize population
 * val fitnessValues = population.fitness
 * println(fitnessValues) // Output: List of fitness values
 * ```
 *
 * @property fitness The list of fitness values of the individuals in the population.
 */
val <T, F, R> Population<T, F, R>.fitness: List<Double> where F : Feature<T, F>, R : Representation<T, F>
    get() = this.map { it.fitness }
