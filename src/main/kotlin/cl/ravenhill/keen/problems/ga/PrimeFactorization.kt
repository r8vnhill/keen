package cl.ravenhill.keen.problems.ga

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.coroutines
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.evaluator
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.builders.ints
import cl.ravenhill.keen.builders.sequential
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.util.math.eq
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPlotter
import kotlinx.coroutines.Dispatchers
import kotlin.math.abs
import kotlin.math.ln

private const val TARGET = 420

/**
 * Calculates the absolute difference between the target and the multiplication of the prime factors
 * of the genotype.
 */
private fun absDiff(genotype: Genotype<Int>) =
    genotype.flatten()
        .map { it.toLong() }
        .let { factors -> abs(TARGET.toLong() - factors.fold(1L) { acc, i -> acc * i }) }
        .toDouble()

/**
 * The Fundamental Theorem of Arithmetic states that every integer greater than 1 is either a prime
 * number itself or can be represented as a product of prime numbers (its prime factors).
 * This example tries to find the prime factors of a given number.
 */
fun main() {
    val engine = engine(::absDiff, genotype {
        chromosome {
            ints {
                size = 10; range = 1 to 200; filter = { it in candidateFactors }
            }
        }
    }) {
        populationSize = 5000
        alterers = listOf(Mutator(0.2), SinglePointCrossover(0.3))
        optimizer = FitnessMinimizer()
        limits = listOf(SteadyGenerations(10), GenerationCount(1000))
        statistics = listOf(StatisticCollector(), StatisticPlotter())
    }
    val result = engine.run()
    println(engine.statistics.first())
    println(buildString {
        append("$TARGET = ")
        append(result.best.genotype.flatten().filter { it > 1 }.joinToString(" * "))
    })
    (engine.statistics[1] as StatisticPlotter).displayFitness { if (it eq 0.0) 0.0 else ln(it) }
}

private val candidateFactors = listOf(
    1,
    2,
    3,
    5,
    7,
    11,
    13,
    17,
    23,
    27,
    29,
    31,
    37,
    41,
    43,
    47,
    53,
    59,
    61,
    67,
    71,
    73,
    79,
    83,
    89,
    97,
    101,
    103,
    107,
    109,
    113,
    127,
    131,
    137,
    139,
    149,
    151,
    157,
    163,
    167,
    173,
    179,
    181,
    191,
    193,
    197,
    199
)