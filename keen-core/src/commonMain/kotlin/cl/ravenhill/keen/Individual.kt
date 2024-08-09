/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.mixins.FlatMappable
import cl.ravenhill.keen.mixins.Foldable
import cl.ravenhill.keen.mixins.Verifiable
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.utils.hash
import cl.ravenhill.keen.utils.isNotNaN

/**
 * Represents an individual in an evolutionary algorithm.
 *
 * The `Individual` class encapsulates the representation and fitness of an individual in an evolutionary process. It
 * implements the [Verifiable], [FlatMappable], and [Foldable] interfaces, allowing it to be verified for consistency,
 * support flat-mapping operations, and perform fold operations.
 *
 * ## Usage:
 * Use this class to represent individuals in an evolutionary algorithm, where each individual has a representation
 * indicating its position in the search or solution space, along with a fitness value that reflects its quality or
 * suitability in the evolutionary context.
 *
 * ### Example (requires the `keen-genetics` module):
 * ```kotlin
 * val gene1 = IntGene(1, 0..10)
 * val gene2 = IntGene(2, 0..10)
 * val chromosome = IntChromosome(gene1, gene2)
 * val representation = Genotype(chromosome)
 * val individual = Individual(representation, fitness = 42.0)
 *
 * println(individual) // Output: Genotype([IntChromosome([IntGene(1), IntGene(2)])]) -> 42.0
 * println(individual.verify()) // Output: true
 * println(individual.flatten()) // Output: [1, 2]
 * println(individual.isEvaluated()) // Output: true
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @property representation The position of the individual in the search or solution space.
 * @property fitness The fitness value of the individual, indicating its quality, defaulting to `Double.NaN` if not
 *   evaluated.
 * @constructor Creates an instance of `Individual` with the specified representation and fitness.
 */
data class Individual<T, F, R>(
    val representation: R,
    val fitness: Double = Double.NaN
) : Verifiable, FlatMappable<T>, Foldable<T> where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * The size of the individual's representation, lazily computed.
     */
    val size by lazy { representation.size }

    /**
     * Verifies the consistency and validity of the individual's representation and fitness.
     *
     * @return `true` if the representation is valid and the fitness is not NaN, `false` otherwise.
     */
    override fun verify() = representation.verify() && fitness.isNotNaN()

    /**
     * Folds the elements of the individual's representation into a single value.
     *
     * @param R The type of the result produced by the fold operation.
     * @param initial The initial value to start the accumulation with.
     * @param operation The binary operation to apply to the accumulator and each element of the representation.
     * @return The final accumulated result after all elements have been processed.
     */
    override fun <R> fold(initial: R, operation: (R, T) -> R): R = representation.fold(initial, operation)

    /**
     * Flattens the individual's representation into a list of elements.
     *
     * @return A list of elements from the flattened representation.
     */
    override fun flatten(): List<T> = representation.flatten()

    /**
     * Checks if the individual's fitness has been evaluated.
     *
     * @return `true` if the fitness is not NaN, `false` otherwise.
     */
    fun isEvaluated() = fitness.isNotNaN()

    /**
     * Returns a string representation of the individual.
     *
     * The format of the string depends on the current domain's toString mode, either a simple format or a more detailed
     * one.
     *
     * @return A string representation of the individual based on the current domain's toString mode.
     */
    override fun toString() = when (Domain.toStringMode) {
        ToStringMode.SIMPLE -> "$representation -> $fitness"
        else -> "Individual(representation=$representation, fitness=$fitness)"
    }

    /**
     * Checks if this individual is equal to another object.
     *
     * Two individuals are considered equal if they have the same representation. Fitness is not considered in equality
     * checks.
     *
     * @param other The object to compare with.
     * @return `true` if the other object is an individual with the same representation, `false` otherwise.
     */
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Individual<*, *, *> -> false
        else -> representation == other.representation
    }

    /**
     * Computes the hash code for this individual.
     *
     * The hash code is based on the individual's class and representation.
     *
     * @return The hash code based on the individual's class and representation.
     */
    override fun hashCode() = hash(Individual::class, representation)
}
