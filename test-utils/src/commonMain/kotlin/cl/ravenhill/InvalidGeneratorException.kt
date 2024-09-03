/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill

/**
 * Exception thrown when invalid parameters are provided to a generator.
 *
 * @param cause The underlying cause of the exception, providing additional context about why the generator parameters
 *   were invalid. This is useful for debugging and understanding the root cause of the issue.
 */
class InvalidGeneratorException(cause: Throwable?) : Exception(
    "Invalid parameters were provided for this generator, please check the cause for more information",
    cause
)
