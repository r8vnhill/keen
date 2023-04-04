package cl.ravenhill.keen.requirements

import cl.ravenhill.keen.IntRequirementException

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
    class BePositive(
        override val lazyDescription: (Int) -> String = { value ->
            "Expected a positive number, but got $value"
        }
    ) : IntRequirement {
        override val validator = { value: Int -> value > 0 }
    }

    /**
     * Represents a requirement that an integer value must be within a specified [range].
     *
     * @property range The range of values that are allowed.
     */
    open class BeInRange(
        private val range: IntRange,
        override val lazyDescription: (Int) -> String = { value ->
            "Expected a number in range $range, but got $value"
        }
    ) : IntRequirement {

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
    ) : BeInRange(min..Int.MAX_VALUE, { lazyDescription(min) })

    /**
     * Represents a requirement that an integer value must be at most a specified value.
     *
     * @property max The maximum allowed value.
     */
    class BeAtMost(
        max: Int,
        lazyDescription: (Int) -> String = {
            "Expected a number at most $max, but got $it"
        }
    ) : BeInRange(Int.MIN_VALUE..max, { lazyDescription(max) })

    /**
     * Represents a requirement that an integer value must be equal to a specified value.
     *
     * @property expected The expected value.
     */
    class BeEqualTo(
        private val expected: Int,
        override val lazyDescription: (Int) -> String = { value ->
            "Expected $expected, but got $value"
        }
    ) : IntRequirement {

        override val validator = { value: Int -> value == expected }
    }
}