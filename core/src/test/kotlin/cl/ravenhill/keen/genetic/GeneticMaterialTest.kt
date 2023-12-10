/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.genetic.genes.Gene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe


class GeneticMaterialTest : FreeSpec({
    "A GeneticMaterial object" - {
        "when transforming genetic material" - {
            "should apply the identity function by default" {
                val geneticMaterial = object : GeneticMaterial<Int, DummyGene> {
                    override fun flatten() = listOf(1, 2, 3).map(transform)
                }
                geneticMaterial.flatten() shouldBe listOf(1, 2, 3)
            }

            "should apply the provided transformation function" {
                val geneticMaterial = object : GeneticMaterial<Int, DummyGene> {
                    override fun flatten() = listOf(1, 2, 3).map(transform)
                }
                geneticMaterial.flatten() shouldBe listOf(2, 4, 6)
            }
        }
    }
}) {
    private class DummyGene(override val value: Int) : Gene<Int, DummyGene> {
        override fun duplicateWithValue(value: Int) = DummyGene(value)
    }
}
