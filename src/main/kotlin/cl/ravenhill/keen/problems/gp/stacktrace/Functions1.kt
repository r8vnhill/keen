/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.problems.gp.stacktrace

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


// region : -== FUNCTIONS THAT THROW EXCEPTIONS ==- :

/**
 * Checks if the input number is positive.
 *
 * If the input number is negative, it throws an IllegalArgumentException.
 *
 * @param number The number to check.
 * @throws IllegalArgumentException If the input number is negative.
 */
fun checkPositiveNumber(number: Int) {
    if (number < 0) {
        throw IllegalArgumentException("Input number must be positive.")
    }
}

/**
 * Checks if the input string is not null.
 *
 * If the input string is null, it throws a NullPointerException.
 *
 * @param input The string to check.
 * @throws NullPointerException If the input string is null.
 */
fun checkNonNullString(input: String?) {
    if (input == null) {
        throw NullPointerException("Input string must not be null.")
    }
}

/**
 * Checks if the input number is not zero.
 *
 * If the input number is zero, it throws an ArithmeticException.
 *
 * @param divisor The number to check.
 * @throws ArithmeticException If the input number is zero.
 */
fun checkNonZeroDivisor(divisor: Int) {
    if (divisor == 0) {
        throw ArithmeticException("Divisor must not be zero.")
    }
}

// endregion EXCEPTION-THROWING FUNCTIONS

// region : -== FUNCTIONS THAT RETURN BOOLEAN ==- :

/**
 * Checks if the input number is positive.
 *
 * @param number The number to check.
 * @return True if the number is positive or zero, false otherwise.
 */
fun isPositiveNumber(number: Int): Boolean {
    return number >= 0
}

/**
 * Checks if the input string is not null.
 *
 * @param input The string to check.
 * @return True if the string is not null, false otherwise.
 */
fun isNonNullString(input: String?): Boolean {
    return input != null
}

/**
 * Checks if the input index is valid for the given list.
 *
 * @param list The list to check the index for.
 * @param index The index to check.
 * @return True if the index is valid for the list, false otherwise.
 */
//fun isValidIndex(list: List<Any>, index: Int): Boolean {
//    return index >= 0 && index < list.size
//}

/**
 * Checks if the input number is not zero.
 *
 * @param divisor The number to check.
 * @return True if the number is not zero, false otherwise.
 */
fun isNonZeroDivisor(divisor: Int): Boolean {
    return divisor != 0
}

// endregion BOOLEAN-RETURNING FUNCTIONS

// region : -== FUNCTION LIST ==- :

/**
 * `functions1` is a list of function references.
 *
 * It includes six functions.
 * Each of these functions accepts a __single argument__ and performs a specific operation.
 * Some of these operations result in _exceptions_ being thrown, while others _return_ a
 * [Boolean] result based on a check performed on the input.
 *
 * The functions included are:
 * - [checkPositiveNumber]: Throws an [IllegalArgumentException] if the input number is negative or
 *   zero.
 * - [checkNonNullString]: Throws a [NullPointerException] if the input string is null.
 * - [checkNonZeroDivisor]: Throws an [ArithmeticException] if the input number is zero (implying a
 *   division by zero scenario).
 * - [isPositiveNumber]: Returns ``true`` if the input number is positive, and false otherwise.
 *   Does not throw any exceptions.
 * - [isNonNullString]: Returns ``true`` if the input string is not null, and false otherwise.
 *   Does not throw any exceptions.
 * - [isNonZeroDivisor]: Returns true if the input number is non-zero (could be used as a divisor),
 *   and false otherwise.
 *   Does not throw any exceptions.
 *
 * These functions provide a set of examples for exception handling and conditional return within
 * functions and could be used in a variety of contexts, including testing and demonstration.
 */
val functions1 = listOf(
    ::checkPositiveNumber,
    ::checkNonNullString,
    ::checkNonZeroDivisor,
    ::isPositiveNumber,
    ::isNonNullString,
    ::isNonZeroDivisor
)

// endregion FUNCTION LIST
