/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.exceptions

/**
 * Represents an exception that occurs during the evaluation process in an evolutionary algorithm.
 *
 * The `EvaluationException` class is a specific type of exception that is thrown when an error occurs during the
 * evaluation phase of an evolutionary algorithm. This phase typically involves assessing the fitness of individuals
 * in a population, and errors during this process can significantly impact the algorithm's progress.
 *
 * @constructor Creates an `EvaluationException` with the specified error message and optional cause.
 * @param message The detail message string describing the error.
 * @param cause The cause of the exception, which can be another throwable that led to this exception.
 */
open class EvaluationException(message: String, cause: Throwable? = null) : Exception(message, cause) {

    /**
     * Creates an `EvaluationException` with the specified cause. The detail message is set to the message from the
     * cause, or a default message if the cause's message is `null`.
     *
     * @param cause The cause of the exception, which can be another throwable that led to this exception.
     */
    constructor(cause: Throwable) : this(cause.message ?: "Evaluation exception", cause)
}
