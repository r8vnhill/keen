/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.utils.box

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class ImmutableBoxTest : FreeSpec({
    "An immutable box" - {
        "can be created with a value" {
            checkAll(Arb.int()) { value ->
                val box = ImmutableBox(value)
                box.value shouldBe value
            }
        }

        "can be folded" {
            checkAll(Arb.int()) { value ->
                val box = ImmutableBox(value)
                box.fold { it + 1 } shouldBe value + 1
            }
        }

        "can be mapped" {
            checkAll(Arb.int()) { value ->
                val box = ImmutableBox(value)
                box.map { it + 1 } shouldBe ImmutableBox(value + 1)
            }
        }

        "can be flat mapped" {
            checkAll(Arb.int()) { value ->
                val box = ImmutableBox(value)
                box.flatMap { ImmutableBox(it + 1) } shouldBe ImmutableBox(value + 1)
            }
        }

        "can be converted to a mutable box" {
            checkAll(Arb.int()) { value ->
                val box = ImmutableBox(value)
                box.toMutable().value shouldBe value
            }
        }

        "can be converted to an immutable box" {
            checkAll(Arb.int()) { value ->
                val box = ImmutableBox(value)
                box.toImmutable().value shouldBe value
            }
        }
    }
})
