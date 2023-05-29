/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.Engine

/**
 * This class represents a limit/stopping condition for a genetic algorithm that is defined by a
 * [predicate].
 * It implements the [Limit] interface, which defines a single [invoke] function that is called to
 * check whether the limit has been reached.
 *
 * @property predicate A lambda expression that takes an [Engine] object as its receiver and returns
 * a boolean value indicating whether the limit has been reached or not.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
open class Match(private val predicate: Engine<*, *>.() -> Boolean) : Limit {
    /// Documentation inherited from [Limit]
    override fun invoke(engine: Engine<*, *>) = predicate(engine)

    /// Documentation inherited from [Any]
    override fun toString() = "Match($predicate)"
}