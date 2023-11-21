/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.prog

import cl.ravenhill.keen.arbs.datatypes.list
import cl.ravenhill.keen.arbs.prog.environment
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class ReducibleTest : FreeSpec({
    "A Reducible" - {

        fun createReducible(args: List<Int> = emptyList()) = object : Reducible<Int> {
            override fun invoke(environment: Environment<Int>, args: List<Int>) = args.sum()

            override val arity = args.size

        }

        "should have a contents property that" - {
            "returns the same object" {
                val reducible = createReducible()
                reducible.contents shouldBeSameInstanceAs reducible
            }
        }

        "can be reduced with" - {
            "a list of arguments and an environment" {
                checkAll(Arb.list(Arb.int()), Arb.environment<Int>()) { value, environment ->
                    val reducible = createReducible(value)
                    reducible(environment, value) shouldBe value.sum()
                }
            }

            "varargs and an environment" {
                checkAll(Arb.int(), Arb.int(), Arb.int(), Arb.environment<Int>()) { a, b, c, environment ->
                    val reducible = createReducible(listOf(a, b, c))
                    reducible(environment, a, b, c) shouldBe (a + b + c)
                }
            }
        }
    }
})
