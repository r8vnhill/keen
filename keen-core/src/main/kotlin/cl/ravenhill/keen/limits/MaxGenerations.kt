/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.evolution.Evolver
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * A limit condition for evolutionary algorithms based on the number of generations.
 *
 * `GenerationLimit` is a class that implements the [Limit] interface to provide a termination condition for an
 * evolutionary algorithm based on the number of generations processed. It is used to stop the evolutionary process
 * after a specified number of generations have been completed.
 *
 * ## Usage:
 * ```kotlin
 * // Define a limit of 100 generations
 * val generationLimit = GenerationLimit<MyDataType, MyGene>(100)
 *
 * // Use in an evolutionary algorithm setup
 * val engine = evolutionEngine(/* ... */) {
 *     limits += generationLimit
 *     // ...
 * }
 * // The algorithm will run until 100 generations have been processed
 * ```
 * In this example, `GenerationLimit` is used to specify that the evolutionary algorithm should terminate
 * after 100 generations have been processed.
 *
 * @param T The type ocf data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 * @param generations The maximum number of generations to run the evolutionary algorithm.
 * @property engine The evolutionary engine to which this limit applies. Unused in this implementation.
 */
data class MaxGenerations<T, G>(val generations: Int) : Limit<T, G> where G : Gene<T, G> {

    override var engine: Evolver<T, G>? = null

    /**
     * Checks whether the specified generation limit has been reached.
     *
     * @param state The current state of the evolution, including the current generation number.
     * @return `true` if the current generation number is greater than or equal to the specified limit,
     *   `false` otherwise.
     */
    override fun invoke(state: EvolutionState<T, G>) = state.generation >= generations
}
