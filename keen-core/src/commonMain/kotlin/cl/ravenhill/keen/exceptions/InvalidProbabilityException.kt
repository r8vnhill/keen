package cl.ravenhill.keen.exceptions

import cl.ravenhill.jakt.exceptions.ConstraintException

/**
 * ### Example 1: Validating a probability
 *
 * ```kotlin
 * constrained {
 *     "Probability must be between 0.0 and 1.0"(::InvalidProbabilityException) {
 *         1.5 must Be
 *     }
 * }
 */
class InvalidProbabilityException(message: String) : ConstraintException(message)
