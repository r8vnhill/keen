/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.genetic.TestGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class GeneTest : FreeSpec({
    "A [Gene]" - {
        "can be created with a DNA value" {
            checkAll<Int> { dna ->
                val gene = TestGene(dna)
                gene.dna shouldBe dna
            }
        }

        "can generate new dna" {
            checkAll<Int> { dna ->
                val gene = TestGene(dna)
                gene.generator() shouldBe dna
            }
        }

        "can be mutated" {
            checkAll<Int> { dna ->
                val gene = TestGene(dna)
                gene.mutate().dna shouldBe dna
            }
        }

        "can be flattened" {
            checkAll<Int> { dna ->
                val gene = TestGene(dna)
                gene.flatten() shouldBe listOf(dna)
            }
        }
    }
})

