/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.util.listeners.records

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.IntRequirement.BeNegative
import kotlinx.serialization.Serializable

@Serializable
data class GenerationRecord(val generation: Int) {
    init {
        enforce {
            "The generation number [$generation] must be positive" { generation mustNot BeNegative }
        }
    }
}
