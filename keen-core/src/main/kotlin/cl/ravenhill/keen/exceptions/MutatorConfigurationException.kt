/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.exceptions

import cl.ravenhill.jakt.Jakt
import cl.ravenhill.jakt.exceptions.ConstraintException


/**
 * Represents an exception for configuration errors in mutators within a genetic algorithm context.
 *
 * `MutatorConfigException` is a specialized exception class extending `ConstraintException`. It is specifically
 * designed to be thrown when encountering issues with the configuration of a mutator, such as invalid mutation rates
 * or incompatible settings. This class plays a crucial role in enforcing the correct configuration of mutators,
 * ensuring that they adhere to defined constraints.
 *
 * ## Usage:
 * `MutatorConfigException` is intended to be used within a [Jakt.constraints] block to enforce constraints on
 * mutator configurations. When a configuration value violates a constraint, this exception is thrown, providing
 * detailed error information.
 *
 * ### Example:
 * ```kotlin
 * constraints {
 *     "The mutation rate must be between 0 and 1"(::MutatorConfigException) {
 *         mutationRate must BeInRange(0.0, 1.0)
 *     }
 * }
 * ```
 * In this example, the exception is used to assert that the mutation rate is within the valid range of 0 to 1.
 * If the `mutationRate` falls outside this range, a `MutatorConfigException` with an appropriate message is raised
 * according to the specified [Jakt.shortCircuit] and [Jakt.skipChecks] flags.
 *
 * @param message A descriptive message providing details about the configuration error.
 */
class MutatorConfigurationException(message: String) : ConstraintException({ message })
