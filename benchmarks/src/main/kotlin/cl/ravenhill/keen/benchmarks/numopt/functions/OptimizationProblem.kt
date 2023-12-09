/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.benchmarks.numopt.functions

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene


/**
 * An interface representing an optimization problem in evolutionary computation.
 *
 * @property name The name of the optimization problem.
 * @property target The minimum value that represents the optimal solution for the problem.
 */
sealed interface OptimizationProblem {

    val name: String

    val target: Double

    val ranges: List<ClosedRange<Double>>

    /**
     * Calculates the fitness value of a given genotype according to the specific optimization problem.
     *
     * @param genotype The genotype to be evaluated, composed of `Double` values within `DoubleGene`.
     * @return The fitness value of the genotype as per the optimization problem's criteria.
     */
    operator fun invoke(genotype: Genotype<Double, DoubleGene>): Double
}
