/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.util

import cl.ravenhill.enforcer.UnfulfilledRequirementException
import cl.ravenhill.keen.any
import cl.ravenhill.keen.random
import cl.ravenhill.keen.shouldBeOfClass
import cl.ravenhill.keen.unfulfilledConstraint
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.set
import io.kotest.property.assume
import io.kotest.property.checkAll


/**
 * Returns the sum of the first [n] elements of this [DoubleArray].
 */
private fun DoubleArray.sumFirst(n: Int) = this.take(n).sum()

// region : -== GENERATORS ==-
// region : -== COLLECTIONS ==-
// region : -== MUTABLE COLLECTIONS ==-
// region : -== LIST ==-
/**
 * Generates an [Arb] for [ArrayList] objects with elements generated by [gen].
 * The size of the list is randomly generated within the range specified by [range].
 *
 * @param gen the generator for the elements in the list.
 * @param range the range of sizes for the list (default is 0..100).
 * @return an [Arb] for [ArrayList] objects with elements generated by [gen].
 */
private fun <E> Arb.Companion.arrayList(gen: Arb<E>, range: IntRange = 0..100) = arbitrary {
    list(gen, range).bind().toMutableList()
}

/**
 * Generates an [Arb] for [ArrayDeque] objects containing elements generated by [gen].
 * The size of the deque is randomly generated within the range specified by [range].
 *
 * @param gen the generator for the elements of the deque.
 * @param range the range of sizes for the deque (default is 0..100).
 * @return an [Arb] for [ArrayDeque] objects containing elements generated by [gen].
 */
private fun <E> Arb.Companion.arrayDeque(gen: Arb<E>, range: IntRange = 0..100) = arbitrary {
    list(gen, range).bind().toCollection(ArrayDeque())
}

/**
 * Generates an [Arb] for [MutableList] objects with elements generated by [gen].
 * The size of the list is randomly generated within the range specified by [range].
 * The type of the generated list is either [ArrayList] or [ArrayDeque].
 *
 * @param gen the generator for the elements in the list.
 * @param range the range of sizes for the list (default is 0..100).
 * @return an [Arb] for [MutableList] objects with elements generated by [gen].
 */
private fun <E> Arb.Companion.mutableList(gen: Arb<E>, range: IntRange = 0..100) = arbitrary {
    choice(arrayList(gen, range), arrayDeque(gen, range)).bind()
}

/**
 * Returns an arbitrary that generates a pair of a mutable list and an index number.
 *
 * @param gen the arbitrary for the elements in the list.
 * @param range the range for the size of the list.
 * @return an [Arb] that generates a pair of a mutable list and an index number.
 */
private fun <E> Arb.Companion.mutableListAndIndex(
    gen: Arb<E>,
    range: IntRange = 0..100
) = arbitrary {
    val list = mutableList(gen, range).bind()
    val number = int(0..list.size).bind()
    list to number
}
// endregion LIST

// region : -== SET ==-
/**
 * Generates an [Arb] for [LinkedHashSet] objects with elements generated by [gen].
 * The size of the set is randomly generated within the range specified by [range].
 *
 * @param gen the generator for the elements in the set.
 * @param range the range of sizes for the set (default is 0..100).
 * @return an [Arb] for [LinkedHashSet] objects with elements generated by [gen].
 */
private fun <E> Arb.Companion.mutableSet(gen: Arb<E>, range: IntRange = 0..100) = arbitrary {
    set(gen, range).bind().toMutableSet()
}
// endregion SET

/**
 * Generates an [Arb] for [MutableCollection] objects with elements generated by [gen].
 * The size of the collection is randomly generated within the range specified by [range].
 * The type of the generated collection is either [MutableList] or [MutableSet].
 *
 * @param gen the generator for the elements in the collection.
 * @param range the range of sizes for the collection (default is 0..100).
 * @return an [Arb] for [MutableCollection] objects with elements generated by [gen].
 */
private fun <E> Arb.Companion.mutableCollection(gen: Arb<E>, range: IntRange = 0..100) = arbitrary {
    choice(mutableList(gen, range), mutableSet(gen, range)).bind()
}
// endregion MUTABLE COLLECTIONS

/**
 * Returns an arbitrary list of unique integers within the given range.
 *
 * @param maxSize The maximum size of the list. Default is 10,000.
 * @param range The range of integers to choose from. Default is Int.MIN_VALUE..Int.MAX_VALUE.
 * @return An arbitrary list of unique integers within the given range.
 * @throws IllegalArgumentException if [maxSize] is greater than the size of the given [range].
 */
