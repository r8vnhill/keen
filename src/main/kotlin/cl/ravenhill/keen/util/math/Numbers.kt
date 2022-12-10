package cl.ravenhill.keen.util.math

import kotlin.math.abs
import kotlin.math.pow

const val MAX_ULP_DISTANCE = 1e10

fun Int.roundUpToMultipleOf(i: Int): Int {
    if (i == 0) return this
    val remainder = this % i
    if (remainder == 0) return this
    return this + i - remainder
}

fun Double.isNotNan() = !this.isNaN()

fun Double.toIntProbability() = ((2.0.pow(32) - 1) * this + Int.MIN_VALUE).toInt()

infix fun Double.eq(d: Double): Boolean = abs(this - d) < 1e-10

