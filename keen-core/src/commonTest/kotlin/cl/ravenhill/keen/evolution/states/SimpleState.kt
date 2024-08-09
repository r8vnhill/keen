/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.states

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

data class SimpleState<T, F, R>(
    override val population: Population<T, F, R>,
    override val ranker: IndividualRanker<T, F, R>,
    override val generation: Int
) : EvolutionState<T, F, R> where F : Feature<T, F>, R : Representation<T, F>
