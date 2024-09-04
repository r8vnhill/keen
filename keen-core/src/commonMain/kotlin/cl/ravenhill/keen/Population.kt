/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import arrow.core.NonEmptyList
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import kotlin.jvm.JvmInline

/**
 * A sealed interface representing a population-like structure in evolutionary algorithms.
 *
 * The `PopulationLike` interface defines the basic structure and behavior of a population in an evolutionary algorithm.
 * It extends the [List] interface and provides additional functionality, such as computing the fitness values of all
 * individuals within the population. Implementations of this interface represent different types of populations that
 * may be either empty or non-empty.
 *
 * @param T The type of value held by the features in the representation.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @property individuals The list of individuals in the population.
 */
interface PopulationLike<T, F, R> : List<Individual<T, F, R>> where F : Feature<T, F>, R : Representation<T, F> {

    val individuals: List<Individual<T, F, R>>

    /**
     * Retrieves the fitness values of all individuals in the population.
     *
     * This property returns a list of fitness values corresponding to each individual in the population. It is computed
     * by mapping the fitness values of each individual in the population list.
     *
     * @return A list of fitness values for the individuals in the population.
     */
    val fitness: List<Double>
        get() = individuals.map { it.fitness }

    /**
     * Maps the individuals in the population using the provided function.
     *
     * @param f The function to apply to each individual in the population.
     * @return A new population with the results of applying the function to each individual.
     */
    fun map(f: (Individual<T, F, R>) -> Individual<T, F, R>) = Population(individuals.map(f))

    /**
     * Converts the population into a list of individuals.
     *
     * @return A list of individuals in the population.
     */
    fun toList(): List<Individual<T, F, R>> = individuals
}

/**
 * A value class representing a population of individuals in an evolutionary algorithm.
 *
 * The `Population` class is a lightweight, inline class that encapsulates a list of individuals. It implements the
 * [PopulationLike] interface, providing access to the population's fitness values and inheriting the behavior of a
 * standard list. This class can represent an empty or non-empty population.
 *
 * @param T The type of value held by the features in the representation.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @property individuals The list of individuals in the population.
 */
open class Population<T, F, R> internal constructor(override val individuals: List<Individual<T, F, R>>) :
    PopulationLike<T, F, R>,
    List<Individual<T, F, R>> by individuals
        where F : Feature<T, F>,
              R : Representation<T, F>

/**
 * A value class representing a non-empty population of individuals in an evolutionary algorithm.
 *
 * The `NonEmptyPopulation` class is an inline class that ensures the population contains at least one individual. It
 * implements the [PopulationLike] interface, providing access to the population's fitness values and inheriting the
 * behavior of a list. This class is useful when an evolutionary algorithm requires a non-empty population for its
 * operations.
 *
 * @param T The type of value held by the features in the representation.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @property individuals A non-empty list of individuals in the population.
 */
@JvmInline
value class NonEmptyPopulation<T, F, R> internal constructor(
    override val individuals: NonEmptyList<Individual<T, F, R>>
) : PopulationLike<T, F, R>, List<Individual<T, F, R>> by individuals
        where F : Feature<T, F>,
              R : Representation<T, F>

/**
 * Creates a `Population` from a variable number of individuals.
 *
 * @param T The type of value held by the features in the representation.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param individuals The individuals that will form the population.
 * @return A `Population` containing the provided individuals.
 */
fun <T, F, R> populationOf(vararg individuals: Individual<T, F, R>)
        where F : Feature<T, F>,
              R : Representation<T, F> = Population(individuals.toList())

/**
 * Creates a `NonEmptyPopulation` from a `NonEmptyList` of individuals.
 *
 * @param T The type of value held by the features in the representation.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param individuals A `NonEmptyList` of individuals that will form the population.
 * @return A [NonEmptyPopulation] containing the provided individuals.
 */
fun <T, F, R> nonEmptyPopulationOf(individuals: NonEmptyList<Individual<T, F, R>>)
        where F : Feature<T, F>,
              R : Representation<T, F> = NonEmptyPopulation(individuals)

/**
 * Creates an empty [Population].
 *
 * @param T The type of value held by the features in the representation.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @return An empty `Population`.
 */
fun <T, F, R> emptyPopulation()
        where F : Feature<T, F>,
              R : Representation<T, F> = Population<T, F, R>(emptyList())

/**
 * Converts a list of individuals into a population.
 *
 * @return A `Population` containing the individuals in the original list.
 * @receiver A `List` of `Individual` instances.
 * @param T The type of the value held by the feature.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 */
fun <T, F, R> List<Individual<T, F, R>>.toPopulation(): Population<T, F, R>
        where F : Feature<T, F>,
              R : Representation<T, F> = Population(this)
