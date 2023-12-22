/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

/**
 * Enum representing the different modes of converting objects to their string representation.
 *
 * `ToStringMode` provides various options for how objects are converted to strings. This is particularly useful in
 * scenarios where different levels of detail or formatting are required for the string representation of objects.
 *
 * ## Enum Values:
 * - `SIMPLE`: Represents a concise, minimal information version of the object's string representation. Ideal for
 *   logging or displaying brief summaries where details are not critical.
 * - `DEFAULT`: Represents the standard string representation, providing a balance between brevity and detail. Suitable
 *   for general purposes where a moderate amount of information is sufficient.
 * - `DETAILED`: Provides an in-depth, comprehensive string representation of the object. Best suited for debugging or
 *   scenarios where detailed information is crucial.
 *
 * ## Usage:
 * This enum can be used to control the verbosity and level of detail of an object's `toString` method. Depending on the
 * chosen mode, the output of `toString` can vary significantly, ranging from a simple summary to a detailed
 * description.
 *
 * ### Example:
 * ```kotlin
 * Domain.toStringMode = ToStringMode.DETAILED
 * val objectString = myObject.toString()
 * // This will produce a detailed string representation of myObject
 * ```
 * In this example, `ToStringMode.DETAILED` is used to generate a comprehensive string representation of `myObject`,
 * which includes all relevant details about the object.
 */
enum class ToStringMode {
    SIMPLE,
    DEFAULT,
    DETAILED
}
