/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.repr

/**
 * Represents a feature in an evolutionary algorithm.
 *
 * A feature is an atomic component in an evolutionary algorithm. For instance, a gene is a feature in a genetic
 * algorithm, and a chromosome is a vector of such features. The `Feature` interface provides a common structure for
 * these components, with methods for duplicating features with new values.
 *
 * ## Usage:
 * This interface is intended to be implemented by classes representing individual elements in evolutionary algorithms,
 * such as genes or other units.
 *
 * ### Example 1: Implementing Feature
 * ```
 * class MyGene(override val value: Int) : Feature<Int, MyGene> {
 *     override fun duplicateWithValue(value: Int) = MyGene(value)
 * }
 * ```
 *
 * @param T The type of the value held by the feature.
 * @param F The type of the feature itself, which must extend [Feature].
 * @property value The value held by the feature.
 */
interface Feature<T, F> where F : Feature<T, F> {
    /**
     * The value held by the feature.
     */
    val value: T

    /**
     * Creates a duplicate of the feature with the specified value.
     *
     * @param value The value for the new feature.
     * @return A new feature with the specified value.
     */
    fun duplicateWithValue(value: T): F
}
