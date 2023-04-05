package cl.ravenhill.keen.util

import cl.ravenhill.keen.InvalidArgumentException
import cl.ravenhill.keen.util.math.eq

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
    range: Pair<T, T>,
    lazyMessage: () -> String = { "Value [$this] must be in range $range" }
) = this.also {
    validatePredicate(
        { this <= range.second && range.first <= this },
        lazyMessage
    )
}

/**
 * Validates that the receiver is in the given range.
 */
fun <T : Comparable<T>> T.validateRange(
    range: Pair<T, T>,
    propertyName: String
) = this.validateRange(range) { "$propertyName [$this] must be in range $range" }

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

/**
 * Validates if the receiver is at least a given minimum.
 *
 * @return the receiver if it is at least the minimum.
 * @throws InvalidArgumentException if the receiver is less than the minimum.
 */
fun <T : Comparable<T>> T.validateAtLeast(
    min: T,
    propertyName: String
) = this.validateAtLeast(min) { "$propertyName [$this] must be at least $min" }


fun DoubleArray.validateSum(d: Double, lazyMessage: () -> String) = validatePredicate(
    { this.sum() eq d }, lazyMessage
)

