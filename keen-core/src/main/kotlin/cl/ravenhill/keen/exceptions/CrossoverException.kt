package cl.ravenhill.keen.exceptions

import cl.ravenhill.jakt.exceptions.ConstraintException

/**
 * Exception thrown when a constraint violation occurs during the crossover operation in evolutionary algorithms.
 * This exception highlights issues related to the constraints imposed on the genotypes involved in the crossover
 * process.
 *
 * ## Usage:
 * `CrossoverException` is typically thrown within a constraints block where specific conditions related to the
 * crossover operation are checked. These conditions might include the number of parents, chromosome consistency, or
 * other requirements for successful crossover.
 *
 * ### Example 1: Ensuring Consistency in Chromosome Count
 * ```
 * constraints {
 *     "Genotypes must have the same number of chromosomes"(::CrossoverException) {
 *         parentGenotypes.map { it.size }.toSet() must HaveSize(1)
 *     }
 * }
 * ```
 * Here, a `CrossoverException` could be thrown if the parent genotypes do not have a consistent number of chromosomes,
 * which is crucial for a successful crossover operation.
 *
 * @param message The detailed message that explains the reason for the exception, providing context for the constraint
 * violation.
 */
class CrossoverException(message: String) : ConstraintException(message)
