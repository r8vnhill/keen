/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.matchers

import cl.ravenhill.jakt.exceptions.CompositeException
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot

/**
 * Matcher to verify that a [CompositeException] contains an exception of a specific type with an optional message.
 *
 * @param E The type of exception to check for within the [CompositeException].
 * @param message An optional string to match against the exception's message. If not provided, only the exception type
 *   is checked.
 * @return A [Matcher] that verifies whether the [CompositeException] contains the specified exception type with the
 *   optional message.
 */
inline fun <reified E> containExceptionOfType(message: String = "")
        where E : Exception = Matcher<CompositeException> { value ->
    val matchingExceptions = value.throwables.filterIsInstance<E>()
    val hasMatchingException = matchingExceptions.isNotEmpty()
    val hasMatchingMessage = matchingExceptions.any { it.message?.contains(message) == true }
    MatcherResult(
        hasMatchingException && hasMatchingMessage,
        {
            "'$value' should have an infringement of type ${E::class.simpleName} with message: '$message'.\n" +
                    "Actual: $matchingExceptions"
        },
        {
            "'$value' should not have an infringement of type ${E::class.simpleName} with message: '$message'.\n" +
                    "Actual: $matchingExceptions"
        }
    )
}

/**
 * Extension function to check if a [CompositeException] contains an exception of the specified type with an optional
 * message.
 *
 * @param E The type of exception to check for within the [CompositeException].
 * @param message An optional string to match against the exception's message. If not provided, only the exception type
 *   is checked.
 * @return The original [CompositeException] to allow for further assertions or operations.
 * @throws AssertionError if the [CompositeException] does not contain the specified exception type with the message.
 */
inline fun <reified E> CompositeException.shouldContainExceptionOfType(message: String = ""): CompositeException
        where E : Exception {
    this should containExceptionOfType<E>(message)
    return this
}

/**
 * Extension function to check that a [CompositeException] does not contain an exception of the specified type with an
 * optional message.
 *
 * @param E The type of exception to check for within the [CompositeException].
 * @param message An optional string to match against the exception's message. If not provided, only the exception type
 *   is checked.
 * @return The original [CompositeException] to allow for further assertions or operations.
 * @throws AssertionError if the [CompositeException] contains the specified exception type with the message.
 */
inline fun <reified E> CompositeException.shouldNotContainExceptionOfType(message: String = ""): CompositeException
        where E : Exception {
    this shouldNot containExceptionOfType<E>(message)
    return this
}
