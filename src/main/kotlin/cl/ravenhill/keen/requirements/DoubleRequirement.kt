package cl.ravenhill.keen.requirements

import cl.ravenhill.keen.DoubleRequirementException
import cl.ravenhill.keen.util.DoubleToDouble
import cl.ravenhill.keen.util.contains

/**
 * Represents a constraint that can be applied to a double.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
sealed interface DoubleRequirement : Requirement<Double> {

    /// Documentation inherited from [Requirement]
    override fun generateException(description: String) = DoubleRequirementException { description }

    /**
     * A [DoubleRequirement] constraint that checks if a given [Double] is within a specified range.
     *
     * @property range The range of acceptable [Double] values, as a [DoubleToDouble] pair.
     * @property lazyDescription A function that returns a [String] describing the requirement,
     * based on a provided input [Double].
     * By default, this function returns a string in the form "Expected a number in range $range,
     * but got $value".
     */
    class BeInRange(
        private val range: DoubleToDouble,
        override val lazyDescription: (Double) -> String = { value ->
            "Expected a number in range $range, but got $value"
        }
    ) : DoubleRequirement {

        /**
         * A secondary constructor that allows for the range to be specified as a
         * [ClosedFloatingPointRange].
         *
         * @param range The range of acceptable [Double] values, as a [ClosedFloatingPointRange].
         * @param lazyDescription A function that returns a [String] describing the requirement,
         * based on a provided input [Double]. By default, this function returns a string in the
         * form "Expected a number in range $range, but got $value".
         */
        constructor(
            range: ClosedFloatingPointRange<Double>,
            lazyDescription: (Double) -> String = { value ->
                "Expected a number in range $range, but got $value"
            }
        ) : this(range.start to range.endInclusive, lazyDescription)

        /// Documentation inherited from [Requirement]
        override val validator = { value: Double -> value in range }
    }
}