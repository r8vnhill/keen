/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.mixins


/**
 * An interface that represents a container for a mutable list of filter functions.
 *
 * This interface is designed to hold a list of filter functions ([filters]), each of which takes an argument of type
 * [T] and returns a `Boolean`. The list of filters can be modified, allowing dynamic adjustment of the filtering
 * criteria. This interface is particularly useful in scenarios where multiple, variable conditions need to be applied
 * to a collection or stream of data.
 *
 * ## Usage:
 * Implement this interface in classes that require a dynamic set of filter conditions. The [filters] list
 * can be updated at runtime, adding, removing, or modifying the filter functions as needed.
 *
 * ### Example:
 * Suppose you have a data processing class that needs to filter a list of data items based on various criteria:
 * ```kotlin
 * class DataProcessor<T> : FilterMutableListContainer<T> {
 *     override var filters = mutableListOf<(T) -> Boolean>()
 *
 *     fun process(data: List<T>): List<T> = data.filter { item -> filters.all { filter -> filter(item) } }
 * }
 *
 * // Usage
 * val processor = DataProcessor<String>()
 * processor.filters.add { it.length > 3 }
 * processor.filters.add { it.startsWith("A") }
 * val filteredData = processor.process(listOf("Apple", "Banana", "Avocado", "Kiwi"))
 * // filteredData will contain "Apple" and "Avocado"
 * ```
 * In this example, `DataProcessor` implements `FilterMutableListContainer`. It processes data items, applying
 * all filters in the `filters` list to each item.
 *
 * @param T The type of data to be filtered by the functions in the `filters` list.
 * @property filters A mutable list of filter functions. Each function takes an input of type [T] and returns
 *                   a `Boolean` indicating whether the item passes the filter.
 */
interface FilterMutableListContainer<T> {
    var filters: MutableList<(T) -> Boolean>
}
