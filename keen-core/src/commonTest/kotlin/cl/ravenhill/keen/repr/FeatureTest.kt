/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.repr

import cl.ravenhill.SimpleFeature
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class FeatureTest : FreeSpec({

    "A Feature" - {
        "can be transformed to a list" {
            checkAll(Arb.int()) { value ->
                val feature = SimpleFeature(value)
                feature.toList() shouldBe listOf(value)
            }
        }

        "when flat-mapped" - {
            "returns a new feature with the same value on identity" {
                checkAll(Arb.int()) { value ->
                    val feature = SimpleFeature(value)
                    feature.flatMap { SimpleFeature(it) } shouldBe feature
                }
            }

            "returns a new feature with the transformed value" {
                checkAll(Arb.int()) { value ->
                    val feature = SimpleFeature(value)
                    val transformed = feature.flatMap { SimpleFeature(it * 2) }
                    transformed.toList() shouldBe listOf(value * 2)
                }
            }

            "when chaining operations, returns a feature with the final transformed value" {
                checkAll(Arb.int()) { value ->
                    val feature = SimpleFeature(value)
                    // Define a composition of functions
                    val operation1: (Int) -> Int = { it * 2 }
                    val operation2: (Int) -> Int = { it + 3 }
                    // Apply the flatMap with a composed function
                    val result = feature
                        .flatMap { SimpleFeature(operation1(it)) }
                        .flatMap { SimpleFeature(operation2(it)) }
                    // Expected result
                    val expected = SimpleFeature(operation2(operation1(value)))
                    result.toList() shouldBe expected.toList()
                }
            }
        }
    }
})
