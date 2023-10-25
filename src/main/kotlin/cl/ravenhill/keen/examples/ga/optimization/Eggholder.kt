package cl.ravenhill.keen.examples.ga.optimization

import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import kotlin.math.abs
import kotlin.math.sin
import kotlin.math.sqrt

private fun eggholder(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    -(y + 47) * sin(sqrt(abs(x / 2 + (y + 47)))) - x * sin(sqrt(abs(x - (y + 47))))
}

fun main() {
    lateinit var engine: Engine<Double, DoubleGene>
    println("========= Random selector =========")
    repeat(2) {
        engine = createEngine(::eggholder, -512.0..512.0, -512.0..512.0)
        engine.evolve()
    }
    println(engine.listeners.first())
    println("========= Tournament selector =========")
    repeat(2) {
        engine = createEngine(::eggholder, -512.0..512.0, -512.0..512.0)
        engine.evolve()
    }
    println(engine.listeners.first())
    println("========= Roulette wheel selector =========")
    repeat(2) {
        engine = createEngine(::eggholder, -512.0..512.0, -512.0..512.0)
        engine.evolve()
    }
    println(engine.listeners.first())
}