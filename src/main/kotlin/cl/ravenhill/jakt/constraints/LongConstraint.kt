/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.jakt.constraints

import cl.ravenhill.jakt.exceptions.LongRequirementException


/**
 * A LongRequirement is a Requirement that is applicable to Long values.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
sealed interface LongConstraint : Constraint<Long> {

    /**
     * Generates a [LongRequirementException] using the provided [description].
     *
     * @param description The description to use in the generated exception.
     * @return A new instance of [LongRequirementException] using the provided [description].
     */
    override fun generateException(description: String) =
        cl.ravenhill.jakt.exceptions.LongRequirementException { description }

    /**
     * A [BeEqualTo] requirement checks whether a given Long value is equal to an expected value.
     *
     * @param expected The expected value to compare with.
     */
    class BeEqualTo(val expected: Long) : LongConstraint {

        /**
         * The validator function for this requirement.
         */
        override val validator = { value: Long -> value == expected }

        /// Documentation inherited from [Any].
        override fun toString() = "BeEqualTo { expected: $expected }"
    }
}
