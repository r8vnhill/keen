package cl.ravenhill.keen.requirements

import cl.ravenhill.keen.IntRequirementException

/**
 * Represents a constraint that can be applied to an integer.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
sealed interface IntRequirement : Requirement<Int> {
    val lazyDescription: (Int) -> String
    val validator: (Int) -> Boolean

    override fun validate(value: Int): Result<Int> =
        if (!validator(value)) {
            Result.failure(IntRequirementException { lazyDescription(value) })
        } else {
            Result.success(value)
        }

    /**
     * Constraint that checks if an integer is positive.
     */
    class BePositive(
        override val lazyDescription: (Int) -> String = { value ->
            "Expected a positive number, but got $value"
        }
    ) : IntRequirement {
        override val validator = { value: Int -> value > 0 }
    }

    /**
     * Constraint that checks if an integer is in a given range.
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
     * Constraint that checks if an integer is at least a given value.
     */
    class BeAtLeast(
        min: Int, lazyDescription: (Int) -> String = { value ->
            "Expected a number at least $min, but got $value"
        }
    ) : BeInRange(min..Int.MAX_VALUE, { lazyDescription(min) })

    /**
     * Constraint that checks if an integer is at most a given value.
     */
    class BeAtMost(
        max: Int,
        lazyDescription: (Int) -> String = {
            "Expected a number at most $max, but got $it"
        }
    ) : BeInRange(Int.MIN_VALUE..max, { lazyDescription(max) })

    /**
     * Constraint that checks if an integer is equal to a given value.
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