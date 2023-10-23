/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.keen.doubleChromosome
import cl.ravenhill.keen.doubleGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class DoubleChromosomeTest : FreeSpec({
    "A [DoubleChromosome]" - {
        "when creating a new one with" - {
            with(Arb) {
                "a list of genes then the chromosome should have the same genes" {
                    checkAll(list(doubleGene())) { genes ->
                        DoubleChromosome(genes).genes shouldBe genes
                    }
                }
            }
        }

        "can create a new one with the given genes" {
            with(Arb) {
                checkAll(doubleChromosome(), list(doubleGene())) { c, genes ->
                    c.withGenes(genes).genes shouldBe genes
                }
            }
        }
    }

    "A [DoubleChromosome.Factory]" - {
        "have a list of ranges that" - {
            "is empty by default" {
                DoubleChromosome.Factory().ranges.isEmpty() shouldBe true
            }
        }
    }
})
