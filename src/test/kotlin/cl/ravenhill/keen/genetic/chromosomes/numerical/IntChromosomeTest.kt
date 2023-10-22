/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.keen.intChromosome
import cl.ravenhill.keen.intGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class IntChromosomeTest : FreeSpec({
    "A [IntChromosome]" - {
        "when creating a new one with" - {
            with(Arb) {
                "a list of genes then the chromosome should have the same genes" {
                    checkAll(list(intGene())) { genes ->
                        IntChromosome(genes).genes shouldBe genes
                    }
                }
            }
        }
    }

    "can create a new one with the given genes" {
        with(Arb) {
            checkAll(intChromosome(), list(intGene())) { c, genes ->
                c.withGenes(genes).genes shouldBe genes
            }
        }
    }
})
