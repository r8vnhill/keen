/*
 *
 *  * "Keen" (c) by R8V.
 *  * "Keen" is licensed under a
 *  * Creative Commons Attribution 4.0 International License.
 *  * You should have received a copy of the license along with this
 *  *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 *
 */

package cl.ravenhill.keen.examples.ga.optimization

import cl.ravenhill.keen.Builders.Chromosomes.doubles
import cl.ravenhill.keen.Builders.engine
import cl.ravenhill.keen.Builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.operators.crossover.MeanCombinator
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPlotter
import kotlin.math.cos

private const val A = 10.0
private const val R = 5.12
private const val N = 2

/**
 * The fitness function is defined as:
 * f(x) = A * n + sum(x_i^2 - A * cos(2 * pi * x_i))
 * where:
 * - n is the number of dimensions
 * - x_i is the i-th dimension
 * - A is a constant
 * - pi is the mathematical constant pi
 *
 * The function is usually evaluated on the square xi ∈ [-5.12, 5.12], for all i = 1, …, n.
 * The global minimum is located at x* = (0, …, 0) where f(x*) = 0.
 */
private fun fitness(x: Genotype<Double>) = A * N + x.flatten().fold(0.0) { acc, gene ->
    acc + gene * gene - A * cos(2 * Math.PI * gene)
}

/**
 * The Rastrigin function is a non-convex function used as a performance test problem for
 * optimization algorithms.
 * The function is continuous, differentiable, and unimodal, but it has many local minima.
 *
 * In this example we will use a genetic algorithm to find the global minimum of the function.
 */
fun main() {
    val engine = engine(::fitness, genotype {
        chromosome { doubles { size = N; range = -R to R } }
    }) {
        populationSize = 500
        optimizer = FitnessMinimizer()
        alterers = listOf(Mutator(0.03), MeanCombinator(0.15))
        limits = listOf(SteadyGenerations(20))
        statistics = listOf(StatisticCollector(), StatisticPlotter())
    }
    engine.run()
    println(engine.statistics[0])
    (engine.statistics[1] as StatisticPlotter).displayFitness()
}