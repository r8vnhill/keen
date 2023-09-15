/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.genes.numerical

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
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
    }
})
