/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.boolGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import kotlin.random.Random

/**
 * Unit tests for the [BoolGene] class, ensuring its proper functionality in terms of data type
 * conversion, reproduction, and mutation.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class BoolGeneTest : FreeSpec({
    "A [BoolGene]" - {
        "can be converted to " - {
            "a [Boolean]" {
                BoolGene.True.toBool().shouldBeTrue()
                BoolGene.False.toBool().shouldBeFalse()
            }

            "an [Int]" {
                BoolGene.True.toInt() shouldBe 1
                BoolGene.False.toInt() shouldBe 0
            }

            "a [String]" {
                BoolGene.True.toString() shouldBe "True"
                BoolGene.False.toString() shouldBe "False"
            }
        }

        "can reproduce" {
            with(Arb) {
                checkAll(boolGene(), boolean()) { gene, new ->
                    val child = gene.withDna(new)
                    when {
                        new -> child shouldBe BoolGene.True
                        else -> child shouldBe BoolGene.False
                    }
                }
            }
        }

        "can mutate" {
            with(Arb) {
                checkAll(boolGene(), long()) { gene, seed ->
                    Core.random = Random(seed)
                    val mutated = gene.generator()
                    mutated shouldBe Random(seed).nextBoolean()
                }
            }
        }
    }
})
