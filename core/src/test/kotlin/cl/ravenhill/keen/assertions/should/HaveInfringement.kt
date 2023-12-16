/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.assertions.should

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.ConstraintException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should

/**
 * Extension function for [CompositeException] to assert the presence of a specific infringement (exception of type [T])
 * with a given message.
 *
 * This function is used in testing to verify whether a [CompositeException] contains a specific type of
 * [ConstraintException] (or its subclass) with the expected message. It enhances the expressiveness and readability of
 * test assertions related to exception handling.
 *
 * ## Usage:
 * This function should be used in a context where a [CompositeException] is expected to contain a specific type of
 * constraint exception. It allows for a concise and clear assertion about the presence and message content of that
 * exception.
 *
 * ### Example:
 * In a test case where a specific constraint exception is expected within a [CompositeException]:
 * ```kotlin
 * shouldThrow<CompositeException> {
 *     genotype[index]
 * }.shouldHaveInfringement<IntConstraintException>(
 *     "The index [$index] must be in the range [0, ${genotype.size})"
 * )
 * ```
 * In this example, the test will pass if the [CompositeException] contains an [IntConstraintException] with the
 * specified message.
 *
 * @param T The specific type of [ConstraintException] to look for within the [CompositeException].
 *          This type must be a reified type due to the usage of the `reified` keyword, allowing the
 *          type information to be available at runtime.
 * @param message The expected message of the constraint exception.
 */
inline fun <reified T> CompositeException.shouldHaveInfringement(message: String): CompositeException
      where T : ConstraintException {
    should(
        if (throwables.none { it is T }) {
            Matcher { value ->
                MatcherResult(
                    false,
                    { "$value should have an infringement of type ${T::class.simpleName} with message: $message" },
                    { "$value should not have an infringement of type ${T::class.simpleName} with message: $message" }
                )
            }
        } else {
            Matcher { value ->
                val filtered = throwables.filterIsInstance<T>()
                MatcherResult(
                    filtered.any { it.message == message },
                    {
                        "$value should have an infringement of type ${T::class.simpleName} with message: $message. " +
                              "Actual: $filtered"
                    },
                    {
                        "$value should not have an infringement of type ${T::class.simpleName} with message: " +
                              "$message. Actual: $filtered"
                    }
                )
            }
        }
    )
    return this
}
