/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.utils

import cl.ravenhill.keen.arbs.datatypes.real
import cl.ravenhill.keen.arbs.datatypes.orderedPair
import cl.ravenhill.keen.arbs.datatypes.orderedTriple
import cl.ravenhill.keen.util.DoubleToDouble
import cl.ravenhill.keen.util.IntToInt
import cl.ravenhill.keen.util.contains
import cl.ravenhill.keen.util.toRange
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

/**
 * This function returns a Matcher object that checks if an Int value is within the specified range.
 *
 * @param range An [IntToInt] pair representing the range within which the value is expected to be.
 *
 * @return A [Matcher] that checks if an [Int] value is within the given range.
 */
private fun beInIntRange(range: IntToInt) = Matcher { value: Int ->
    MatcherResult(
        value in range,
        { "$value should be in $range" },
        { "$value should not be in $range" }
    )
}

/**
 * This infix function is a check function for an [Int] value that asserts if the value should be within the specified range.
 * It uses the [beInIntRange] Matcher.
 *
 * @param range An [IntToInt] pair representing the range within which the value is expected to be.
 *
 * @throws AssertionError If the value is not within the specified range.
 */
infix fun Int.shouldBeInRange(range: IntToInt) = should(beInIntRange(range))

/**
 * This infix function is a check function for an [Int] value that asserts if the value should not be within the specified range.
 * It uses the [beInIntRange] Matcher.
 *
 * @param range An [IntToInt] pair representing the range outside of which the value is expected to be.
 *
 * @throws AssertionError If the value is within the specified range.
 */
infix fun Int.shouldNotBeIn(range: IntToInt) = shouldNot(beInIntRange(range))

/**
 * This function returns a Matcher object that checks if a Double value is within the specified
 * range.
 *
 * @param range A [DoubleToDouble] pair representing the range within which the value is expected
 * to be.
 * @return A [Matcher] that checks if a [Double] value is within the given range.
 */
private fun beInDoubleRange(range: DoubleToDouble) = Matcher { value: Double ->
    MatcherResult(
        value in range,
        { "$value should be in $range" },
        { "$value should not be in $range" }
    )
}

/**
 * This infix function is a check function for a [Double] value that asserts if the value should be
 * within the specified range.
 * It uses the [beInIntRange] Matcher.
 *
 * @param range A [DoubleToDouble] pair representing the range within which the value is expected
 * to be.
 * @throws AssertionError if the value is not within the specified range.
 */
private infix fun Double.shouldBeInRange(range: DoubleToDouble) = should(beInDoubleRange(range))

/**
 * This infix function is a check function for a [Double] value that asserts if the value should
 * not be within the specified range.
 * It uses the [beInIntRange] Matcher.
 *
 * @param range A [DoubleToDouble] pair representing the range outside which the value is expected
 * to be.
 * @throws AssertionError if the value is within the specified range.
 */
private infix fun Double.shouldNotBeIn(range: DoubleToDouble) = shouldNot(beInDoubleRange(range))
