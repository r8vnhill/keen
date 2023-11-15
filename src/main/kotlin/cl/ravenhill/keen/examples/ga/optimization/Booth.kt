/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.examples.ga.optimization

import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.keen.operators.selector.RandomSelector
import cl.ravenhill.keen.operators.selector.RouletteWheelSelector
import kotlin.math.pow

/**
 * Computes the Booth's function value for a given genotype.
 *
 * The Booth's function is a test problem for optimization. It is defined as:
 * f(x,y) = (x + 2y - 7)^2 + (2x + y - 5)^2
 *
 * This function has a global minimum at x = 1 and y = 3.
 *
 * @param genotype The genotype representing the solution, which contains genes for x and y values.
 * @return The computed Booth's function value for the given genotype.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
private fun booth(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    (x + 2 * y - 7).pow(2) + (2 * x + y - 5).pow(2)
}

/**
 * Initializes an evolutionary computation engine using the Booth's function
 * and a gene range of -10.0 to 10.0. After evolving the population, the result and
 * output from the last listener attached to the engine are printed.
 *
 * The `createEngine` function sets up an evolutionary engine with a given fitness
 * function and range for the genes. The Booth's function serves as the fitness function
 * in this example.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
fun main() {
    lateinit var engine: Engine<Double, DoubleGene>
    println("========= Random selector =========")
    repeat(2) {
        engine = createEngine(::booth, selector = RandomSelector())
        engine.evolve()
    }
    println(engine.listeners.first())
    println("========= Tournament selector =========")
    repeat(2) {
        engine = createEngine(::booth)
        engine.evolve()
    }
    println(engine.listeners.first())
    println("========= Roulette wheel selector =========")
    repeat(2) {
        engine = createEngine(::booth, selector = RouletteWheelSelector())
        engine.evolve()
    }
    println(engine.listeners.first())
}
