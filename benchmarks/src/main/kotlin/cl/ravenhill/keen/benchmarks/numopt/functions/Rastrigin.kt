/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.benchmarks.numopt.functions

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow

data object Rastrigin : OptimizationProblem {

    override val name = "Rastrigin"
    override val target = 0.0

    private const val MIN = -5.12
    private const val MAX = 5.12
    override val ranges: List<ClosedRange<Double>> = listOf(MIN..MAX, MIN..MAX)

    override fun invoke(genotype: Genotype<Double, DoubleGene>) = genotype.flatten().let { (x, y) ->
        20 + x.pow(2) - 10 * x * cos(2 * PI * x) + y.pow(2) - 10 * y * cos(2 * PI * y)
    }
}