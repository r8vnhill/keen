/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.exceptions

/**
 * Exception class for handling errors during crossover operations in genetic algorithms.
 *
 * The `CrossoverException` class extends the [OperatorInvocationException] to specifically address issues that arise
 * during the crossover phase of a genetic algorithm. Crossover is a critical operation in genetic algorithms, where
 * genetic material from parent individuals is combined to produce offspring. Errors during this process can include
 * invalid parent configurations, incompatible chromosome structures, or other issues related to the combination of
 * genetic material.
 *
 * @param message A detailed message describing the error that occurred during the crossover operation.
 * @param cause The underlying cause of the error, if any. This parameter is optional and can be used to chain
 *   exceptions.
 */
class CrossoverException(message: String, cause: Throwable? = null) : OperatorInvocationException(message, cause)
