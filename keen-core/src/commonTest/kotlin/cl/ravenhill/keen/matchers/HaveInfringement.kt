/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.matchers

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.ConstraintException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should


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
