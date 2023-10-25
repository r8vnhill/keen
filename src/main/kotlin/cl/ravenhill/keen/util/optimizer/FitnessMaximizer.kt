/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.optimizer

import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene

/**
 * [IndividualOptimizer] that prioritizes the maximum fitness value.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
class FitnessMaximizer<DNA, G : Gene<DNA, G>> : IndividualOptimizer<DNA, G> {

    override fun compare(p1: Individual<*, *>, p2: Individual<*, *>) =
        p1.fitness compareTo p2.fitness
}
