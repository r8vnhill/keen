/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.repr

import cl.ravenhill.keen.mixins.FlatMappable
import cl.ravenhill.keen.mixins.Foldable
import cl.ravenhill.keen.mixins.Mappable
import cl.ravenhill.keen.mixins.Verifiable

/**
 * Represents a generic representation in an evolutionary algorithm.
 *
 * The `Representation` interface defines the structure for representing individuals in an evolutionary algorithm.
 * It extends the [Verifiable], [FlatMappable], [Foldable], and [Mappable] interfaces, providing methods for
 * verification, flat-mapping, folding, and mapping operations.
 *
 * ## Usage:
 * Use this interface to define the structure of representations in an evolutionary algorithm. A representation
 * typically encapsulates the position of an individual in the search or solution space and provides methods for
 * verification, flattening, and manipulating the representation by taking or dropping elements.
 *
 * ### Example:
 * Implementing a simple representation:
 * ```kotlin
 * data class SimpleRepresentation(val genes: List<IntGene>) : Representation<Int, IntGene> {
 *     override val size = genes.size
 *
 *     override fun verify() = genes.all { it.verify() }
 *
 *     override fun flatten(): List<Int> = genes.flatMap { it.flatten() }
 *
 *     override fun drop(n: Int): SimpleRepresentation = SimpleRepresentation(genes.drop(n))
 *
 *     override fun take(n: Int): SimpleRepresentation = SimpleRepresentation(genes.take(n))
 *
 *     // ... other methods and properties ...
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 *
 * @property size The size of the representation, typically representing the number of features it contains.
 */
interface Representation<T, F> : Verifiable, FlatMappable<T>, Foldable<T>, Mappable<T> where F : Feature<T, F> {

    /**
     * The size of the representation, representing the number of features or elements it contains.
     */
    val size: Int

    /**
     * Returns a new representation by dropping the first [n] elements from this representation.
     *
     * @param n The number of elements to drop from the start.
     * @return A new [Representation] with [n] elements removed from the beginning.
     */
    fun drop(n: Int): Representation<T, F>

    /**
     * Returns a new representation by taking the first [n] elements from this representation.
     *
     * @param n The number of elements to take from the start.
     * @return A new [Representation] with only the first [n] elements.
     */
    fun take(n: Int): Representation<T, F>
}
