/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.optimizer

import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene

/**
 * An optimizer that focuses on maximizing the fitness of individuals in a genetic algorithm.
 *
 * This class implements the [IndividualOptimizer] interface and provides mechanisms to compare
 * individuals based on their fitness values. It is designed to prioritize individuals with higher
 * fitness scores, making it suitable for optimization problems where the goal is to find the
 * maximum value of a fitness function.
 *
 * ### Functionality:
 * - **Fitness Comparison**: Implements a comparison method that evaluates individuals based on
 *   their fitness scores. An individual with a higher fitness score is considered "better" or
 *   more optimal.
 * - **Equality and HashCode**: Overrides standard `equals` and `hashCode` methods to ensure
 *   correct behavior in collections and when comparing optimizer instances.
 *
 * ### Usage:
 * Typically used in the configuration of a genetic algorithm where the objective is to maximize
 * a specific fitness metric. The `FitnessMaximizer` can be passed as a parameter to the genetic
 * algorithm engine to guide the selection process towards more fit individuals.
 *
 * @param DNA The type of data that represents an individual's genotype.
 * @param G The specific type of [Gene] that encapsulates the [DNA] type data.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 1.0.0
 * @version 2.0.0
 */
class FitnessMaximizer<DNA, G : Gene<DNA, G>> : IndividualOptimizer<DNA, G> {

    override fun compare(p1: Individual<*, *>, p2: Individual<*, *>) =
        p1.fitness compareTo p2.fitness

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FitnessMaximizer<*, *>) return false
        return true
    }

    override fun hashCode() = FitnessMaximizer::class.hashCode()
}
