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
 * Creates a matcher that checks if an [Either] instance is an [Either.Left] value.
 *
 * @return A `Matcher<Either<T, *>>` that checks if an [Either] instance is a `Left` value.
 */
fun <T> beLeft() = Matcher<Either<T, *>> {
    MatcherResult(
        it.isLeft(),
        { "Expected Either to be Left, but was Right" },
        { "Expected Either to be Right, but was Left" }
    )
}

/**
 * Asserts that the [Either] instance is an [Either.Left] value.
 *
 * @return The `Either<T, *>` instance itself for further assertions or chaining.
 */
fun <T> Either<T, *>.shouldBeLeft(): Either<T, *> {
    this should beLeft()
    return this
}

/**
 * Asserts that the [Either]` instance is not an [Either.Left] value.
 *
 * @return The `Either<T, *>` instance itself for further assertions or chaining.
 */
fun <T> Either<T, *>.shouldNotBeLeft(): Either<T, *> {
    this shouldNot beLeft()
    return this
}
