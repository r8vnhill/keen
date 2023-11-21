/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.optimizer

import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene

/**
 * Optimizer that prioritizes individuals based on the minimum fitness value between two comparisons.
 *
 * This [IndividualOptimizer] is designed to select individuals with the lowest fitness values
 * in genetic algorithms or similar evolutionary computation models. It's useful in scenarios where
 * the objective is to minimize a certain value, such as in cost minimization problems.
 *
 * @param DNA The genetic data type of the individuals.
 * @param G The type representing the gene structures.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @version 2.0.0
 * @since 1.0.0
 */
class FitnessMinimizer<DNA, G> : IndividualOptimizer<DNA, G> where G : Gene<DNA, G> {

    /**
     * Compares the fitness of two individuals.
     *
     * Prioritizes the individual with the lower fitness value. This comparator function
     * will return a positive value if the first individual (`p1`) has a higher fitness
     * than the second one (`p2`), zero if their fitness values are equal, and a negative
     * value otherwise.
     *
     * @param p1 The first individual to compare.
     * @param p2 The second individual to compare.
     * @return A positive value if `p1` has a higher fitness than `p2`, zero if they're equal, and a negative value otherwise.
     */
    override fun compare(p1: Individual<*, *>, p2: Individual<*, *>) =
        p2.fitness compareTo p1.fitness
}
