/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.plotter

import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

internal actual suspend fun <T, F : Feature<T, F>, R : Representation<T, F>> plot(
    evolution: EvolutionRecord<T, F, R>,
    ranker: IndividualRanker<T, F, R>
) {
    throw NotImplementedError("Plotting is not supported in JS.")
}
