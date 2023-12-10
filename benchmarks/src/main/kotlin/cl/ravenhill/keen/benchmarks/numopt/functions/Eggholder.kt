/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.benchmarks.numopt.functions

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import kotlin.math.abs
import kotlin.math.sin
import kotlin.math.sqrt

data object Eggholder : OptimizationProblem {
    override val name = "Eggholder"
    override val target = -959.6407
    override val ranges: List<ClosedRange<Double>> = listOf(-512.0..512.0, -512.0..512.0)

    override fun invoke(genotype: Genotype<Double, DoubleGene>) = genotype.flatten().let { (x, y) ->
        -(y + 47) * sin(sqrt(abs(x / 2 + (y + 47)))) - x * sin(sqrt(abs(x - (y + 47))))
    }
}