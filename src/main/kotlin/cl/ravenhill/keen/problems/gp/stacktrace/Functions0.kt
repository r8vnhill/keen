@file:Suppress("KotlinConstantConditions", "UNUSED_VARIABLE", "DIVISION_BY_ZERO")

package cl.ravenhill.keen.problems.gp.stacktrace

/***************************************************************************************************
 * This file is instrumental for testing a genetic algorithm intended for automatic crash
 * reproduction.
 *
 * Two parameterless functions are included in this file.
 * The first function is designed to consistently generate an IllegalArgumentException, while the
 * second function merely prints a non-exception-throwing message.
 * These functions are used as test cases, facilitating the genetic algorithm's exploration and
 * understanding of different crash scenarios, specifically those that do not involve parameter
 * interactions.
 **************************************************************************************************/


/**
 * Function that throws an IllegalArgumentException.
 */
fun throwException() {
    throw IllegalArgumentException("This function always throws an exception!")
}

// Example 1: Function that throws an IllegalArgumentException when the input number is negative.
fun checkPositiveNumber() {
    throw IllegalArgumentException("Input number must be positive.")
}

// Example 2: Function that throws an IllegalArgumentException when the input string is blank.
fun checkNonBlankString() {
    throw IllegalArgumentException("Input string must not be blank.")
}

// Example 3: Function that throws an IllegalArgumentException when the input list is empty.
fun checkNonEmptyList() {
    throw IllegalArgumentException("Input list must not be empty.")
}

// Example 2: Function that throws an ArithmeticException.
fun divideByZero() {
    val result = 10 / 0  // This will throw an ArithmeticException.
}

// Example 3: Function that throws a NullPointerException.
fun throwNullPointerException() {
    val nullString: String? = null
    val length = nullString!!.length  // This will throw a NullPointerException.
}

/**
 * Function that prints a message and doesn't throw any exceptions.
 */
fun printMessage() {
    println("Hello, this function does not throw an exception!")
}

/**
 * A list of function references named ``functions0``.
 *
 * This includes two parameterless functions: [throwException] and [printMessage].
 *
 * These functions serve as test scenarios for the genetic algorithm in automatic crash
 * reproduction.
 */
val functions0 = listOf(
    ::throwException,
    ::throwNullPointerException,
    ::divideByZero,
    ::checkNonBlankString,
    ::checkNonEmptyList,
    ::checkPositiveNumber,
    ::printMessage
)
