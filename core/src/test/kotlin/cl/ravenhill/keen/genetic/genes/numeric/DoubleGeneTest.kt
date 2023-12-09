/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.genes.numeric

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.arb.genetic.datatypes.orderedPair
import cl.ravenhill.keen.arb.genetic.genes.doubleGene
import cl.ravenhill.keen.utils.nextDoubleInRange
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.matchers.types.shouldNotHaveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random

class DoubleGeneTest : FreeSpec({

    "A Double Gene" - {
        "can be converted to" - {
            "an Int" {
                checkAll<Double> { d ->
                    DoubleGene(d).toInt() shouldBe d.toInt()
                }
            }

            "a Double" {
                checkAll<Double> { d ->
                    DoubleGene(d).toDouble() shouldBe d
                }
            }

            "a String" {
                checkAll<Double> { d ->
                    DoubleGene(d).toString() shouldBe
                          "DoubleGene(value=$d, range=-1.7976931348623157E308..1.7976931348623157E308)"
                }
            }
        }

        "equality should" - {
            "be reflexive" {
                checkAll(Arb.doubleGene()) { g ->
                    g shouldBe g
                }
            }

            "be symmetric" {
                checkAll(Arb.double().filterNot { it.isNaN() }) { d ->
                    val g1 = DoubleGene(d)
                    val g2 = DoubleGene(d)
                    g1 shouldBe g2
                    g2 shouldBe g1
                }
            }

            "be transitive" {
                checkAll(Arb.double().filterNot { it.isNaN() }) { d ->
                    val g1 = DoubleGene(d)
                    val g2 = DoubleGene(d)
                    val g3 = DoubleGene(d)
                    g1 shouldBe g2
                    g2 shouldBe g3
                    g1 shouldBe g3
                }
            }


            "have the same hash code as another gene with the same value" {
                checkAll<Double> { d ->
                    val gene = DoubleGene(d)
                    val copy = gene.copy()
                    gene shouldHaveSameHashCodeAs copy
                }
            }

            "not have the same hash code as another gene with a different value" {
                checkAll(Arb.doubleGene(), Arb.doubleGene()) { g1, g2 ->
                    assume { g1 shouldNotBe g2 }
                    g1 shouldNotHaveSameHashCodeAs g2
                }
            }
        }

        "can generate a random value" {
            checkAll(Arb.doubleGene(), Arb.long().map { Random(it) to Random(it) }) { gene, (r1, r2) ->
                Domain.random = r1
                val expected = r2.nextDoubleInRange(gene.range)
                gene.generator() shouldBe expected
            }
        }

        "can create a copy with a different value" {
            checkAll(Arb.doubleGene(), Arb.double()) { gene, newValue ->
                val copy = gene.duplicateWithValue(newValue)
                copy.value shouldBe newValue
                copy.range shouldBe gene.range
                copy.filter shouldBe gene.filter
            }
        }

        "can verify its validity when" - {
            "the value is within the range and satisfies the filter" {
                checkAll(Arb.doubleGene().filter {
                    it.value in it.range && it.filter(it.value)
                }) { gene ->
                    gene.verify().shouldBeTrue()
                }
            }

            "the value is outside the range" {
                checkAll(Arb.doubleGene().filter { it.value !in it.range }) { gene ->
                    gene.verify().shouldBeFalse()
                }
            }

            "the value does not satisfy the filter" {
                checkAll(Arb.doubleGene { it > 0 }, Arb.double().filterNot { it > 0 }) { gene, newValue ->
                    gene.copy(value = newValue).verify().shouldBeFalse()
                }
            }
        }

        "can be averaged" - {
            checkAll(
                Arb.doubleGene(
                    range = Arb.orderedPair(Arb.double(-100.0..100.0).filterNot { it.isNaN() })
                        .filter { it.first < it.second }
                        .map { it.first..it.second }),
                Arb.list(Arb.doubleGene(), 1..10)
            ) { gene, genes ->
                val expected = (genes.sumOf { it.value } + gene.value) / (genes.size + 1)
                gene.average(genes) shouldBe gene.copy(value = expected)
            }
        }
    }
})