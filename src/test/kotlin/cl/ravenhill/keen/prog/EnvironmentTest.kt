/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.prog

import cl.ravenhill.keen.arbs.datatypes.any
import cl.ravenhill.keen.arbs.prog.environment
import cl.ravenhill.keen.prog.terminals.Variable
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.pair
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class EnvironmentTest : FreeSpec({
    "An [Environment]" - {
        "is created with an empty memory map" {
            checkAll<String> { name ->
                val env = Environment<Any>(name)
                env.id shouldBe name
                env.memory.isEmpty().shouldBeTrue()
            }
        }

        "can add values to its memory" {
            checkAll(Arb.list(Arb.pair(Arb.int(), Arb.any())), Arb.environment<Any>()) { values, env ->
                val distinctValues = values.distinctBy { it.first }
                distinctValues.forEach { (index, value) -> env += index to value }
                env.memory.size shouldBe distinctValues.size
                distinctValues.forEach { (index, value) ->
                    env.memory shouldContain (index to value)
                }
            }
        }
    }
})
