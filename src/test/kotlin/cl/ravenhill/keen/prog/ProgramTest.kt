/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.prog

import cl.ravenhill.keen.prog.terminals.Constant
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class ProgramTest : FreeSpec({

    "A [Program]" - {
        "when created" - {
            "without explicit [children] should default to an empty list" {
                checkAll(Arb.int()) { i ->
                    Program(Constant(i)).children shouldBe emptyList()
                }
            }
        }
    }
})
