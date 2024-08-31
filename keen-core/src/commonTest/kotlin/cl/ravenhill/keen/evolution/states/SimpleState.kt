/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.states

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Represents the state of an evolutionary algorithm at a given generation.
 *
 * @param T The type of value stored by the feature.
 * @param F The kind of feature stored in a representation, which must implement [Feature].
 * @param R The type of representation used by the individual, which must implement [Representation].
 *
 * @property population The population of individuals in the evolutionary algorithm.
 * @property ranker The [IndividualRanker] used to evaluate and rank individuals.
 * @property generation The current generation number.
 *
 * @constructor Creates an instance of [SimpleState] with the specified population, ranker, and generation.
 */
data class SimpleState<T, F, R>(
    override val population: Population<T, F, R>,
    override val ranker: IndividualRanker<T, F, R>,
    override val generation: Int
) : EvolutionState<T, F, R, SimpleState<T, F, R>> where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * Creates a copy of the current state with the specified population, ranker, and generation.
     *
     * @param population The new population of individuals.
     * @param ranker The new [IndividualRanker] to evaluate and rank individuals.
     * @param generation The new generation number.
     */
    override fun makeCopy(
        population: Population<T, F, R>,
        ranker: IndividualRanker<T, F, R>,
        generation: Int
    ) = copy(population = population, ranker = ranker, generation = generation)
}
