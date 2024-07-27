/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.mixins

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class VerifiableTest : FreeSpec({
    "A Verifiable object" - {
        "should return true when verified" {
            val verifiable = object : Verifiable {}
            verifiable.verify() shouldBe true
        }
    }
})
