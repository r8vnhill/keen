/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.examples.ga.optimization

import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import kotlin.math.pow

private fun bukinN6(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    100 * (y - 0.01 * x.pow(2) + 1).pow(2) + 0.01 * (x + 10).pow(2)
}

fun main() {
    lateinit var engine: Engine<Double, DoubleGene>
    println("========= Random selector =========")
    repeat(2) {
        engine = createEngine(::bukinN6, -15.0..-5.0, -3.0..3.0)
        engine.evolve()
    }
    println(engine.listeners.first())
    println("========= Tournament selector =========")
    repeat(2) {
        engine = createEngine(::bukinN6, -15.0..-5.0, -3.0..3.0)
        engine.evolve()
    }
    println(engine.listeners.first())
    println("========= Roulette wheel selector =========")
    repeat(2) {
        engine = createEngine(::bukinN6, -15.0..-5.0, -3.0..3.0)
        engine.evolve()
    }
    println(engine.listeners.first())
}
