/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.benchmarks.stacktrace

import kotlin.reflect.KFunction

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

fun addNumbers(x: Int, y: Int) = x + y
fun multiplyNumbers(x: Int, y: Int) = x * y

fun subtractNumbers(x: Int, y: Int) = x - y

fun divideNumbers(x: Int, y: Int) = x / y

val functions2 = listOf(::addNumbers, ::multiplyNumbers, ::subtractNumbers, ::divideNumbers)
