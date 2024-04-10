/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.assertions.should

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.utils.eq
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot

/**
 * Creates a matcher for approximate equality comparison of [Double] values.
 *
 * This function returns a [Matcher]`<Double>` that checks if the provided `Double` value is approximately equal to
 * another specified `Double` value ([x]), within a certain tolerance. The tolerance is determined by the
 * [Domain.equalityThreshold], representing the acceptable margin of error for equality. This range is
 * `[x - Domain.equalityThreshold..x + Domain.equalityThreshold]`.
 *
 * ## Usage:
 * This matcher can be used in test assertions to verify that a `Double` value is approximately equal
 * to another `Double` value, considering a predefined margin of error. This is particularly useful for
 * floating-point numbers, where exact comparisons can be unreliable due to precision limitations.
 *
 * ### Example:
 * ```kotlin
 * val result = 1.000001
 * // Assuming Domain.equalityThreshold is set to a small value like 0.00001
 * result shouldBe beEq(1.0) // Assertion passes as 1.000001 is approximately equal to 1.0
 * ```
 * In this example, `result` is considered approximately equal to `1.0` based on the predefined `equalityThreshold`
 * in `Domain`. The matcher `beEq(1.0)` facilitates this comparison.
 *
 * @param x The [Double] value against which other `Double` values will be compared for approximate equality.
 * @return A [Matcher]<[Double]> that evaluates whether a `Double` value is approximately equal to [x] within the
 *   range defined by the [Domain.equalityThreshold].
 */
fun beEq(x: Double) = Matcher { value: Double ->
    MatcherResult(
        value eq x,
        {
            "$value should be in the vicinity of $x within a tolerance of ${
                x - Domain.equalityThreshold..x + Domain.equalityThreshold
            }"
        },
        {
            "$value should not be in the vicinity of $x within a tolerance of ${
                x - Domain.equalityThreshold..x + Domain.equalityThreshold
            }"
        }
    )
}


/**
 * Infix function for asserting approximate equality of a [Double] value with another [Double].
 *
 * This function extends the capabilities of the `Double` type to include an infix assertion for approximate equality.
 * It leverages the [beEq] matcher function to compare the caller `Double` value (`this`) with another specified
 * `Double` value ([x]), within a predefined tolerance determined by [Domain.equalityThreshold]. This assertion
 * is useful for testing floating-point values where exact equality is not practical due to precision limitations
 * of floating-point representation.
 *
 * ## Usage:
 * The `shouldBeEq` function can be used in test cases to assert that a `Double` value is approximately equal to
 * another `Double` value. It simplifies the syntax for writing such assertions, making tests more readable.
 *
 * ### Example:
 * ```kotlin
 * val actual = 1.000001
 * val expected = 1.0
 * actual shouldBeEq expected
 * // This assertion will pass if 'actual' is approximately equal to 'expected'
 * ```
 * In this example, `shouldBeEq` asserts whether `actual` is approximately equal to `expected` considering the
 * margin of error defined in `Domain.equalityThreshold`.
 *
 * @param x The `Double` value to compare with the receiver `Double` for approximate equality.
 */
infix fun Double.shouldBeEq(x: Double) = this should beEq(x)

/**
 * Infix function for asserting that a [Double] value is not approximately equal to another [Double].
 *
 * This function extends the `Double` type to include an infix assertion for non-approximate equality. It uses the
 * [beEq] matcher function to compare the caller `Double` value (`this`) with another specified `Double` value ([x]).
 * However, unlike `shouldBeEq`, this function asserts that the two values are not approximately equal, considering
 * the tolerance defined by [Domain.equalityThreshold]. This assertion is particularly useful in tests where you
 * expect floating-point values to differ beyond a certain acceptable margin of error.
 *
 * ## Usage:
 * `shouldNotBeEq` can be used in testing scenarios where it's necessary to assert that two `Double` values are
 * distinctly different within the context of floating-point precision limitations.
 *
 * ### Example:
 * ```kotlin
 * val actual = 1.0001
 * val notExpected = 1.0
 * actual shouldNotBeEq notExpected
 * // This assertion will pass if 'actual' is not approximately equal to 'notExpected'
 * ```
 * In this example, `shouldNotBeEq` checks that `actual` is not approximately equal to `notExpected`, based on the
 * predefined margin of error in `Domain.equalityThreshold`.
 *
 * @param x The `Double` value to compare with the receiver `Double` for non-approximate equality.
 */
infix fun Double.shouldNotBeEq(x: Double) = this shouldNot beEq(x)
