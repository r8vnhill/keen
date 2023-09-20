/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.genes.numerical

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.matchers.types.shouldNotHaveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.assume
import io.kotest.property.checkAll

class IntGeneTest : FreeSpec({
    "An [IntGene]" - {
        "can be converted to" - {
            "an [Int]" {
                checkAll<Int> { i ->
                    IntGene(i).toInt() shouldBe i
                }
            }

            "a [Double]" {
                checkAll<Int> { i ->
                    IntGene(i).toDouble() shouldBe i.toDouble()
                }
            }

            "a [String]" {
                checkAll<Int> { i ->
                    IntGene(i).toString() shouldBe "$i"
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
