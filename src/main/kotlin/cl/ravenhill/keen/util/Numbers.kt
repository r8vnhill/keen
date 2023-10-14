/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


@file:Suppress("ConvertTwoComparisonsToRangeCheck")

package cl.ravenhill.keen.util

import org.apache.commons.math3.util.Precision
import kotlin.math.ceil
import kotlin.math.floor

/***************************************************************************************************
 * This file contains a collection of Kotlin extension functions, which provide additional
 * functionality to builtin types like Int and Double.
 * These functions include rounding up an integer to the next multiple of a given integer, checking
 * if a double is not NaN, and comparing doubles with a given tolerance.
 * The file is designed to be used as a utility library and is not intended to be a standalone
 * program.
 **************************************************************************************************/

/**
 * Rounds up this integer to the next multiple of the given integer.
 */
infix fun Int.roundUpToMultipleOf(i: Int): Int = when (i) {
    0 -> this
    else -> when (val remainder = this % i) {
        0 -> this
        else -> this + i - remainder
    }
}

/**
 * This extension function calculates the ceiling of a [Double] and converts it to an [Int].
 *
 * The ceiling of a number is the smallest integer that is greater than or equal to the number.
 * For example, the ceiling of 2.3 is 3, and the ceiling of -2.3 is -2.
 */
fun Double.ceil() = ceil(this).toInt()

/**
 * This extension function calculates the floor of a [Double] and converts it to an [Int].
 *
 * The floor of a number is the largest integer that is less than or equal to the number.
 * For example, the floor of 2.3 is 2, and the floor of -2.3 is -3.
 */
fun Double.floor() = floor(this).toInt()

/**
 * Returns true if this double is not NaN.
 */
fun Double.isNotNan() = !this.isNaN()

/**
 * Returns true if this double is equal to the given double.
 */
infix fun Double.eq(d: Double): Boolean = Precision.equals(this, d, 1e-10)

/**
 * Returns true if this double is not equal to the given double.
 */
infix fun Double.neq(d: Double): Boolean = !this.eq(d)
