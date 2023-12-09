/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.utils

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.doubles.shouldNotBeNaN
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.assume
import io.kotest.property.checkAll

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
})
