/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.exceptions

/**
 * Represents an exception that occurs during the evolutionary process.
 *
 * The `EvolutionException` class is a custom exception type used to indicate errors or issues that arise within the
 * evolutionary process. This exception is intended to capture and convey meaningful error messages that describe what
 * went wrong during the execution of an evolutionary algorithm. The class provides constructors to create an exception
 * with or without a root cause.
 *
 * @param message The detailed message describing the cause of the exception.
 * @param cause The root cause of the exception, if any. Defaults to `null`.
 * @constructor Creates an `EvolutionException` with a specific error message and an optional cause.
 */
class EvolutionException(message: String, cause: Throwable? = null) : Exception(message, cause) {

    /**
     * Secondary constructor to create an `EvolutionException` with just a message.
     *
     * This constructor allows for the creation of an `EvolutionException` with only a descriptive message, without
     * specifying an underlying cause.
     *
     * @param message The detailed message describing the cause of the exception.
     */
    constructor(message: String) : this(message, null)
}
