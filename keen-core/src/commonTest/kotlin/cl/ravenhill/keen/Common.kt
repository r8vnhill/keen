/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.pair
import kotlin.random.Random

fun Arb.Companion.random(seed: Arb<Long> = long()) = arbitrary {
    Random(seed.bind())
}

fun arbRngPair(seed: Arb<Long> = Arb.long()) = arbitrary {
    seed.bind().let { Random(it) to Random(it) }
}

fun <T> arbOrderedPair(
    a: Arb<T>,
    b: Arb<T>,
    strict: Boolean = false,
    reversed: Boolean = false,
) where T : Comparable<T> = Arb.pair(a, b).filter { (first, second) ->
    if (strict) first != second else true
}.filter { (first, second) ->
    if (reversed) first >= second else first <= second
}

fun <T> arbOrderedPair(
    gen: Arb<T>,
    strict: Boolean = false,
    reverted: Boolean = false,
) where T : Comparable<T> = arbOrderedPair(gen, gen, strict, reverted)
