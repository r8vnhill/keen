/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.arb.genetic.arbGeneticMaterial
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class GeneticMaterialTest : FreeSpec({
    "A GeneticMaterial object" - {
        "can be flat-mapped" {
            checkAll(intGeneticMaterial()) { material ->
                val flatMapped = material.flatMap { it * 2 }
                val result = mutableListOf<Int>()
                material.flatten().forEach { result += it * 2 }
                flatMapped shouldBe result
            }
        }
    }
})

private fun intGeneticMaterial() = arbGeneticMaterial<Int, IntGene>(Arb.int(-100, 100))