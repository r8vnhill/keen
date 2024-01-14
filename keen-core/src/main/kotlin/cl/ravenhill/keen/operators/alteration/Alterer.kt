/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.GeneticOperator


/**
 * An interface representing an alterer in an evolutionary algorithm.
 *
 * An `Alterer` is a specialized type of [GeneticOperator] focused on modifying or altering the genetic makeup
 * of individuals within a population. These alterations are typically mutations or other genetic modifications
 * that introduce diversity and aid in the evolutionary process.
 *
 * ## Responsibilities of an Alterer:
 * - **Genetic Modification**: Implementations of this interface are responsible for altering the genes of individuals
 *   in a population. This could involve mutation, gene swapping, or other forms of genetic manipulation.
 * - **State Transformation**: As with any [GeneticOperator], an `Alterer` takes an [EvolutionState] as input and
 *   produces a new [EvolutionState] reflecting the changes made to the population.
 *
 * ## Usage:
 * Implement this interface to define specific alteration strategies in an evolutionary algorithm. The specific
 * alteration logic can vary widely depending on the requirements of the algorithm and the nature of the problem
 * being addressed.
 *
 * ### Example:
 * Implementing a simple mutation alterer:
 * ```kotlin
 * class MutationAlterer<T, G> : Alterer<T, G> where G : Gene<T, G> {
 *     override fun invoke(state: EvolutionState<T, G>, outputSize: Int): EvolutionState<T, G> {
 *         // Implement mutation logic here
 *         val mutatedPopulation = state.population.map { /* Apply mutation to each individual */ }
 *         return EvolutionState(state.generation + 1, mutatedPopulation)
 *     }
 * }
 *
 * // Usage in an evolutionary algorithm
 * val alterer: Alterer<MyDataType, MyGene> = MutationAlterer()
 * val newState = alterer(currentState, outputSize)
 * ```
 * In this example, `MutationAlterer` implements the `Alterer` interface and applies mutations to each individual
 * in the population of the current [EvolutionState], resulting in a new state for the next generation.
 *
 * @param T The type of data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 */
interface Alterer<T, G> : GeneticOperator<T, G> where G : Gene<T, G> {
    operator fun plus(alterer: Alterer<T, G>) = listOf(this, alterer)
}
