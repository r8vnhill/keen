/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util

import cl.ravenhill.any
import cl.ravenhill.enforcer.CollectionRequirementException
import cl.ravenhill.enforcer.EnforcementException
import cl.ravenhill.enforcer.IntRequirementException
import cl.ravenhill.enforcer.UnfulfilledRequirementException
import cl.ravenhill.keen.random
import cl.ravenhill.keen.shouldBeOfClass
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.real
import cl.ravenhill.unfulfilledConstraint
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
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.set
import io.kotest.property.assume
import io.kotest.property.checkAll


@ExperimentalStdlibApi
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
                shouldThrow<EnforcementException> {
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
                val ex = shouldThrow<EnforcementException> {
                    list.dropFirst(n)
                }
                with(ex.infringements.first()) {
                    shouldBeInstanceOf<IntRequirementException>()
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

        "throw an exception if" - {
            "the list is empty" {
                checkAll(Arb.int(), Arb.int()) { i, j ->
                    shouldThrow<EnforcementException> {
                        mutableListOf<Any>().swap(i, j)
                    }.shouldHaveInfringement<CollectionRequirementException>(
                        unfulfilledConstraint("The list must not be empty")
                    )
                }
            }

            "the first index is negative" {
                checkAll(
                    Arb.indices(
                        Arb.mutableList(Arb.any(), 1..100),
                        x = IndexType.Underflow
                    )
                ) { (list, i, j) ->
                    `check index constraints`(
                        list,
                        i to IndexType.Underflow,
                        j to IndexType.Valid,
                        "i" to "j"
                    )
                }
            }

            "the second index is negative" {
                checkAll(
                    Arb.indices(
                        Arb.mutableList(Arb.any(), 1..100),
                        y = IndexType.Underflow
                    )
                ) { (list, i, j) ->
                    `check index constraints`(
                        list,
                        i to IndexType.Valid,
                        j to IndexType.Underflow,
                        "i" to "j"
                    )
                }
            }

            "both indices are negative" {
                checkAll(
                    Arb.indices(
                        Arb.mutableList(Arb.any(), 1..100),
                        x = IndexType.Underflow,
                        y = IndexType.Underflow
                    )
                ) { (list, i, j) ->
                    `check index constraints`(
                        list,
                        i to IndexType.Underflow,
                        j to IndexType.Underflow,
                        "i" to "j"
                    )
                }
            }

            "the first index is greater than the list's size" {
                checkAll(
                    Arb.indices(
                        Arb.mutableList(Arb.any(), 1..100),
                        x = IndexType.Overflow
                    )
                ) { (list, i, j) ->
                    `check index constraints`(
                        list,
                        i to IndexType.Overflow,
                        j to IndexType.Valid,
                        "i" to "j"
                    )
                }
            }

            "the second index is greater than the list's size" {
                checkAll(
                    Arb.indices(
                        Arb.mutableList(Arb.any(), 1..100),
                        y = IndexType.Overflow
                    )
                ) { (list, i, j) ->
                    `check index constraints`(
                        list,
                        i to IndexType.Valid,
                        j to IndexType.Overflow,
                        "i" to "j"
                    )
                }
            }

            "both indices are greater than the list's size" {
                checkAll(
                    Arb.indices(
                        Arb.mutableList(Arb.any(), 1..100),
                        x = IndexType.Overflow,
                        y = IndexType.Overflow
                    )
                ) { (list, i, j) ->
                    `check index constraints`(
                        list,
                        i to IndexType.Overflow,
                        j to IndexType.Overflow,
                        "i" to "j"
                    )
                }
            }
        }
    }
})


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
    range: IntRange = 0..100,
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
 * @param maxSize The maximum size of the list.
 * Default is 10,000.
 * @param range The range of integers to choose from.
 * Default is Int.MIN_VALUE..Int.MAX_VALUE.
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

private fun <T> Arb.Companion.indices(
    list: Arb<MutableList<T>>,
    x: IndexType = IndexType.Valid,
    y: IndexType = IndexType.Valid,
) = arbitrary {
    val l = list.bind()
    val generateIndex = { indexType: IndexType ->
        when (indexType) {
            is IndexType.Valid -> nonNegativeInt(l.size - 1)
            is IndexType.Underflow -> negativeInt()
            is IndexType.Overflow -> int(l.size..Int.MAX_VALUE)
        }
    }
    val i = generateIndex(x).bind()
    val j = generateIndex(y).bind()
    Indices(l, i, j)
}

// endregion GENERATORS

/**
 * An experimental function that checks for invalid indices within a given mutable list and
 * throws an enforcement exception if any index is invalid. This function assumes that the list
 * is not empty. It accepts pairs representing the index and its type (valid, underflow, overflow),
 * and a pair of names for error messaging.
 *
 * @param list The MutableList of generic type T.
 * @param x Pair consisting of an Int and an IndexType to be checked.
 * @param y Pair consisting of another Int and an IndexType to be checked.
 * @param names Pair of strings used for descriptive error messaging.
 *
 * @throws EnforcementException If the index is out of the list's bounds.
 */
@ExperimentalStdlibApi
private fun `check index constraints`(
    list: MutableList<Any>,
    x: Pair<Int, IndexType>,
    y: Pair<Int, IndexType>,
    names: Pair<String, String>,
) {
    assume { list.shouldNotBeEmpty() }
    val ex = shouldThrow<EnforcementException> {
        list.swap(x.first, y.first)
    }
    with(ex) {
        if (x.second is IndexType.Invalid) {
            shouldHaveInfringement<IntRequirementException>(
                unfulfilledConstraint("${names.first} [${x.first}] should be in range [0, ${list.size})")
            )
        }
        if (y.second is IndexType.Invalid) {
            shouldHaveInfringement<IntRequirementException>(
                unfulfilledConstraint("${names.second} [${y.first}] should be in range [0, ${list.size})")
            )
        }
        infringements.size shouldBe if (x.second is IndexType.Invalid && y.second is IndexType.Invalid) 2 else 1
    }
}

/**
 * The `IndexType` is a sealed interface that represents the types of indices in the context of list operations.
 * It is used primarily to specify whether a generated index should be valid (within the bounds of a list),
 * underflow (negative), or overflow (exceed the list size).
 */
private sealed interface IndexType {

    /**
     * `Valid` is an object that represents valid indices within the bounds of a list.
     */
    data object Valid : IndexType

    /**
     * `Invalid` is a sealed interface itself, further dividing the invalid indices into two types.
     */
    sealed interface Invalid : IndexType

    /**
     * `Underflow` is an object that represents indices that are less than the lower bound (0) of a list.
     */
    data object Underflow : Invalid

    /**
     * `Overflow` is an object that represents indices that exceed the size of the list.
     */
    data object Overflow : Invalid
}

/**
 * A data class that encapsulates a list of type [T] and a pair of indices (`i`, `j`).
 *
 * @property list The list.
 * @property i The first index.
 * @property j The second index.
 */
private data class Indices<T>(val list: MutableList<T>, val i: Int, val j: Int)
