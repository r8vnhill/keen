/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class NothingChromosomeTest : FreeSpec({
    "A [NothingChromosome]" - {
        "can be created with a list of genes" {
            with(Arb) {
                checkAll(list(constant(NothingGene))) { genes ->
                    NothingChromosome(genes).genes shouldBe genes
                }
            }
        }
    }
})
