/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

data class ListenerConfiguration<T, F, R>(
    val ranker: IndividualRanker<T, F, R>
) where F : Feature<T, F>, R : Representation<T, F>
