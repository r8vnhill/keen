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

