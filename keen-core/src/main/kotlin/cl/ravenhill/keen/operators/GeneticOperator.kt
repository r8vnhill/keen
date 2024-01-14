/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Defines a genetic operator used in evolutionary algorithms.
 *
 * `GeneticOperator` is an interface representing operations applied to an [EvolutionState] in the context of
 * evolutionary algorithms. These operations can include mutation, crossover, selection, and more. Each genetic
 * operator takes the current state of evolution and modifies it, usually to produce the next generation.
 *
 * ## Key Concepts:
 * - **Evolution State Manipulation**: Each genetic operator works with an [EvolutionState], which represents
 *   the current state of the evolutionary process, including the population and generation number.
 * - **Output Size Control**: Operators are responsible for producing a new [EvolutionState] with a specified
 *   number of individuals (outputSize), which may be different from the input state's population size.
 *
 * ## Usage:
 * Implement this interface to define custom genetic operations. The operator should be designed to take the
 * current state of evolution and apply specific genetic manipulations to produce a new state.
 *
 * ### Example:
 * Implementing a simple mutation operator:
 * ```kotlin
 * class MutationOperator<T, G> : GeneticOperator<T, G> where G : Gene<T, G> {
 *     override fun invoke(state: EvolutionState<T, G>, outputSize: Int): EvolutionState<T, G> {
 *         // Mutation logic here
 *         val mutatedPopulation = state.population.map { /* Apply mutation to each individual */ }
 *         return EvolutionState(state.generation + 1, mutatedPopulation)
 *     }
 * }
 *
 * // Usage in an evolutionary algorithm
 * val mutation = MutationOperator<MyDataType, MyGene>()
 * val newState = mutation(currentState, outputSize)
 * ```
 * In this example, `MutationOperator` applies a mutation to each individual in the population of the current
 * [EvolutionState] and returns a new state representing the next generation.
 *
 * @param T The type of data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 */
interface GeneticOperator<T, G> where G : Gene<T, G> {

    /**
     * Applies the genetic operation to the provided [EvolutionState] and returns a new state.
     *
     * See the [GeneticOperator] documentation for more information.
     *
     * @param state The current state of evolution, including the population and generation number.
     * @param outputSize The desired size of the population in the new evolution state. This allows the operator
     *                   to control the population size of the next generation.
     * @return A new [EvolutionState] resulting from applying the genetic operation.
     */
    operator fun invoke(state: EvolutionState<T, G>, outputSize: Int): EvolutionState<T, G>
}
