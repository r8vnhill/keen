package cl.ravenhill.keen.exceptions

import cl.ravenhill.jakt.exceptions.ConstraintException
import cl.ravenhill.jakt.Jakt

/**
 * Represents an exception for configuration errors in crossover operations within an evolutionary algorithm context.
 *
 * `CrossoverConfigException` is a specialized exception class extending `ConstraintException`. It is specifically
 * designed to be thrown when encountering issues with the configuration of a crossover, such as invalid crossover rates
 * or incompatible settings between parent and offspring genotypes. This class plays a crucial role in enforcing the
 * correct configuration of crossovers, ensuring that they adhere to defined constraints.
 *
 * ## Usage:
 * `CrossoverConfigException` is intended to be used within a [Jakt.constraints] block to enforce constraints on
 * crossover configurations. When a configuration value violates a constraint, this exception is thrown, providing
 * detailed error information.
 *
 * ### Example:
 * ```kotlin
 * constraints {
 *     "The crossover rate must be between 0 and 1"(::CrossoverConfigException) {
 *         crossoverRate must BeInRange(0.0, 1.0)
 *     }
 *     "The number of parents must be compatible with the crossover strategy"(::CrossoverConfigException) {
 *         numberOfParents must BeEqual(requiredParents)
 *     }
 * }
 * ```
 * In this example, the exception is used to assert specific conditions like the crossover rate being within the valid
 * range of 0 to 1, and the number of parents being adequate for the chosen crossover strategy. If any condition is not
 * met, a `CrossoverConfigException` with an appropriate message is raised according to the specified
 * [Jakt.shortCircuit] and [Jakt.skipChecks] flags.
 *
 * @param message A descriptive message providing details about the configuration error.
 */
class CrossoverConfigException(message: String) : ConstraintException(message)
