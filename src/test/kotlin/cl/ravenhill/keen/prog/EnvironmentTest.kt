/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.prog

import cl.ravenhill.keen.arbs.prog.environment
import cl.ravenhill.keen.prog.terminals.Variable
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class EnvironmentTest : FreeSpec({
    "An [Environment]" - {
        "is created with an empty map of variables" {
            checkAll<String> { name ->
                val env = Environment(name)
                env.id shouldBe name
                env.variables.isEmpty().shouldBeTrue()
            }
        }

        "can add variables" {
            checkAll(Arb.list(Arb.string()), Arb.environment()) { variableNames, env ->
                variableNames.forEachIndexed { index, s -> env += Variable<Int>(s, index, env) }
                env.variables.size shouldBe variableNames.size
                variableNames.forEachIndexed { index, s ->
                    env.variables shouldContain (index to Variable<Int>(s, index, env))
                }
            }
        }
    }
})
