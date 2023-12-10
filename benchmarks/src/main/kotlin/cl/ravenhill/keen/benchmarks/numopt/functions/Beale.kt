/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.benchmarks.numopt.functions

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import kotlin.math.pow

data object Beale : OptimizationProblem {

    override val name = "Beale"
    override val target = 0.0

    private const val MIN = -4.5
    private const val MAX = 4.5
    override val ranges: List<ClosedRange<Double>> = listOf(MIN..MAX)

    private const val A = 1.5
    private const val B = 2.25
    private const val C = 2.625

    override fun invoke(genotype: Genotype<Double, DoubleGene>) = genotype.flatten().let { (x, y) ->
        (A - x + x * y).pow(n = 2) + (B - x + x * y.pow(n = 2)).pow(2) + (C - x + x * y.pow(n = 3)).pow(n = 2)
    }
}
