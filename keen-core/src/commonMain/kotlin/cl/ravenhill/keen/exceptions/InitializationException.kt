/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.exceptions

/**
 * Exception thrown during the initialization phase of an evolutionary algorithm.
 *
 * The `InitializationException` class represents errors that occur specifically during the initialization phase of
 * an evolutionary algorithm or similar processes. Initialization is a critical step where the initial population
 * or state is set up, and any issues during this phase can prevent the algorithm from starting correctly. This
 * exception provides a way to capture and report such errors, offering the possibility to include an optional
 * cause that triggered the exception.
 *
 * @param message The detail message explaining the reason for the exception.
 * @param cause The cause of the exception, which can be another throwable that led to this error. Default is `null`.
 */
open class InitializationException(message: String, cause: Throwable? = null) : Exception(message, cause)
