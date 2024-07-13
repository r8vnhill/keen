package cl.ravenhill.keen.exceptions

import cl.ravenhill.jakt.exceptions.ConstraintException

/**
 * Represents an exception for configuration errors related to limits within an evolutionary algorithm context.
 *
 * `LimitConfigurationException` is a specialized exception class extending `ConstraintException`. It is specifically
 * designed to be thrown when encountering issues with the configuration of limits, such as invalid generation limits or
 * incompatible settings between different evolutionary constraints. This class plays a crucial role in enforcing the
 * correct configuration of limits, ensuring that they adhere to defined constraints.
 *
 * ## Usage:
 * `LimitConfigurationException` is intended to be used within a constraints block to enforce constraints on
 * limit configurations. When a configuration value violates a constraint, this exception is thrown, providing
 * detailed error information.
 *
 * ### Example:
 * ```kotlin
 * constraints {
 *     "The generation limit must be a positive integer"(::LimitConfigurationException) {
 *         generationLimit must BePositive
 *     }
 *     "The fitness threshold must be a non-negative value"(::LimitConfigurationException) {
 *         fitnessThreshold must BeNonNegative
 *     }
 * }
 * ```
 * In this example, the exception is used to assert specific conditions like the generation limit being a positive
 * integer, and the fitness threshold being a non-negative value. If any condition is not met, a
 * `LimitConfigurationException` with an appropriate message is raised.
 *
 * @param message A descriptive message providing details about the configuration error.
 */
class LimitConfigurationException(message: String) : ConstraintException(message)
