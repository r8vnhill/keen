@file:Suppress("KotlinConstantConditions", "UNUSED_VARIABLE", "DIVISION_BY_ZERO")

package cl.ravenhill.keen.problems.gp.stacktrace

/***************************************************************************************************
 * This file contains several functions designed to throw exceptions.
 * These are intended for use in testing scenarios, specifically for a genetic algorithm for
 * automatic crash reproduction.
 *
 * The file contains a total of nine functions.
 * Seven of these are designed to consistently throw different types of exceptions regardless of any
 * input parameters or internal state.
 * These include throwing IllegalArgumentException, ArithmeticException, and NullPointerException.
 *
 * The remaining two functions do not throw exceptions.
 * One simply prints a non-exception-throwing message.
 *
 * A list named `functions0` is included that holds references to all of these functions.
 * This serves as a quick reference for testing scenarios.
 **************************************************************************************************/


/**
 * Function that throws an IllegalArgumentException.
 */
fun throwException0() {
    throw IllegalArgumentException("This function always throws an exception!")
}

/**
 * Function that throws an IllegalArgumentException.
 */
fun throwException1() {
    throw IllegalArgumentException("This function always throws an exception!")
}

/**
 * Function that throws an IllegalArgumentException.
 */
fun throwException2() {
    throw IllegalArgumentException("This function always throws an exception!")
}

/**
 * This function always throws an IllegalArgumentException with the specific message.
 *
 * Note that despite its name, this function does not take any arguments or perform any checks.
 * Instead, it is designed to consistently throw an exception, simulating scenarios in which an
 * exception is thrown regardless of the input or internal state.
 * It's intended to be used in test scenarios where consistent exception-throwing behavior is
 * required.
 */
fun checkPositiveNumber() {
    throw IllegalArgumentException("Input number must be positive.")
}


/**
 * Function that always throws an IllegalArgumentException.
 *
 * This function is named to suggest that it checks whether a string is non-blank, but it does not
 * take any arguments or perform any such check.
 * Rather, it always throws an IllegalArgumentException with a specific message.
 * This is designed to simulate scenarios where an exception is consistently thrown regardless of
 * input or state.
 * This can be particularly useful in test cases where a predictable, exception-throwing behavior is
 * required.
 */
fun checkNonBlankString() {
    throw IllegalArgumentException("Input string must not be blank.")
}

/**
 * This function consistently throws an IllegalArgumentException with a specific message.
 *
 * Despite its name suggesting that it checks whether a list is non-empty, it does not take any
 * arguments or perform any checks.
 * Instead, it always throws an IllegalArgumentException with a message implying a
 * non-empty list requirement.
 * This is designed for testing scenarios where an exception is consistently thrown, regardless of
 * the input or state.
 */
fun checkNonEmptyList() {
    throw IllegalArgumentException("Input list must not be empty.")
}

/**
 * Function that consistently throws an ArithmeticException due to a division by zero.
 *
 * This function does not accept any parameters.
 * It deliberately attempts to divide an integer by zero, an operation which is undefined and
 * results in an ArithmeticException in Kotlin.
 * This function is useful for simulating scenarios where an ArithmeticException is consistently
 * thrown, regardless of the input or state.
 */
fun divideByZero() {
    val result = 10 / 0  // This will throw an ArithmeticException.
}

/**
 * Function that consistently throws a NullPointerException.
 *
 * This function does not accept any parameters.
 * It deliberately attempts to access the `length` property of a null String, an operation that
 * results in a NullPointerException in Kotlin.
 * This function is useful for simulating scenarios where a NullPointerException is consistently
 * thrown, regardless of the input or state.
 */
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
 * This includes two parameterless functions: [throwException0] and [printMessage].
 *
 * These functions serve as test scenarios for the genetic algorithm in automatic crash
 * reproduction.
 */
val functions0 = listOf(
    ::throwException0,
    ::throwException1,
    ::throwException2,
    ::checkPositiveNumber,
    ::checkNonBlankString,
    ::checkNonEmptyList,
    ::divideByZero,
    ::throwNullPointerException,
    ::printMessage
)
