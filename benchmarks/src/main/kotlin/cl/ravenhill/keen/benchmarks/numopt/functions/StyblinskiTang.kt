/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.benchmarks.numopt.functions

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import kotlin.math.pow


data object StyblinskiTang : OptimizationProblem {

    override val name = "Styblinski-Tang"
    override val target = -39.16616570377142

    private const val MIN = -5.0
    private const val MAX = 5.0
    override val ranges: List<ClosedRange<Double>> = listOf(MIN..MAX, MIN..MAX)

    override fun invoke(genotype: Genotype<Double, DoubleGene>) = genotype.flatten().let { (x, y) ->
        (x.pow(4) - 16 * x.pow(2) + 5 * x + y.pow(4) - 16 * y.pow(2) + 5 * y) / 2
    }
}