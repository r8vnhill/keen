/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.exceptions.constraints

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.checkAll

class BeNaNTest : FreeSpec({

    "A BeNaN constraint" - {
        "should have a validator function that" - {
            "returns true when the value is NaN" {
                BeNaN.validator(Double.NaN).shouldBeTrue()
            }

            "returns false when the value is not NaN" {
                checkAll(Arb.double().filterNot { it.isNaN() }) {
                    BeNaN.validator(it).shouldBeFalse()
                }
            }
        }
    }
})
