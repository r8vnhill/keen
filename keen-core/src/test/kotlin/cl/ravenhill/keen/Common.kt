/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.long
import kotlin.random.Random

fun Arb.Companion.random(seed: Arb<Long> = long()) = arbitrary {
    Random(seed.bind())
}

fun arbRngPair(seed: Arb<Long> = Arb.long()) = arbitrary {
    seed.bind().let { Random(it) to Random(it) }
}
