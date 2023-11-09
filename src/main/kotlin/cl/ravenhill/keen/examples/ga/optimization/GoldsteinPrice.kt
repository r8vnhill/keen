package cl.ravenhill.keen.examples.ga.optimization

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import kotlin.math.abs
import kotlin.math.pow

private fun goldsteinPrice(g: Genotype<Double, DoubleGene>)= g.flatMap().let { (x, y) ->
    (1 + (x + y + 1).pow(2) * (19 - 14 * x + 3 * x.pow(2) - 14 * y + 6 * x * y + 3 * y.pow(2))) *
            (30 + (2 * x - 3 * y).pow(2) * (18 - 32 * x + 12 * x.pow(2) + 48 * y - 36 * x * y + 27 * y.pow(2)))
}

fun main() {
    lateinit var engine: cl.ravenhill.keen.evolution.Engine<Double, DoubleGene>
    println("========= Random selector =========")
    repeat(2) {
        engine = createEngine(::goldsteinPrice, -2.0..2.0, -2.0..2.0)
        engine.evolve()
    }
    println(engine.listeners.first())
    println("Error: ${abs( - 3)}")
    println("========= Tournament selector =========")
    repeat(2) {
        engine = createEngine(::goldsteinPrice, -2.0..2.0, -2.0..2.0)
        engine.evolve()
    }
    println(engine.listeners.first())
    println("Error: ${abs( - 3)}")
    println("========= Roulette wheel selector =========")
    repeat(2) {
        engine = createEngine(::goldsteinPrice, -2.0..2.0, -2.0..2.0)
        engine.evolve()
    }
    println(engine.listeners.first())
    println("Error: ${abs( - 3)}")
}