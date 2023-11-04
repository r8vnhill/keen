/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.prog.terminals

import cl.ravenhill.keen.prog.Environment
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class VariableTest : FreeSpec({
    "A [Variable]" - {
        "when created" - {
            "without an explicit index defaults to 0" {
                checkAll(Arb.string(), Arb.string()) { variableName, environmentName ->
                    val environment = Environment(environmentName)
                    with(Variable<Int>(variableName, environment = environment)) {
                        name shouldBe variableName
                        index shouldBe 0
                        environment shouldBe environment
                    }
                }
            }
        }
    }
})