/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.ga

import cl.ravenhill.keen.dsl.chars
import cl.ravenhill.keen.dsl.chromosomeOf
import cl.ravenhill.keen.dsl.evolutionEngine
import cl.ravenhill.keen.dsl.genotypeOf
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.CharGene
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.listeners.EvolutionPlotter
import cl.ravenhill.keen.listeners.EvolutionSummary
import cl.ravenhill.keen.operators.alteration.crossover.SinglePointCrossover
import cl.ravenhill.keen.operators.alteration.mutation.RandomMutator

/**
 * The target word that the genetic algorithm aims to evolve towards.
 */
private const val TARGET = "Sopaipilla"

/**
 * Calculates the fitness of a genotype by counting how many characters match the target word at the same index.
 */
private fun matches(genotype: Genotype<Char, CharGene>) = genotype.flatten()
    .filterIndexed { index, char -> char == TARGET[index] }
    .size.toDouble()

/**
 * An implementation of the word guessing problem using a genetic algorithm.
 *
 * In this problem, the goal is to evolve a population of character strings (chromosomes) until they match a target
 * word.
 * The fitness function evaluates how close each chromosome is to the target word by counting the number of characters
 * that match the target word at the same position.
 *
 * ## Evolution Setup:
 * The genetic algorithm is configured with an evolution engine that uses the [matches] function as the fitness
 * evaluator.
 * The initial population is generated with a chromosome size equal to the length of the target word. The algorithm uses
 * random mutation and single-point crossover as alterers. The evolution stops when a chromosome achieves a fitness
 * equal to the length of the target word (i.e., when it matches the target word).
 *
 * ## Usage:
 * To run the genetic algorithm, execute the `main` function. The function sets up the evolution engine, evolves the
 * population, and displays the results using the configured listeners.
 */
fun main() {
    val engine = evolutionEngine(
        ::matches,
        genotypeOf {
            chromosomeOf {
                chars {
                    size = TARGET.length
                    ranges += ' '..'z'
                }
            }
        }) {
        populationSize = 500
        alterers += listOf(RandomMutator(0.1), SinglePointCrossover(0.2))
        limits += TargetFitness(TARGET.length.toDouble())
        listeners += listOf(EvolutionSummary(), EvolutionPlotter())
    }
    engine.evolve()
    engine.listeners.forEach { it.display() }
}
