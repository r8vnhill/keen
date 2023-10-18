/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.examples.ga.optimization

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.keen.util.listeners.EvolutionPlotter
import kotlin.math.pow

private fun bukinN6(genotype: Genotype<Double, DoubleGene>) = genotype.flatten().let { (x, y) ->
    100 * (y - 0.01 * x.pow(2) + 1).pow(2) + 0.01 * (x + 10).pow(2)
}

fun main() {
    val engine = createEngine(::bukinN6, -15.0..-5.0, -3.0..3.0)
    val result = engine.evolve()
    println("Result: $result")
    (engine.listeners.first() as EvolutionPlotter).displayFitness()
    engine.listeners.last().let { "$it" }.let(::println)
}
