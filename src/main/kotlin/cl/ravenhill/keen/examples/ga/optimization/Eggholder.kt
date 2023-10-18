package cl.ravenhill.keen.examples.ga.optimization

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import kotlin.math.abs
import kotlin.math.sin
import kotlin.math.sqrt

private fun eggholder(genotype: Genotype<Double, DoubleGene>) = genotype.flatten().let { (x, y) ->
    -(y + 47) * sin(sqrt(abs(x / 2 + (y + 47)))) - x * sin(sqrt(abs(x - (y + 47))))
}

fun main() {
    val engine = createEngine(::eggholder, -512.0..512.0, -512.0..512.0)
    val result = engine.evolve()
    println("Result: $result")
    engine.listeners.last().let { "$it" }.let(::println)
}