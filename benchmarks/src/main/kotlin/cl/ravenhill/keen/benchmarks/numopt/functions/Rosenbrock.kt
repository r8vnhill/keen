/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.benchmarks.numopt.functions

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import kotlin.math.pow

data object Rosenbrock : OptimizationProblem {

    override val name = "Rosenbrock"
    override val target = 0.0

    private const val MIN = -2.048
    private const val MAX = 2.048
    override val ranges: List<ClosedRange<Double>> = listOf(MIN..MAX, MIN..MAX)

    override fun invoke(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
        100 * (y - x.pow(2)).pow(2) + (1 - x).pow(2)
    }
}