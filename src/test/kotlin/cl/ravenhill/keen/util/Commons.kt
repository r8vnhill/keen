/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.next

// region : -======================= SHOULD MATCHERS ==============================================-
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

/**
 * Asserts that this [Double] value is finite.
 */
fun Double.shouldBeFinite() = should(Matcher { value ->
    MatcherResult(
        value.isFinite(),
        { "$value should be finite" },
        { "$value should not be finite" }
    )
})

/**
 * Asserts that at least one element in the Iterable matches the given predicate.
 *
 * @param predicate The predicate to match the element against.
 *
 * @return A matcher result indicating whether the Iterable has any elements that match the predicate.
 *
 * @throws AssertionError If the Iterable has no elements that match the predicate.
 */
infix fun <T> Iterable<T>.shouldAny(predicate: (T) -> Boolean) = should(Matcher { value ->
    MatcherResult(
        value.any(predicate),
        { "Iterable should have at least one element that matches the predicate" },
        { "Iterable should not have any elements that match the predicate" })
})

// endregion SHOULD MATCHERS

// region : -========================= GENERATORS =================================================-

// endregion GENERATORS
