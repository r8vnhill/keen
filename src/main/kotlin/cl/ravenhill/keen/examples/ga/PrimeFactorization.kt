/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.examples.ga

import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.builders.ints
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import cl.ravenhill.keen.operators.mutator.RandomMutator
import cl.ravenhill.keen.util.eq
import cl.ravenhill.keen.util.listeners.EvolutionPlotter
import cl.ravenhill.keen.util.listeners.EvolutionSummary
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import kotlin.math.abs
import kotlin.math.ln

private const val TARGET = 420

/**
 * Calculates the absolute difference between the target number and the product of the genes in a
 * genotype.
 *
 * Given a genotype consisting of a single chromosome of N integer genes, the function flattens the
 * genotype, converts each gene to a Long and calculates the product of the genes.
 * The function then computes the absolute difference between this product and the target number,
 * and returns this difference as a Double.
 */
private fun absDiff(genotype: Genotype<Int, IntGene>) =
    genotype.flatMap()
        .map { it.toLong() }
        .let { factors -> abs(TARGET.toLong() - factors.fold(1L) { acc, i -> acc * i }) }
        .toDouble()

/**
 * This genetic algorithm solves the fundamental theorem of arithmetic, which states that every
 * positive integer greater than 1 can be uniquely represented as a product of prime numbers.
 *
 * The algorithm uses a genetic algorithm approach to find a set of 10 factors that when multiplied
 * together will result in the target number.
 * The possible factors are restricted to a pre-defined list of primes, specified in the
 * [candidateFactors] list.
 *
 * The [absDiff] function calculates the absolute difference between the target number and the
 * product of the candidate factors in a given genotype.
 * This is used as the fitness function for the genetic algorithm.
 */
fun main() {
    // Set up the genetic algorithm
    val engine = engine(
        ::absDiff,
        genotype {
            chromosome {
                ints {
                    size = 10;
                    range = 1 to 200;
                    filter = { it in candidateFactors }
                }
            }
            chromosome {
                ints {
                    size = 1;
                    range = 1 to 200;
                    filter = { it in candidateFactors }
                }
            }
        }
    ) {
        populationSize = 1000
        alterers = listOf(RandomMutator(0.1), SinglePointCrossover(0.3))
        optimizer = FitnessMinimizer()
        limits = listOf(SteadyGenerations(10), GenerationCount(1000))
        listeners += listOf(EvolutionSummary(), EvolutionPlotter())
    }
    // Run the genetic algorithm and output the results
    val result = engine.evolve()
    println("Statistics: ${engine.listeners.first()}")
    println(
        buildString {
            append("$TARGET = ")
            append(result.best.genotype.flatMap().filter { it > 1 }.joinToString(" * "))
        }
    )
    (engine.listeners[1] as EvolutionPlotter).displayFitness { if (it eq 0.0) 0.0 else ln(it) }
}

/**
 * A list of candidate factors used in a genetic algorithm to find the factors of a target number.
 * The genetic algorithm generates genotypes consisting of 1 or more integers, where each integer is
 * one of the candidate factors in this list.
 * The fitness of each genotype is evaluated based on its proximity to the factors of the target
 * number.
 * This list contains 43 prime numbers between 1 and 200.
 */
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
