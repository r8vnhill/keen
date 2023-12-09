/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.benchmarks.numopt.functions

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import kotlin.math.pow

data object Matyas : OptimizationProblem {

    override val name = "Matyas"
    override val target = 0.0

    private const val MIN = -10.0
    private const val MAX = 10.0
    override val ranges: List<ClosedRange<Double>> = listOf(MIN..MAX, MIN..MAX)

    override fun invoke(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
        0.26 * (x.pow(2) + y.pow(2)) - 0.48 * x * y
    }
}