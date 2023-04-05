/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.requirements

import cl.ravenhill.keen.LongRequirementException


/**
 * A LongRequirement is a Requirement that is applicable to Long values.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
sealed interface LongRequirement : Requirement<Long> {

    /**
     * Generates a [LongRequirementException] using the provided [description].
     *
     * @param description The description to use in the generated exception.
     * @return A new instance of [LongRequirementException] using the provided [description].
     */
    override fun generateException(description: String) = LongRequirementException { description }

    /**
     * A [BeEqualTo] requirement checks whether a given Long value is equal to an expected value.
     *
     * @param expected The expected value to compare with.
     * @param lazyDescription A lazily evaluated description of the requirement. It takes a Long parameter
     * and returns a String.
     */
    class BeEqualTo(
        private val expected: Long,
        override val lazyDescription: (Long) -> String = { value ->
            "Expected $expected, but got $value"
        }
    ) : LongRequirement {

        /**
         * The validator function for this requirement.
         */
        override val validator = { value: Long -> value == expected }
    }
}
