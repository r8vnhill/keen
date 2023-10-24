/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.charGene
import cl.ravenhill.keen.arbs.charRange
import cl.ravenhill.keen.util.nextChar
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.matchers.types.shouldNotHaveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.long
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random

class CharGeneTest : FreeSpec({
    "A [CharGene]" - {
        "can be converted to    " - {
            "a [Char]" {
                with(Arb) {
                    checkAll(char()) { c ->
                        CharGene(c).toChar() shouldBe c
                    }
                }
            }

            "an [Int]" {
                with(Arb) {
                    checkAll(char()) { c ->
                        CharGene(c).toInt() shouldBe c.code
                    }
                }
            }

            "a [String]" {
                with(Arb) {
                    checkAll(char()) { c ->
                        CharGene(c).toString() shouldBe "$c"
                    }
                }
            }
        }

        "equality should" - {
            "be reflexive" {
                with(Arb) {
                    checkAll(charGene(char())) { g ->
                        g shouldBe g
                    }
                }
            }

            "be symmetric" {
                with(Arb) {
                    checkAll(char()) { c ->
                        val g1 = CharGene(c)
                        val g2 = CharGene(c)
                        g1 shouldBe g2
                        g2 shouldBe g1
                    }
                }
            }

            "be transitive" {
                with(Arb) {
                    checkAll(char()) { c ->
                        val g1 = CharGene(c)
                        val g2 = CharGene(c)
                        val g3 = CharGene(c)
                        g1 shouldBe g2
                        g2 shouldBe g3
                        g1 shouldBe g3
                    }
                }
            }
        }

        "hash code should" - {
            "be consistent with equality" {
                with(Arb) {
                    checkAll(char()) { c ->
                        val g1 = CharGene(c)
                        val g2 = CharGene(c)
                        g1 shouldBe g2
                        g1 shouldHaveSameHashCodeAs g2
                    }
                }
            }

            "be different for different genes" {
                with(Arb) {
                    checkAll(charGene(char()), charGene(char())) { g1, g2 ->
                        assume { g1 shouldNotBe g2 }
                        g1 shouldNotHaveSameHashCodeAs g2
                    }
                }
            }
        }

        "can generate a random value" {
            with(Arb) {
                checkAll(charGene(char()), long()) { gene, seed ->
                    Core.random = Random(seed)
                    gene.generator() shouldBe Random(seed).nextChar(gene.range, gene.filter)
                }
            }
        }

        "can create a copy with a different value" {
            with(Arb) {
                checkAll(charGene(char()), char()) { gene, c ->
                    val copy = gene.withDna(c)
                    copy.dna shouldBe c
                    copy.range shouldBe gene.range
                    copy.filter shouldBe gene.filter
                }
            }
        }

        "can verify if a given DNA sequence is valid when" - {
            "it is within the specified range and satisfies the filter criteria" {
                with(Arb) {
                    checkAll(charGeneWithRange(charRange())) { gene ->
                        gene.verify() shouldBe true
                    }
                }
            }

            "it is not within the specified range" {
                with(Arb) {
                    checkAll(charGeneOutOfRange(charRange(' ', 'z'))) { gene ->
                        gene.verify() shouldBe false
                    }
                }
            }

            "it does not satisfy the filter criteria" {
                with(Arb) {
                    checkAll(charGeneWithFilter { false }) { gene ->
                        gene.verify() shouldBe false
                    }
                }
            }
        }
    }
})



/**
 * Generates an [Arb] (Arbitrary) of [CharGene] that lies within the specified range arbitrary.
 *
 * @param range The arbitrary source of character ranges.
 * @return An arbitrary of CharGene with values from the provided range.
 */
private fun Arb.Companion.charGeneWithRange(range: Arb<ClosedRange<Char>>) = arbitrary {
    val r = range.bind()
    val c = char(r.start..r.endInclusive).bind()
    CharGene(c, r)
}

/**
 * Generates an [Arb] (Arbitrary) of [CharGene] that conforms to a given filter.
 *
 * @param filter A function that determines if a character should be included.
 * @return An arbitrary of CharGene that respects the filter.
 */
private fun Arb.Companion.charGeneWithFilter(filter: (Char) -> Boolean) = arbitrary {
    CharGene(char().bind(), filter = filter)
}

/**
 * Generates an [Arb] (Arbitrary) of characters that lie outside the specified character range.
 *
 * @param range The character range to exclude.
 * @return An arbitrary of characters not in the provided range.
 */
private fun Arb.Companion.charOutOfRange(range: ClosedRange<Char>) = arbitrary {
    choice(
        char(Char.MIN_VALUE..<range.start),
        char((range.endInclusive + 1)..Char.MAX_VALUE)
    ).bind()
}

/**
 * Generates an [Arb] (Arbitrary) of [CharGene] whose values are outside the specified range arbitrary.
 *
 * @param range The arbitrary source of character ranges to exclude.
 * @return An arbitrary of CharGene with values not in the provided range.
 */
private fun Arb.Companion.charGeneOutOfRange(range: Arb<ClosedRange<Char>>) = arbitrary {
    val r = range.bind()
    val c = charOutOfRange(r).bind()
    CharGene(c, r)
}
