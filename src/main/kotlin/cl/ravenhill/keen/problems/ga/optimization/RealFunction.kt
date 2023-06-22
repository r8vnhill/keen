package cl.ravenhill.keen.problems.ga.optimization

import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.doubles
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.operators.crossover.combination.MeanCrossover
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import cl.ravenhill.keen.util.listeners.EvolutionSummary
import cl.ravenhill.keen.util.listeners.EvolutionPlotter
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sin


/**
 * The fitness function used in the genetic algorithm optimization.
 *
 * The fitness of a genotype is defined as the natural logarithm of the sum of the cosine of the
 * sine of the first gene value and the sine of the cosine of the first gene value.
 * The fitness function operates on a genotype consisting of a single chromosome of N double genes,
 * and returns a single fitness value.
 */
private fun fitnessFunction(genotype: Genotype<Double, DoubleGene>) = genotype.flatten().first()
    .let {
        ln(cos(sin(it)) + sin(cos(it)))
    }

/**
 * Calculates the minimum of the real function:
 * ```
 * f(x) = ln(cos(sin(it)) + sin(cos(it)))
 * ```
 */
fun main() {
    val engine = engine(::fitnessFunction, genotype {
        chromosome { doubles { size = 1; range = (-2.0 * Math.PI) to (2 * Math.PI) } }
    }) {
        populationSize = 500
        optimizer = FitnessMinimizer()
        alterers = listOf(Mutator(0.03), MeanCrossover(0.3))
        limits = listOf(SteadyGenerations(20))
        statistics = listOf(EvolutionSummary(), EvolutionPlotter())
    }
    engine.evolve()
    println(engine.listeners.first())
    (engine.listeners.last() as EvolutionPlotter).displayFitness()
}