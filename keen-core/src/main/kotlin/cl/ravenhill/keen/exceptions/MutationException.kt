package cl.ravenhill.keen.exceptions

import cl.ravenhill.jakt.exceptions.ConstraintException

/**
 * Exception thrown when a constraint violation occurs during the mutation process in evolutionary algorithms.
 * This exception highlights issues related to the constraints imposed on the genes or chromosomes involved in the
 * mutation process.
 *
 * ## Usage:
 * `MutationException` is typically thrown within a `constraints` block where specific conditions related to the
 * mutation operation are checked. These conditions might include mutation rates, gene integrity, or other requirements
 * for a successful mutation.
 *
 * ### Example 1: Ensuring Chromosome Integrity
 * ```
 * constraints {
 *     "The chromosome must not be empty"(::MutationException) {
 *         chromosome mustNot BeEmpty
 *     }
 * }
 * ```
 * Here, a `MutationException` could be thrown if the chromosome is empty, which is crucial for maintaining valid
 * mutation operations.
 *
 * @param message The detailed message that explains the reason for the exception, providing context for the constraint
 * violation.
 */
class MutationException(message: String) : ConstraintException(message)
