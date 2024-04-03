package cl.ravenhill.keen.exceptions

import cl.ravenhill.jakt.exceptions.ConstraintException

/**
 * Represents an exception related to issues within the genetic algorithm's engine, extending [ConstraintException].
 * This exception is used to indicate problems that occur during the execution or configuration of the evolutionary
 * algorithm engine, such as invalid parameters or operational errors.
 *
 * @param message A descriptive message detailing the specific issue encountered within the engine.
 */
class EngineException(message: String) : ConstraintException({ message })
