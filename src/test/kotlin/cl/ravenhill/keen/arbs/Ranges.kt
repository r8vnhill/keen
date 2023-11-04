/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs

import cl.ravenhill.keen.arbs.datatypes.real
import cl.ravenhill.orderedPair
import cl.ravenhill.utils.DoubleRange
import cl.ravenhill.utils.toRange
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int

/**
 * Generates an [Arb] (Arbitrary) of [CharRange] between the given bounds `lo` and `hi`.
 *
 * @param lo The lower bound of the character range, defaulting to the smallest possible Char value.
 * @param hi The upper bound of the character range, defaulting to the largest possible Char value.
 * @return An arbitrary of CharRange.
 */
fun Arb.Companion.charRange(
    lo: Char = Char.MIN_VALUE,
    hi: Char = Char.MAX_VALUE,
): Arb<ClosedRange<Char>> =
    arbitrary {
        range(char(lo..hi), char(lo..hi)).bind()
    }

/**
 * Returns an arbitrary that generates a [DoubleRange].
 *
 * This function first creates an ordered pair of random double values using the [double] arbitrary.
 * The pair is then transformed into a range using the `toRange` function.
 *
 * @return An arbitrary of [DoubleRange].
 */
fun Arb.Companion.doubleRange() = arbitrary {
    orderedPair(real()).bind().toRange()
}

/**
 * Returns an arbitrary that generates an [IntRange].
 *
 * This function first creates an ordered pair of random integer values using the [int] arbitrary.
 * The pair is then transformed into a range using the `toRange` function.
 *
 * @return An arbitrary of [IntRange].
 */
fun Arb.Companion.intRange() = arbitrary {
    range(int(), int()).bind()
}

fun <T : Comparable<T>> Arb.Companion.range(generator: Arb<T>) = arbitrary {
    range(generator, generator).bind()
}

fun <T : Comparable<T>> Arb.Companion.range(
    a: Arb<T>,
    b: Arb<T>,
    reverted: Boolean = false,
) = arbitrary {
    val i = a.bind()
    var j = b.bind()

    while (i == j) {
        j = b.bind() // Re-bind `j` until it is different from `i` if `strict` is `true`
    }

    if ((i < j && !reverted) || (i > j && reverted)) i..j else j..i
}
