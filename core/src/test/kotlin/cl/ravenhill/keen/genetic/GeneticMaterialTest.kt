/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.arb.genetic.geneticMaterial
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class GeneticMaterialTest : FreeSpec({
    "A GeneticMaterial object" - {
        "can be flat-mapped" {
            checkAll(Arb.geneticMaterial<Int, IntGene>(Arb.int(-100, 100))) { material ->
                val flatMapped = material.flatMap { it * 2 }
                flatMapped shouldBe material.flatten().map { it * 2 }
            }
        }
    }
})
