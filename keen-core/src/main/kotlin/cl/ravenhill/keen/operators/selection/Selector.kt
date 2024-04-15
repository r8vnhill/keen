/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.selection

import cl.ravenhill.jakt.Jakt
import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.GeneticOperator
import cl.ravenhill.keen.ranking.IndividualRanker


/**
 * Defines the behavior of a selector in an evolutionary algorithm.
 *
 * The `Selector` interface is used to define how individuals from a population are selected for various operations such
 * as crossover, mutation, or the next generation. The selection process is based on certain criteria, typically
 * involving fitness levels of the individuals.
 *
 * ## Usage:
 * Implement this interface to define custom selection logic in an evolutionary algorithm. The selection mechanism can
 * vary depending on the specific requirements of the algorithm, such as tournament selection, roulette wheel selection,
 * etc.
 *
 * ### Example:
 * ```kotlin
 * class TournamentSelector<T, G> : Selector<T, G> where G : Gene<T, G> {
 *     override fun select(population: Population<T, G>, count: Int, ranker: IndividualRanker<T, G>): Population<T, G> {
 *         // Implementation of tournament selection logic
 *     }
 * }
 *
 * // Usage in an evolutionary algorithm
 * val selector: Selector<MyDataType, MyGene> = TournamentSelector()
 * val selected = selector(state, outputSize)
 * ```
 *
 * In the example, `TournamentSelector` implements the `Selector` interface, defining how individuals are selected
 * for subsequent operations in the evolutionary algorithm.
 *
 *
 * @param T The type of data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 */
interface Selector<T, G> : GeneticOperator<T, G> where G : Gene<T, G> {

    /**
     * Invokes the selection method and produces a modified [EvolutionState] with a new population.
     *
     * This method is the main execution point for the selector, applying its logic to the given [state] and producing
     * a new [EvolutionState] that reflects the results of the selection process. It ensures that the selection
     * operation adheres to specified constraints and correctly modifies the population.
     *
     * ## Constraints:
     * - **Non-empty Population**: Validates that the initial population in [state] is not empty, ensuring that
     *   there are individuals to select from.
     * - **Positive Output Size**: Ensures that the desired output size ([outputSize]) is not negative, as negative
     *   values are not meaningful in the context of population selection.
     * - **Consistent Output Size**: Confirms that the size of the population in the resulting [EvolutionState] matches
     *   the specified [outputSize], maintaining consistency in population size after selection.
     *
     * ## Process:
     * - Validates the initial population size and desired output size.
     * - Applies the selection logic to the initial population to produce a new subset of individuals.
     * - Constructs a new [EvolutionState] with the selected individuals and the same generation number.
     * - Validates that the resulting population size matches the desired output size.
     *
     * ## Example:
     * ```kotlin
     * val selector: Selector<MyDataType, MyGene> = /* ... */
     * val currentState: EvolutionState<MyDataType, MyGene> = /* ... */
     * val outputSize = 10
     *
     * val newState = selector(currentState, outputSize)
     * // newState now contains a selected subset of individuals from currentState
     * ```
     *
     * In this example, the `selector` is used to select a subset of individuals from the current state, producing
     * a new state with a population that matches the desired output size.
     *
     * @param state The current [EvolutionState], containing the population to select from and the current generation
     *   number.
     * @param outputSize The number of individuals to be included in the new population of the resulting
     *   [EvolutionState].
     * @return A new [EvolutionState] object that contains a population resulting from the selection process,
     *   maintaining the same generation number as the input [state].
     */
    override fun invoke(state: EvolutionState<T, G>, outputSize: Int): EvolutionState<T, G> {
        constraints {
            "Population must not be empty" {
                state.population mustNot BeEmpty
            }
            "Selection count ($outputSize) must not be negative" {
                outputSize mustNot BeNegative
            }
        }
        val selectedPopulation = select(state.population, outputSize, state.ranker)
        return EvolutionState(
            state.generation,
            state.ranker,
            selectedPopulation
        ).apply {
            constraints {
                "Expected output size ($outputSize) must be equal to actual output size (${selectedPopulation.size})" {
                    selectedPopulation must HaveSize(outputSize)
                }
            }
        }
    }

    /**
     * Selects a subset of individuals from a population based on specific criteria.
     *
     * See [Selector] for more information.
     *
     * @param population The population from which to select individuals.
     * @param count The number of individuals to select.
     * @param ranker The [IndividualRanker] used to rank individuals in the population.
     * @return A [Population] consisting of the selected individuals.
     */
    fun select(population: Population<T, G>, count: Int, ranker: IndividualRanker<T, G>): Population<T, G>
}
