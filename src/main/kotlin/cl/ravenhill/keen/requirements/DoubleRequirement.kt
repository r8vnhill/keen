package cl.ravenhill.keen.requirements

import cl.ravenhill.keen.DoubleRequirementException

/**
 * Represents a constraint that can be applied to a double.
 */
sealed interface DoubleRequirement : Requirement<Double> {
    val validator: (Double) -> Boolean
    val lazyDescription: (Double) -> String

    override fun validate(value: Double): Result<Double> =
        if (!validator(value)) {
            Result.failure(DoubleRequirementException { lazyDescription(value) })
        } else {
            Result.success(value)
        }
    /**
     * Constraint that checks if a double is in a given range.
     */
    class BeInRange(
        private val range: ClosedFloatingPointRange<Double>,
        override val lazyDescription: (Double) -> String = { value ->
            "Expected a number in range $range, but got $value"
        }
    ) : DoubleRequirement {
        override val validator = { value: Double -> value in range }
    }
}