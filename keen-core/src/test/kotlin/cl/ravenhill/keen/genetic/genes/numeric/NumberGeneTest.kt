/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.genes.numeric

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.arb.genetic.genes.arbIntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll
import kotlin.random.Random

class NumberGeneTest : FreeSpec({

    "A NumberGene instance" - {
        "can be mutated" {
            checkAll(arbIntGene(), Arb.long().map { Random(it) to Random(it) }) { gene, (r1, r2) ->
                Domain.random = r1
                val mutated = gene.mutate()
                Domain.random = r2
                mutated shouldBe gene.duplicateWithValue(gene.generator())
            }
        }
    }
})
