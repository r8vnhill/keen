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
 * Computes the Beale function value for a given genotype.
 *
 * The Beale function is a commonly used test function for optimization algorithms
 * and is defined as:
 * f(x,y) = (1.5 - x + xy)^2 + (2.25 - x + xy^2)^2 + (2.625 - x + xy^3)^2
 *
 * This function calculates and returns the value of the Beale function
 * for the given genotype's `x` and `y` values.
 *
 * @param genotype The genotype representing the solution, which contains genes for x and y values.
 * @return The computed Beale function value for the given genotype.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
private fun beale(genotype: Genotype<Double, DoubleGene>): Double {
    val (x, y) = genotype.flatMap()
    return (1.5 - x + x * y).pow(2) + (2.25 - x + x * y.pow(2)).pow(2) +
            (2.625 - x + x * y.pow(3)).pow(2)
}

/**
 * Initializes an evolutionary computation engine using the Beale function
 * and a gene range of -4.5 to 4.5. After evolving the population, the result and
 * output from the last listener attached to the engine are printed.
 *
 * The `createEngine` function sets up an evolutionary engine with a given fitness
 * function and range for the genes. The Beale function serves as the fitness function
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
        engine = createEngine(::beale, selector = RandomSelector())
        engine.evolve()
    }
    println(engine.listeners.first())
    println("========= Tournament selector =========")
    repeat(2) {
         engine = createEngine(::beale)
        engine.evolve()
    }
    println(engine.listeners.first())
    println("========= Roulette wheel selector =========")
    repeat(2) {
        engine = createEngine(::beale, selector = RouletteWheelSelector())
        engine.evolve()
    }
    println(engine.listeners.first())
}
