/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.enforcer.requirements

import cl.ravenhill.enforcer.UnfulfilledRequirementException


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
interface Requirement<T> {
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
     * @return A new instance of [UnfulfilledRequirementException] with the given message.
     */
    fun generateException(description: String): UnfulfilledRequirementException
}