private fun Arb.Companion.uniqueList(
    maxSize: Int = 10_000,
    range: IntRange = Int.MIN_VALUE..Int.MAX_VALUE
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

/**
 * Returns an [Arb] that generates a matrix with random elements of type [T].
 * The number of rows and columns of the matrix are randomly generated between 1 and 100.
 */
private fun <T> Arb.Companion.matrix(gen: Arb<T>) = arbitrary {
    val rows = int(1..50).bind()
    val cols = int(1..50).bind()
    List(rows) { List(cols) { gen.bind() } }
}
// endregion COLLECTIONS
// endregion GENERATORS

class CollectionsTest : FreeSpec({
    "An array can be transformed into an incremental array" {
        checkAll(Arb.list(Arb.real(0.0..100_000.0))) { ds ->
            val array = ds.toDoubleArray()
            val copy = array.copyOf()
            array.incremental()
            assertSoftly {
                array.forEachIndexed { i, d ->
                    d shouldEq copy.sumFirst(i + 1)
                }
            }
        }
    }

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

    "Transposing a list of lists should" - {
        "return an empty list if the list is empty" {
            emptyList<List<Any>>().transpose() shouldBe emptyList()
        }

        "return a list of lists with the elements at the same indices" {
            checkAll(Arb.matrix(Arb.any())) { matrix ->
                val transposed = matrix.transpose()
                assertSoftly {
                    transposed.forEachIndexed { i, list ->
                        list.forEachIndexed { j, e ->
                            e shouldBe matrix[j][i]
                        }
                    }
                }
            }
        }

        "throw an exception if the lists are not of the same size" {
            checkAll(Arb.list(Arb.list(Arb.any()))) { ass ->
                assume {
                    ass.shouldNotBeEmpty()
                    ass shouldAny { it.size != ass.first().size }
                }
                shouldThrow<cl.ravenhill.enforcer.EnforcementException> {
                    ass.transpose()
                }.infringements.first() shouldBeOfClass UnfulfilledRequirementException::class
            }
        }
    }

    "Adding an element if absent should" - {
        "Add the element if it's not present" {
            checkAll(Arb.mutableCollection(Arb.any()), Arb.any()) { collection, element ->
                assume {
                    collection shouldNotContain element
                }
                collection.addIfAbsent(element)
                collection shouldContain element
            }
        }

        "Not add the element if it's present" {
            checkAll(Arb.mutableCollection(Arb.any(), 1..100), Arb.random()) { collection, rng ->
                val element = collection.random(rng)
                collection shouldContain element
                val size = collection.size
                collection.addIfAbsent(element)
                collection shouldHaveSize size
                collection shouldContain element
            }
        }
    }

    "Removing the first n elements of a list should" - {
        "Remove the first n elements if the size of the collection is greater than n" {
            checkAll(Arb.mutableListAndIndex(Arb.any())) { (list, n) ->
                val copy = list.toMutableList()
                copy.dropFirst(n)
                assertSoftly {
                    copy shouldHaveSize list.size - n
                    copy.forEachIndexed { i, e ->
                        e shouldBe list[i + n]
                    }
                }
            }
        }

        "Throw an exception if n is greater than the list's size" {
            checkAll(Arb.mutableList(Arb.any()), Arb.positiveInt()) { list, n ->
                assume {
                    n shouldBeGreaterThan list.size
                }
                val ex = shouldThrow<cl.ravenhill.enforcer.EnforcementException> {
                    list.dropFirst(n)
                }
                with(ex.infringements.first()) {
                    shouldBeInstanceOf<cl.ravenhill.enforcer.IntRequirementException>()
                    message shouldBe unfulfilledConstraint("Size [$n] should be in range [0, ${n}]")
                }
            }
        }
    }

    "Swapping two elements in a list should" - {
        "swap the elements if they are different" {
            checkAll(Arb.list(Arb.any(), 2..100), Arb.random()) { list, rng ->
                val (i, j) = if (list.size == 2) {
                    0 to 1
                } else {
                    rng.nextInt(0, list.size - 1) to rng.nextInt(0, list.size - 1)
                }
                val copy = list.toMutableList()
                copy.swap(i, j)
                copy[i] shouldBe list[j]
                copy[j] shouldBe list[i]
            }
        }

        "not change the list if the elements are the same" {
            checkAll(Arb.list(Arb.any(), 2..100), Arb.random()) { list, rng ->
                val i = rng.nextInt(0, list.size - 1)
                val copy = list.toMutableList()
                copy.swap(i, i)
                copy shouldBe list
            }
        }

        "throw an exception if the indices are negative" {
            checkAll(
                Arb.mutableList(Arb.any()),
                Arb.negativeInt(),
                Arb.negativeInt()
            ) { list, i, j ->
                val ex = shouldThrow<cl.ravenhill.enforcer.EnforcementException> {
                    list.swap(i, j)
                }
                with(ex.infringements) {
                    assertSoftly {
                        size shouldBe 2
                        first().shouldBeOfClass(cl.ravenhill.enforcer.IntRequirementException::class)
                        first().message shouldBe "Unfulfilled constraint: i [$i] should be in range [0, ${list.size})"
                        last().shouldBeOfClass(cl.ravenhill.enforcer.IntRequirementException::class)
                        last().message shouldBe "Unfulfilled constraint: j [$j] should be in range [0, ${list.size})"
                    }
                }
            }
        }
    }
})
