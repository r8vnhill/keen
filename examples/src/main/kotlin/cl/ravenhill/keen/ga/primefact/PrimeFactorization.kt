/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.primefact

import cl.ravenhill.keen.dsl.chromosomeOf
import cl.ravenhill.keen.dsl.evolutionEngine
import cl.ravenhill.keen.dsl.genotypeOf
import cl.ravenhill.keen.dsl.integers
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.listeners.plotter.EvolutionPlotter
import cl.ravenhill.keen.listeners.summary.EvolutionSummary
import cl.ravenhill.keen.operators.alteration.crossover.SinglePointCrossover
import cl.ravenhill.keen.operators.alteration.mutation.RandomMutator
import cl.ravenhill.keen.operators.alteration.mutation.SwapMutator
import cl.ravenhill.keen.ranking.FitnessMinRanker
import kotlin.math.abs

/**
 * The target number for the prime factorization in the genetic algorithm.
 *
 * `TARGET` specifies the integer that the genetic algorithm attempts to factorize into its prime components. The value
 * is set to 420, serving as the goal for the algorithm's factorization process.
 */
private const val TARGET = 420

/**
 * The size of the population in the genetic algorithm.
 *
 * `POPULATION_SIZE` sets the total number of individuals in each generation of the genetic algorithm. The constant is
 * defined with a value of 5000, establishing the scale of the population that will be evolved and evaluated during the
 * algorithm's execution.
 */
private const val POPULATION_SIZE = 5000

/**
 * The specified number of genes in each chromosome for the genetic algorithm.
 *
 * `CHROMOSOME_SIZE` defines the length of each chromosome within the population of the genetic algorithm. It determines
 * how many genes each chromosome will contain, with the value set to 10. This size impacts the genetic diversity and
 * complexity of the solutions that the algorithm can explore.
 */
private const val CHROMOSOME_SIZE = 10

/**
 * The number of generations with no improvement in fitness after which the genetic algorithm will terminate.
 *
 * This constant is used as a criterion for stopping the genetic algorithm's evolution process. If there is no
 * improvement in the fitness of the population for `STEADY_GENERATIONS` consecutive generations, it is inferred that
 * the algorithm has potentially reached a state of convergence, and the evolutionary process is halted. The value is
 * set to 500, meaning the algorithm will stop if there is no improvement observed over 500 successive generations.
 */
private const val STEADY_GENERATIONS = 500

/**
 * Lazily initialized list of candidate factors used for genetic computations in prime factorization.
 *
 * This property holds a list of numbers that includes all prime numbers up to a certain limit, plus the number 1.
 * The list is generated by the [primes] function, which calculates prime numbers, and then 1 is appended to this
 * list. Being a [lazy] property, `candidateFactors` is initialized only when it is first accessed, not when the object
 * containing it is created. This lazy initialization ensures efficiency, as the computation to generate prime numbers
 * is deferred until absolutely necessary.
 *
 * The inclusion of 1 alongside prime numbers is notable, as 1 is not a prime number. However, its presence in this list
 * suggests its utility in specific computations or algorithms where 1 is considered a valid factor, such as in certain
 * types of factorization problems.
 */
private val candidateFactors: List<Int> by lazy { primes() + 1 }

/**
 * Computes the absolute difference between the product of gene values in a genotype and the predefined target number.
 *
 * This function serves as the fitness function in the genetic algorithm for the Prime Factorization Problem. It
 * evaluates how close a given genotype's gene product is to the specified target number (420). The fitness is measured
 * as the absolute difference between the product of the genes in the genotype and the target number. A lower fitness
 * value indicates that the gene product is closer to the target, making the genotype a more suitable solution in the
 * context of factorization.
 *
 * ## Process:
 * - The function first converts the target number to a `Long` to handle large products without overflow.
 * - It then flattens the genotype to get a list of gene values and calculates their product.
 * - The absolute difference between this product and the target number is calculated.
 * - This difference is returned as a `Double`, representing the fitness value.
 *
 * A genotype with a fitness value of 0.0 would indicate a perfect factorization of the target number.
 *
 * ## Example Usage:
 * ```kotlin
 * val genotype = Genotype(listOf(Chromosome(listOf(IntGene(5), IntGene(84)))))
 * val fitness = absDiff(genotype)
 * println("Fitness: $fitness") // Output depends on the product of genes in the genotype
 * ```
 *
 * In this example, `absDiff` calculates the fitness of a genotype based on its gene product in relation to the target
 * number.
 *
 * @param genotype The genotype to be evaluated for fitness.
 * @return The absolute difference between the product of the genes in the genotype and the target number.
 */
private fun absDiff(genotype: Genotype<Int, IntGene>) = abs(
    TARGET.toLong() - genotype.flatMap { it.toLong() }.fold(1L) { acc, i -> acc * i }
).toDouble()

/**
 * Implementation of the Prime Factorization Problem using a genetic algorithm.
 *
 * This program aims to find the prime factors of a given target number (420) by evolving a population of potential
 * solutions using genetic algorithms. The fundamental theorem of arithmetic states that every integer greater than 1
 * is either a prime number itself or can be factorized into prime numbers, which are unique for each number.
 *
 * ## Genetic Algorithm Setup:
 * The genetic algorithm is configured with a custom genotype comprising integers. These integers are restricted to the
 * set of potential factors, ensuring that only viable candidates are considered for factorization. The algorithm
 * evolves the population towards a solution where the product of genes in the fittest individual equals the target
 * number.
 *
 * ## Usage:
 * The `main` function sets up and runs the genetic algorithm. It evolves the population while applying genetic
 * operations like mutation and crossover. The evolution process is subject to constraints such as reaching a fitness
 * target or a steady state. The result is displayed, showing the factorization of the target number.
 */
fun main() {
    val summary = EvolutionSummary<Int, IntGene>()
    val plotter = EvolutionPlotter<Int, IntGene>()
    val engine = evolutionEngine(
        ::absDiff,
        genotypeOf {
            chromosomeOf {
                integers {
                    size = CHROMOSOME_SIZE
                    filters += { it in candidateFactors }
                    ranges += 1..candidateFactors.maxOrNull()!!
                }
            }
        }
    ) {
        populationSize = POPULATION_SIZE
        alterers += listOf(
            RandomMutator(individualRate = 0.2),
            SwapMutator(individualRate = 0.2),
            SinglePointCrossover(chromosomeRate = 0.3)
        )
        ranker = FitnessMinRanker()
        limits += listOf(TargetFitness(0.0), SteadyGenerations(STEADY_GENERATIONS))
        listeners += summary + plotter
    }
    engine.evolve()
    summary.display()
    println(
        buildString {
            append("Solution: ")
            append(summary.fittest.genotype.flatten().filter { it > 1 }.joinToString(" * "))
            append(" = $TARGET")
        }
    )
    plotter.display()
}
