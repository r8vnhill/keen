package cl.ravenhill.keen.examples.ga.optimization

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import kotlin.math.pow

private fun himmelblau(g: Genotype<Double, DoubleGene>) = g.flatMap().let { (x, y) ->
    (x.pow(2) + y - 11).pow(2) + (x + y.pow(2) - 7).pow(2)
}

fun main() {
    lateinit var engine: cl.ravenhill.keen.evolution.Engine<Double, DoubleGene>
    println("========= Random selector =========")
    repeat(2) {
        engine = createEngine(::himmelblau, -10.0..10.0, -10.0..10.0)
        engine.evolve()
    }
    println(engine.listeners.first())
//    println("Error: ${engine.bestFitness}")
//    println("========= Tournament selector =========")
//    repeat(2) {
//        engine = createEngine(::himmelblau, -10.0..10.0, -10.0..10.0)
//        engine.evolve()
//    }
//    println(engine.listeners.first())
//    println("Error: ${engine.bestFitness}")
//    println("========= Roulette wheel selector =========")
//    repeat(2) {
//        engine = createEngine(::himmelblau, -10.0..10.0, -10.0..10.0)
//        engine.evolve()
//    }
//    println(engine.listeners.first())
//    println("Error: ${engine.bestFitness}")
}