package cl.ravenhill.keen.examples.ga.optimization

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import kotlin.math.*

private fun crossInTray(genotype: Genotype<Double, DoubleGene>) = genotype.flatten().let { (x, y) ->
    -0.0001 * (abs(sin(x) * sin(y) * exp(abs(100 - sqrt(x.pow(2) + y.pow(2)) / PI))) + 1).pow(0.1)
}

fun main() {
    val engine = createEngine(::crossInTray, -10.0..10.0, -10.0..10.0)
    val result = engine.evolve()
    println("Result: $result")
    engine.listeners.last().let { "$it" }.let(::println)
}