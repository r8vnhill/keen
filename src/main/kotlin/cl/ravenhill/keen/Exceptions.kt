package cl.ravenhill.keen

/**
 * Base exception for all exceptions thrown by Keen.
 *
 * @param prefix The prefix to be used in the message.
 * @param lazyMessage The message to be used in the exception.
 */
open class KeenException(prefix: String, lazyMessage: () -> String) :
    Exception("$prefix ${lazyMessage()}")

/**
 * Exception thrown when the receiver of a function is not valid.
 */
class InvalidReceiverException(lazyMessage: () -> String) :
    KeenException("Invalid receiver: ", lazyMessage)

/**
 * Exception thrown when an argument is not valid.
 */
class InvalidArgumentException(lazyMessage: () -> String) :
    KeenException("Invalid argument: ", lazyMessage)

class InvalidStateException(state: String, lazyMessage: () -> String) :
    KeenException("Invalid state ($state): ", lazyMessage)

/**
 * Exception thrown when a limit is not configured correctly.
 */
class LimitConfigurationException(lazyMessage: () -> String) :
    KeenException("Genotype configuration error:", lazyMessage)

/**
 * Exception thrown when a selection operation fails.
 */
class SelectorException(lazyMessage: () -> String) :
    KeenException("Selector operation exception:", lazyMessage)


/**
 * Exception thrown when a constraint is not fulfilled.
 */
open class UnfulfilledConstraintException(lazyMessage: () -> String) :
    KeenException("Unfulfilled constraint: ", lazyMessage)

class IntConstraintException(lazyMessage: () -> String) :
    UnfulfilledConstraintException(lazyMessage)

/**
 * Exception thrown when a pair constraint is not fulfilled.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class PairConstraintException(lazyMessage: () -> String) :
    UnfulfilledConstraintException(lazyMessage)

class UnfulfilledContractException(val violations: List<Throwable>) : KeenException(
    "Unfulfilled contract: ",
    { violations.joinToString(", ") { "{ ${it.message} }" } })

