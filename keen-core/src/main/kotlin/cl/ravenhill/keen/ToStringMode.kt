/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

/**
 * Enum representing the different modes for converting objects to their string representation.
 *
 * The `ToStringMode` enum provides two modes for converting objects to strings: `SIMPLE` and `DEFAULT`.
 * These modes can be used to control the level of detail included in the string representation.
 *
 * ## Usage:
 * This enum can be used to specify the desired mode when converting objects to strings, allowing for either a simple
 * or a detailed representation.
 *
 * ### Example:
 * ```kotlin
 * val mode = ToStringMode.SIMPLE
 * val detailedMode = ToStringMode.DEFAULT
 * ```
 *
 * @property SIMPLE A simple string representation with minimal details.
 * @property DEFAULT The default string representation with comprehensive details.
 */
enum class ToStringMode {
    SIMPLE,
    DEFAULT,
}
