/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.util

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should

/**
 * Asserts that this Double is equal to the specified Double value ``d``, within a certain tolerance
 * range.
 * Uses the [eq] function to compare the values with a default tolerance of 1e-10.
 */
infix fun Double.shouldEq(d: Double) = should(Matcher { value ->
    MatcherResult(
        value eq d,
        { "Expected $d but got $value" },
        { "Expected $d to be different from $value" }
    )
})

/**
 * Asserts that this Double is not equal to the specified Double value ``d``, within a certain
 * tolerance range.
 * Uses the [neq] function to compare the values with a default tolerance of 1e-10.
 */
infix fun Double.shouldNeq(d: Double) = should(Matcher { value ->
    MatcherResult(
        value neq d,
        { "Expected $d to be different from $value" },
        { "Expected $d but got $value" }
    )
})