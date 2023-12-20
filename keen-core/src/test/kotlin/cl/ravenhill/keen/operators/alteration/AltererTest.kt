/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration

import cl.ravenhill.keen.arb.operators.alterer
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll


class AltererTest : FreeSpec({

    "An Alterer instance" - {
        "can be concatenated with another Alterer" {
            checkAll(Arb.alterer<Nothing, NothingGene>(), Arb.alterer<Nothing, NothingGene>()) { a1, a2 ->
                (a1 + a2) shouldBe listOf(a1, a2)
            }
        }
    }
})