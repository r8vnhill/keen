package cl.ravenhill.keen.requirements

import cl.ravenhill.keen.DoubleRequirementException

/**
 * Represents a constraint that can be applied to a double.
 */
sealed interface DoubleRequirement : Requirement<Double> {

    // Inherit documentation from Requirement
    override fun generateException(description: String) = DoubleRequirementException { description }

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