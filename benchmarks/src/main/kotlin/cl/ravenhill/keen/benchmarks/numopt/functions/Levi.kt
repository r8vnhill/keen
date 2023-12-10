/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.benchmarks.numopt.functions

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sin

data object Levi : OptimizationProblem {
    override val name = "Levi"
    override val target = 0.0
    override val ranges = listOf(-10.0..10.0, -10.0..10.0)

    override fun invoke(genotype: Genotype<Double, DoubleGene>) = genotype.flatten().let { (x, y) ->
        sin(3 * PI * x).pow(2) + (x - 1).pow(2) * (1 + sin(3 * PI * y).pow(2)) +
              (y - 1).pow(2) * (1 + sin(2 * PI * y).pow(2))
    }
}
