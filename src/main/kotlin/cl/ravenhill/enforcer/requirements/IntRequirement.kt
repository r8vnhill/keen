/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.enforcer.requirements

import cl.ravenhill.utils.IntToInt
import cl.ravenhill.utils.contains

/**
 * Represents a requirement that can be applied to an integer value.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
sealed interface IntRequirement : Requirement<Int> {

    /// Documentation inherited from [Requirement].
    override fun generateException(description: String) =
        cl.ravenhill.enforcer.IntRequirementException { description }

    /**
     * Represents a requirement that an integer value must be positive.
     */
    data object BePositive : IntRequirement {

        /// Documentation inherited from [Requirement].
        override val validator = { value: Int -> value > 0 }
    }

    /**
     * Represents a requirement that an integer value must be within a specified [range].
     *
     * @constructor Creates a [BeInRange] requirement with a range of integer values specified as an
     * [IntToInt].
     * @property range The range of values that are allowed.
     */
    open class BeInRange(val range: IntToInt) : IntRequirement {

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
     * @property min The minimum allowed value.
     */
    class BeAtLeast(min: Int) : BeInRange(min to Int.MAX_VALUE) {
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
    class BeEqualTo(private val expected: Int) : IntRequirement {

        /// Documentation inherited from [Requirement].
        override val validator = { value: Int -> value == expected }
    }
}