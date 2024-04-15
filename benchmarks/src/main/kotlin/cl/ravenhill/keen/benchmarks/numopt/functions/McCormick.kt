/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.benchmarks.numopt.functions

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import kotlin.math.pow
import kotlin.math.sin

data object McCormick : OptimizationProblem {

    override val name = "McCormick"
    override val target = -1.9133

    private const val MIN_X = -1.5
    private const val MAX_X = 4.0
    private const val MIN_Y = -3.0
    private const val MAX_Y = 4.0
    override val ranges: List<ClosedRange<Double>> = listOf(MIN_X..MAX_X, MIN_Y..MAX_Y)

    override fun invoke(genotype: Genotype<Double, DoubleGene>) = genotype.flatten().let { (x, y) ->
        sin(x + y) + (x - y).pow(2) - 1.5 * x + 2.5 * y + 1.0
    }
}