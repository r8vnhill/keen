/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.ga

import cl.ravenhill.keen.dsl.booleans
import cl.ravenhill.keen.dsl.chromosomeOf
import cl.ravenhill.keen.dsl.evolutionEngine
import cl.ravenhill.keen.dsl.genotypeOf
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.BooleanGene
import cl.ravenhill.keen.limits.MaxGenerations
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.listeners.plotter.EvolutionPlotter
import cl.ravenhill.keen.listeners.summary.EvolutionSummary
import cl.ravenhill.keen.operators.alteration.crossover.UniformCrossover
import cl.ravenhill.keen.operators.alteration.mutation.BitFlipMutator
import cl.ravenhill.keen.operators.selection.RouletteWheelSelector
import cl.ravenhill.keen.operators.selection.TournamentSelector

/**
 * The size of the population in each generation. Set to 100.
 */
private const val POPULATION_SIZE = 100

/**
 * The number of genes in each chromosome. Set to 50.
 */
private const val CHROMOSOME_SIZE = 50

/**
 * The initial probability of each gene being `true`. Set to 0.15.
 */
private const val TRUE_RATE = 0.15

/**
 * The target fitness value at which the evolution will stop. Set to 50.0.
 */
private const val TARGET_FITNESS = CHROMOSOME_SIZE.toDouble()

/**
 * The maximum number of generations for the evolution. Set to 500.
 */
private const val MAX_GENERATIONS = 500

/**
 * A function that calculates the fitness of a genotype by counting the number of `true` genes.
 */
private fun count(genotype: Genotype<Boolean, BooleanGene>) = genotype.flatten().count { it }.toDouble()

/**
 * Implementation of the OneMax Problem using a genetic algorithm.
 *
 * The OneMax problem is a classic optimization problem in genetic algorithms where the goal is to maximize the number
 * of `true` values in a Boolean chromosome. This implementation sets up and runs a genetic algorithm to solve this
 * problem.
 *
 * ## Evolution Setup:
 * The genetic algorithm is configured with an evolution engine that uses the `count` function as the fitness evaluator.
 * The initial population is generated with a predefined chromosome size and `true` gene rate. The algorithm uses
 * random mutation and single-point crossover as alterers. The evolution stops either when the target fitness is
 * achieved or the maximum number of generations is reached.
 *
 * ## Usage:
 * To run the genetic algorithm, simply execute the `main` function. The function sets up the evolution engine,
 * evolves the population, and displays the results using the configured listeners.
 *
 * In this implementation, executing `main` will initiate the genetic algorithm process and output the evolution
 * summary.
 */
fun main() {
    val engine = evolutionEngine(::count, genotypeOf {
        chromosomeOf {
            booleans {
                size = CHROMOSOME_SIZE
                trueRate = TRUE_RATE
            }
        }
    }) {
        populationSize = POPULATION_SIZE
        parentSelector = RouletteWheelSelector()
        survivorSelector = TournamentSelector()
        alterers += listOf(BitFlipMutator(individualRate = 0.5), UniformCrossover(chromosomeRate = 0.6))
        limits += listOf(MaxGenerations(MAX_GENERATIONS), TargetFitness(TARGET_FITNESS))
        listeners += listOf(EvolutionSummary(), EvolutionPlotter())
    }
    engine.evolve()
    engine.listeners.forEach { it.display() }
}
