/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.benchmarks.numopt.functions

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow

data object Easom : OptimizationProblem {

    override val name = "Easom"
    override val target = -1.0

    private const val MIN = -100.0
    private const val MAX = 100.0
    override val ranges: List<ClosedRange<Double>> = listOf(MIN..MAX, MIN..MAX)
    override fun invoke(genotype: Genotype<Double, DoubleGene>) = genotype.flatten().let { (x, y) ->
        -cos(x) * cos(y) * exp(-(x - PI).pow(2) - (y - PI).pow(2))
    }
}
