package cl.ravenhill.keen

/***************************************************************************************************
 * This code defines a hierarchy of exception classes for the Keen library.
 * The base class KeenException is extended by other classes that represent specific types of
 * exceptions.
 * Each exception class has a constructor that takes a lazyMessage lambda expression, which provides
 * the message to be displayed when the exception is thrown.
 * The exception classes are organized based on the type of error that caused the exception, such as
 * an invalid argument or an unfulfilled requirement.
 * These classes help to provide informative error messages to users of the library.
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
        Exception("$prefix ${lazyMessage()}")

/**
 * Exception thrown when the receiver of a function is not valid.
 *
 * @param lazyMessage The message to be used in the exception.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 1.0.0
 * @since 1.0.0
 */
class InvalidReceiverException(lazyMessage: () -> String) :
        KeenException("Invalid receiver: ", lazyMessage)

/**
 * Exception thrown when an argument is not valid.
 *
 * @param lazyMessage The message to be used in the exception.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 1.0.0
 * @since 1.0.0
 */
class InvalidArgumentException(lazyMessage: () -> String) :
        KeenException("Invalid argument: ", lazyMessage)

/**
 * Exception thrown when a state is not valid.
 *
 * @param state The state that is not valid.
 * @param lazyMessage The message to be used in the exception.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 1.0.0
 * @since 1.0.0
 */
class InvalidStateException(state: String, lazyMessage: () -> String) :
        KeenException("Invalid state ($state): ", lazyMessage)

/**
 * Exception thrown when a limit is not configured correctly.
 *
 * @param lazyMessage The message to be used in the exception.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 1.0.0
 * @since 1.0.0
 */
class LimitConfigurationException(lazyMessage: () -> String) :
        KeenException("Genotype configuration error:", lazyMessage)

/**
 * Base exception for unfulfilled requirements.
 *
 * @param lazyMessage The message to be used in the exception.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
open class UnfulfilledRequirementException(lazyMessage: () -> String) :
        KeenException("Unfulfilled constraint: ", lazyMessage)

/**
 * Exception thrown when an integer constraint is not fulfilled.
 *
 * @param lazyMessage The message to be used in the exception.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
class IntRequirementException(lazyMessage: () -> String) :
        UnfulfilledRequirementException(lazyMessage)

/**
 * Exception thrown when a long constraint is not fulfilled.
 *
 * @param lazyMessage The message to be used in the exception.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
class LongRequirementException(lazyMessage: () -> String) :
        UnfulfilledRequirementException(lazyMessage)

/**
 * Exception thrown when a pair constraint is not fulfilled.
 *
 * @param lazyMessage The message to be used in the exception.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class PairRequirementException(lazyMessage: () -> String) :
        UnfulfilledRequirementException(lazyMessage)

/**
 * Exception thrown when a double constraint is not fulfilled.
 *
 * @param lazyMessage The message to be used in the exception.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
class DoubleRequirementException(lazyMessage: () -> String) :
        UnfulfilledRequirementException(lazyMessage)

/**
 * Exception thrown when a collection constraint is not fulfilled.
 *
 * @param lazyMessage The message to be used in the exception.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class CollectionRequirementException(lazyMessage: () -> String) :
        UnfulfilledRequirementException(lazyMessage)

/**
 * Exception thrown when a contract is not fulfilled.
 *
 * @param violations List of contract violations.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
class EnforcementException(val violations: List<Throwable>) : KeenException(
    "Unfulfilled contract: ",
    { violations.joinToString(", ") { "{ ${it.message} }" } })
