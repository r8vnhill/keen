/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs.datatypes

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.next

fun Arb.Companion.probability() = Arb.real(0.0..1.0)

/**
 * Returns an arbitrary generator for [Double] values within the given [range], excluding NaN and
 * infinite values.
 */
fun Arb.Companion.real(
    range: ClosedFloatingPointRange<Double> = Double.MIN_VALUE..Double.MAX_VALUE
) = arbitrary {
    double(range).next()
}
