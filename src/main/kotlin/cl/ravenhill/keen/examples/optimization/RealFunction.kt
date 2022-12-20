package cl.ravenhill.keen.examples.optimization

import cl.ravenhill.keen.Builders.Chromosomes.doubles
import cl.ravenhill.keen.Builders.engine
import cl.ravenhill.keen.Builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.operators.crossover.MeanCrossover
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPlotter
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sin


/**
 * The function to minimize.
 */
private fun fitnessFunction(genotype: Genotype<Double>) = genotype.flatten().first()
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
        alterers = listOf(Mutator(0.1), MeanCrossover(0.15))
        limits = listOf(SteadyGenerations(20))
        statistics = listOf(StatisticCollector(), StatisticPlotter())
    }
    engine.run()
    println(engine.statistics.first())
    (engine.statistics.last() as StatisticPlotter).displayFitness()
}