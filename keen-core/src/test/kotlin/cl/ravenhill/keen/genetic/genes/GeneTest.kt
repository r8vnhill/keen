/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.arb.genetic.genes.DummyGene
import cl.ravenhill.keen.arb.genetic.genes.doubleGene
import cl.ravenhill.keen.arb.genetic.genes.gene
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import kotlin.random.Random

class GeneTest : FreeSpec({

    "A Gene" - {
        "should have a generator function that" - {
            "should generate a valid gene" {
                checkAll(Arb.gene(isValid = Arb.constant(true))) { gene ->
                    gene.verify().shouldBeTrue()
                }
            }
        }

        "can be converted to a" - {
            "simple string" {
                checkAll(Arb.gene()) { gene ->
                    val dummyGene = DummyGene(gene.generator(), gene.isValid)
                    dummyGene.toSimpleString() shouldBe dummyGene.value.toString()
                }
            }

            "detailed string" {
                checkAll(Arb.gene()) { gene ->
                    val dummyGene = DummyGene(gene.generator(), gene.isValid)
                    dummyGene.toDetailedString() shouldBe "DummyGene(value=${dummyGene.value})"
                }
            }
        }

        "can be duplicated with a new value" {
            checkAll(Arb.gene()) { gene ->
                val newValue = Random.nextInt()
                val newGene = gene.duplicateWithValue(newValue)
                newGene.value shouldBe newValue
                newGene.isValid shouldBe gene.isValid
            }
        }

        "can be mutated" {
            checkAll(Arb.gene(), Arb.long()) { gene, seed ->
                Domain.random = Random(seed)
                val mutatedGene = gene.mutate()
                Domain.random = Random(seed)
                mutatedGene.value shouldBe gene.generator()
            }
        }
    }
})
