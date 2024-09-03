/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ToStringMode
import cl.ravenhill.keen.dsl.booleans
import cl.ravenhill.keen.dsl.chromosomeOf
import cl.ravenhill.keen.dsl.geneticAlgorithm
import cl.ravenhill.keen.dsl.genotypeOf
import cl.ravenhill.keen.evolution.executors.construction.CoroutineConcurrentConstructor
import cl.ravenhill.keen.evolution.executors.construction.SequentialConstructor
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.BooleanGene
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.listeners.plotter.EvolutionPlotter
import cl.ravenhill.keen.listeners.summary.EvolutionSummary
import cl.ravenhill.keen.operators.alteration.crossover.UniformCrossover
import cl.ravenhill.keen.operators.alteration.mutation.BitFlipMutator
import cl.ravenhill.keen.operators.selection.TournamentSelector

/**
 * Counts the number of `true` values in a flattened genotype.
 *
 * @param genotype The genotype composed of Boolean genes.
 * @return The number of `true` values in the genotype as a Double.
 */
private fun count(genotype: Genotype<Boolean, BooleanGene>) = genotype.flatten().count { it }.toDouble()

/**
 * Runs a genetic algorithm to solve the OneMax problem.
 *
 * ## Problem Statement:
 * Given a binary string x = (x1, x2, ..., xn) of length n, where each xi is either 0 or 1, the goal of the OneMax
 * problem is to maximize the sum of the elements in x, represented by the fitness function:
 *
 *   f(x) = x1 + x2 + ... + xn
 *
 * The objective is to find the binary string x that maximizes f(x). The optimal solution occurs when all bits are set
 * to 1, meaning f(x) = n.
 *
 * In this implementation, the length of the binary string n is set to 50.
 *
 * ## Description:
 * The `oneMax` function sets up and runs a genetic algorithm to solve the OneMax problem. The algorithm evolves a
 * population of individuals, each represented by a binary string (genotype), to maximize the fitness function f(x).
 *
 * ### Genetic Algorithm Configuration:
 * - **Genotype**: The genotype is a binary string of length 50, with an initial `true` (or 1) rate of 0.15.
 * - **Population Size**: The population consists of 500 individuals.
 * - **Parent and Survivor Selection**: A tournament selection strategy is used for both selecting parents and
 *   survivors.
 * - **Alterers**: The algorithm applies a `BitFlipMutator` to mutate individual genes and a `UniformCrossover` with a
 *   chromosome rate of 0.6 for recombination.
 * - **Fitness Target**: The algorithm's evolution process is limited by a target fitness of 50, corresponding to the
 *   maximum possible sum of the binary string (i.e., all bits are 1).
 * - **Listeners**: An `EvolutionSummary` listener is added to monitor and display the progress of the evolution
 *   process.
 *
 * ## Execution:
 * The algorithm evolves the population until one of the individuals reaches the target fitness f(x) = 50, where all
 * bits in the binary string are set to 1. After the evolution process is complete, the summary of the evolution is
 * displayed.
 *
 * ## Example Usage:
 * ```
 * fun main() {
 *     runBlocking {
 *        oneMax()
 *     }
 * }
 * ```
 */
suspend fun oneMax() {

    // Set the display mode for Boolean chromosomes to a simple binary string representation.
    Domain.toStringMode = ToStringMode.SIMPLE

    // Configure and initialize the genetic algorithm engine.
    val engine = geneticAlgorithm(
        ::count, // Fitness function: counts the number of true values in the genotype.
        genotypeOf {
            chromosomeOf {
                booleans {
                    size = 50 // Length of the binary string (genotype).
                    trueRate = 0.15 // Initial rate of true values (1-bits) in the binary string.
                    executor = SequentialConstructor() // Sequential chromosome constructor.
                }
            }
        }
    ) {
        populationSize = 500 // Size of the population.
        parentSelector = TournamentSelector() // Selection strategy for parents.
        survivorSelector = TournamentSelector() // Selection strategy for survivors.
        alterers += listOf(BitFlipMutator(), UniformCrossover(chromosomeRate = 0.6)) // Mutation and crossover operators
        limits += TargetFitness(50.0) // Evolution stops when fitness reaches 50.
        listeners += listOf(EvolutionSummary(), EvolutionPlotter()) // Listeners for evolution monitoring.
    }

    // Run the evolution process.
    engine.evolve()

    // Display the summary of the evolution.
    engine.publicListeners.filterIsInstance<EvolutionSummary<*, *, *, *>>().first().display()
    engine.publicListeners.filterIsInstance<EvolutionPlotter<*, *, *, *>>().first().display()
}
