package cl.ravenhill.keen

import java.util.Objects

/***************************************************************************************************
 * TODO: Add top-level file documentation.
 **************************************************************************************************/

/**
 * Base exception for all exceptions thrown by Keen.
 *
 * @param prefix The prefix to be used in the message.
 * @param lazyMessage The message to be used in the exception.
 *
 * @since 1.0.0
 * @version 1.0.0
 */
open class KeenException(prefix: String, lazyMessage: () -> String) :
        Exception("$prefix ${lazyMessage()}") {
    /// Documentation inherited from [Any].
    override fun toString() = "${this::class.simpleName} { message: $message }"
}

/**
 * Exception thrown when a state is not valid.
 *
 * @param state The state that is not valid.
 * @param lazyMessage The message to be used in the exception.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
class InvalidStateException(state: String, lazyMessage: () -> String) :
        KeenException("Invalid state ($state):", lazyMessage) {

    /// Documentation inherited from [Any].
    override fun equals(other: Any?) = when (other) {
        is InvalidStateException -> other.message == message
        else -> false
    }

    /// Documentation inherited from [Any].
    override fun hashCode() = Objects.hash(InvalidStateException::class, message)
}

/**
 * Represents an exception indicating that a specific operation is not allowed or supported.
 *
 * Use this exception when you need to signal that an operation isn't permitted
 * in the given context or under the current state of the application.
 * Its design allows for a lazy-evaluated message, optimizing for performance by constructing
 * the message only when throwing the exception.
 *
 * Typically, you'd use this exception to give clear, specific errors
 * when you encounter unsupported operations in classes or methods.
 *
 * @param lazyMessage A lambda function providing a detailed error message
 *                    about the specific reason for the exception.
 *                    The system only evaluates and constructs this message upon throwing the
 *                    exception.
 *
 * @constructor Creates an `IllegalOperationException` with a prefixed message "Illegal operation:"
 *              and appends the detailed reason provided by the `lazyMessage`.
 */
class IllegalOperationException(lazyMessage: () -> String) :
        KeenException("Illegal operation:", lazyMessage)


