/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.matchers

import cl.ravenhill.keen.mixins.Verifiable
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot

/**
 * Creates a custom matcher to verify the validity of an object implementing the `Verifiable` interface.
 *
 * @return A `Matcher<Verifiable>` that asserts the validity of an object.
 */
fun beValid() = Matcher<Verifiable> {
    MatcherResult(
        it.verify(),
        { "The object is not valid" },
        { "The object is valid" }
    )
}

/**
 * Asserts that the `Verifiable` object is valid.
 */
fun Verifiable.shouldBeValid() = this should beValid()

/**
 * Asserts that the `Verifiable` object is not valid.
 */
fun Verifiable.shouldNotBeValid() = this shouldNot beValid()
