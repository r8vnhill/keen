/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.arb.arbRange
import cl.ravenhill.keen.arb.genetic.genes.charGene
import cl.ravenhill.keen.utils.nextChar
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.matchers.types.shouldNotHaveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.pair
import io.kotest.property.checkAll
import kotlin.random.Random

class CharGeneTest : FreeSpec({

    "A CharGene" - {
        "should have a value property that is set according to the constructor" {
            checkAll(Arb.char()) { value ->
                val gene = CharGene(value)
                gene.value shouldBe value
            }
        }

        "should have a range property that" - {
            "defaults to ' '..'z" {
                checkAll(Arb.char()) { value ->
                    val gene = CharGene(value)
                    gene.range shouldBe ' '..'z'
                }
            }

            "can be set using the constructor" {
                checkAll(Arb.char(), arbRange(Arb.char(), Arb.char())) { value, range ->
                    val gene = CharGene(value, range)
                    gene.range shouldBe range
                }
            }
        }

        "should have a filter property that" - {
            "defaults to { true }" {
                checkAll(Arb.char()) { value ->
                    val gene = CharGene(value)
                    gene.filter(value) shouldBe true
                }
            }

            "can be set using the constructor" {
                checkAll(Arb.char(), arbRange(Arb.char(), Arb.char())) { value, range ->
                    val gene = CharGene(value, range) { it in range }
                    gene.filter(value) shouldBe (value in range)
                }
            }
        }

        "can generate a random value" {
            checkAll(Arb.charGene(), Arb.long().map { Random(it) to Random(it) }) { gene, (r1, r2) ->
                Domain.random = r1
                val value = gene.generator()
                val expected = r2.nextChar(gene.range, gene.filter)
                value shouldBe expected
            }
        }

        "can be duplicated with a new value" {
            checkAll(Arb.charGene(), Arb.char()) { gene, value ->
                val duplicated = gene.duplicateWithValue(value)
                duplicated.value shouldBe value
                duplicated.range shouldBe gene.range
                duplicated.filter shouldBe gene.filter
            }
        }

        "when verifying" - {
            "should return true if the value is within the range and passes the filter" {
                checkAll(
                    Arb.charGene(range = arbRange(Arb.char(), Arb.char()), filter = { it.isLetter() })
                        .filter { it.value in it.range && it.filter(it.value) }
                ) { gene ->
                    gene.verify() shouldBe true
                }
            }

            "should return false if the value is not within the range" {
                checkAll(
                    Arb.charGene(range = arbRange(Arb.char(), Arb.char())).filter { it.value !in it.range }
                ) { gene ->
                    gene.verify() shouldBe false
                }
            }

            "should return false if the value does not pass the filter" {
                checkAll(Arb.charGene { !it.isLetter() }.filter { !it.filter(it.value) }) { gene ->
                    gene.verify() shouldBe false
                }
            }
        }

        "can be converted to a character" {
            checkAll(Arb.charGene()) { gene ->
                gene.toChar() shouldBe gene.value
            }
        }

        "can be converted to a simple string" {
            checkAll(Arb.charGene()) { gene ->
                gene.toSimpleString() shouldBe gene.value.toString()
            }
        }

        "should have an equals function that" - {
            "is reflexive" {
                checkAll(Arb.charGene()) { gene ->
                    gene shouldBe gene
                }
            }

            "is symmetric" {
                checkAll(Arb.charGene()) { gene ->
                    val copy = gene.copy()
                    gene shouldBe copy
                    copy shouldBe gene
                }
            }

            "is transitive" {
                checkAll(Arb.charGene()) { gene ->
                    val copy1 = gene.copy()
                    val copy2 = gene.copy()
                    gene shouldBe copy1
                    copy1 shouldBe copy2
                    gene shouldBe copy2
                }
            }

            "returns false for a gene with a different value" {
                checkAll(
                    Arb.pair(Arb.charGene(), Arb.charGene()).filter { (g1, g2) -> g1.value != g2.value }
                ) { (g1, g2) ->
                    g1 shouldNotBe g2
                }
            }
        }

        "should have a hashCode function that" - {
            "returns the same value for a gene with the same value" {
                checkAll(Arb.charGene()) { gene ->
                    val copy = gene.copy()
                    gene shouldHaveSameHashCodeAs copy
                }
            }

            "returns a different value for a gene with a different value" {
                checkAll(
                    Arb.pair(Arb.charGene(), Arb.charGene()).filter { (g1, g2) -> g1.value != g2.value }
                ) { (g1, g2) ->
                    g1 shouldNotHaveSameHashCodeAs g2
                }
            }
        }
    }
})
