/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.assertions

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot

/**
 * Provides a matcher to check if a value of a comparable type falls within a specified closed range.
 *
 * This function is useful in scenarios where you need to assert that a particular value lies within a certain range. It
 * is applicable to any type that implements [Comparable], making it versatile for various data types like numbers,
 * dates, etc.
 *
 * ## Usage:
 * The `beInRange` matcher can be utilized in Kotest where you need to assert the range of a value. It compares a given
 * value against a specified range and returns a [MatcherResult] indicating whether the value falls within that range.
 *
 * ### Example:
 * ```
 * val myValue = 5
 * val range = 1..10
 * myValue should beInRange(range) // Assertion passes as 5 is within the range 1..10
 * ```
 *
 * In this example, `beInRange` is used to assert that `myValue` (5) lies within the range of 1 to 10.
 *
 * @param T The type of the value being tested. Must implement [Comparable].
 * @param range The [ClosedRange]<[T]> against which the value is tested.
 *
 * @return A [Matcher]<[T]> which tests if the value falls within the specified range.
 *
 * @see [shouldBeInRange]
 * @see [shouldNotBeInRange]
 */
fun <T> beInRange(range: ClosedRange<T>) where T : Comparable<T> = object : Matcher<T> {
    override fun test(value: T): MatcherResult {
        return MatcherResult(
            value in range,
            { "$value should be in range $range" },
            { "$value should not be in range $range" }
        )
    }
}

/**
 * An infix function for asserting that a value of a comparable type falls within a specified closed range.
 *
 * This function enhances readability and usability in test assertions, allowing you to assert whether a value
 * falls within a given range using a natural language-like syntax. It is applicable to any type that implements
 * [Comparable], making it versatile for a wide range of data types like numbers, dates, etc.
 *
 * ## Usage:
 * The `shouldBeInRange` function can be used in testing frameworks (like Kotest) for expressive and readable
 * assertions. It leverages the `beInRange` matcher to perform the range check.
 *
 * ### Example:
 * ```
 * val myValue = 5
 * val range = 1..10
 * myValue shouldBeInRange range // Assertion passes as 5 is within the range 1..10
 * ```
 *
 * In this example, `shouldBeInRange` is used to assert in a readable manner that `myValue` (5) lies within the
 * range of 1 to 10.
 *
 * @param T The type of the value being tested. Must implement [Comparable].
 * @param range The [ClosedRange]<[T]> against which the value is tested.
 *
 * This function is an infix extension function on [T], allowing for a fluent syntax in tests.
 */
infix fun <T> T.shouldBeInRange(range: ClosedRange<T>) where T : Comparable<T> = this should beInRange(range)

/**
 * An infix function for asserting that a value of a comparable type does not fall within a specified closed range.
 *
 * This function is used in test assertions to verify that a given value is outside a specified range.
 * It is applicable to any type that implements [Comparable], making it versatile for various data types
 * such as numbers, dates, and more.
 *
 * ## Usage:
 * The `shouldNotBeInRange` function can be employed in testing frameworks (like Kotest) to create clear and
 * expressive assertions. It negates the `beInRange` matcher, ensuring the value is outside the specified range.
 *
 * ### Example:
 * ```
 * val myValue = 15
 * val range = 1..10
 * myValue shouldNotBeInRange range // Assertion passes as 15 is not within the range 1..10
 * ```
 *
 * In this example, `shouldNotBeInRange` is used to assert that `myValue` (15) is not within the specified
 * range of 1 to 10, leading to a passing assertion.
 *
 * @param T The type of the value being tested. Must implement [Comparable].
 * @param range The [ClosedRange]<[T]> against which the value is tested.
 *
 * This function is an infix extension function on [T], which enables a fluent and intuitive syntax in tests.
 */
infix fun <T> T.shouldNotBeInRange(range: ClosedRange<T>) where T : Comparable<T> = this shouldNot beInRange(range)
