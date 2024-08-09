/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.config.EvolutionConfiguration
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * An abstract base class for implementing an evolutionary algorithm.
 *
 * The `AbstractEvolver` class provides a framework for managing the evolutionary process, including handling the
 * evolutionary state, invoking lifecycle listeners, and applying evolutionary limits. Subclasses are required to
 * implement the `iterateGeneration` method, which defines the specific logic for advancing the evolutionary state
 * through generations.
 *
 * ## Usage:
 * This class is intended to be extended by concrete implementations of evolutionary algorithms. Subclasses must
 * provide an implementation for the `iterateGeneration` method, which advances the evolutionary state by one
 * generation.
 *
 * ### Example 1: Implementing a Simple Genetic Algorithm
 * ```
 * class SimpleGeneticEvolver(
 *     evolutionConfiguration: EvolutionConfiguration<Double, SimpleGene, SimpleRepresentation, SimpleState>
 * ) : AbstractEvolver<Double, SimpleGene, SimpleRepresentation, SimpleState>(evolutionConfiguration) {
 *
 *     override var state: SimpleState = initialState
 *
 *     override fun iterateGeneration(state: SimpleState): SimpleState {
 *         // Example logic: Select parents, perform crossover and mutation, update state
 *         val parents = selectParents(state.population)
 *         val offspring = performCrossover(parents)
 *         val mutatedOffspring = performMutation(offspring)
 *         return state.copy(population = mutatedOffspring)
 *     }
 *
 *     private fun selectParents(population: List<SimpleRepresentation>): List<SimpleRepresentation> {
 *         // Selection logic (e.g., tournament selection)
 *         return population.shuffled().take(2)
 *     }
 *
 *     private fun performCrossover(parents: List<SimpleRepresentation>): List<SimpleRepresentation> {
 *         // Crossover logic (e.g., single-point crossover)
 *         return parents // Placeholder logic for simplicity
 *     }
 *
 *     private fun performMutation(offspring: List<SimpleRepresentation>): List<SimpleRepresentation> {
 *         // Mutation logic (e.g., bit-flip mutation)
 *         return offspring.map { it.mutate() }
 *     }
 * }
 * ```
 * In this example, `SimpleGeneticEvolver` implements a basic genetic algorithm with steps for selection, crossover,
 * and mutation. The `iterateGeneration` method defines how the state is updated in each generation by applying
 * these steps.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 * @param evolutionConfiguration The configuration settings for the evolutionary algorithm, including listeners and
 *   limits that control the evolution process.
 * @constructor Initializes the `AbstractEvolver` with the provided evolution configuration.
 */
abstract class AbstractEvolver<T, F, R, S>(
    evolutionConfiguration: EvolutionConfiguration<T, F, R, S>
) : Evolver<T, F, R, S> where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> {

    /**
     * The current evolutionary state that is updated as the algorithm progresses.
     */
    protected abstract var state: S

    private val listeners = evolutionConfiguration.listeners
    private val limits = evolutionConfiguration.limits

    /**
     * Executes the evolutionary process until a termination condition is met.
     *
     * This method manages the main evolutionary loop, invoking lifecycle listeners at appropriate
     * stages (start of evolution, start and end of each generation, and end of evolution). The loop
     * continues until one of the configured limits is met.
     *
     * @return The final evolutionary state after the process has completed.
     */
    override fun evolve(): S {
        listeners.forEach { it.onEvolutionStart() }
        do {
            listeners.forEach { it.onGenerationStart(state) }
            state = iterateGeneration(state)
            listeners.forEach { it.onGenerationEnd(state) }
        } while (limits.none { it(state) })
        listeners.forEach { it.onEvolutionEnd(state) }
        return state
    }

    /**
     * Advances the evolutionary state by one generation.
     *
     * Subclasses must implement this method to define how the state is updated in each generation
     * of the evolutionary process.
     *
     * @param state The current evolutionary state.
     * @return The updated evolutionary state after one generation.
     */
    abstract fun iterateGeneration(state: S): S
}
