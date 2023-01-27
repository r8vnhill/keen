package cl.ravenhill.keen.util

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.Contract
import cl.ravenhill.keen.IntRequirement.*
import kotlin.random.Random

/**
 * Subset permutation.
 */
object Subset {
    /**
     * Returns a random permutation of the given size.
     *
     * @param n the size of the permutation.
     * @return a random permutation of the given size.
     */
    fun next(n: Int, size: Int): IntArray {
        val subset = IntArray(size)
        next(n, subset)
        return subset
    }

    private fun next(n: Int, arr: IntArray) {
        val k = arr.size
        checkSubset(n, k)
        if (k == n) {
            for (i in 0 until k) {
                arr[i] = i
            }
            return
        }

        if (k > n - k) {
            subset0(n, n - k, arr)
            invert(n, k, arr)
        } else {
            subset0(n, k, arr)
        }
    }

    /**
     * "Inverts" the given subset array `a`. The first n - k elements represents
     * the set, which must not be part of the "inverted" subset. This is done by
     * filling the array from the back, starting with the highest possible element,
     * which is not part of the "forbidden" subset elements. The result is a
     * subset array, filled with elements, which where not part of the original
     * "forbidden" subset.
     */
    private fun invert(n: Int, k: Int, a: IntArray) {
        var v = n - 1
        var j = n - k - 1
        var vi: Int

        val ac: IntArray = a.copyOfRange(0, n - k)
        for (i in k downTo 0) {
            while (indexOf(ac, j, v).also { vi = it } != -1) {
                --v
                j = vi
            }
            a[i] = v--
        }
    }

    private fun indexOf(a: IntArray, start: Int, value: Int): Int {
        for (i in start downTo 0) {
            if (a[i] < value) {
                return -1
            } else if (a[i] == value) {
                return i
            }
        }
        return -1
    }

    private fun subset0(n: Int, k: Int, a: IntArray) {
        if (k == 0) {
            a[0] = Core.random.nextInt(n)
            return
        }
        // (A): Initialize a[i] to "zero" point for bin Ri.
        for (i in 0 until k) {
            a[i] = i * n / k
        }
        // (B)
        var l: Int
        var x: Int
        for (c in 0 until k) {
            do {
                // Choose random x;
                x = 1 + Core.random.nextInt(n)
                // determine range Rl;
                l = (x * k - 1) / n
            } while (a[l] >= x) // accept or reject.
            ++a[l]
        }
        var s = k
        // (C) Move a[i] of nonempty bins to the left.
        var m = 0
        var p = 0
        for (i in 0 until k) {
            if (a[i] == i * n / k) {
                a[i] = 0
            } else {
                ++p
                m = a[i]
                a[i] = 0
                a[p - 1] = m
            }
        }
        // (D) Determine l, set up space for Bl.

        // (D) Determine l, set up space for Bl.
        while (p > 0) {
            l = 1 + (a[p - 1] * k - 1) / n
            val ds = a[p - 1] - (l - 1) * n / k
            a[p - 1] = 0
            a[s - 1] = l
            s -= ds
            --p
        }
        // (E) If a[l] != 0, a new bin is to be processed.
        var r = 0
        var m0 = 0
        for (ll in 1..k) {
            l = k + 1 - ll
            if (a[l - 1] != 0) {
                r = l
                m0 = 1 + (a[l - 1] - 1) * n / k
                m = a[l - 1] * n / k - m0 + 1
            }

            // (F) Choose a random x.
            x = m0 + Core.random.nextInt(m)
            var i = l + 1

            // (G) Check x against previously entered elements in bin;
            //     increment x as it jumps over elements <= x.
            while (i <= r && x >= a[i - 1]) {
                ++x
                a[i - 2] = a[i - 1]
                ++i
            }
            a[i - 2] = x
            --m
        }
        // Convert to zero based indexed arrays.
        for (i in 0 until k) {
            --a[i]
        }
    }

    private fun checkSubset(n: Int, k: Int) {
        k.validateAtLeast(1, "Subset size")
        n.validateAtLeast(k, "k")
        k.validateSafeMultiplication(n) {
            "n * subset.length [${n * k}] > Int.MAX_VALUE [${Int.MAX_VALUE}]"
        }
    }
}

// region : MutableCollection
/**
 * Adds the given element to the collection if it is not already present.
 *
 * @return `true` if the element was added, `false` otherwise.
 */
fun <E> MutableCollection<E>.addIfAbsent(element: E): Boolean {
    if (element in this) {
        return false
    }
    return this.add(element)
}
// endregion

// region : MutableList
/**
 * Swaps the elements at the given indices in the receiver.
 */
fun <E> MutableList<E>.swap(i:  Int, j: Int) {
    Contract {
        i should BeInRange(0 until this@swap.size)
        j should BeInRange(0 until this@swap.size)
    }
    if (i == j) return
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

/**
 * Swaps the elements in the range [start, end) from [this] [MutableList] with the
 * elements in the range [`otherStart`, `otherStart + (end - start)`) from the [other]
 *
 * @param start the start index of the range to swap in [this] list.
 *              Must be in the range [0, size).
 * @param end the end index of the range to swap in [this] list.
 *            Must be in the range [start, size].
 * @param other the other list to swap elements with.
 * @param otherStart the start index of the range to swap in the [other] list.
 *                   Must be in the range [0, other.size).
 */
fun <E> MutableList<E>.swap(
    start: Int,
    end: Int,
    other: MutableList<E>,
    otherStart: Int
) {
    Contract {
        start should BePositive()
        end should BeAtMost(size) {
            "End index [$end] should be at most size [$size]"
        }
        end should BeAtLeast(start) {
            "End index [$end] should be at least start [$start]"
        }
        otherStart should BePositive()
        other.size should BeAtLeast(otherStart + (end - start)) {
            "Other list length [${other.size}] should be at least end - start [${end - start}]"
        }
    }
    var i = end - start
    while (--i >= 0) {
        val temp = this[i + start]
        this[i + start] = other[otherStart + i]
        other[otherStart + i] = temp
    }
}
// endregion

// region : List
/**
 * Returns a new list with the subtrahend subtracted from each element.
 */
infix fun List<Double>.sub(subtrahend: Double) = this.map { it - subtrahend }

/**
 * Returns a ``pick`` sized random subset of the receiver.
 */
fun <E> List<E>.subset(pick: Int) = subset(pick, Core.random)

/**
 * Returns a ``pick`` sized random subset of the receiver with the given [random] number
 * generator.
 */
fun <E> List<E>.subset(pick: Int, random: Random) = random
    .subset(pick, this.size)    // Get the indices of the elements to pick
    .map { this[it] }           // Map the indices to the elements
// endregion

// region : Arrays
/**
 * Transforms the given array into an incremental array, where each value is the
 * sum of the previous values.
 */
fun DoubleArray.incremental() {
    for (i in 1 until this.size) {
        this[i] += this[i - 1]
    }
}
// endregion