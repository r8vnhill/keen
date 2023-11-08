/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.ConstraintException
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.set
import io.kotest.property.arbitrary.string
import kotlin.random.Random

// region : -====================== SHOULD ASSERTIONS =============================================-

/**
 * Enforces that an [ConstraintException] is present in the
 * [CompositeException.throwables] list.
 *
 * @param message the message to match against the [ConstraintException].
 * @throws AssertionError if the [CompositeException.throwables] list does not contain an
 * [ConstraintException] of type [T] with the specified [message].
 */
inline fun <reified T> CompositeException.shouldHaveInfringement(message: String)
    where T : ConstraintException = should(
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
                    "$value should not have an infringement of type ${T::class.simpleName} with message: $message. " +
                        "Actual: $filtered"
                }
            )
        }
    }
)
// endregion SHOULD ASSERTIONS

// region : -== ARBITRARY GENERATORS ==-

/**
 * Returns an arbitrary generator of a list of unique strings.
 * The function generates a set of strings and then converts it to a list, ensuring that
 * there are no repeated strings in the output.
 *
 * @return An arbitrary generator of a list of unique strings.
 */
fun Arb.Companion.uniqueStrings(range: IntRange = 0..100) = arbitrary {
    set(string(), range).bind().toList()
}

/**
 * Returns an arbitrary that generates random instances of [Random] class.
 *
 * @param seed the arbitrary of the seed value to be used for the initialization of the [Random]
 * instance.
 * @return an [Arb] that generates [Random] instances with the given seed.
 */
fun Arb.Companion.random(seed: Arb<Long> = Arb.long()) = arbitrary {
    Random(seed.bind())
}
// endregion ARBITRARY GENERATORS

