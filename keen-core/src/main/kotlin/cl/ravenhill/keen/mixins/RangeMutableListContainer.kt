/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.mixins

/**
 * An interface defining a mutable collection of ranges.
 *
 * This interface is useful in scenarios where an object needs to maintain a dynamic list of ranges. Each range is
 * represented by a [ClosedRange] of a comparable type [T]. The mutability of the ranges allows for the addition,
 * removal, or modification of range elements at runtime.
 *
 * ## Usage:
 * Implement this interface in classes that require managing a collection of ranges. This could be in applications
 * involving numerical intervals, time periods, or any other context where ranges are a key aspect of the data model.
 *
 * ### Example Implementation:
 * ```kotlin
 * class MyRangeHolder : RangeMutableListContainer<Int> {
 *     override val ranges = mutableListOf<IntRange>()
 *
 *     // Implementation of methods to manipulate ranges
 * }
 * ```
 * In this example, `MyRangeHolder` implements `MutableRanged` for integer ranges. It provides the flexibility to
 * manipulate the collection of integer ranges as needed.
 *
 * @param T The type of the range bounds, which must be [Comparable].
 * @property ranges A mutable list of [ClosedRange] elements, allowing for dynamic manipulation of ranges.
 */
interface RangeMutableListContainer<T> where T : Comparable<T> {
    var ranges: MutableList<ClosedRange<T>>
}
