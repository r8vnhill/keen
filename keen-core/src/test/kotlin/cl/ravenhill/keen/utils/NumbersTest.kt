/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.utils

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.arb.datatypes.arbNonNaNDoublePair
import cl.ravenhill.keen.assertions.should.shouldBeEq
import cl.ravenhill.keen.assertions.should.shouldNotBeEq
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.PropTestListener
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.checkAll
import kotlin.math.abs

@OptIn(ExperimentalKotest::class)
class NumbersTest : FreeSpec({
    "A Double value" - {
        "when not NaN" - {
            "should return true when calling isNotNaN()" {
                checkAll(Arb.double().filterNot { it.isNaN() }) { value ->
                    value.isNotNaN().shouldBeTrue()
                }
            }
        }

        "when NaN" - {
            "should return false when calling isNotNaN()" {
                Double.NaN.isNotNaN().shouldBeFalse()
            }
        }
    }

    "Double equality should" - {
        "be true if the difference between the values is less than the tolerance" {
            checkAll(
                PropTestConfig(listeners = listOf(ResetEqualityThresholdListener)),
                arbNonNaNDoublePair(Arb.double(-100_000_000.0..100_000_000.0)).map { (a, b) ->
                    val threshold = Arb.double().filter { it > abs(a - b) }.next()
                    Triple(a, b, threshold)
                }
            ) { (a, b, threshold) ->
                Domain.equalityThreshold = threshold
                a shouldBeEq b
            }
        }

        "be false if the difference between the values is greater than the tolerance" {
            checkAll(
                PropTestConfig(listeners = listOf(ResetEqualityThresholdListener)),
                arbNonNaNDoublePair(Arb.double(-100_000_000.0..100_000_000.0))
                    .filter { abs(it.first - it.second) > 0.0 }
                    .map { (a, b) ->
                        val threshold = Arb.double(0.0..abs(a - b)).next()
                        Triple(a, b, threshold)
                    }
            ) { (a, b, threshold) ->
                Domain.equalityThreshold = threshold
                a shouldNotBeEq b
            }
        }

        "be false if the first value is NaN" {
            checkAll<Double> { x ->
                Double.NaN shouldNotBeEq x
            }
        }

        "be false if the second value is NaN" {
            checkAll<Double> { x ->
                x shouldNotBeEq Double.NaN
            }
        }

        "be true if both values are positive infinity" {
            Double.POSITIVE_INFINITY shouldBeEq Double.POSITIVE_INFINITY
        }

        "be true if both values are negative infinity" {
            Double.NEGATIVE_INFINITY shouldBeEq Double.NEGATIVE_INFINITY
        }
    }
}) {
    object ResetEqualityThresholdListener : PropTestListener {
        override suspend fun afterTest() {
            Domain.equalityThreshold = Domain.DEFAULT_EQUALITY_THRESHOLD
        }
    }
}
