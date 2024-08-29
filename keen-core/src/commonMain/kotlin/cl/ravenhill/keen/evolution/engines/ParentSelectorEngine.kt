/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Interface for selecting parents in an evolutionary algorithm.
 *
 * The `ParentSelectorEngine` interface defines the contract for a component responsible for selecting parents from a
 * population in an evolutionary algorithm. Parent selection is a critical step in the evolutionary process, as it
 * determines which individuals will contribute to the next generation. The selection process typically involves
 * evaluating the fitness of individuals and applying selection strategies (e.g., tournament selection, roulette wheel
 * selection) to choose the parents that will undergo crossover and mutation.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 */
interface ParentSelectorEngine<T, F, R, S>
        where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> {

    /**
     * Selects parents from the current evolutionary state.
     *
     * The `selectParents` function is responsible for selecting a subset of individuals from the population to act as
     * parents for the next generation. The selection process is typically based on the fitness of the individuals and
     * the specific selection strategy implemented. The function returns an updated state where the selected parents are
     * prepared for crossover and mutation operations.
     *
     * @param state The current evolutionary state from which parents are to be selected.
     * @return An updated evolutionary state with the selected parents.
     */
    suspend fun selectParents(state: S): S
}
