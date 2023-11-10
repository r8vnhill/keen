/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BePositive

/**
 * Represents a termination criterion for a genetic algorithm based on a fixed number of generations.
 *
 * This limit is used to stop the evolutionary process once a certain number of generations
 * have been processed. It encapsulates a simple condition that checks if the current generation
 * number meets or exceeds a predefined count.
 *
 * @property count The maximum number of generations to process before stopping the evolution.
 *                 Must be a positive integer, representing the stopping point of the evolution.
 * @constructor Initializes a `GenerationCount` with a given generation count.
 *
 * The constructor also imposes a constraint that the provided `count` must be a positive integer.
 * This ensures that the evolutionary process will run for at least one generation.
 */
data class GenerationCount(val count: Int) : MatchLimit({ generation >= count }) {
    init {
        constraints { "Generation count [$count] must be at least 1" { count must BePositive } }
    }
}

