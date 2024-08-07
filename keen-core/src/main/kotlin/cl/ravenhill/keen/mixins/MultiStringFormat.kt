/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.mixins

/**
 * An interface providing methods for different string representations.
 *
 * This interface defines methods for converting an object into string formats. It includes
 * two methods: `toSimpleString` and `toDetailedString`. By default, both methods return
 * the standard string representation of the object (`toString`), but they can be overridden
 * to provide more specific or formatted representations.
 *
 * ## Usage:
 * This interface is particularly useful in scenarios where an object might have multiple
 * ways of being represented as a string, such as a simplified view for user interfaces or
 * a detailed view for logging and debugging.
 *
 * ### Example:
 * Implementing the `MultiStringFormat` interface in a class:
 * ```kotlin
 * class MyData(val id: Int, val name: String) : MultiStringFormat {
 *     override fun toSimpleString(): String = name
 *     override fun toDetailedString(): String = "MyData(id=$id, name=$name)"
 * }
 *
 * val myData = MyData(1, "Sample")
 * println(myData.toSimpleString()) // Output: Sample
 * println(myData.toDetailedString()) // Output: MyData(id=1, name=Sample)
 * ```
 *
 * In this example, `MyData` class implements `MultiStringFormat` and provides custom
 * implementations for `toSimpleString` and `toDetailedString`. `toSimpleString` returns
 * just the `name` field, while `toDetailedString` provides a more detailed representation
 * of the object.
 */
@Deprecated("Use the standard toString method instead")
interface MultiStringFormat {
    /**
     * Provides a simple string representation of the object.
     * By default, it returns the standard `toString()` representation.
     *
     * @return A simple string representation of the object.
     */
    @Deprecated("Use the standard toString method instead", ReplaceWith("toString()"))
    fun toSimpleString(): String = toString()

    /**
     * Provides a detailed string representation of the object.
     * By default, it also returns the standard `toString()` representation.
     *
     * @return A detailed string representation of the object.
     */
    @Deprecated("Use the standard toString method instead", ReplaceWith("toString()"))
    fun toDetailedString(): String = toString()
}
