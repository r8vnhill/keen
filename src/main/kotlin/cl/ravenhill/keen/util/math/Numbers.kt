package cl.ravenhill.keen.util.math

import kotlin.math.pow


fun Int.roundUpToMultipleOf(i: Int): Int {
    if (i == 0) return this
    val remainder = this % i
    if (remainder == 0) return this
    return this + i - remainder
}

fun Double.isNotNan() = !this.isNaN()

fun Double.toIntProbability() = ((2.0.pow(32) - 1) * this + Int.MIN_VALUE).toInt()
