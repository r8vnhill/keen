/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.examples.ga.optimization

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt

private fun ackley(genotype: Genotype<Double, DoubleGene>): Double {
    val (x, y) = genotype.flatten()
    return -20 * exp(-0.2 * sqrt(0.5 * (x.pow(2) + y.pow(2)))) -
        exp(0.5 * (cos(2 * PI * x) + cos(2 * PI * y))) + exp(1.0) + 20.0
}

fun main() {
    val engine = createEngine(::ackley, -5.0 to 5.0)
    engine.evolve()
    println(engine.listeners.last())
//    (engine.listeners.first() as EvolutionPlotter).displayFitness()
}
