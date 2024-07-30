/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.records

import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

data class EvolutionRecord<T, F, R>(
    val generations: List<GenerationRecord<T, F, R>> = emptyList()
) : AbstractTimedRecord() where F : Feature<T, F>, R : Representation<T, F> {
    val initialization = InitializationRecord()

    class InitializationRecord : AbstractTimedRecord()
}
