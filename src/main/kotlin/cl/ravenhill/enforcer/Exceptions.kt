/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.enforcer

/***************************************************************************************************
 * This file defines a hierarchy of custom exceptions for unfulfilled requirements.
 * The hierarchy is based on the open class `UnfulfilledRequirementException`, which itself extends
 * `Exception`.
 * Specific exceptions for various data types (integer, long, pair, double, and collection) are
 * derived from `UnfulfilledRequirementException`, enabling the system to raise specific exceptions
 * based on the type of unfulfilled requirement.
 * Furthermore, `EnforcementException` is used to handle situations where a contract as a whole is
 * unfulfilled, and it aggregates a list of violations for clearer error reporting.
 **************************************************************************************************/

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
        Exception("Unfulfilled constraint: ${lazyMessage()}")

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
 * @param infringements List of contract violations.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
class EnforcementException(val infringements: List<Throwable>) : Exception(
    "Unfulfilled contract: [ " +
            infringements.joinToString(", ") { "{ ${it.message} }" } +
            " ]"
)