/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package keen.repr

import cl.ravenhill.SimpleFeature
import io.kotest.assertions.fail
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.Shrinker
import io.kotest.property.arbitrary.IntShrinker
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.withEdgecases
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

/**
 * Generates an arbitrary `SimpleFeature` instance for property-based testing.
 *
 * @param range The range of integers to generate for the `SimpleFeature` values. Defaults to
 *   `Int.MIN_VALUE..Int.MAX_VALUE`.
 * @return An `Arb<SimpleFeature>` generator.
 */
fun arbSimpleFeature(range: IntRange = Int.MIN_VALUE..Int.MAX_VALUE) =
    arbitrary(SimpleFeatureShrinker(range)) {
        Arb.int(range)
            .withEdgecases(range.first, -1, 0, 1, range.last)
            .filter { it in range }
            .map { SimpleFeature(it) }
            .bind()
    }

/**
 * A custom shrinker for `SimpleFeature` instances.
 *
 * @param range The range of values that can be generated and shrunk for `SimpleFeature` instances.
 */
class SimpleFeatureShrinker(private val range: IntRange) : Shrinker<SimpleFeature> {
    override fun shrink(value: SimpleFeature) =
        IntShrinker(range).shrink(value.value).map { SimpleFeature(it) }
}
