/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.matchers

import arrow.core.Either
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot

/**
 * Creates a matcher that checks if an [Either] instance is an [Either.Right].
 *
 * @return A `Matcher<Either<*, T>>` that checks if an [Either] is `Right`.
 */
fun <T> beRight() = Matcher<Either<*, T>> {
    MatcherResult(
        it.isRight(),
        { "Expected Either to be Right, but was Left with value: ${it.leftOrNull()}" },
        { "Expected Either to be Left, but was Right with value: ${it.getOrNull()}" }
    )
}

/**
 * Asserts that the [Either] instance is an [Either.Right].
 *
 * @return The `Either<*, T>` instance itself for further assertions or chaining.
 */
fun <T> Either<*, T>.shouldBeRight(): Either<*, T> {
    this should beRight()
    return this
}

/**
 * Asserts that the [Either] instance is not an [Either.Right].
 *
 * @return The `Either<*, T>` instance itself for further assertions or chaining.
 */
fun <T> Either<*, T>.shouldNotBeRight(): Either<*, T> {
    this shouldNot beRight()
    return this
}
