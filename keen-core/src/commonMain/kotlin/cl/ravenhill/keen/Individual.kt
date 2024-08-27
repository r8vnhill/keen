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
     * Folds the values in the individual's representation from left to right, accumulating a result.
     *
     * The `fold` function allows you to reduce the individual's representation to a single value by applying a binary
     * operation to an initial value and each element in the representation, processing elements from left to right.
     * This is useful for operations like summing values, combining elements, or any aggregation task where the order
     * of processing follows the sequence of elements in the representation.
     *
     * ### Example: Summing Gene Values in the Representation
     * Suppose you have an individual with a genotype where each gene holds an integer value, and you want to calculate
     * the sum of these values:
     * ```kotlin
     * val gene1: IntGene = IntGene(1, 0..10)
     * val gene2 = IntGene(2, 0..10)
     * val chromosome = IntChromosome(gene1, gene2)
     * val representation = Genotype(chromosome)
     * val individual = Individual(representation)
     * val sumOfGeneValues = individual.fold(0) { acc, value -> acc + value }
     * println(sumOfGeneValues) // Output: 3
     * ```
     *
     * @param R The type of the result produced by the fold operation.
     * @param initial The initial value to start the accumulation with.
     * @param operation The binary operation to apply to the accumulator and each value in the representation.
     * @return The final accumulated result after processing all values from left to right.
     */
    override fun <R> fold(initial: R, operation: (R, T) -> R): R = representation.fold(initial, operation)

    /**
     * Folds the values in the individual's representation from right to left, accumulating a result.
     *
     * The `foldRight` function allows you to reduce the individual's representation to a single value by applying a
     * binary operation to each element in the representation and an initial value, processing elements from right to
     * left. This is useful for operations where the order of processing is important and should start from the last
     * element and move towards the first, such as building a result in reverse order.
     *
     * ### Example: Building a String Representation of Gene Values in Reverse Order
     * Suppose you have an individual with a genotype where each gene holds a character, and you want to build a string
     * that represents the gene values in reverse order:
     * ```kotlin
     * val gene1 = CharGene('A')
     * val gene2 = CharGene('B')
     * val chromosome = CharChromosome(gene1, gene2)
     * val representation = Genotype(chromosome)
     * val individual = Individual(representation)
     * val reversedGeneString = individual.foldRight("") { value, acc -> value + acc }
     * println(reversedGeneString) // Output: "BA"
     * ```
     *
     * ## Efficiency Considerations:
     * - **Folding Left (`fold`)**: Efficient when the accumulation order naturally follows the sequence of elements.
     *   Ideal for linear data structures like lists when you need to process elements in their original order.
     * - **Folding Right (`foldRight`)**: Useful for right-associative operations, such as when the last element has
     *   more significance, or when building results in reverse order or from the end of a structure.
     *
     * @param R The type of the result produced by the fold operation.
     * @param initial The initial value to start the accumulation with.
     * @param operation The binary operation to apply to each value in the representation and the accumulator.
     * @return The final accumulated result after processing all values from right to left.
     */
    override fun <R> foldRight(initial: R, operation: (T, R) -> R): R = representation.foldRight(initial, operation)

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
