/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.exceptions


/**
 * Exception thrown when an error occurs during the selection process in an evolutionary algorithm.
 *
 * The `SelectionException` class extends the [OperatorInvocationException] and represents errors specifically related
 * to the selection phase of an evolutionary algorithm. This exception is used to indicate issues that arise when
 * selecting individuals from a population, such as when a selection method fails to produce a valid result or when
 * constraints related to selection are violated.
 *
 * @param message The detail message explaining the reason for the selection error. This message should clearly describe
 *   the problem encountered during the selection process.
 */
class SelectionException(message: String, cause: Throwable?) : OperatorInvocationException(message, cause) {
    constructor(message: String) : this(message, null)
}
