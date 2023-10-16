/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.boolGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class BoolChromosomeTest : FreeSpec({
    "A [BoolChromosome]" - {
        "can be created with a list of genes" {
            with(Arb) {
                checkAll(list(boolGene())) { genes ->
                    BoolChromosome(genes).genes shouldBe genes
                }
            }
        }
    }
})
