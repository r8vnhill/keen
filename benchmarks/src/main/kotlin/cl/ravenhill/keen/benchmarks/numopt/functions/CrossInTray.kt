/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.benchmarks.numopt.functions

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

data object CrossInTray : OptimizationProblem {

    override val name = "Cross-in-tray"
    override val target = -2.06261

    private const val MIN = -10.0
    private const val MAX = 10.0
    override val ranges: List<ClosedRange<Double>> = listOf(MIN..MAX, MIN..MAX)

    override fun invoke(genotype: Genotype<Double, DoubleGene>) = genotype.flatten().let { (x, y) ->
        -0.0001 * (abs(sin(x) * sin(y) * exp(abs(100 - sqrt(x.pow(2) + y.pow(2)) / PI))) + 1).pow(0.1)
    }
}