package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.arb.genetic.genes.booleanGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll
import kotlin.random.Random

class BooleanGeneTest : FreeSpec({

    "A Boolean Gene" - {
        "should be able to generate random values" {
            checkAll(Arb.booleanGene(), Arb.long().map { Random(it) to Random(it) }) { gene, (r1, r2) ->
                Domain.random = r1
                val randomValue = gene.generator()
                randomValue shouldBe r2.nextBoolean()
            }
        }

        "should be able to duplicate with a specific value" {
            checkAll(Arb.booleanGene(), Arb.boolean()) { gene, value ->
                val duplicated = gene.duplicateWithValue(value)
                duplicated.value shouldBe value
            }
        }

        "can be converted to a boolean when" - {
            "it represents true" {
                BooleanGene.True.toBoolean() shouldBe true
            }

            "it represents false" {
                BooleanGene.False.toBoolean() shouldBe false
            }
        }

        "can be converted to an integer when" - {
            "it represents true" {
                BooleanGene.True.toInt() shouldBe 1
            }

            "it represents false" {
                BooleanGene.False.toInt() shouldBe 0
            }
        }

        "can be converted to a double when" - {
            "it represents true" {
                BooleanGene.True.toDouble() shouldBe 1.0
            }

            "it represents false" {
                BooleanGene.False.toDouble() shouldBe 0.0
            }
        }
    }
})