/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.dsl

import cl.ravenhill.keen.evolution.engines.ga.GeneticAlgorithm
import cl.ravenhill.keen.evolution.engines.ga.GeneticAlgorithmFactory
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.GenotypeFactory
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.operators.alteration.crossover.Crossover
import cl.ravenhill.keen.operators.alteration.mutation.Mutator

/**
 * Configures and initializes a genetic algorithm to solve optimization problems.
 *
 * The `geneticAlgorithm` function sets up a genetic algorithm by specifying the fitness function, the genotype factory,
 * and additional algorithm configurations such as population size, selection methods, and genetic operators. This
 * function is designed to be flexible, allowing users to customize various aspects of the algorithm to suit specific
 * optimization problems.
 *
 * ## Example Usage:
 * ```kotlin
 * fun main() {
 *     runBlocking { // `runBlocking` is not available in JS
 *         val engine = geneticAlgorithm(
 *             ::fitnessFunction, // Define your fitness function
 *             genotypeOf { /* Define your genotype factory */ }
 *         ) {
 *             populationSize = 100
 *             parentSelector = TournamentSelector()
 *             survivorSelector = TournamentSelector()
 *             alterers += listOf(BitFlipMutator(), UniformCrossover(chromosomeRate = 0.6))
 *             limits += targetFitness(100.0)
 *             listeners += EvolutionSummary()
 *         }
 *         engine.evolve()
 *     }
 * }
 * ```
 *
 * ## Description:
 * The `geneticAlgorithm` function simplifies the creation of genetic algorithms by providing a high-level interface for
 * defining the necessary components and configurations. Users specify a fitness function to evaluate the quality of
 * solutions, a genotype factory to generate the initial population, and an initialization block ([init]) to configure
 * the algorithm's parameters.
 *
 * Within the `init` block, users can:
 * - Set the population size, which determines how many individuals are in each generation.
 * - Choose parent and survivor selection strategies, such as tournament selection or roulette wheel selection.
 * - Apply genetic operators like mutation ([Mutator]) and crossover ([Crossover]) to introduce variation in the
 *   population.
 * - Define stopping criteria, such as a target fitness or a maximum number of generations.
 * - Add listeners to monitor and log the evolution process, providing insights into the algorithm's progress.
 *
 * ## Output:
 * The function returns a configured genetic algorithm engine that can be executed to evolve the population towards an
 * optimal solution.
 *
 * ## Notes:
 * - The genetic algorithm is highly customizable, making it suitable for a wide range of optimization problems.
 * - The function leverages Kotlin's `apply` function to configure the genetic algorithm within the `init` block,
 *   allowing for a clean and concise configuration syntax.
 *
 * @param T The type of value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @param fitnessFunction A function that takes a `Genotype<T, G>` as input and returns a `Double` representing the
 *   fitness score of the genotype. The genetic algorithm aims to minimize the distance between the fitness score and
 *   the target fitness value (this could be unbounded).
 * @param genotype A `GenotypeFactory<T, G>` that produces the initial population of genotypes for the genetic
 *   algorithm. The genotype represents the genetic makeup of individuals in the population.
 * @param init A lambda function that allows for the customization of the genetic algorithm's configuration. This
 *   includes setting parameters like population size, selection strategies, mutation and crossover operators, stopping
 *   criteria, and listeners to monitor the algorithm's progress.
 * @return A configured genetic algorithm engine ready to be executed.
 * @see GeneticAlgorithm
 */
fun <T, G> geneticAlgorithm(
    fitnessFunction: (Genotype<T, G>) -> Double,
    genotype: GenotypeFactory<T, G>,
    init: GeneticAlgorithmFactory<T, G>.() -> Unit,
) where G : Gene<T, G> = GeneticAlgorithmFactory(fitnessFunction, genotype).apply(init).make()
