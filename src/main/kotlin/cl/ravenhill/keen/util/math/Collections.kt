package cl.ravenhill.keen.util.math

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.util.validateAtLeast
import cl.ravenhill.keen.util.validateSafeMultiplication
import java.util.Objects
import java.util.Objects.checkIndex

object Subset {
    fun next(n: Int, k: Int): IntArray {
        val subset = IntArray(k)
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

    private fun invert(n: Int, k: Int, a: IntArray) {
        TODO("Not yet implemented")
    }

    private fun subset0(n: Int, k: Int, a: IntArray) {
        if (k == 0) {
            a[0] = Core.rng.nextInt(n)
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
                x = 1 + Core.rng.nextInt(n)
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
            x = m0 + Core.rng.nextInt(m)
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
        n.validateAtLeast(k)
        k.validateSafeMultiplication(n) {
            "n * subset.length [${n * k}] > Int.MAX_VALUE [${Int.MAX_VALUE}]"
        }
    }
}

infix fun List<Double>.sub(min: Double) = this.map { it - min }

/**
 * Swaps the elements at the given indices in the receiver.
 */
fun <E> MutableList<E>.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

/**
 * Swaps the elements in the range [start, end) from this list with the elements in the range
 * [otherStart, otherStart + (end - start)) from the other list.
 */
fun <E> MutableList<E>.swap(start: Int, end: Int, other: MutableList<E>, otherStart: Int) {
    this.checkIndex(start, end, otherStart, other.size)
    var i = end - start
    while (--i >= 0) {
        val temp = this[i + start]
        this[i + start] = other[otherStart + i]
        other[otherStart + i] = temp
    }
}

private fun <E> MutableList<E>.checkIndex(
    start: Int,
    end: Int,
    otherStart: Int,
    otherLength: Int
) {
    this.checkIndex(start, end)
    otherStart.validateAtLeast(0) { "Start index [$otherStart] should be positive" }
    otherLength.validateAtLeast(otherStart + (end - start)) {
        "End index [$otherLength] should be at least otherStart + (end - start) " +
                "[${otherStart + (end - start)}]"
    }
}

private fun <E> MutableList<E>.checkIndex(start: Int, end: Int) {
    start.validateAtLeast(0) { "Start index [$start] should be positive" }
    size.validateAtLeast(end) { "End index [$end] should be at least the length [$size] of the list" }
    end.validateAtLeast(start)
}


fun <E> List<E>.indexOf(element: E, start: Int, end: Int) = if (element != null) {
    indexWhere(element::equals, start, end)
} else {
    indexWhere(Objects::isNull, start, end)
}

fun <T> List<T>.indexWhere(predicate: (T) -> Boolean, start: Int, end: Int): Int {
    checkIndex(start, end)
    var index = -1
    var i = start
    while (i < end && index == -1) {
        if (predicate(this[++i])) {
            index = i
        }
    }
    return index
}

