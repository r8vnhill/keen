/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.jakt.constraints

import cl.ravenhill.jakt.exceptions.DoubleConstraintException
import cl.ravenhill.utils.DoubleRange
import cl.ravenhill.utils.DoubleToDouble
import cl.ravenhill.utils.toRange
import kotlin.math.abs

/**
 * Represents a constraint that can be applied to a double.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
sealed interface DoubleConstraint : Constraint<Double> {

    /// Documentation inherited from [Requirement]
    override fun generateException(description: String) = DoubleConstraintException { description }

    /**
     * A [DoubleConstraint] constraint that checks if a given [Double] is within a specified range.
     *
     * @property range The range of acceptable [Double] values, as a [DoubleToDouble] pair.
     */
    open class BeInRange(val range: DoubleRange) : DoubleConstraint {
        final override val validator = { value: Double -> value in range }
    }

    /**
     * A [DoubleConstraint] constraint that checks if a given [Double] is equal to within a certain
     * tolerance of an expected value.
     *
     * @property expected The expected [Double] value.
     * @property tolerance The maximum allowable difference between the given value and the expected
     * value. Defaults to `1e-8`.
     */
    class BeEqualTo(val expected: Double, val tolerance: Double = 1e-8) : DoubleConstraint {

        init {
            require(tolerance >= 0) { "The tolerance must be non-negative." }
        }

        /// Documentation inherited from [Requirement]
        override val validator = { value: Double -> abs(value - expected) <= tolerance }

        /// Documentation inherited from [Any]
        override fun toString() = "BeEqualTo { expected: $expected, tolerance: $tolerance }"
    }
}