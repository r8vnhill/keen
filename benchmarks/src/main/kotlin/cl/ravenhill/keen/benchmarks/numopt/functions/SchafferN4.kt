/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.benchmarks.numopt.functions

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import kotlin.math.pow
import kotlin.math.sin

data object SchafferN4 : OptimizationProblem {

    override val name = "Schaffer N.4"
    override val target = 0.0

    private const val MIN = -100.0
    private const val MAX = 100.0
    override val ranges: List<ClosedRange<Double>> = listOf(MIN..MAX, MIN..MAX)

    override fun invoke(genotype: Genotype<Double, DoubleGene>) = genotype.flatten().let { (x, y) ->
        0.5 + ((sin(x.pow(2) - y.pow(2)).pow(2) - 0.5) / (1 + 0.001 * (x.pow(2) + y.pow(2))).pow(2))
    }
}