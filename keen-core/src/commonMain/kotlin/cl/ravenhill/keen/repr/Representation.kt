/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.repr

import arrow.core.Either
import cl.ravenhill.keen.mixins.FlatMappable
import cl.ravenhill.keen.mixins.Foldable
import cl.ravenhill.keen.mixins.Mappable
import cl.ravenhill.keen.mixins.Verifiable

/**
 * Represents a generic structure for individuals in an evolutionary algorithm.
 *
 * The `Representation` interface defines the contract for representing individuals, encapsulating their structure
 * within the search or solution space. It provides core methods for operations such as verification, flat-mapping,
 * folding, and mapping, making it adaptable for use in various evolutionary computations.
 *
 * This interface also allows representations to be manipulated by removing or extracting a subset of elements,
 * using [drop] and [take] methods, respectively. Additionally, it supports methods for verifying the correctness
 * of the representation.
 *
 * ## Usage:
 * Implement this interface to define the structure of a representation in an evolutionary algorithm. It is expected
 * to handle features (or components) of type [F], and should provide mechanisms for validation, transformation, and
 * traversal of the representation.
 *
 * ### Example: Implementing a simple representation
 * ```kotlin
 * data class SimpleRepresentation(val genes: List<IntGene>) : Representation<Int, IntGene> {
 *     override val size = genes.size
 *
 *     override fun verify() = genes.all { it.verify() }
 *
 *     override fun flatten(): List<Int> = genes.flatMap { it.flatten() }
 *
 *     override fun drop(n: Int): Either<Exception, SimpleRepresentation> =
 *         if (n <= size) SimpleRepresentation(genes.drop(n)).right() else Exception("Drop out of bounds").left()
 *
 *     override fun take(n: Int): Either<Exception, SimpleRepresentation> =
 *         if (n <= size) SimpleRepresentation(genes.take(n)).right() else Exception("Take out of bounds").left()
 *
 *     // ... other methods and properties ...
 * }
 * ```
 *
 * @param T The type of value held by the features.
 * @param F The type of feature within the representation, which must extend [Feature].
 *
 * @property size The size of the representation, typically representing the number of features or components it contains.
 */
interface Representation<T, F> : Verifiable, FlatMappable<T>, Foldable<T>, Mappable<T> where F : Feature<T, F> {

    /**
     * The size of the representation, representing the number of components or features it contains.
     */
    val size: Int

    /**
     * Returns a new representation by removing the first [n] elements.
     *
     * The `drop` method allows removing a certain number of elements from the representation, starting from the
     * beginning. It produces a new representation with [n] elements removed. If [n] exceeds the size of the
     * representation, an exception is returned.
     *
     * @param n The number of elements to drop from the beginning.
     * @return An [Either] containing the new representation if successful, or an exception if [n] exceeds the size.
     */
    fun drop(n: Int): Either<Exception, Representation<T, F>>

    /**
     * Returns a new representation by keeping the first [n] elements.
     *
     * The `take` method allows extracting the first [n] elements from the representation, returning a new
     * representation containing only these elements. If [n] exceeds the size of the representation, an exception
     * is returned.
     *
     * @param n The number of elements to take from the beginning.
     * @return An [Either] containing the new representation if successful, or an exception if [n] exceeds the size.
     */
    fun take(n: Int): Either<Exception, Representation<T, F>>
}
