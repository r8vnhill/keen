/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.examples.ga.optimization

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.keen.util.listeners.EvolutionPlotter
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Calculates the Ackley function's value for a given genotype.
 *
 * The Ackley function is a well-known optimization test problem. It has a global minimum at (0, 0)
 * where its value is 0. The function is characterized by a nearly flat outer region and a large
 * hole at the center, which poses challenges for optimization algorithms. The genotype used here is
 * expected to contain two [DoubleGene] genes representing `x` and `y`.
 *
 * @param genotype The genotype containing two double genes representing `x` and `y`.
 * @return The value of the Ackley function for the given genotype.
 */
private fun ackley(genotype: Genotype<Double, DoubleGene>): Double {
    val (x, y) = genotype.flatten()
    return -20 * exp(-0.2 * sqrt(0.5 * (x.pow(2) + y.pow(2)))) -
        exp(0.5 * (cos(2 * PI * x) + cos(2 * PI * y))) + exp(1.0) + 20.0
}

/**
 * Initializes an evolutionary computation engine using the Ackley function
 * and a gene range of -5.0 to 5.0. It then evolves the population and prints the
 * output from the last listener attached to the engine.
 *
 * The `createEngine` function, used in this method, sets up an evolutionary engine
 * with a given fitness function and range for the genes. The Ackley function
 * serves as the fitness function in this example.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
fun main() {
    val engine = createEngine(::ackley, -5.0..5.0, -5.0..5.0)
    engine.evolve()
    println(engine.listeners.last())
    (engine.listeners.first() as EvolutionPlotter).displayFitness()
}
