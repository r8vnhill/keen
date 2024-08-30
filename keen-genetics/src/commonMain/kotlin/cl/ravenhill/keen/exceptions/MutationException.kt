/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.exceptions

/**
 * Represents an exception that occurs during the mutation process in an evolutionary algorithm.
 *
 * The `MutationException` class extends the [OperatorInvocationException] to specifically handle errors related to
 * mutation operations within an evolutionary algorithm. Mutation is a key operator in evolutionary algorithms,
 * responsible for introducing genetic diversity by making random changes to an individual's genetic representation.
 * This exception is thrown when a mutation operation fails, allowing the algorithm to handle the error appropriately.
 *
 * @param message A descriptive message providing details about the mutation error.
 * @param cause The underlying cause of the exception, if available (default is `null`).
 */
open class MutationException(message: String, cause: Throwable? = null) : OperatorInvocationException(message, cause)
