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
        "should apply the flat map operation to its elements" {
            checkAll(arbFlatMappableAndFlattened()) { (flatMappable, flattened) ->
                val flatMapped = flatMappable.flatMap { listOf(it, it * 10) }
                val expected = flattened.flatMap { listOf(it, it * 10) }
                flatMapped shouldBe expected
            }
        }
    }
})

fun arbFlatMappableAndFlattened(): Arb<Pair<FlatMappable<Int>, List<Int>>> = arbitrary {
    val size = Arb.int(0..10).bind()
    val flattened = mutableListOf<Int>()
    val elements = mutableListOf<List<Int>>()
    for (i in 0..<size) {
        val list = Arb.list(Arb.int(), 0..10).bind()
        elements.add(list)
        flattened.addAll(list)
    }
    object : FlatMappable<Int> {
        override fun flatten(): List<Int> = flattened
    } to flattened
}
