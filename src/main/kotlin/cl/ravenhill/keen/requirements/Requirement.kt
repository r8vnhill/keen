package cl.ravenhill.keen.requirements

import cl.ravenhill.keen.UnfulfilledRequirementException


/**
 * A constraint that can be applied to a value of type [T].
 *
 * @param T The type of the value this constraint can be applied to.
 *
 * @property lazyDescription A function that generates a description of the constraint violation,
 *  given a value that failed the validation.
 * @property validator A function that checks if a value satisfies the constraint.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Requirement<T> {
    val validator: (T) -> Boolean

    /**
     * Checks if the given value fulfills the constraint.
     *
     * @return A [Result] object that contains the original value if it satisfies the constraint,
     * or an exception with the description of the constraint violation otherwise.
     */
    fun validate(value: T, message: String): Result<T> = if (!validator(value)) {
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
