/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.enforcer.requirements

import cl.ravenhill.utils.DoubleRange
import cl.ravenhill.utils.DoubleToDouble
import cl.ravenhill.utils.contains
import cl.ravenhill.utils.toRange
import kotlin.math.abs

/**
 * Represents a constraint that can be applied to a double.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
sealed interface DoubleRequirement : Requirement<Double> {

    /// Documentation inherited from [Requirement]
    override fun generateException(description: String) =
        cl.ravenhill.enforcer.DoubleRequirementException { description }

    /**
     * A [DoubleRequirement] constraint that checks if a given [Double] is within a specified range.
     *
     * @property range The range of acceptable [Double] values, as a [DoubleToDouble] pair.
     */
    class BeInRange(val range: DoubleRange) : DoubleRequirement {

        /**
         * A secondary constructor that allows for the range to be specified as a
         * [ClosedFloatingPointRange].
         *
         * @param range The range of acceptable [Double] values, as a [ClosedFloatingPointRange].
         */
        constructor(range: DoubleToDouble) : this(range.toRange()) {
            require(range.first <= range.second) {
                "The first value in the range must be less than or equal to the second value."
            }
        }

        /// Documentation inherited from [Requirement]
        override val validator = { value: Double -> value in range }

        override fun toString() = "BeInRange { range: $range }"
    }

    /**
     * A [DoubleRequirement] constraint that checks if a given [Double] is equal to within a certain
     * tolerance of an expected value.
     *
     * @property expected The expected [Double] value.
     * @property tolerance The maximum allowable difference between the given value and the expected
     * value. Defaults to `1e-8`.
     */
    class BeEqualTo(val expected: Double, val tolerance: Double = 1e-8) : DoubleRequirement {

        init {
            require(tolerance >= 0) { "The tolerance must be non-negative." }
        }

        /// Documentation inherited from [Requirement]
        override val validator = { value: Double -> abs(value - expected) <= tolerance }

        /// Documentation inherited from [Any]
        override fun toString() = "BeEqualTo { expected: $expected, tolerance: $tolerance }"
    }
}