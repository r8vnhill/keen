/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.benchmarks.numopt.functions

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import kotlin.math.pow

data object Himmelblau : OptimizationProblem {

        override val name = "Himmelblau"
        override val target = 0.0

        private const val MIN = -5.0
        private const val MAX = 5.0
        override val ranges: List<ClosedRange<Double>> = listOf(MIN..MAX, MIN..MAX)

        override fun invoke(genotype: Genotype<Double, DoubleGene>): Double = genotype.flatMap().let { (x, y) ->
            (x.pow(2) + y - 11).pow(2) + (x + y.pow(2) - 7).pow(2)
        }
}