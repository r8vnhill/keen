/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BePositive

/**
 * Represents a limit based on the number of consecutive generations without fitness improvement.
 * This [MatchLimit] evaluates if the population's fitness has remained unchanged (steady) for a specified
 * number of generations, indicating a potential convergence of the genetic algorithm.
 *
 * @property generations The number of generations for which the fitness should remain steady to meet the limit.
 *             A positive value is required to ensure a valid check.
 *
 * When the limit is reached, it signals that the population may have reached an evolutionary plateau,
 * potentially triggering the termination of the evolution process.
 *
 * Usage in an evolutionary algorithm setup might look like:
 * ```
 * val evolutionAlgorithm = Engine.builder { ... }
 *     .withLimit(SteadyGenerations(5))
 *     .build()
 * ```
 *
 * @throws ConstraintException if [generations] is not positive, ensuring the parameter is valid for the evolutionary process.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 1.0.0
 * @version 2.0.0
 */
data class SteadyGenerations(val generations: Int) : MatchLimit({ steadyGenerations >= generations }) {
    init {
        constraints {
            "Number of steady generations [$generations] must be a positive integer" { generations must BePositive }
        }
    }
}

