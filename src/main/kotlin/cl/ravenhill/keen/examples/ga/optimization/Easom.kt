package cl.ravenhill.keen.examples.ga.optimization

import cl.ravenhill.keen.evolution.Engine
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
    lateinit var engine: Engine<Double, DoubleGene>
    println("========= Random selector =========")
    repeat(2) {
        engine = createEngine(::easom, -100.0..100.0, -100.0..100.0)
        engine.evolve()
    }
    println(engine.listeners.first())
    println("========= Tournament selector =========")
    repeat(2) {
        engine = createEngine(::easom, -100.0..100.0, -100.0..100.0)
        engine.evolve()
    }
    println(engine.listeners.first())
    println("========= Roulette wheel selector =========")
    repeat(2) {
        engine = createEngine(::easom, -100.0..100.0, -100.0..100.0)
        engine.evolve()
    }
    println(engine.listeners.first())
}