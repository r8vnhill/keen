package cl.ravenhill.keen.examples

import cl.ravenhill.keen.Builders.engine
import cl.ravenhill.keen.Builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.operators.Mutator
import cl.ravenhill.keen.operators.crossover.SinglePointCrossover
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPrinter
import kotlin.math.abs

private const val TARGET = 345

/**
 * Calculates the absolute difference between the target and the multiplication of the prime factors
 * of the genotype.
 */
fun absDiff(genotype: Genotype<Int>) =
    genotype.chromosomes.first().genes
        .map { it.dna.toLong() }
        .let { factors -> abs(TARGET.toLong() - factors.fold(1L) { acc, i -> acc * i }) }
        .toDouble()

/**
 * The Fundamental Theorem of Arithmetic states that every integer greater than 1 is either a prime
 * number itself or can be represented as a product of prime numbers (its prime factors).
 * This example tries to find the prime factors of a given number.
 */
fun main() {
    val engine = engine(::absDiff, genotype {
        chromosomes = listOf(IntChromosome.Factory(10, 1..200) { it in candidateFactors })
    }) {
        populationSize = 10000
        alterers = listOf(SinglePointCrossover(0.6), Mutator(0.2))
        optimizer = FitnessMinimizer()
        limits = listOf(TargetFitness(0.0), GenerationCount(1000))
        statistics = listOf(StatisticCollector(), StatisticPrinter(2))
    }
    engine.run()
    println(engine.statistics[0])
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