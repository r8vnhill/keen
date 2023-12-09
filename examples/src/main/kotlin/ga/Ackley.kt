/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package ga

import cl.ravenhill.keen.dsl.chromosomeOf
import cl.ravenhill.keen.dsl.doubles
import cl.ravenhill.keen.dsl.evolutionEngine
import cl.ravenhill.keen.dsl.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import cl.ravenhill.keen.limits.MaxGenerations
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.listeners.EvolutionPlotter
import cl.ravenhill.keen.listeners.EvolutionSummary
import cl.ravenhill.keen.operators.crossover.AverageCrossover
import cl.ravenhill.keen.operators.mutation.RandomMutator
import cl.ravenhill.keen.operators.selection.RandomSelector
import cl.ravenhill.keen.operators.selection.TournamentSelector
import cl.ravenhill.keen.ranking.FitnessMinRanker
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt


// Constants used in the Ackley function calculation
private const val A = 20.0       // Constant 'A' in the Ackley function
private const val B = 0.2        // Constant 'B' in the Ackley function
private const val C = 2 * PI     // Constant 'C' in the Ackley function, representing 2 times Pi
private const val D = 0.5        // Factor used in the Ackley function's square root and exponential components
private val EULER_NUMBER = exp(1.0)     // Euler's number, used in the Ackley function

/**
 * Calculates the Ackley function for a given genotype.
 *
 * The Ackley function is a well-known mathematical function used for testing optimization algorithms.
 * It is characterized by a nearly flat outer region and a large hole at the center. The function poses
 * a risk of optimization algorithms getting stuck in the outer region.
 *
 * @param genotype A genotype consisting of DoubleGene, representing the coordinates for the Ackley function.
 * @return The value of the Ackley function for the given genotype.
 *
 * ## Function Calculation:
 * The function is calculated using the formula:
 *   -A * exp(-B * sqrt(D * (x^2 + y^2))) - exp(D * (cos(C * x) + cos(C * y))) + e + A
 * where 'x' and 'y' are the first two genes in the provided genotype.
 *
 * ## Usage Example:
 * ```
 * val genotype = Genotype(listOf(DoubleGene(0.5), DoubleGene(-0.5)))
 * val ackleyValue = ackley(genotype)
 * ```
 * In this example, the Ackley function is calculated for a genotype with two genes representing the coordinates.
 */
private fun ackley(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    val sumOfSquares = x.pow(2) + y.pow(2)
    val cosComponent = cos(C * x) + cos(C * y)

    -A * exp(-B * sqrt(D * sumOfSquares)) -
          exp(D * cosComponent) + EULER_NUMBER + A
}

private const val POPULATION_SIZE = 500
private const val LO = -5.0
private const val HI = 5.0
private const val MUTATION_RATE = 0.1
private val range = LO..HI

fun main() {
    val summary = EvolutionSummary<Double, DoubleGene>()
    val plotter = EvolutionPlotter<Double, DoubleGene>()
    val engine = evolutionEngine(::ackley, genotype {
        chromosomeOf {
            doubles {
                size = 2
                ranges += range
            }
        }
    }) {
        populationSize = POPULATION_SIZE
        ranker = FitnessMinRanker()
        parentSelector = TournamentSelector()
        survivorSelector = TournamentSelector()
        alterers += listOf(RandomMutator(MUTATION_RATE), AverageCrossover(geneRate = 0.5))
        limits += listOf(SteadyGenerations(generations = 50), MaxGenerations(500))
        listeners += summary + plotter
    }
    engine.evolve()
    engine.listeners.filterIsInstance<EvolutionSummary<*, *>>().first().display()
    engine.listeners.filterIsInstance<EvolutionPlotter<*, *>>().first().display()
}
