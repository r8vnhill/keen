/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.benchmarks.stacktrace

/***************************************************************************************************
 * Stacktrace-related functions for the Keen Genetic Algorithm library.
 *
 * This Kotlin file, under the `cl.ravenhill.keen.problems.gp.stacktrace` package, comprises several
 * functions.
 * These functions are specifically designed to perform checks and validations.
 * They might result in throwing exceptions or returning Boolean values based on the input provided.
 *
 * The functions are grouped into two categories: Exception-Throwing Functions and Boolean-Returning
 * Functions.
 * Some functions check conditions such as positive numbers, non-null strings, non-zero divisors and
 * valid list indices, throwing appropriate exceptions when these conditions are not met.
 *
 * On the other hand, the Boolean-Returning functions check the same conditions but return true or
 * false instead of throwing exceptions.
 *
 * Furthermore, a list named `functions1` aggregates references to all these functions for
 * convenience.
 **************************************************************************************************/

fun compareWithOneHundred(x: Int): Nothing = if (x < 100) {
    if (x > 50) throwIAE("The number is greater than 50")
    else throw AssertionError("The number is less than 50")
} else {
    throw IllegalArgumentException("The number is greater than 100")
}

private fun throwIAE(s: String): Nothing {
    throw IllegalArgumentException(s)
}

val functions1 = listOf(::compareWithOneHundred)
