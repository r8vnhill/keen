/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines

import arrow.core.Either
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.exceptions.AlterationException
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Interface representing an alteration engine in an evolutionary algorithm.
 *
 * The `AlterationEngine` interface defines the contract for components that perform genetic alterations on a population
 * within an evolutionary algorithm. Alterations typically include operations like mutation, crossover, or any other
 * genetic transformation applied to individuals in the population. These operations are crucial in introducing genetic
 * diversity and enabling the exploration of the solution space.
 *
 * @param T The type of value held by the features.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param S The type of evolutionary state, which must extend [EvolutionState].
 */
interface AlterationEngine<T, F, R, S> where F : Feature<T, F>,
                                             R : Representation<T, F>,
                                             S : EvolutionState<T, F, R, S> {

    /**
     * Performs genetic alterations on the population within the given evolutionary state.
     *
     * The `alter` function is responsible for applying one or more genetic alteration operations to the individuals
     * in the population. These alterations could include mutation, crossover, or other transformations that modify
     * the genetic makeup of the population. The function is a `suspend` function, allowing it to be used in
     * asynchronous contexts, which is particularly useful in large-scale evolutionary algorithms where alterations may
     * involve complex computations.
     *
     * @param state The current evolutionary state that contains the population to be altered.
     * @return The new evolutionary state after applying the genetic alterations.
     */
    suspend fun alter(state: S): Either<AlterationException, S>
}
