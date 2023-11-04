/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.prog.terminals

import cl.ravenhill.keen.arbs.datatypes.any
import cl.ravenhill.keen.arbs.datatypes.list
import cl.ravenhill.keen.arbs.prog.environment
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll

class ConstantTest : FreeSpec({

    "A [Constant]" - {
        "should have 0 arity" {
            checkAll(Arb.any()) {
                Constant(it).arity shouldBe 0
            }
        }

        "should return its value when invoked" - {
            "with an environment and a list of arguments" {
                checkAll(Arb.any(), Arb.list(Arb.any()), Arb.environment<Any>()) { a, args, env ->
                    Constant(a).invoke(env, args) shouldBe a
                }
            }
        }

        "should be able to create a new instance with the same value" {
            checkAll(Arb.any()) { a ->
                val constant = Constant(a)
                constant.create() shouldBe constant
            }
        }
    }
})
