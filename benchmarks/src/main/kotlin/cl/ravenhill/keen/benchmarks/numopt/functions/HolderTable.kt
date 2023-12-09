/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.benchmarks.numopt.functions

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

data object HolderTable : OptimizationProblem {

    override val name = "Holder Table"
    override val target = -19.2085

    private const val MIN = -10.0
    private const val MAX = 10.0
    override val ranges: List<ClosedRange<Double>> = listOf(MIN..MAX, MIN..MAX)

    override fun invoke(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
        -abs(sin(x) * cos(y) * exp(abs(1 - sqrt(x.pow(2) + y.pow(2)) / PI)))
    }
}