/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ToStringMode
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.features.Representation
import cl.ravenhill.keen.mixins.FitnessEvaluable
import cl.ravenhill.keen.mixins.FlatMappable
import cl.ravenhill.keen.utils.hash
import cl.ravenhill.keen.utils.isNotNaN

/**
 * Represents an individual in an evolutionary algorithm.
 *
 * The `Individual` class encapsulates the representation and fitness of an individual in an evolutionary process.
 * It implements the [FitnessEvaluable] and [FlatMappable] interfaces, allowing it to be evaluated for fitness
 * and to support flat mapping operations.
 *
 * ## Usage:
 * This class is used in evolutionary algorithms to manage individuals' states, including their features and
 * fitness values. It supports operations for checking fitness evaluation, transforming features, and ensuring
 * integrity.
 *
 * ### Example:
 * ```
 * val representation = MyRepresentation(...)
 * val individual = Individual(representation, fitness = 42.0)
 * println(individual.isEvaluated()) // Prints: true
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @property representation The representation of the individual.
 * @property fitness The fitness value of the individual, defaulting to `Double.NaN`.
 * @constructor Creates an instance of `Individual` with the specified representation and fitness.
 */
data class Individual<T, F>(val representation: Representation<T, F>, override val fitness: Double = Double.NaN) :
    FitnessEvaluable, FlatMappable<T> where F : Feature<T, F> {

    /**
     * Deprecated property for accessing the representation.
     */
    @Deprecated("Use the representation property instead.", ReplaceWith("representation"))
    val genotype: Representation<T, F> get() = representation

    /**
     * The size of the representation, lazily computed.
     */
    val size by lazy { representation.size }

    /**
     * Verifies the integrity of the individual.
     *
     * @return `true` if the representation and fitness are valid, `false` otherwise.
     */
    fun verify() = representation.verify() && fitness.isNotNaN()

    /**
     * Flattens the representation into a list of values.
     *
     * @return A flat list of values from the representation.
     */
    override fun flatten(): List<T> = representation.flatten()

    /**
     * Applies a transformation function to each element in the representation and returns a list of the results.
     *
     * @param R The type of elements in the resulting list.
     * @param transform The transformation function to apply to each element.
     * @return A list of transformed elements.
     */
    override fun <R> flatMap(transform: (T) -> R): List<R> = representation.flatMap(transform)

    /**
     * Checks if the individual has been evaluated for fitness.
     *
     * @return `true` if the fitness value is not `Double.NaN`, `false` otherwise.
     */
    override fun isEvaluated() = fitness.isNotNaN()

    /**
     * Returns a string representation of the individual.
     *
     * @return A string representation of the individual based on the current [Domain.toStringMode].
     */
    override fun toString() = when (Domain.toStringMode) {
        ToStringMode.SIMPLE -> "$representation -> $fitness"
        else -> "Individual(representation=$representation, fitness=$fitness)"
    }

    /**
     * Checks if the given object is equal to this individual.
     *
     * @param other The object to compare with.
     * @return `true` if the objects are equal, `false` otherwise.
     */
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Individual<*, *> -> false
        else -> representation == other.representation
    }

    /**
     * Returns the hash code of the individual.
     *
     * @return The hash code of the individual.
     */
    override fun hashCode() = hash(Individual::class, representation)
}
