package cl.ravenhill.keen.limits

/**
 * A [Limit] that checks whether the genetic algorithm has reached the target fitness.
 *
 * @param fitness The target fitness to reach.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 1.0.0
 */
data class TargetFitness(val fitness: Double) : Match({ bestFitness == fitness })
