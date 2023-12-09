/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.benchmarks.numopt.functions

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import kotlin.math.pow

data object GoldsteinPrice : OptimizationProblem {

    override val name = "Goldstein-Price"
    override val target = 3.0

    private const val MIN = -2.0
    private const val MAX = 2.0
    override val ranges: List<ClosedRange<Double>> = listOf(MIN..MAX, MIN..MAX)
    override fun invoke(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
        (1 + (x + y + 1).pow(2) * (19 - 14 * x + 3 * x.pow(2) - 14 * y + 6 * x * y + 3 * y.pow(2))) *
                (30 + (2 * x - 3 * y).pow(2) * (18 - 32 * x + 12 * x.pow(2) + 48 * y - 36 * x * y + 27 * y.pow(2)))
    }
}