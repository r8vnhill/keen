package cl.ravenhill.keen.examples.ga.optimization

import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import kotlin.math.*

private fun crossInTray(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    -0.0001 * (abs(sin(x) * sin(y) * exp(abs(100 - sqrt(x.pow(2) + y.pow(2)) / PI))) + 1).pow(0.1)
}

fun main() {
    lateinit var engine: Engine<Double, DoubleGene>
    println()
    println("========= Random selector =========")
    repeat(2) {
        engine = createEngine(::crossInTray)
        engine.evolve()
    }
    println(engine.listeners.first())
    println()
    println("========= Tournament selector =========")
    repeat(2) {
        engine = createEngine(::crossInTray)
        engine.evolve()
    }
    println(engine.listeners.first())
    println()
    println("========= Roulette wheel selector =========")
    repeat(2) {
        engine = createEngine(::crossInTray)
        engine.evolve()
    }
    println(engine.listeners.first())
}