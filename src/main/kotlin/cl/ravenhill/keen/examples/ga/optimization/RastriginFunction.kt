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

import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.doubles
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.operators.crossover.combination.AverageCrossover
import cl.ravenhill.keen.operators.mutator.RandomMutator
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import cl.ravenhill.keen.util.listeners.EvolutionSummary
import cl.ravenhill.keen.util.listeners.EvolutionPlotter
import kotlin.math.cos

private const val A = 10.0
private const val R = 5.12
private const val N = 2

/**
 * Computes the Rastrigin function for the input genotype.
 *
 * The Rastrigin function is a non-convex multimodal function commonly used as a benchmark for
 * optimization algorithms.
 * It is defined as:
 *    f(x) = A * N + sum(i=1 to N) [ x_i^2 - A * cos(2*pi*x_i) ]
 * where A is a constant, N is the number of dimensions, and x_i is the i-th component of the input
 * genotype.
 *
 * The function computes the sum of the squared values of the genes in the input genotype, subtracts
 * A times the cosine of twice the value of pi times each gene, and adds A times the number of
 * genes.
 * The result is returned as the fitness value of the input genotype.
 */
private fun rastriginFunction(x: Genotype<Double, DoubleGene>) =
    A * N + x.flatten().fold(0.0) { acc, gene ->
        acc + gene * gene - A * cos(2 * Math.PI * gene)
    }

/**
 * The main function of the program that performs a genetic algorithm optimization using the
 * Rastrigin function.
 *
 * The function first creates a genetic algorithm engine with the Rastrigin function as the fitness
 * function and a genotype consisting of a single chromosome of N doubles.
 * The population size is set to 500 and the optimizer is set to minimize the fitness value.
 *
 * The engine is then configured with two alterers: a mutator that randomly changes the value of a
 * gene with a probability of 0.03 and a mean crossover that combines the values of two genes by
 * taking their average with a probability of 0.3.
 *
 * The limits of the engine are set to a single steady generation limit, which stops the evolution
 * if the best fitness value has not improved for 50 consecutive generations.
 *
 * Two statistics collectors are added to the engine: a `StatisticCollector` that records the best,
 * worst, and average fitness values for each generation, and a `StatisticPlotter` that displays a
 * plot of the fitness values over time.
 *
 * Finally, the engine is run for one evolution cycle and the best fitness value and fitness plot
 * are printed to the console.
 */
fun main() {
    val engine = engine(::rastriginFunction, genotype {
        chromosome {
            doubles { size = N; ranges += List(N) { -R..R } }
        }
    }) {
        populationSize = 500
        optimizer = FitnessMinimizer()
        alterers = listOf(RandomMutator(0.03), AverageCrossover(0.3, geneRate = 0.5))
        limits = listOf(SteadyGenerations(50))
        listeners += listOf(EvolutionSummary(), EvolutionPlotter())
    }
    engine.evolve()
    println(engine.listeners[0])
    (engine.listeners[1] as EvolutionPlotter).displayFitness()
}