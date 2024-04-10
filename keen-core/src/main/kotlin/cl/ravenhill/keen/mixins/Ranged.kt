/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.mixins


/**
 * An interface representing an entity with a defined range of comparable values.
 *
 * `Ranged` is used to define objects that have a specific range, indicated by a start and an end point. The range is
 * represented by a [ClosedRange] of a comparable type [T]. This interface is particularly useful in scenarios where an
 * object must adhere to a defined range, such as in numeric constraints, bounded genetic algorithms, or in any context
 * where limits and boundaries are essential.
 *
 * ## Features:
 * - **Range Definition**: Provides a clear and concise way to define the range of an object.
 * - **Generic and Versatile**: Can be implemented by a variety of objects, as long as the range type [T]
 *   is [Comparable].
 *
 * ## Usage:
 * Implement this interface in classes where it's crucial to define boundaries or limits for the values
 * an object can hold or represent.
 *
 * ### Example:
 * Implementing a `Ranged` for a numeric range:
 * ```kotlin
 * class NumericRange(override val range: ClosedRange<Int>) : Ranged<Int> {
 *     // Additional implementations...
 * }
 *
 * val numericRange = NumericRange(1..10)
 * // The numericRange object now represents a range from 1 to 10
 * ```
 * In this example, `NumericRange` implements `Ranged` to represent a range of integers. The range is
 * defined as a [ClosedRange] from 1 to 10, meaning it includes both the start and end points.
 *
 * @param T The type of the range's start and end points. Must be a [Comparable] type.
 * @property range The range of values the object represents. Defined as a [ClosedRange] of type [T].
 *
 * @author <https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Ranged<T> where T : Comparable<T> {

    /**
     * The range defining the boundaries or limits of the object.
     *
     * This property represents a closed interval from a start to an end point, both included.
     * It is essential for scenarios where the object's value must be constrained within certain bounds.
     */
    val range: ClosedRange<T>
}
