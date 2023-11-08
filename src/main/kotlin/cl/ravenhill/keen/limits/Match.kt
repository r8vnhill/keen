/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.Engine

/**
 * Represents a termination condition (or "limit") for the evolutionary engine based on a custom predicate.
 *
 * This limit is evaluated as a function that checks whether a given condition is true for the current state
 * of the evolution engine. If the predicate evaluates to true, it signals that the evolutionary process
 * should terminate.
 *
 * @property predicate A lambda function that takes the evolution engine as a receiver and returns a Boolean.
 *                     It defines the condition upon which the evolutionary process should stop.
 *
 * @constructor Creates a [Match] limit with a specified predicate.
 *
 * @see Engine The evolution engine that this limit will be applied to.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 1.0.0
 * @version 2.0.0
 */
open class Match(private val predicate: Engine<*, *>.() -> Boolean) : Limit {

    /**
     * Invokes the predicate on the given [Engine] instance to determine if the evolution should stop.
     *
     * @param engine The [Engine] instance on which to evaluate the predicate.
     * @return A Boolean indicating whether the limit has been reached according to the predicate.
     */
    override fun invoke(engine: Engine<*, *>) = predicate(engine)

    /**
     * Destructures this [Match] object, allowing its internal predicate to be used in a component-like manner.
     *
     * @return The predicate function that defines the limit condition.
     */
    operator fun component1() = predicate
}
