package cl.ravenhill.keen.requirements

import cl.ravenhill.keen.IntRequirementException
import cl.ravenhill.keen.util.IntToInt
import cl.ravenhill.keen.util.contains

/**
 * Represents a requirement that can be applied to an integer value.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
sealed interface IntRequirement : Requirement<Int> {

    // Inherit documentation from Requirement
    override fun generateException(description: String) = IntRequirementException { description }

    /**
     * Represents a requirement that an integer value must be positive.
     */
    data object BePositive : IntRequirement {
        override val validator = { value: Int -> value > 0 }
    }

    /**
     * Represents a requirement that an integer value must be within a specified [range].
     *
     * @property range The range of values that are allowed.
     */
    open class BeInRange(
        private val range: IntToInt
    ) : IntRequirement {
        constructor(range: IntRange) : this(range.first to range.last)

        override val validator = { value: Int -> value in range }
    }

    /**
     * Represents a requirement that an integer value must be at least a specified value.
     *
     * @property min The minimum allowed value.
     */
    class BeAtLeast(
        min: Int, lazyDescription: (Int) -> String = { value ->
            "Expected a number at least $min, but got $value"
        }
    ) : BeInRange(min to Int.MAX_VALUE)

    /**
     * Represents a requirement that an integer value must be at most a specified value.
     *
     * @property max The maximum allowed value.
     */
    class BeAtMost(
        max: Int
    ) : BeInRange(Int.MIN_VALUE to max)

    /**
     * Represents a requirement that an integer value must be equal to a specified value.
     *
     * @property expected The expected value.
     */
    class BeEqualTo(
        private val expected: Int
    ) : IntRequirement {

        override val validator = { value: Int -> value == expected }
    }
}