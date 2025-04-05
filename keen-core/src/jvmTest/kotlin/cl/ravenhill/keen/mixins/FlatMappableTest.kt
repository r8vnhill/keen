/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.mixins

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class FlatMappableTest : FreeSpec({
    "A FlatMappable object" - {
        "when flat-mapping" - {
            "should return the same elements as the flattened list on identity" {
                checkAll(arbFlatMappableAndFlattened()) { (flatMappable, flattened) ->
                    flatMappable.flatMap { listOf(it) } shouldBe flattened
                }
            }

            "to a constant list should return a list with the same element" {
                checkAll(
                    arbFlatMappableAndFlattened(),
                    Arb.list(Arb.int(), 1..10)
                ) { (flatMappable, flattened), constant ->
                    flatMappable.flatMap { constant } shouldBe flattened.map { constant }.flatten()
                }
            }

            "should return each value doubled when applying a double function" {
                checkAll(arbFlatMappableAndFlattened()) { (flatMappable, flattened) ->
                    flatMappable.flatMap { listOf(it * 2) } shouldBe flattened.map { it * 2 }
                }
            }
        }
    }
})

private fun arbFlatMappableAndFlattened(): Arb<Pair<FlatMappable<Int>, List<Int>>> = arbitrary {
    val size = Arb.int(0..10).bind()
    val elementsAndFlattened = Arb.list(Arb.list(Arb.int()), size..size).bind()
    val elements = elementsAndFlattened.flatten()
    object : FlatMappable<Int> {
        override fun flatten() = elements
    } to elements
}
