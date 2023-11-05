/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.jakt.constraints

import cl.ravenhill.jakt.exceptions.IntRequirementException
import cl.ravenhill.utils.IntToInt
import cl.ravenhill.utils.contains

/**
 * Represents a requirement that can be applied to an integer value.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
sealed interface IntConstraint : Constraint<Int> {

    /// Documentation inherited from [Requirement].
    override fun generateException(description: String) =
        IntRequirementException { description }

    /**
     * Represents a requirement that an integer value must be positive.
     */
    data object BePositive : IntConstraint {

        /// Documentation inherited from [Requirement].
        override val validator = { value: Int -> value > 0 }
    }

    data object BeNegative : IntConstraint {
        override val validator = { value: Int -> value < 0 }
    }

    /**
     * Represents a requirement that an integer value must be within a specified [range].
     *
     * @constructor Creates a [BeInRange] requirement with a range of integer values specified as an
     * [IntToInt].
     * @property range The range of values that are allowed.
     */
    open class BeInRange(val range: IntToInt) : IntConstraint {

        init {
            require(range.first <= range.second) {
                "The first value in the range [${range.first}] must be less than or equal to the second value [${range.second}]."
            }
        }

        /**
         * Creates a [BeInRange] requirement with a range of integer values specified as an
         * [IntRange].
         *
         * @param range The [IntRange] of allowed values.
         */
        constructor(range: IntRange) : this(range.first to range.last)

        /// Documentation inherited from [Requirement].
        override val validator = { value: Int -> value in range }

        /// Documentation inherited from [Any].
        override fun toString() = "BeInRange { range: $range }"
    }

    /**
     * Represents a requirement that an integer value must be at least a specified value.
     *
     * @param min The minimum allowed value.
     * @property least The minimum allowed value.
     */
    class BeAtLeast(min: Int) : BeInRange(min to Int.MAX_VALUE) {
        val least: Int get() = range.first

        /// Documentation inherited from [Any].
        override fun toString() = "BeAtLeast { min: ${range.first} }"
    }

    /**
     * Represents a requirement that an integer value must be at most a specified value.
     *
     * @param max The maximum allowed value.
     * @property most The maximum allowed value.
     */
    class BeAtMost(max: Int) : BeInRange(Int.MIN_VALUE to max) {
        val most: Int get() = range.second

        /// Documentation inherited from [Any].
        override fun toString() = "BeAtMost { max: ${range.second} }"
    }

    /**
     * Represents a requirement that an integer value must be equal to a specified value.
     *
     * @property expected The expected value.
     */
    class BeEqualTo(val expected: Int) : IntConstraint {

        /// Documentation inherited from [Requirement].
        override val validator = { value: Int -> value == expected }

        /// Documentation inherited from [Any].
        override fun toString() = "BeEqualTo { expected: $expected }"
    }
}