/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.examples.ga.optimization

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import kotlin.math.pow

private fun beale(genotype: Genotype<Double, DoubleGene>): Double {
    val (x, y) = genotype.flatten()
    return (1.5 - x + x * y).pow(2) + (2.25 - x + x * y.pow(2)).pow(2) +
        (2.625 - x + x * y.pow(3)).pow(2)
}

fun main() {
    val engine = createEngine(::beale, -4.5 to 4.5)
    val result = engine.evolve()
    println("Result: $result")
    engine.listeners.last().let { "$it" }.let(::println)
}
