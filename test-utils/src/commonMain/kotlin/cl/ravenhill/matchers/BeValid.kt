/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.matchers

import cl.ravenhill.keen.mixins.Verifiable
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot

/**
 * Creates a matcher that checks if a [Verifiable] instance is valid.
 *
 * This function returns a [Matcher] that tests whether the [Verifiable.verify] method of a [Verifiable] object returns
 * `true`. It is used to assert the validity of an object in a testing context.
 *
 * @return A [Matcher] that verifies if a [Verifiable] instance is valid.
 */
fun beValid() = object : Matcher<Verifiable> {
    override fun test(value: Verifiable) = MatcherResult(
        value.verify(), // Check if the object is valid
        { "$value should be valid" }, // Failure message when the object is not valid
        { "$value should not be valid" } // Failure message when the object is valid but expected not to be
    )
}

/**
 * Asserts that the [Verifiable] instance is valid.
 *
 * This function uses the [beValid] matcher to assert that the [Verifiable.verify] method of the [Verifiable] instance
 * returns `true`. It provides a convenient way to test object validity in assertions.
 *
 * @receiver The [Verifiable] instance to be tested.
 */
fun Verifiable.shouldBeValid() = this should beValid()

/**
 * Asserts that the [Verifiable] instance is not valid.
 *
 * This function uses the [beValid] matcher to assert that the [Verifiable.verify] method of the [Verifiable] instance
 * returns `false`. It provides a convenient way to test object invalidity in assertions.
 *
 * @receiver The [Verifiable] instance to be tested.
 */
fun Verifiable.shouldNotBeValid() = this shouldNot beValid()
