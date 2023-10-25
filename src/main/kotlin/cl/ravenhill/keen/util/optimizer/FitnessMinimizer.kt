/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.optimizer

import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene

/**
 * [IndividualOptimizer] that prioritizes the minimum of two values.
 */
class FitnessMinimizer<DNA, G> : IndividualOptimizer<DNA, G> where G : Gene<DNA, G> {
    override fun compare(p1: Individual<*, *>, p2: Individual<*, *>) =
        p2.fitness compareTo p1.fitness

    override fun toString() = "FitnessMinimizer"
}
