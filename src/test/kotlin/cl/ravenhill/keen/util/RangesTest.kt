/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util

import cl.ravenhill.keen.arbs.datatypes.orderedPair
import cl.ravenhill.keen.arbs.datatypes.orderedTriple
import cl.ravenhill.keen.arbs.datatypes.real
import cl.ravenhill.keen.shouldBeInRange
import cl.ravenhill.keen.shouldNotBeInRange
import cl.ravenhill.keen.shouldNotBeIn
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
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
                mid shouldBeInRange lo..hi
            }
        }

        "does not contain a [Double] outside the range" {
            checkAll(Arb.orderedTriple(Arb.real(), true)) { (lo, mid, hi) ->
                lo shouldNotBeInRange mid..hi
                hi shouldNotBeInRange lo..mid
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