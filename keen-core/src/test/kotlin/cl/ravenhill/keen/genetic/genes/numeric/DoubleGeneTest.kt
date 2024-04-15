/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.genes.numeric

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ResetDomainListener
import cl.ravenhill.keen.ToStringMode
import cl.ravenhill.keen.arb.datatypes.arbNonNaNDouble
import cl.ravenhill.keen.arb.datatypes.arbOrderedPair
import cl.ravenhill.keen.arb.genetic.genes.arbDoubleGene
import cl.ravenhill.keen.assertions.*
import cl.ravenhill.keen.utils.nextDoubleInRange
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.matchers.types.shouldNotHaveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.*
import io.kotest.property.assume
import io.kotest.property.checkAll

@OptIn(ExperimentalKotest::class)
class DoubleGeneTest : FreeSpec({

    "A Double Gene" - {
        `test that the gene value is set to the expected value`(Arb.double()) { DoubleGene(it) }

        `test that the gene range is set to the expected range`(
            Arb.double(),
            -Double.MAX_VALUE..Double.MAX_VALUE,
            { DoubleGene(it) },
            { v, r -> DoubleGene(v, r) }
        )

        `test that the gene filter is set to the expected filter`(
            Arb.double(),
            { DoubleGene(it) },
            { v, f -> DoubleGene(v, filter = f) }
        ) {
            it % 2 == 0.0
        }

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
                checkAll(PropTestConfig(listeners = listOf(ResetDomainListener)), Arb.double()) { d ->
                    Domain.toStringMode = ToStringMode.DEFAULT
                    DoubleGene(d).toString() shouldBe
                            "DoubleGene(value=$d, range=-1.7976931348623157E308..1.7976931348623157E308)"
                }
            }

            "a Detailed String" {
                checkAll(PropTestConfig(listeners = listOf(ResetDomainListener)), Arb.double()) { d ->
                    Domain.toStringMode = ToStringMode.DETAILED
                    DoubleGene(d).toString() shouldBe
                            "DoubleGene(" +
                            "value=$d, " +
                            "range=-1.7976931348623157E308..1.7976931348623157E308, " +
                            "filter=(kotlin.Double) -> kotlin.Boolean)"
                }
            }

            "a Simple String" {
                checkAll(PropTestConfig(listeners = listOf(ResetDomainListener)), Arb.double()) { d ->
                    Domain.toStringMode = ToStringMode.SIMPLE
                    DoubleGene(d).toString() shouldBe d.toString()
                }
            }
        }

        "equality should" - {
            "be reflexive" {
                checkAll(arbDoubleGene()) { g ->
                    g shouldBe g
                }
            }

            "be symmetric" {
                checkAll(arbNonNaNDouble()) { d ->
                    val g1 = DoubleGene(d)
                    val g2 = DoubleGene(d)
                    g1 shouldBe g2
                    g2 shouldBe g1
                }
            }

            "be transitive" {
                checkAll(arbNonNaNDouble()) { d ->
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
                checkAll(arbDoubleGene(), arbDoubleGene()) { g1, g2 ->
                    assume { g1 shouldNotBe g2 }
                    g1 shouldNotHaveSameHashCodeAs g2
                }
            }
        }

        `test that a gene can generate a value`(
            Arb.double(),
            { DoubleGene(it) },
            { random, range -> random.nextDoubleInRange(range) }
        )

        `test that a gene can duplicate itself`(Arb.double(), arbDoubleGene())

        "can verify its validity when" - {
            "the value is within the range and satisfies the filter" {
                checkAll(
                    arbDoubleGene()
                        .filter { it.value in it.range && it.filter(it.value) }
                ) { gene ->
                    gene.verify().shouldBeTrue()
                }
            }

            "the value is outside the range" {
                checkAll(
                    arbDoubleGene()
                        .filter { it.value !in it.range }
                ) { gene ->
                    gene.verify().shouldBeFalse()
                }
            }

            "the value does not satisfy the filter" {
                checkAll(arbDoubleGene { it > 0 }, Arb.double().filterNot { it > 0 }) { gene, newValue ->
                    gene.copy(value = newValue).verify().shouldBeFalse()
                }
            }
        }

        "can be averaged" - {
            checkAll(
                arbDoubleGene(
                    range = arbOrderedPair(Arb.double(-100.0..100.0).filterNot { it.isNaN() })
                        .filter { it.first < it.second }
                        .map { it.first..it.second }),
                Arb.list(arbDoubleGene(), 1..10)
            ) { gene, genes ->
                val expected = (genes.sumOf { it.value } + gene.value) / (genes.size + 1)
                gene.average(genes) shouldBe gene.copy(value = expected)
            }
        }
    }
})