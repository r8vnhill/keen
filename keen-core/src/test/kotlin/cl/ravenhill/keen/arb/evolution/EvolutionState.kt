/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.evolution

import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.ranking.FitnessRanker
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.nonNegativeInt

/**
 * Generates an arbitrary [GeneticEvolutionState] for property-based testing in evolutionary algorithms.
 *
 * This function creates instances of [GeneticEvolutionState] with varying generations and populations, making it
 * suitable for testing different scenarios within evolutionary algorithms. The `population` parameter provides
 * an arbitrary generator for populations, while the `generation` parameter allows specifying the generation
 * number, which defaults to non-negative values.
 *
 * ## Usage:
 * This function is particularly useful in property-based testing frameworks like Kotest, where it can be
 * employed to generate diverse states of evolutionary processes. This assists in ensuring the robustness
 * and effectiveness of algorithms that manage or rely on the state of evolutionary processes.
 *
 * ### Example:
 * Generating an [GeneticEvolutionState] with a random generation number and population:
 * ```kotlin
 * val individualArb = Arb.individual(MyGenotypeArb, Arb.double(0.0..1.0))
 * val populationArb = Arb.population(individualArb)
 * val evolutionStateArb = Arb.evolutionState(populationArb)
 * val evolutionState = evolutionStateArb.bind()
 * // Results in an EvolutionState with a random generation number and population
 * ```
 *
 * In this example, `evolutionStateArb` generates an instance of [GeneticEvolutionState] with a population provided
 * by `populationArb` and a random generation number. This setup is ideal for testing the behavior of evolutionary
 * algorithms across different generations and population configurations.
 *
 * @param T The type of data encapsulated by the genes in the individuals of the population.
 * @param G The specific type of gene contained within the individuals of the population.
 * @param population An [Arb]<[Population]<[T], [G]>> for generating populations of individuals.
 * @param generation An optional [Arb]<[Int]> for generating the generation number. Defaults to generating non-negative
 *   integers.
 * @return An [Arb] that generates [GeneticEvolutionState] instances, each representing a state in an evolutionary process with
 *   a specific generation number and population.
 */
fun <T, G> arbEvolutionState(
    population: Arb<Population<T, G>>,
    ranker: Arb<FitnessRanker<T, G>>,
    generation: Arb<Int> = Arb.nonNegativeInt(),
) where G : Gene<T, G> = arbitrary { GeneticEvolutionState(generation.bind(), ranker.bind(), population.bind()) }
