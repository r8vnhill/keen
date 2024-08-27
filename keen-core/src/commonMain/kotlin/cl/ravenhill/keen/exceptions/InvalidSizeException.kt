/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.exceptions

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constrainedTo
import cl.ravenhill.jakt.exceptions.ConstraintException

/**
 * Exception thrown when a constraint related to size is violated.
 *
 * This exception is a specific type of `ConstraintException` and is used to indicate that a size-related constraint
 * has failed.
 *
 * ## Usage:
 * This exception is typically used in cases where a size constraint (e.g., length of a collection, string, or array)
 * does not meet the expected criteria.
 *
 * ### Example 1: Using with a [constraints] block
 *
 * ```kotlin
 * constraints {
 *     "Size must be positive"(::InvalidSizeException) {
 *         size must BePositive
 *     }
 * }
 * ```
 *
 * ### Example 2: Using in a [constrainedTo] block
 *
 * ```kotlin
 * size.constrainedTo {
 *     "Size must be positive"(::InvalidSizeException) {
 *         it must BePositive
 *     }
 * }
 * ```
 *
 * @param message The detail message explaining the reason for the exception.
 */
class InvalidSizeException(message: String) : ConstraintException(message)
