/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.prog

import cl.ravenhill.keen.arbs.datatypes.list
import cl.ravenhill.keen.arbs.prog.environment
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class ReduceableTest : FreeSpec({
    "A [Reduceable]" - {
        "can be reduced" {
            checkAll(Arb.list(Arb.int()), Arb.environment<Int>()) { value, environment ->
                val reduceable = object : Reduceable<Int> {
                    override fun invoke(environment: Environment<Int>, args: List<Int>) = args.sum()

                    override val arity = value.size
                }
                reduceable(environment, value) shouldBe value.sum()
            }
        }
    }
})