/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.arbs.genetic.geneticMaterial
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll


class MutatorTest : FreeSpec({
    "A [Mutator]" - {}

    "A [MutatorResult]" - {
        "when created" - {
            "without mutations then the number of mutations should be 0" {
                checkAll(Arb.geneticMaterial()) { material ->
                    val result = MutatorResult(material)
                    result.mutated shouldBe material
                    result.mutations shouldBe 0
                }
            }
        }
    }
})

