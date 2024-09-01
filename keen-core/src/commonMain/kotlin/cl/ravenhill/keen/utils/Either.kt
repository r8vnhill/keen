/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.utils

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right

/**
 * Sequences a list of `Either` values, transforming a `List<Either<L, R>>` into an `Either<L, List<R>>`.
 *
 * The `sequence` function processes a list of [Either] values and attempts to collect all the `Right` values into a
 * single [Either.Right] containing a list of the [R] values. If any `Either` in the list is `Left`, the function
 * short-circuits and returns the first `Left` encountered, preserving the error.
 *
 * ## Example Usage:
 * ```kotlin
 * val results: List<Either<Error, Int>> = listOf(1.right(), 2.right(), 3.right())
 * val sequenced: Either<Error, List<Int>> = results.sequence()
 *
 * when (sequenced) {
 *     is Either.Right -> println("All values are successfully collected: ${sequenced.value}")
 *     is Either.Left -> println("An error occurred: ${sequenced.value}")
 * }
 * ```
 *
 * @param L The type of the `Left` value, representing an error or failure.
 * @param R The type of the `Right` value, representing a success.
 * @return An `Either` where the `Right` side contains a list of `R` values if all computations succeeded, or a `Left`
 *   value representing the first encountered error.
 */
fun <L, R> List<Either<L, R>>.sequence(): Either<L, List<R>> =
    fold(emptyList<R>().right() as Either<L, List<R>>) { acc, either ->
        acc.flatMap { list ->
            either.map { list + it }
        }
    }
