package cl.ravenhill.keen.assertions

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.ConstraintException
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should

/**
 * Enforces that an [CompositeException] is present in the
 * [CompositeException.throwables] list.
 *
 * @param message the message to match against the [CompositeException].
 * @throws AssertionError if the [CompositeException.throwables] list does not contain an
 * [CompositeException] of type [T] with the specified [message].
 */
inline fun <reified T> CompositeException.shouldHaveInfringement(message: String) where T : ConstraintException =
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
