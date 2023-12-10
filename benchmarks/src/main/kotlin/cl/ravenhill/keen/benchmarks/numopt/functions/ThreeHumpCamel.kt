/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.benchmarks.numopt.functions

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import kotlin.math.pow

data object ThreeHumpCamel : OptimizationProblem {

        override val name = "Three-Hump Camel"
        override val target = 0.0

        private const val MIN = -5.0
        private const val MAX = 5.0
        override val ranges: List<ClosedRange<Double>> = listOf(MIN..MAX, MIN..MAX)

    override fun invoke(genotype: Genotype<Double, DoubleGene>) = genotype.flatten().let { (x, y) ->
        2 * x.pow(2) - 1.05 * x.pow(4) + x.pow(6) / 6 + x * y + y.pow(2)
    }
}