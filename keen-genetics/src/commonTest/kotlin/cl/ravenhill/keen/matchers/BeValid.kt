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

fun beValid() = object : Matcher<Verifiable> {
    override fun test(value: Verifiable) = MatcherResult(
        value.verify(),
        { "$value should be valid" },
        { "$value should not be valid" }
    )
}

fun Verifiable.shouldBeValid() = this should beValid()

fun Verifiable.shouldNotBeValid() = this shouldNot beValid()
