/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.collections

import cl.ravenhill.keen.arbs.datatypes.real
import cl.ravenhill.keen.util.incremental
import cl.ravenhill.keen.util.isSorted
import cl.ravenhill.keen.util.shouldEq
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeMonotonicallyIncreasing
import io.kotest.matchers.collections.shouldNotBeMonotonicallyIncreasing
import io.kotest.matchers.doubles.shouldNotBeNaN
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.list
import io.kotest.property.assume
import io.kotest.property.checkAll

/**
 * A test suite for validating various operations on arrays.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class ArraysTest : FreeSpec({
    "An array can be transformed into an incremental array" {
        checkAll(Arb.list(Arb.real(0.0..100_000.0))) { ds ->
            val array = ds.toDoubleArray()
            val copy = array.copyOf()
            array.incremental()
            assertSoftly {
                array.forEachIndexed { i, d ->
                    d shouldEq copy.sumFirst(i + 1)
                }
            }
        }
    }

    "When checking if a [DoubleArray] is sorted" - {
        "should return true if the array is empty" {
            doubleArrayOf().isSorted().shouldBeTrue()
        }

        "should return true if the array has a single element" {
            checkAll<Double> { x ->
                assume {
                    x.shouldNotBeNaN()
                }
                doubleArrayOf(x).isSorted().shouldBeTrue()
            }
        }

        "should return true if the array is sorted" {
            checkAll(Arb.list(Arb.double(), 1..50)) { ds ->
                assume {
                    ds.shouldBeMonotonicallyIncreasing()
                    ds.forEach { it.shouldNotBeNaN() }
                }
                ds.sorted().toDoubleArray().isSorted().shouldBeTrue()
            }
        }

        "should return false if the array is not sorted" {
            checkAll(Arb.list(Arb.double(), 1..50)) { ds ->
                assume { ds.shouldNotBeMonotonicallyIncreasing() }
                ds.toDoubleArray().isSorted().shouldBeFalse()
            }
        }
    }
})

/**
 * Returns the sum of the first [n] elements of this [DoubleArray].
 */
private fun DoubleArray.sumFirst(n: Int) = take(n).sum()
