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
 * Represents a structure in an evolutionary algorithm that holds features and supports various operations.
 *
 * The `Representation` interface defines the structure and behavior of a representation within an evolutionary
 * algorithm. A representation typically consists of a collection of features (genes) and provides essential
 * functionalities for working with these features, such as mapping, folding, and verifying. The interface ensures that
 * representations are flexible and capable of supporting a wide range of genetic operations, including folding values,
 * mapping transformations, and verifying structural integrity.
 *
 * ## Usage:
 * Implement this interface to create representations of individuals in evolutionary algorithms. The [size] property
 * denotes the number of components (e.g. chromosomes) contained in the representation, which is crucial for many
 * evolutionary operations.
 *
 * ### Example: Implementing a Simple Representation
 * ```kotlin
 * data class MyRepresentation(
 *     override val size: Int,
 *     val features: List<MyFeature>
 * ) : Representation<Int, MyFeature> {
 *     override fun flatten(): List<Int> =
 *         features.map { it.value }
 *     override fun fold(initial: Int, operation: (Int, Int) -> Int): Int =
 *         features.fold(initial) { acc, f -> operation(acc, f.value) }
 *     override fun verify(): Boolean =
 *         size == features.size
 *     override fun map(transform: (Int) -> Int): MyRepresentation =
 *         MyRepresentation(size, features.map { it.copyWithValue(transform(it.value)) })
 * }
 * ```
 *
 * In this example, `MyRepresentation` implements the `Representation` interface, with a list of features (genes) and
 * methods that handle common operations like flattening, folding, and verification.
 *
 * @param T The type of value held by the features.
 * @param F The type of feature, which must extend [Feature].
 * @property size The number of features (genes) contained within the representation.
 */
interface Representation<T, F> : Verifiable, FlatMappable<T>, Foldable<T>, Mappable<T> where F : Feature<T, F> {

    /**
     * The total number of components in this representation.
     */
    val size: Int
}
