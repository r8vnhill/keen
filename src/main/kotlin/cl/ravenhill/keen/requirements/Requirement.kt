package cl.ravenhill.keen.requirements

import cl.ravenhill.keen.UnfulfilledRequirementException


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
     * Validates that the given value satisfies a condition based on a validator function.
     * If the value does not satisfy the condition, a failure result with a generated exception
     * containing the specified message is returned.
     * Otherwise, a success result with the original value is returned.
     *
     * @param value the value to validate.
     * @param message the message to include in the generated exception if the validation fails.
     * @return a [Result] object that represents the validation result.
     */
    fun validate(value: T, message: String): Result<T> = if (!validator(value)) {
        Result.failure(generateException(message))
    } else {
        Result.success(value)
    }

    /**
     * Validates that the given value does not satisfy a condition based on a validator function.
     * If the value satisfies the condition, a failure result with a generated exception containing
     * the specified message is returned.
     * Otherwise, a success result with the original value is returned.
     *
     * @param value the value to validate.
     * @param message the message to include in the generated exception if the validation fails.
     * @return a [Result] object that represents the validation result.
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
