package cl.ravenhill.keen.genetics.genes

import cl.ravenhill.utils.arbRandomPair
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll

class BooleanGeneTest : FreeSpec({
    "A BooleanGene" - {
        "when created" - {
            "should correctly initialize from Boolean values" - {
                withData(
                    nameFn = { "initializing with `$it` should result in $it gene" },
                    true to BooleanGene.True,
                    false to BooleanGene.False
                ) { (input, expectedGene) ->
                    BooleanGene.pure(input)
                        .shouldBe(expectedGene)
                        .value shouldBe input
                    BooleanGene.from(input)
                        .shouldBe(expectedGene)
                        .value shouldBe input
                }
            }

            "with an integer value different from 0 should initialize to a True gene" {
                checkAll(Arb.int().filter { it != 0 }) { value ->
                    BooleanGene.fromInt(value)
                        .shouldBe(BooleanGene.True)
                        .value shouldBe true
                }
            }
        }

        "when converted to an integer" - {
            "should return 1 for True genes" {
                BooleanGene.True.toInt() shouldBe 1
            }

            "should return 0 for False genes" {
                BooleanGene.False.toInt() shouldBe 0
            }
        }

        "when copied with a different value" - {
            "should create a new gene with the specified value" {
                checkAll(arbBooleanGene(), Arb.boolean()) { gene, value ->
                    val copy = gene.copyWithValue(value)
                    copy shouldBe BooleanGene.from(value)
                }
            }
        }

        "when generating random instances" - {
            "should create a gene using the provided generator" {
                checkAll(arbBooleanGene(), arbRandomPair()) { gene, (r1, r2) ->
                    gene.generator(r1) shouldBe r2.nextBoolean()
                }
            }
        }
    }
})

/**
 * Generates a random `Arb<BooleanGene>` instance, leveraging the `Arb.boolean` generator.
 *
 * This method maps Boolean values to corresponding `BooleanGene` instances using the `BooleanGene.from` function.
 *
 * @return An `Arb<BooleanGene>` instance that produces random `BooleanGene` values.
 */
private fun arbBooleanGene(): Arb<BooleanGene> = Arb.boolean().map { BooleanGene.from(it) }
