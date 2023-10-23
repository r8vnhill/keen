/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.benchmarks.optimization

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt

fun ackley(genotype: Genotype<Double, DoubleGene>) = genotype.flatten().let { (x, y) ->
    -20 * exp(-0.2 * sqrt(0.5 * (x.pow(2) + y.pow(2)))) -
        exp(0.5 * (cos(2 * PI * x) + cos(2 * PI * y))) + exp(1.0) + 20.0
}
