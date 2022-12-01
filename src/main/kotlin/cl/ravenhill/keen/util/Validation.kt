package cl.ravenhill.keen.util

import cl.ravenhill.keen.InvalidArgumentException
import cl.ravenhill.keen.util.math.MAX_ULP_DISTANCE
import cl.ravenhill.keen.util.math.eq
import cl.ravenhill.keen.util.math.ulpDistance
import kotlin.math.abs

/**
 * Validates if a given predicate is true, otherwise throws an [InvalidArgumentException].
 */
fun validatePredicate(predicate: () -> Boolean, lazyMessage: () -> String) {
    if (!predicate()) {
        throw InvalidArgumentException(lazyMessage)
    }
}

/**
 * Validates if a given probability is between 0 and 1.
 */
fun Double.validateProbability() =
    validatePredicate({ this in 0.0..1.0 }) { "Probability [$this] must be between 0 and 1" }

fun <T : Comparable<T>> T.validateRange(
    range: ClosedRange<T>,
    lazyMessage: () -> String = { "Value [$this] must be in range $range" }
) = this.also { validatePredicate({ this in range }, lazyMessage) }

/**
 * Validates that the receiver is in the given range.
 */
fun <T : Comparable<T>> T.validateRange(
    range: ClosedRange<T>,
    propertyName: String
) = this.validateRange(range) { "$propertyName [$this] must be in range $range" }

fun Int.validateSize(
    vararg lazyMessages: Pair<Boolean, () -> String>,
    strictlyPositive: Boolean = true
) = mapOf(*lazyMessages).let {
    if (strictlyPositive) {
        validatePredicate(
            { this > 0 },
            it.getOrDefault(true) { "Size must be strictly positive" })
    } else {
        validatePredicate(
            { this >= 0 },
            it.getOrDefault(false) { "Size must be strictly positive" })
    }
}

/**
 * Validates if the receiver is at least a given minimum.
 *
 * @return the receiver if it is at least the minimum.
 * @throws InvalidArgumentException if the receiver is less than the minimum.
 */
fun <T : Comparable<T>> T.validateAtLeast(
    min: T,
    lazyMessage: () -> String = { "Value [$this] must be at least $min" }
) = this.also { validatePredicate({ this >= min }, lazyMessage) }

fun <T : Comparable<T>> T.validateAtLeast(
    min: T,
    propertyName: String
) = this.validateAtLeast(min) { "$propertyName [$this] must be at least $min" }

fun Int.validateSafeMultiplication(n: Int, lazyMessage: () -> String) = validatePredicate(
    {
        val m = this.toLong() * n.toLong()
        m.toInt().toLong() == m
    }, lazyMessage
)

fun DoubleArray.validateSum(d: Double, lazyMessage: () -> String) = validatePredicate(
    { this.sum() eq d }, lazyMessage
)

/**
 * Validates if the receiving list is not empty.
 *
 * @return the list itself.
 * @throws InvalidArgumentException if the list is empty.
 */
fun <E> List<E>.validateNotEmpty(lazyMessage: () -> String) = this.also {
    validatePredicate({ this.isNotEmpty() }, lazyMessage)
}

