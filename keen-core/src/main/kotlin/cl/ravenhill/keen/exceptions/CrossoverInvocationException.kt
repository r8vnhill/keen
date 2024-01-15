/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.exceptions

import cl.ravenhill.jakt.exceptions.ConstraintException

/**
 * Represents an exception that occurs during the crossover operation in a genetic algorithm.
 *
 * ## Common Scenarios
 * This exception may be thrown in scenarios such as:
 * - Invalid chromosome structures that prevent successful crossover.
 * - Incompatibility between parent chromosomes.
 * - Violation of crossover-related constraints or preconditions.
 *
 * ## Usage
 * `CrossoverInvocationException` is typically thrown within the crossover operation implementations, like in classes or
 * methods responsible for executing crossover logic. It serves to identify and report specific issues related to the
 * crossover process.
 *
 * ### Example
 * Imagine a crossover method that requires chromosomes of equal length but receives chromosomes of different lengths.
 * In such a case, `CrossoverInvocationException` could be thrown to indicate this specific error:
 *
 * ```kotlin
 * fun crossover(chromosome1: Chromosome, chromosome2: Chromosome): Offspring {
 *     Jakt.constraints {
 *         "The chromosomes must have the same length"(::CrossoverInvocationException) {
 *             chromosome1.size must BeEqualTo(chromosome2.size)
 *         }
 *     }
 * }
 * ```
 * This snippet demonstrates how `CrossoverInvocationException` might be used to enforce certain constraints necessary
 * for a successful crossover.
 *
 * ## Benefits
 * - **Clarity in Error Handling**: By specifically addressing crossover-related errors, this exception enhances the
 *   clarity and precision in the error-handling mechanism of genetic algorithms.
 * - **Targeted Debugging**: It aids in quickly pinpointing the source of errors during the crossover phase,
 *   facilitating more effective debugging and resolution.
 * - **Consistency and Safety**: Ensures that the genetic algorithm adheres to its defined constraints and
 *   preconditions, thereby maintaining consistency and operational safety.
 *
 * `CrossoverInvocationException` is essential for robust error management in genetic algorithms, especially in ensuring the integrity and validity of the crossover process.
 *
 * @param message A detailed message describing the reason for the exception, providing context and specifics about the
 *   crossover operation failure.
 */
class CrossoverInvocationException(message: String) : ConstraintException(message)
