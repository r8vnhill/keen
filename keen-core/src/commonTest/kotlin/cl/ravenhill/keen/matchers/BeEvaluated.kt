/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.matchers

import cl.ravenhill.keen.Individual
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot

/**
 * A matcher that checks whether an `Individual` has been evaluated.
 *
 * @return A `Matcher<Individual<*, *, *>>` that asserts the individual has been evaluated.
 */
fun beEvaluated() = Matcher<Individual<*, *, *>> {
    MatcherResult(
        !it.fitness.isNaN(),
        { "Individual should be evaluated" },
        { "Individual should not be evaluated" }
    )
}

/**
 * Asserts that this `Individual` has been evaluated.
 */
fun Individual<*, *, *>.shouldBeEvaluated() = this should beEvaluated()

/**
 * Asserts that this `Individual` has not been evaluated.
 */
fun Individual<*, *, *>.shouldNotBeEvaluated() = this shouldNot beEvaluated()
