/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.mixins

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.property.Exhaustive
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.boolean

class VerifiableTest : FreeSpec({

    "A Verifiable object that" - {
        "has no verification logic" - {
            "should return true when verified" {
                val verifiable = object : Verifiable {}
                verifiable.verify().shouldBeTrue()
            }
        }

        "has verification logic" - {
            "should return a value according to the logic" {
                checkAll(Exhaustive.boolean()) { value ->
                    val verifiable = object : Verifiable {
                        override fun verify(): Boolean = value
                    }
                    verifiable.verify() shouldBe value
                }
            }
        }
    }
})