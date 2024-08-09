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

class MutableBoxTest : FreeSpec({
    "A mutable box" - {
        "can be created with a value" {
            checkAll(Arb.int()) { value ->
                val box = MutableBox(value)
                box.value shouldBe value
            }
        }

        "can be folded" {
            checkAll(Arb.int()) { value ->
                val box = MutableBox(value)
                box.fold { it + 1 } shouldBe value + 1
            }
        }

        "can be mapped" {
            checkAll(Arb.int()) { value ->
                val box = MutableBox(value)
                box.map { it + 1 } shouldBe MutableBox(value + 1)
            }
        }

        "can be flat mapped" {
            checkAll(Arb.int()) { value ->
                val box = MutableBox(value)
                box.flatMap { MutableBox(it + 1) } shouldBe MutableBox(value + 1)
            }
        }

        "can be converted to a mutable box" {
            checkAll(Arb.int()) { value ->
                val box = MutableBox(value)
                box.toMutable().value shouldBe value
            }
        }

        "can be converted to an immutable box" {
            checkAll(Arb.int()) { value ->
                val box = MutableBox(value)
                box.toImmutable().value shouldBe value
            }
        }
    }
})
