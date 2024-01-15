/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.exceptions

import cl.ravenhill.jakt.exceptions.ConstraintException
import cl.ravenhill.keen.operators.alteration.mutation.InversionMutator
import cl.ravenhill.keen.operators.alteration.mutation.SwapMutator

/**
 * Represents an exception that occurs during mutator operations in Keen.
 *
 * ## Overview
 * `MutatorOperationException` is a specialized exception class that extends [ConstraintException]. It is thrown when an
 * operation within a mutator, such as gene or chromosome mutation, encounters an error or when a specific operation
 * constraint is violated. This exception ensures that issues specific to mutator operations are clearly identifiable
 * and distinct from other types of exceptions.
 *
 * ## Usage
 * This exception is typically thrown in scenarios where the logic within a mutator class, such as [InversionMutator],
 * [SwapMutator], or similar, fails to execute as expected. This could be due to invalid parameters, unexpected
 * gene configurations, or violation of pre-defined constraints in the mutator's logic.
 *
 * ### Example
 * Imagine a scenario in a genetic algorithm where a mutator attempts to invert a chromosome segment, but the
 * indices for inversion are out of bounds. In such a case, `MutatorOperationException` could be thrown to
 * indicate this specific error:
 *
 * ```kotlin
 * constraints {
 *     "The inversion indices must be within the chromosome bounds"(::MutatorOperationException) {
 *         inversionStart must BeInRange(0, chromosome.size)
 *         inversionEnd must BeInRange(0, chromosome.size)
 *     }
 * }
 * ```
 *
 * ## Benefits
 * Using a specific exception class for mutator operations helps in debugging and maintaining the genetic algorithm
 * framework by:
 * - Clearly distinguishing mutator operation errors from other types of exceptions.
 * - Providing detailed error messages that assist in quickly identifying and resolving issues.
 *
 * @param message A detailed message describing the exception cause.
 */
class MutatorOperationException(message: String) : ConstraintException({ message })
