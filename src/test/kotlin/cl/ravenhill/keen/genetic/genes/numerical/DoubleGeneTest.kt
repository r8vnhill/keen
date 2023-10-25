/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.genes.numerical

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.genetic.doubleGene
import cl.ravenhill.keen.util.nextDoubleInRange
import cl.ravenhill.keen.util.shouldEq
import cl.ravenhill.real
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.matchers.types.shouldNotHaveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random

class DoubleGeneTest : FreeSpec({
    "A [DoubleGene]" - {
        "can be converted to" - {
            "an [Int]" {
                checkAll<Double> { d ->
                    DoubleGene(d).toInt() shouldBe d.toInt()
                }
            }

            "a [Double]" {
                checkAll<Double> { d ->
                    DoubleGene(d).toDouble() shouldBe d
                }
            }

            "a [String]" {
                checkAll<Double> { d ->
                    DoubleGene(d).toString() shouldBe
                        "DoubleGene(dna=$d, range=-1.7976931348623157E308..1.7976931348623157E308)"
                }
            }
        }

        "equality should" - {
            with(Arb) {
                "be reflexive" {
                    checkAll(doubleGene(double())) { g ->
                        g shouldBe g
                    }
                }

                "be symmetric" {
                    checkAll(real()) { x ->
                        val g1 = DoubleGene(x)
                        val g2 = DoubleGene(x)
                        g1 shouldBe g2
                        g2 shouldBe g1
                    }
                }

                "be transitive" {
                    checkAll(real()) { x ->
                        val g1 = DoubleGene(x)
                        val g2 = DoubleGene(x)
                        val g3 = DoubleGene(x)
                        g1 shouldBe g2
                        g2 shouldBe g3
                        g1 shouldBe g3
                    }
                }
            }
        }

        "hash code should" - {
            "be consistent with equality" {
                checkAll<Double> { i ->
                    val g1 = DoubleGene(i)
                    val g2 = DoubleGene(i)
                    g1 shouldHaveSameHashCodeAs g2
                }
            }

            "be different for different genes" {
                with(Arb) {
                    checkAll(doubleGene(double()), doubleGene(double())) { g1, g2 ->
                        assume { g1 shouldNotBe g2 }
                        g1 shouldNotHaveSameHashCodeAs g2
                    }
                }
            }
        }

        "can generate a random value" {
            with(Arb) {
                checkAll(doubleGene(double()), long()) { gene, seed ->
                    Core.random = Random(seed)
                    val rng = Random(seed)
                    val expected = rng.nextDoubleInRange(gene.range)
                    gene.mutate() shouldBe DoubleGene(expected, gene.range)
                }
            }
        }

        "can create a copy with a different value" {
            with(Arb) {
                checkAll(doubleGene(double()), double()) { gene, i ->
                    val copy = gene.withDna(i)
                    copy.dna shouldBe i
                    copy.start shouldBe gene.start
                    copy.end shouldBe gene.end
                    copy.filter shouldBe gene.filter
                }
            }
        }

        with(Arb) {
            "can verify if a given DNA sequence is valid when" - {
                "it is within the specified range and satisfies the filter criteria" {
                    checkAll(doubleGeneWithRange(-100.0..100.0)) { gene ->
                        gene.verify().shouldBeTrue()
                    }
                }

                "it is not within the specified range" {
                    checkAll(doubleGeneOutOfRange(-100.0..100.0)) { gene ->
                        gene.verify().shouldBeFalse()
                    }
                }

                "it doesn't satisfy the filter criteria" {
                    checkAll(doubleGeneWithFilter { false }) { gene ->
                        gene.verify().shouldBeFalse()
                    }
                }
            }
        }

        "can be averaged" {
            with(Arb) {
                checkAll(
                    doubleGene(real(-100.0..100.0)),
                    list(doubleGene(real(-100.0..100.0)), 1..100)
                ) { gene, genes ->
                    val expected = (genes + gene).map { it.dna }.average()
                    gene.average(genes).dna shouldEq expected
                }
            }
        }
    }
})

/**
 * Creates an arbitrary gene instance containing a double value within the specified range.
 *
 * @param range A closed floating-point range specifying the inclusive boundaries for the generated double values.
 * @return An arbitrary gene instance with a double value that lies within the boundaries of the specified range.
 */
private fun Arb.Companion.doubleGeneWithRange(range: ClosedFloatingPointRange<Double>) =
    arbitrary { DoubleGene(real(range).bind(), range) }

/**
 * Produces an arbitrary double value that lies outside the specified range.
 *
 * This function returns a double value that is either less than the start or greater than the end inclusive value of the specified range.
 *
 * @param range A closed floating-point range specifying the boundaries to exclude for the generated double values.
 * @return An arbitrary double value that lies outside the boundaries of the specified range.
 */
private fun Arb.Companion.doubleOutOfRange(range: ClosedFloatingPointRange<Double>) = arbitrary {
    choice(
        real(-Double.MAX_VALUE..range.start - Double.MIN_VALUE),
        real(range.endInclusive + 1..Double.MAX_VALUE)
    ).bind()
}

/**
 * Creates an arbitrary gene instance containing a double value that lies outside the specified range.
 *
 * @param range A closed floating-point range specifying the boundaries to exclude for the generated double values within the gene.
 * @return An arbitrary gene instance with a double value that lies outside the boundaries of the specified range.
 */
private fun Arb.Companion.doubleGeneOutOfRange(range: ClosedFloatingPointRange<Double>) =
    arbitrary { DoubleGene(doubleOutOfRange(range).bind(), range) }

/**
 * Creates an arbitrary gene instance containing a double value, which can be verified using the provided filter function.
 *
 * The filter does not guarantee that the generated value meets the filter criteria; it merely provides a way to verify the generated value against some condition.
 *
 * @param filter A function to verify the generated double value against a condition.
 * @return An arbitrary gene instance with a double value.
 */
private fun Arb.Companion.doubleGeneWithFilter(filter: (Double) -> Boolean) = arbitrary {
    DoubleGene(double().bind(), filter = filter)
}

