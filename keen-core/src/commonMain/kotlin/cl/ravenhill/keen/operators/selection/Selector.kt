/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selection

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constrainedTo
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.exceptions.SelectionException
import cl.ravenhill.keen.operators.Operator
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import kotlin.random.Random

/**
 * Represents a selection operator in an evolutionary algorithm.
 *
 * The `Selector` interface defines a contract for selection mechanisms that choose a subset of individuals
 * from a population based on their fitness or other criteria. Implementations of this interface are responsible
 * for selecting individuals according to specific strategies, such as tournament selection or roulette wheel selection.
 *
 * ## Usage:
 * This interface should be implemented to create different selection strategies in evolutionary algorithms.
 * The `invoke` operator function is used to apply the selection process to an evolutionary state, returning
 * a new state with the selected individuals.
 *
 * ### Example 1:
 *
 * Implementing a custom selector as a class:
 * ```kotlin
 * class MySelector<T, F, R> : Selector<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {
 *     override fun select(
 *         population: Population<T, F, R>,
 *         count: Int,
 *         ranker: IndividualRanker<T, F, R>
 *     ): Result<Population<T, F, R>> {
 *         // Custom selection logic
 *     }
 *     // ... other methods and properties ...
 * }
 * ```
 *
 * ### Example 2:
 *
 * Implementing as an anonymous object:
 * ```kotlin
 * val selector = object : Selector<MyType, MyFeature, MyRepresentation> {
 *     override fun select(
 *         population: Population<MyType, MyFeature, MyRepresentation>,
 *         count: Int,
 *         ranker: IndividualRanker<MyType, MyFeature, MyRepresentation>
 * ): Result<Population<MyType, MyFeature, MyRepresentation>> {
 *     // Custom selection logic
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 *
 * @see invoke
 */
interface Selector<T, F, R> : Operator<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * Applies the selection process to the given evolutionary state, producing a new state with the selected
     * individuals.
     *
     * This function performs the selection based on the population in the given state. It checks constraints to ensure
     * that the population is not empty and that the selection count is non-negative. The selected individuals are then
     * used to build a new evolutionary state.
     *
     * The function wraps the operation in a [runCatching] block, meaning that any exceptions encountered during the
     * selection process are caught and returned as part of a [Result].
     *
     * ## Potential Exceptions:
     * The following exceptions may be generated during the selection process and will be wrapped in a [Result]:
     * - [CompositeException]: Thrown whenever a constraint is violated.
     * - [SelectionException]: Thrown if the population is empty, the output size is negative, or the selection count
     *   does not match the expected size.
     *
     * @param S The type of the evolutionary state.
     * @param state The current evolutionary state.
     * @param outputSize The number of individuals to select.
     * @param buildState A function that builds a new state from the selected individuals.
     * @return A [Result] containing the new evolutionary state with the selected individuals, or an exception wrapped
     *   in the [Result] if the selection fails.
     */
    override suspend operator fun <S> invoke(
        state: S,
        outputSize: Int,
        buildState: (List<Individual<T, F, R>>) -> S,
        random: Random
    ): Result<S> where S : EvolutionState<T, F, R> = runCatching {
        constraints {
            "Population must not be empty"(::SelectionException) {
                state.population mustNot BeEmpty
            }
            "Selection count ($outputSize) must not be negative"(::SelectionException) {
                outputSize mustNot BeNegative
            }
        }
        val selectedPopulation = select(state.population, outputSize, state.ranker)
        selectedPopulation.getOrElse { throw it }
            .constrainedTo {
                ("Expected output size ($outputSize) must be equal to actual output size " +
                        "(${selectedPopulation.getOrThrow().size})")(::SelectionException) {
                    selectedPopulation.getOrThrow() must HaveSize(outputSize)
                }
            }
            .run(buildState)
    }

    /**
     * Selects a subset of individuals from the population based on the provided ranker and count.
     *
     * This method performs the core selection logic, returning a [Result] that contains the selected population.
     * The selection strategy is determined by the implementation of this method in concrete classes.
     *
     * Implementing classes may return [Result.Failure]s containing exceptions if the selection process fails. These
     * exceptions must be documented in the class's documentation.
     *
     * @param population The population from which individuals are selected.
     * @param count The number of individuals to select.
     * @param ranker The ranker used to evaluate and compare individuals in the population.
     * @param random The random number generator used to make random selections.
     * @return A [Result] containing the selected population, or an exception wrapped in the [Result] if the selection
     *   fails.
     */
    fun select(
        population: Population<T, F, R>,
        count: Int,
        ranker: IndividualRanker<T, F, R>,
        random: Random = Domain.random
    ): Result<Population<T, F, R>>
}
