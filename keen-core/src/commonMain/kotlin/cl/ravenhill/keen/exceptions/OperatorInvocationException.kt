/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.exceptions

/**
 * Exception thrown when an error occurs during the invocation of an operator in an evolutionary algorithm.
 *
 * The `OperatorInvocationException` class represents an error that occurs when invoking an operator, such as selection,
 * crossover, or mutation, within an evolutionary algorithm. This exception is used to signal issues specifically
 * related to the execution of these operations, providing a clear and specific error type that can be caught and
 * handled within the evolutionary algorithm's flow.
 *
 * @param message The detail message explaining the reason for the exception. This message should provide enough context
 *   to understand what caused the error during the operator's invocation.
 */
open class OperatorInvocationException(message: String, cause: Throwable?) : Exception(message, cause) {
    constructor(message: String) : this(message, null)
}
