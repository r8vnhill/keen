/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.evolution.Evolver

/**
 * A `Limit` represents a stopping condition for a genetic algorithm.
 * It defines a function that takes an [Engine] object and returns a boolean indicating whether the
 * algorithm should stop.
 * If the function returns `true`, the algorithm will stop; otherwise, it will continue running.
 *
 * Limits can be used to define a wide range of stopping conditions for a genetic algorithm.
 * For example, you might use a limit to stop the algorithm after a certain number of generations,
 * when a specific fitness threshold has been reached, or when the population has converged to a
 * stable solution.
 *
 * To create a new limit, you can implement this interface and define your own stopping condition in
 * the [invoke] method.
 *
 * @see MatchLimit
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
interface Limit {

    /**
     * Defines the stopping condition for the genetic algorithm.
     *
     * @param engine the engine object that is running the algorithm
     * @return a boolean indicating whether the algorithm should stop
     */
    operator fun invoke(engine: Evolver<*, *>): Boolean
}

