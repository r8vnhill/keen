/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.genes.numerical

import cl.ravenhill.keen.Core
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
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random

class IntGeneTest : FreeSpec({
    "An [IntGene]" - {
        "can be converted to" - {
            "an [Int]" {
                checkAll(Arb.int(), Arb.int(), Arb.int()) { i, i2, i3 ->
                    IntGene(i, i2..i3).toInt() shouldBe i
                }
            }

            "a [Double]" {
                checkAll<Int> { i ->
                    IntGene(i).toDouble() shouldBe i.toDouble()
                }
            }

            "a [String]" {
                checkAll<Int> { i ->
                    IntGene(i).toString() shouldBe
                        "IntGene(dna=$i, range=-2147483648..2147483647)"
                }
            }
        }

        "equality should" - {
            with(Arb) {
                "be reflexive" {
                    checkAll(intGene(int())) { g ->
                        g shouldBe g
                    }
                }

                "be symmetric" {
                    checkAll<Int> { i ->
                        val g1 = IntGene(i)
                        val g2 = IntGene(i)
                        g1 shouldBe g2
                        g2 shouldBe g1
                    }
                }

                "be transitive" {
                    checkAll<Int> {
                        val g1 = IntGene(it)
                        val g2 = IntGene(it)
                        val g3 = IntGene(it)
                        g1 shouldBe g2
                        g2 shouldBe g3
                        g1 shouldBe g3
                    }
                }
            }
        }

        "hash code should" - {
            "be consistent with equality" {
                checkAll<Int> { i ->
                    val g1 = IntGene(i)
                    val g2 = IntGene(i)
                    g1 shouldHaveSameHashCodeAs g2
                }
            }

            "be different for different genes" {
                with(Arb) {
                    checkAll(intGene(int()), intGene(int())) { g1, g2 ->
                        assume { g1 shouldNotBe g2 }
                        g1 shouldNotHaveSameHashCodeAs g2
                    }
                }
            }
        }

        "can generate a random value" {
            with(Arb) {
                checkAll(intGene(int()), long()) { gene, seed ->
                    Core.random = Random(seed)
                    val rng = Random(seed)
                    val expected = rng.nextInt(gene.range.first, gene.range.last)
                    gene.mutate() shouldBe IntGene(expected, gene.range)
                }
            }
        }

        "can create a copy with a different value" {
            with(Arb) {
                checkAll(intGene(int()), int()) { gene, i ->
                    val copy = gene.withDna(i)
                    copy.dna shouldBe i
                    copy.range.first shouldBe gene.range.first
                    copy.range.last shouldBe gene.range.last
                    copy.filter shouldBe gene.filter
                }
            }
        }

        with(Arb) {
            "can verify if a given DNA sequence is valid when" - {
                "it is within the specified range and satisfies the filter criteria" {
                    checkAll(intGeneWithRange(-100..100)) { gene ->
                        gene.verify().shouldBeTrue()
                    }
                }

                "it is not within the specified range" {
                    checkAll(intGeneOutOfRange(-100..100)) { gene ->
                        gene.verify().shouldBeFalse()
                    }
                }

                "it doesn't satisfy the filter criteria" {
                    checkAll(intGeneWithFilter { false }) { gene ->
                        gene.verify().shouldBeFalse()
                    }
                }
            }
        }
    }
})

/**
 * Creates an arbitrary generator for an `IntGene` using the provided arbitrary generator for
 * integers.
 *
 * @param i The arbitrary generator for integers which will be bound to the `IntGene`.
 * @return An arbitrary generator that produces instances of `IntGene`.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
private fun Arb.Companion.intGene(i: Arb<Int>) = arbitrary {
    IntGene(i.bind())
}

/**
 * Produces an arbitrary `IntGene` with values bounded within the specified integer range.
 *
 * The generated `IntGene` has its value restricted within the given `range`. The range
 * is also stored in the `IntGene` for reference.
 *
 * @param range The range within which the integer value for the `IntGene` is to be generated.
 * @return An arbitrary of type `IntGene` bounded within the specified range.
 */
private fun Arb.Companion.intGeneWithRange(range: IntRange) =
    arbitrary { IntGene(int(range).bind(), range) }

/**
 * Produces an arbitrary integer that lies outside the specified integer range.
 *
 * The function generates an integer value that is either below the start or above the end
 * of the given `range`.
 *
 * @param range The range to avoid while generating the integer value.
 * @return An arbitrary integer that's outside the specified range.
 */
private fun Arb.Companion.intOutOfRange(range: IntRange) = arbitrary {
    choice(
        int(Int.MIN_VALUE..<range.first),
        int(range.last + 1..Int.MAX_VALUE)
    ).bind()
}

/**
 * Produces an arbitrary `IntGene` with values that are outside the specified integer range.
 *
 * The generated `IntGene` has its value restricted to be outside the given `range`. Despite this,
 * the provided range is still stored in the `IntGene` for reference.
 *
 * @param range The range to avoid while generating the value for the `IntGene`.
 * @return An arbitrary of type `IntGene` whose value is outside the specified range.
 */
private fun Arb.Companion.intGeneOutOfRange(range: IntRange) =
    arbitrary { IntGene(intOutOfRange(range).bind(), range) }

private fun Arb.Companion.intGeneWithFilter(filter: (Int) -> Boolean) = arbitrary {
    IntGene(int().bind(), filter = filter)
}
