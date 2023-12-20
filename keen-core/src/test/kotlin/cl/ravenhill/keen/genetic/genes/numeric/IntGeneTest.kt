/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.genes.numeric

import cl.ravenhill.keen.arb.genetic.genes.intGene
import cl.ravenhill.keen.assertions.`test that a gene can duplicate itself`
import cl.ravenhill.keen.assertions.`test that a gene can generate a value`
import cl.ravenhill.keen.assertions.`test that the gene filter is set to the expected filter`
import cl.ravenhill.keen.assertions.`test that the gene range is set to the expected range`
import cl.ravenhill.keen.assertions.`test that the gene value is set to the expected value`
import cl.ravenhill.keen.utils.nextIntInRange
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class IntGeneTest : FreeSpec({

    "An Int Gene" - {
        `test that the gene value is set to the expected value`(Arb.int()) { IntGene(it) }

        `test that the gene range is set to the expected range`(
            Arb.int(), Int.MIN_VALUE..Int.MAX_VALUE, { IntGene(it) }, { v, r -> IntGene(v, r) }
        )

        `test that the gene filter is set to the expected filter`(
            Arb.int(), { IntGene(it) }, { v, f -> IntGene(v, filter = f) }
        ) { it % 2 == 0 }

        `test that a gene can generate a value`(
            Arb.int(), { IntGene(it) }, { random, range -> random.nextIntInRange(range) }
        )

        `test that a gene can duplicate itself`(Arb.int(), Arb.intGene())

        "can verify its validity when" - {
            "the value is within the range and satisfies the filter" {
                checkAll(Arb.intGene().filter {
                    it.value in it.range && it.filter(it.value)
                }) { gene ->
                    gene.verify().shouldBeTrue()
                }
            }

            "the value is outside the range" {
                checkAll(Arb.intGene().filter { it.value !in it.range }) { gene ->
                    gene.verify().shouldBeFalse()
                }
            }

            "the value does not satisfy the filter" {
                checkAll(Arb.intGene { it > 0 }, Arb.int().filterNot { it > 0 }) { gene, newValue ->
                    gene.copy(value = newValue).verify().shouldBeFalse()
                }
            }
        }

        "can be averaged" {
            checkAll(
                Arb.intGene(Arb.int(-100, 100)),
                Arb.list(Arb.intGene(Arb.int(-100, 100)), 1..10),
            ) { gene, genes ->
                val expectedValue = (genes.map { it.value } + gene.value).average().toInt()
                // +/- 1 to avoid rounding errors
                gene.average(genes).value shouldBeInRange expectedValue - 1..expectedValue + 1
            }
        }

        "can be converted to" - {
            "a Double" {
                checkAll(Arb.intGene()) { gene ->
                    gene.toDouble() shouldBe gene.value.toDouble()
                }
            }

            "an Int" {
                checkAll(Arb.intGene()) { gene ->
                    gene.toInt() shouldBe gene.value
                }
            }

            "a String" {
                checkAll(Arb.intGene()) { gene ->
                    gene.toString() shouldBe "IntGene(value=${gene.value}, range=${gene.range})"
                }
            }

            "a Detailed String" {
                checkAll(Arb.intGene()) { gene ->
                    gene.toDetailedString() shouldBe
                          "IntGene(value=${gene.value}, range=${gene.range}, filter=(${gene.filter})@${
                              System.identityHashCode(gene.filter)
                          })"
                }
            }
        }

        "should have an equals method that" - {
            "is reflexive" {
                checkAll(Arb.intGene()) { gene ->
                    gene shouldBe gene
                }
            }

            "is symmetric" {
                checkAll(Arb.intGene()) { gene ->
                    val other = gene.copy()
                    gene shouldBe other
                    other shouldBe gene
                }
            }

            "is transitive" {
                checkAll(Arb.intGene()) { gene ->
                    val other1 = gene.copy()
                    val other2 = gene.copy()
                    gene shouldBe other1
                    other1 shouldBe other2
                    gene shouldBe other2
                }
            }

            "returns false when comparing to null" {
                checkAll(Arb.intGene()) { gene ->
                    gene shouldNotBe null
                }
            }

            ""
        }
    }
})
