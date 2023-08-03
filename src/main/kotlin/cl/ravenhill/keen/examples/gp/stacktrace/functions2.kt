/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.examples.gp.stacktrace

/***************************************************************************************************
 * This file defines and demonstrates a set of functions that primarily deal with
 * exception handling and integer operations.
 *
 * This file consists of five independent functions - assertPositive, assertNonNullString,
 * safeDivide, checkIndex, and addNumbers. Each function has its own purpose like checking if a
 * number is positive, ensuring a string is not null, safely dividing two numbers, checking if an
 * index is within the bounds of an array, and adding two numbers, respectively. The final
 * declaration is a list of function references, `functions2`, that includes all the defined functions.
 * This list serves as a compact representation of these functions for easy access and execution. The
 * defined functions and the list can be used in various contexts, like testing and demonstration,
 * due to their generality and widespread applicability.
 **************************************************************************************************/

/**
 * Asserts if the provided number [x] is positive.
 *
 * @param x The number to be checked.
 * @param errorMsg The custom error message for the exception.
 * @throws IllegalArgumentException If the number is less than or equal to zero.
 */
fun assertPositive(x: Int, errorMsg: String) {
    if (x <= 0) {
        throw IllegalArgumentException(errorMsg)
    }
}

/**
 * Asserts if the provided string [s] is not null.
 *
 * @param s The string to be checked.
 * @param errorMsg The custom error message for the exception.
 * @throws NullPointerException If the string is null.
 */
fun assertNonNullString(s: String?, errorMsg: String) {
    if (s == null) {
        throw NullPointerException(errorMsg)
    }
}

/**
 * Safely divides the [numerator] by the [divisor].
 *
 * @param numerator The number to be divided.
 * @param divisor The number by which the numerator will be divided.
 * @throws ArithmeticException If the divisor is zero.
 */
fun safeDivide(numerator: Int, divisor: Int) {
    if (divisor == 0) {
        throw ArithmeticException("Cannot divide by zero.")
    }
}

/**
 * Checks if the [index] is within the bounds of the [size].
 *
 * @param size The array in which the index will be checked.
 * @param index The index to be checked.
 * @throws ArrayIndexOutOfBoundsException If the index is not within the bounds of the array.
 */
fun checkIndex(size: Int, index: Int) {
    if (size <= 0) {
        throw ArrayIndexOutOfBoundsException("Array size must be positive.")
    } else if (index <= 0) {
        throw ArrayIndexOutOfBoundsException("Index must be positive.")
    } else if (index >= size) {
        throw ArrayIndexOutOfBoundsException("Index must be less than array size.")
    }
}

/**
 * Adds two integers [a] and [b].
 *
 * @param a The first number to be added.
 * @param b The second number to be added.
 * @return The sum of [a] and [b].
 */
fun addNumbers(a: Int, b: Int): Int {
    return a + b
}

/**
 * A list of function references (`KFunction`), which includes five functions.
 * Each function accepts two arguments and performs a specific operation.
 *
 * The functions included are:
 * - [assertPositive]: Throws an [IllegalArgumentException] if the input number is less than or
 * equal to zero.
 * - [assertNonNullString]: Throws a [NullPointerException] if the input string is null.
 * - [safeDivide]: Safely performs division operation, and throws an [ArithmeticException] if the
 * divisor is zero.
 * - [checkIndex]: Checks if an index is valid for an array, and throws an
 * [ArrayIndexOutOfBoundsException] if it's not.
 * - [addNumbers]: Returns the sum of two input numbers. This function does not throw any exceptions.
 *
 * These functions provide a set of examples for exception handling and operations with two
 * parameters, and could be used in a variety of contexts, including testing and demonstration.
 */
val functions2 =
    listOf(::assertPositive, ::assertNonNullString, ::safeDivide, ::checkIndex, ::addNumbers)
