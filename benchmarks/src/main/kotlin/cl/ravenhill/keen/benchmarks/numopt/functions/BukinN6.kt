/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.benchmarks.numopt.functions

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import kotlin.math.pow

data object BukinN6 : OptimizationProblem {

    override val name = "Bukin N.6"
    override val target = 0.0

    private const val MIN_X = -15.0
    private const val MAX_X = -5.0
    private const val MIN_Y = -3.0
    private const val MAX_Y = 3.0
    override val ranges: List<ClosedRange<Double>> = listOf(MIN_X..MAX_X, MIN_Y..MAX_Y)

    override fun invoke(genotype: Genotype<Double, DoubleGene>) = genotype.flatten().let { (x, y) ->
        100 * (y - 0.01 * x.pow(2) + 1).pow(2) + 0.01 * (x + 10).pow(2)
    }
}
