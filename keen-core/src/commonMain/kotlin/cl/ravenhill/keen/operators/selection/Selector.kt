/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selection

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constrainedTo
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.exceptions.SelectionException
import cl.ravenhill.keen.operators.Operator
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Interface representing a selection operator in an evolutionary algorithm.
 *
 * The `Selector` interface defines the contract for selection mechanisms that choose a subset of individuals from a
 * population based on their fitness or other criteria. Implementations of this interface are responsible for selecting
 * individuals according to specific strategies, such as tournament selection or roulette wheel selection.
 *
 * ## Usage:
 * This interface is intended to be implemented by classes that define specific selection strategies within an
 * evolutionary algorithm. The main responsibilities of a `Selector` include verifying that the population and output
 * size are valid, and then performing the selection operation based on the provided criteria.
 *
 * ### Example:
 * Implementing a custom selector:
 * ```kotlin
 * class MyCustomSelector<T, F, R> : Selector<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {
 *     override fun select(
 *         population: Population<T, F, R>,
 *         count: Int,
 *         ranker: IndividualRanker<T, F, R>
 *     ): Either<SelectionException, Population<T, F, R>> {
 *         // Custom selection logic
 *     }
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 */
interface Selector<T, F, R> : Operator<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * Applies the selection process to the given evolutionary state, producing a new state with the selected
     * individuals.
     *
     * This function is responsible for verifying that the population is not empty and that the selection count is
     * non-negative. It then invokes the `select` method to perform the selection and ensures that the output size
     * matches the expected size. The method returns an `Either` type, indicating either a successful state or a
     * `SelectionException` if an error occurs.
     *
     * @param S The type of the evolutionary state.
     * @param state The current evolutionary state.
     * @param outputSize The number of individuals to select.
     * @param buildState A function that builds a new state from the selected individuals.
     * @return An `Either` containing the new evolutionary state on success, or a `SelectionException` on failure.
     */
    override suspend operator fun <S> invoke(
        state: S,
        outputSize: Int,
        buildState: (List<Individual<T, F, R>>) -> S
    ): Either<SelectionException, S> where S : EvolutionState<T, F, R, S> {
        constrained {
            "Population must not be empty" { state.population mustNot BeEmpty }
            "Selection count ($outputSize) must not be negative" { outputSize mustNot BeNegative }
        }.getOrElse {
            return SelectionException("Invalid selection parameters", it).left()
        }
        val selectedPopulation = select(state.population, outputSize, state.ranker)
            .getOrElse { return it.left() }
            .constrainedTo {
                ("Expected output size ($outputSize) must be equal to actual output size (${it.size})")(
                    ::SelectionException
                ) { it must HaveSize(outputSize) }
            }.getOrElse { return SelectionException("Invalid selection output size", it).left() }
        return buildState(selectedPopulation).right()
    }

    /**
     * Selects a subset of individuals from the population based on the provided ranker and count.
     *
     * This method is responsible for performing the core selection logic and returning an `Either` containing the
     * selected population on success, or a `SelectionException` on failure. Implementations of this method should
     * handle any specific selection strategy, such as tournament or roulette wheel selection.
     *
     * @param population The population from which individuals are selected.
     * @param count The number of individuals to select.
     * @param ranker The ranker used to evaluate and compare individuals in the population.
     * @return An `Either` containing the selected population on success, or a `SelectionException` on failure.
     */
    suspend fun select(
        population: Population<T, F, R>,
        count: Int,
        ranker: IndividualRanker<T, F, R>,
    ): Either<SelectionException, Population<T, F, R>>
}
