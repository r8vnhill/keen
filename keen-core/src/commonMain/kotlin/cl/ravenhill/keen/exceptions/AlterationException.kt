/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.exceptions

/**
 * Exception class for handling errors during genetic alteration processes in evolutionary algorithms.
 *
 * The `AlterationException` class is used to signal errors that occur during the genetic alteration phase of an
 * evolutionary algorithm. This phase typically includes operations like crossover and mutation, which modify the
 * genetic makeup of individuals in a population. If an error occurs during these processes, an `AlterationException`
 * is thrown, encapsulating the error message and the underlying cause, if any.
 *
 * @param message The detail message describing the error.
 * @param cause The underlying cause of the exception, if available. Defaults to `null`.
 */
class AlterationException(message: String, cause: Throwable? = null) : OperatorInvocationException(message, cause)
