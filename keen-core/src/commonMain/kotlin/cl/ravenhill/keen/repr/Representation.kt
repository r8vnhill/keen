/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.repr

import cl.ravenhill.keen.mixins.FlatMappable
import cl.ravenhill.keen.mixins.Verifiable

/**
 * Represents a generic representation in an evolutionary algorithm.
 *
 * The `Representation` interface defines the structure for representing individuals in an evolutionary algorithm.
 * It extends the [Verifiable] and [FlatMappable] interfaces, providing methods for verification and flat-mapping
 * operations.
 *
 * ## Usage:
 * Use this interface to define the structure of representations in an evolutionary algorithm. A representation
 * typically encapsulates the position of an individual in the search or solution space and provides methods for
 * verification and flattening.
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
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @property size The size of the representation, typically representing the number of features it contains.
 */
interface Representation<T, F> : Verifiable, FlatMappable<T> where F : Feature<T, F> {

    val size: Int
}
