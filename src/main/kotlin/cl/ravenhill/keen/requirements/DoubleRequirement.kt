package cl.ravenhill.keen.requirements

import cl.ravenhill.keen.DoubleRequirementException
import cl.ravenhill.keen.util.DoubleToDouble
import cl.ravenhill.keen.util.contains
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
    override fun generateException(description: String) = DoubleRequirementException { description }

    /**
     * A [DoubleRequirement] constraint that checks if a given [Double] is within a specified range.
     *
     * @property range The range of acceptable [Double] values, as a [DoubleToDouble] pair.
     */
    class BeInRange(private val range: DoubleToDouble) : DoubleRequirement {

        /**
         * A secondary constructor that allows for the range to be specified as a
         * [ClosedFloatingPointRange].
         *
         * @param range The range of acceptable [Double] values, as a [ClosedFloatingPointRange].
         * @param lazyDescription A function that returns a [String] describing the requirement,
         * based on a provided input [Double]. By default, this function returns a string in the
         * form "Expected a number in range $range, but got $value".
         */
        constructor(range: ClosedFloatingPointRange<Double>) : this(range.start to range.endInclusive)

        /// Documentation inherited from [Requirement]
        override val validator = { value: Double -> value in range }
    }

    /**
     * A [DoubleRequirement] constraint that checks if a given [Double] is equal to within a certain
     * tolerance of an expected value.
     *
     * @property expected The expected [Double] value.
     * @property tolerance The maximum allowable difference between the given value and the expected
     * value. Defaults to `1e-8`.
     */
    class BeEqualTo(private val expected: Double, private val tolerance: Double = 1e-8) :
            DoubleRequirement {

        /// Documentation inherited from [Requirement]
        override val validator = { value: Double -> abs(value - expected) <= tolerance }
    }
}