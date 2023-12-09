@file:Suppress("KotlinConstantConditions", "UNUSED_VARIABLE", "DIVISION_BY_ZERO")

package cl.ravenhill.keen.benchmarks.stacktrace

fun one(): Int = 1

fun two(): Int = 2

fun zero(): Int = 0

fun minusOne(): Int = -1

fun throwException0(): Nothing = throw NotImplementedError()

val functions0 = listOf(::one, ::two, ::minusOne, ::zero, ::throwException0)
