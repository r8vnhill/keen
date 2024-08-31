/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.records

import arrow.core.getOrElse
import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constrainedTo
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

data class GenerationRecord<T, F, R>(val generation: Int) :
    AbstractTimedRecord() where F : Feature<T, F>, R : Representation<T, F> {
    init {
        constrained {
            "The generation number ($generation) must not be negative" { generation mustNot BeNegative }
        }.onLeft { throw it }
    }

    var steady = 0
        set(value) {
            field = value.constrainedTo {
                "The steady counter ($value) must not be negative" { value mustNot BeNegative }
            }.getOrElse { throw it }
        }

    val population: PopulationRecord<T, F, R> = PopulationRecord()

    class AlterationRecord : AbstractTimedRecord()

    class EvaluationRecord : AbstractTimedRecord()

    class SelectionRecord : AbstractTimedRecord()

    data class PopulationRecord<T, F, R>(
        val parents: List<IndividualRecord<T, F, R>> = emptyList(),
        var offspring: List<IndividualRecord<T, F, R>> = emptyList()
    ) : AbstractTimedRecord() where F : Feature<T, F>, R : Representation<T, F>
}
