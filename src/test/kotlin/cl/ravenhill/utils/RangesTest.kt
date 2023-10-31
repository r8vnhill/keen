/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.utils

import cl.ravenhill.orderedPair
import cl.ravenhill.orderedTriple
import cl.ravenhill.keen.arbs.real
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
 * This is a test class for [IntToInt] and [DoubleToDouble] ranges, implemented in a
 * behaviour-driven development (BDD) style.
 *
 * The test suite is divided into two main sections, each dealing with a specific type of range:
 * - An [IntToInt] range
 * - A [DoubleToDouble] range
 *
 * In each section, three main behaviours are tested:
 * 1. If the range correctly identifies a number within it.
 * 2. If the range correctly identifies a number outside of it.
 * 3. If the range can be correctly converted into its respective Kotlin standard range
 * representation.
 *
 * To ensure a comprehensive test, this class uses property-based testing.
 * This means that for each test case, multiple different inputs are automatically generated and
 * tested to ensure the function behaves as expected.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class RangesTest : FreeSpec({
    "An [IntToInt] range" - {
        "contains an integer within the range" {
            checkAll(Arb.orderedTriple(Arb.int())) { (lo, mid, hi) ->
                mid shouldBeInRange (lo to hi)
            }
        }

        "does not contain an integer outside the range" {
            checkAll(Arb.orderedTriple(Arb.int(), true)) { (lo, mid, hi) ->
                lo shouldNotBeIn (mid to hi)
                hi shouldNotBeIn (lo to mid)
            }
        }

        "can be converted to an [IntRange]" {
            checkAll(Arb.orderedPair(Arb.int())) { range ->
                with(range.toRange()) {
                    shouldBeInstanceOf<IntRange>()
                    start shouldBe range.first
                    endInclusive shouldBe range.second
                }
            }
        }
    }


    "A [DoubleToDouble] range" - {
        "contains a [Double] within the range" {
            checkAll(Arb.orderedTriple(Arb.real())) { (lo, mid, hi) ->
                mid shouldBeInRange (lo to hi)
            }
        }

        "does not contain a [Double] outside the range" {
            checkAll(Arb.orderedTriple(Arb.real(), true)) { (lo, mid, hi) ->
                lo shouldNotBeIn (mid to hi)
                hi shouldNotBeIn (lo to mid)
            }
        }

        "can be converted to a [ClosedFloatingPointRange]" {
            checkAll(Arb.orderedPair(Arb.double())) { range ->
                with(range.toRange()) {
                    shouldBeInstanceOf<ClosedFloatingPointRange<Double>>()
                    start shouldBe range.first
                    endInclusive shouldBe range.second
                }
            }
        }
    }
})

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
private infix fun Int.shouldBeInRange(range: IntToInt) = should(beInIntRange(range))

/**
 * This infix function is a check function for an [Int] value that asserts if the value should not be within the specified range.
 * It uses the [beInIntRange] Matcher.
 *
 * @param range An [IntToInt] pair representing the range outside of which the value is expected to be.
 *
 * @throws AssertionError If the value is within the specified range.
 */
private infix fun Int.shouldNotBeIn(range: IntToInt) = shouldNot(beInIntRange(range))

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
