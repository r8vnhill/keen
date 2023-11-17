package cl.ravenhill.keen.arbs.evolution

import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.nonNegativeInt

/**
 * Generates an arbitrary [EvolutionState] instance for property-based testing.
 *
 * This function leverages Kotest's property-based testing framework to create random instances of
 * [EvolutionState]. It's useful for testing functionalities that rely on or manipulate the state
 * of a genetic algorithm's evolution process.
 *
 * The [EvolutionState] encapsulates the current state of an evolutionary algorithm, comprising
 * the current population and the generation number. This state is crucial for understanding
 * the progress and dynamics of the algorithm at any given point in its execution.
 *
 *
 * ## Example Usage:
 * ```
 * val evolutionStateArb = Arb.evolutionState(
 *     population = Arb.intPopulation(),
 *     generation = Arb.nonNegativeInt()
 * )
 * val evolutionState = evolutionStateArb.bind() // Instance of EvolutionState
 * ```
 *
 * @param population An [Arb]<[Population]<T, G>> that generates arbitrary populations.
 *   Populations are essential components of evolutionary algorithms, representing
 *   a collection of candidate solutions.
 * @param generation An [Arb]<[Int]> (defaulting to non-negative integers) that generates arbitrary
 *   generation numbers. This parameter signifies the current generation of the evolutionary process.
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of [Gene] associated with the genetic data.
 *
 * @return An arbitrary generator ([Arb]) that produces instances of [EvolutionState] with random
 *         populations and generation numbers, suitable for diverse testing scenarios.
 */
fun <T, G> Arb.Companion.evolutionState(
    population: Arb<Population<T, G>>,
    generation: Arb<Int> = nonNegativeInt()
) where G : Gene<T, G> = arbitrary {
    EvolutionState(population.bind(), generation.bind())
}
