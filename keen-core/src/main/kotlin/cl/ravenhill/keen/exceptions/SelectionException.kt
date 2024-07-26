/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.exceptions

import cl.ravenhill.jakt.exceptions.ConstraintException


/**
 * Exception thrown when a constraint violation occurs during the selection operation in evolutionary algorithms.
 * This exception highlights issues related to the constraints imposed on the selection process.
 *
 * ## Usage:
 * `SelectionException` is typically thrown within a constraints block where specific conditions related to the
 * selection operation are checked. These conditions might include the size of the population, the validity of the
 * selection count, or other requirements for successful selection.
 *
 * ### Example 1: Ensuring Population is Not Empty
 * ```
 * constraints {
 *     "Population must not be empty"(::SelectionException) {
 *         state.population mustNot BeEmpty
 *     }
 * }
 * ```
 * Here, a `SelectionException` could be thrown if the population is empty, which is crucial for a successful selection
 * operation.
 *
 * @param message The detailed message that explains the reason for the exception, providing context for the constraint
 *  violation.
 */
class SelectionException(message: String) : ConstraintException(message)
