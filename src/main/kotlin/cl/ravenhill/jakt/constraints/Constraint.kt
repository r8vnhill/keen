/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.jakt.constraints

import cl.ravenhill.jakt.exceptions.ConstraintException


/**
 * A constraint that can be applied to a value of type [T].
 *
 * @param T The type of the value this constraint can be applied to.
 *
 * @property validator A function that checks if a value satisfies the constraint.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Constraint<T> {
    val validator: (T) -> Boolean

    /**
     * Validates the given value using the provided validator function and returns a [Result]
     * indicating the outcome.
     * If the validator function returns `false`, a failure [Result] is returned with the generated
     * exception message.
     * Otherwise, a success [Result] is returned with the original value.
     *
     * @param value the value to validate.
     * @param message the error message to use in case of validation failure.
     * @return a [Result] indicating the validation outcome.
     */
    fun validate(value: T, message: String): Result<T> = if (!validator(value)) {
        Result.failure(generateException(message))
    } else {
        Result.success(value)
    }

    /**
     * Validates the given value using the provided validator function and returns a [Result]
     * indicating the outcome.
     * If the validator function returns `true`, a failure [Result] is returned with the generated
     * exception message.
     * Otherwise, a success [Result] is returned with the original value.
     *
     * @param value the value to validate.
     * @param message the error message to use in case of validation failure.
     * @return a [Result] indicating the validation outcome.
     */
    fun validateNot(value: T, message: String): Result<T> = if (validator(value)) {
        Result.failure(generateException(message))
    } else {
        Result.success(value)
    }

    /**
     * Generates an exception with the given description.
     *
     * @return A new instance of [ConstraintException] with the given message.
     */
    fun generateException(description: String): ConstraintException
}
