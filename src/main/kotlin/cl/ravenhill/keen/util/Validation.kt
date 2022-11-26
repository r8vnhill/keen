package cl.ravenhill.keen.util

import cl.ravenhill.keen.InvalidArgumentException

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
) = validatePredicate({ this in range }, lazyMessage)

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

fun <T : Comparable<T>> T.validateAtLeast(
    min: T,
    lazyMessage: () -> String = { "Value [$this] must be at least $min" }
) = validatePredicate({ this >= min }, lazyMessage)