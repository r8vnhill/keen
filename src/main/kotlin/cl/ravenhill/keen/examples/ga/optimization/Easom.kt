package cl.ravenhill.keen.examples.ga.optimization

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow

private fun easom(genotype: Genotype<Double, DoubleGene>) = genotype.flatten().let { (x, y) ->
    -cos(x) * cos(y) * exp(-(x - PI).pow(2) - (y - PI).pow(2))
}

fun main() {
    val engine = createEngine(::easom, -100.0..100.0, -100.0..100.0)
    val result = engine.evolve()
    println("Result: $result")
    engine.listeners.last().let { "$it" }.let(::println)
}