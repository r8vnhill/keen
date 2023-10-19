/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.charChromosome
import cl.ravenhill.keen.charGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class CharChromosomeTest : FreeSpec({
    "A [CharChromosome]" - {
        "when creating a new one with" - {
            with(Arb) {
                "a list of genes then the chromosome should have the same genes" {
                    checkAll(list(charGene(char()))) { genes ->
                        CharChromosome(genes).genes shouldBe genes
                    }
                }
            }
        }

        "can create a new one with the given genes" {
            with(Arb) {
                checkAll(charChromosome(), list(charGene())) { c, genes ->
                    c.withGenes(genes).genes shouldBe genes
                }
            }
        }

        "can be converted to a [String]" {
            with(Arb) {
                checkAll(charChromosome()) { c ->
                    c.toSimpleString() shouldBe c.genes.joinToString("") { it.toChar().toString() }
                }
            }
        }
    }

    "A chromosome [Factory]" - {
        TODO("Add tests for the chromosome factory")
    }
})
