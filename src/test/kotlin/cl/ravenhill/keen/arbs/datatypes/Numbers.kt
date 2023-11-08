/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs.datatypes

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
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

/**
 * Generates pairs of integers and their associated values of type [T], where the integer is selected from a range
 * determined by the value of type [T].
 *
 * This generator is particularly useful when the range from which an integer should be generated depends on
 * another value that is also being generated. For example, if the range of valid integers is contingent upon
 * some property of an object, this function facilitates their joint generation for testing purposes.
 *
 * @param gen An [Arb] instance that generates the values of type [T].
 * @param rangeFn A function that takes a value of type [T] and returns an [IntRange]. This range is then used
 *                to generate an integer associated with the value of type [T].
 * @return An [Arb] that generates pairs of the form `(Int, T)`, where the integer is randomly chosen from a
 *         range specified by the associated value of type [T].
 */
fun <T> Arb.Companion.intWith(gen: Arb<T>, rangeFn: (T) -> IntRange) = arbitrary {
    val value = gen.bind()
    int(rangeFn(value)).next() to value
}
