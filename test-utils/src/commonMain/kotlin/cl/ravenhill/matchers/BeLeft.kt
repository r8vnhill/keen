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
 * @return A `Matcher<Either<L, *>>` that checks if an [Either] instance is a `Left` value.
 */
fun <L> beLeft() = Matcher<Either<L, *>> {
    MatcherResult(
        it.isLeft(),
        { "Expected Either to be Left, but was Right" },
        { "Expected Either to be Right, but was Left" }
    )
}

/**
 * Asserts that the [Either] instance is an [Either.Left] value.
 *
 * @return The value of the `Left` side of the [Either] instance.
 */
fun <L> Either<L, *>.shouldBeLeft(): L {
    this should beLeft()
    return this.leftOrNull()!!  // In case, since `should` short-circuits, the value is not null.
}

/**
 * Asserts that the [Either] instance is not an [Either.Left] value.
 *
 * @return The value of the `Right` side of the [Either] instance or `null` if the
 */
fun <R> Either<*, R>.shouldNotBeLeft(): R {
    this shouldNot beLeft()
    return this.getOrNull()!!   // In case, since `shouldNot` short-circuits, the value is not null.
}
