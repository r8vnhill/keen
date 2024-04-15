/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.mixins


/**
 * Interface for defining filterable behavior.
 *
 * This interface is designed to be implemented by classes that require a filtering mechanism. It provides a structure
 * for filtering objects based on specified criteria encapsulated in the [filter] function. The filter function is a
 * higher-order function that takes an object of type [T] and returns a [Boolean] indicating whether the object meets
 * the filtering criteria.
 *
 * ## Usage:
 * - Implement this interface in classes where objects need to be filtered based on certain conditions.
 * - Define the [filter] function to encapsulate the logic for determining whether an object should be included or
 *   excluded based on the filter criteria.
 *
 * ### Example:
 * Implementing `Filterable` in a class that filters numeric values:
 * ```kotlin
 * class NumericFilter : Filterable<Int> {
 *     override val filter: (Int) -> Boolean = { it > 0 }
 * }
 *
 * val numericFilter = NumericFilter()
 * val isPositive = numericFilter.filter(5) // Returns true
 * val isNegative = numericFilter.filter(-3) // Returns false
 * ```
 * In this example, `NumericFilter` implements `Filterable<Int>` with a filter that checks if the number is positive.
 * The `filter` method is then used to test different numbers.
 *
 * @param T The type of objects to be filtered. This type parameter is contravariant, meaning that a `Filterable<Any>`
 *   can be used where a `Filterable<String>` is expected, for example.
 * @property filter A higher-order function that takes an object of type [T] and returns a Boolean
 *   indicating whether the object meets the filtering criteria.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Filterable<in T> {
    val filter: (T) -> Boolean
}
