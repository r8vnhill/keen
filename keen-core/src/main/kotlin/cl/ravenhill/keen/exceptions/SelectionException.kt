/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.exceptions


/**
 * Exception class representing errors that occur during the selection process in evolutionary algorithms.
 *
 * `SelectionException` is a specialized form of [Exception] that is thrown to indicate problems that arise specifically
 * during the selection phase of an evolutionary algorithm. This can include issues such as failing to select any
 * individuals from a population or encountering invalid parameters during the selection process.
 *
 * ## Usage:
 * This exception is typically thrown by selection-related classes or functions when they encounter a state that
 * prevents them from completing the selection process successfully. It helps in diagnosing and handling issues that are
 * specific to the selection mechanism of evolutionary computations.
 *
 * ### Example:
 * ```kotlin
 * class MySelector<T, G> : Selector<T, G> where G : Gene<T, G> {
 *     override fun select(population: Population<T, G>, count: Int, ranker: IndividualRanker<T, G>): Population<T, G> {
 *         if (population.isEmpty()) {
 *             throw SelectionException { "Population must not be empty for selection" }
 *         }
 *         // Selection logic...
 *     }
 * }
 * ```
 * In this example, `MySelector` throws a `SelectionException` if it is given an empty population to select from.
 * This informs users of the class about the specific reason for the failure of the selection process.
 *
 * @param lazyMessage A lambda function that provides the detail message for the exception. This approach allows for
 *   lazy evaluation of the message, which can be beneficial for performance if the message construction is complex.
 */
class SelectionException(lazyMessage: () -> String) : Exception(lazyMessage())
