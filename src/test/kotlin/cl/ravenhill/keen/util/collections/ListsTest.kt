/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.util.collections

import cl.ravenhill.any
import cl.ravenhill.keen.util.duplicates
import cl.ravenhill.keen.util.get
import cl.ravenhill.keen.util.sub
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.set
import io.kotest.property.checkAll


class ListsTest : FreeSpec({
    "Subtracting a list by an integer should return a list with each element subtracted by the integer" {
        checkAll(Arb.list(Arb.double(), 0..10_000), Arb.double()) { list, d ->
            (list sub d) shouldBe list.map { it - d }
        }
    }

    "Finding duplicate elements in a list" - {
        "should return an empty map if there are no duplicates" {
            checkAll(Arb.uniqueList()) { list ->
                list.duplicates shouldBe emptyMap()
            }
        }

        "should return a map with the duplicates and their indices" {
            checkAll(Arb.list(Arb.double(), 0..10_000)) { list ->
                assertSoftly {
                    list.duplicates.forEach { (e, indices) ->
                        indices.forEach {
                            list[it] shouldBe e
                        }
                    }
                }
            }
        }
    }

    "Accessing the elements at the given indices on a list should return a list with those elements" {
        checkAll(Arb.listIndexPairs()) { (index, list) ->
            list[index] shouldBe index.map { list[it] }
        }
    }
})


/**
 * Returns an arbitrary list of unique integers within the given range.
 *
 * @param maxSize The maximum size of the list.
 *                Default is 10,000.
 * @param range The range of integers to choose from.
 *              Default is ``Int.MIN_VALUE..Int.MAX_VALUE``.
 * @return An arbitrary list of unique integers within the given range.
 * @throws IllegalArgumentException if [maxSize] is greater than the size of the given [range].
 */
private fun Arb.Companion.uniqueList(
    maxSize: Int = 10_000,
    range: IntRange = Int.MIN_VALUE..Int.MAX_VALUE,
) = arbitrary { rs ->
    val size = positiveInt(maxSize).bind()
    require(size <= range.last.toLong() - range.first) { "List size must be less than or equal to the range size" }
    val set = mutableSetOf<Int>()
    while (set.size < size) {
        set.add((range).random(rs.random))
    }
    set.toList()
}

/**
 * Generates a pair of a list and a set of random indices for that list.
 * The size of the set of indices is also randomly generated, and cannot be larger
 * than the size of the list.
 */
private fun Arb.Companion.listIndexPairs() = arbitrary {
    val list = list(any(), 1..100).bind()
    val size = int(list.indices).bind()
    val indices = set(int(list.indices), size..size).bind().toList()
    indices to list
}
