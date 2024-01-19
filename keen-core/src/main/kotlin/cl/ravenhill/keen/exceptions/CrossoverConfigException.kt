/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.exceptions

import cl.ravenhill.jakt.exceptions.ConstraintException


/**
 * Represents an exception specific to configuration errors in crossover operations within genetic algorithms.
 * This class extends `ConstraintException`, indicating that it is used to signal issues with constraints
 * or configuration parameters in crossover-related functionality.
 *
 * ## Overview:
 * `CrossoverConfigException` is thrown when there is an error in the configuration of crossover parameters
 * or constraints. This could include invalid settings, incompatible parameters, or other issues that prevent
 * the proper execution of a crossover operation.
 *
 * ## Usage:
 * Use this exception class to handle errors related to crossover configuration in genetic algorithm implementations.
 * This exception provides a clear indication of configuration issues, making debugging and error handling more straightforward.
 *
 * ### Example:
 * ```kotlin
 * constraints {
 *    "The chromosome rate must be in the range [0.0, 1.0]"(::CrossoverConfigException) {
 *        chromosomeRate must BeInRange(0.0..1.0)
 *    }
 * }
 * ```
 *
 * @param message A descriptive message detailing the specific configuration error encountered.
 */
class CrossoverConfigException(message: String) : ConstraintException(message)
