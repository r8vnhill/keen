/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.util.listeners.records

import cl.ravenhill.keen.genetic.genes.Gene
import kotlinx.serialization.Serializable

@Serializable
data class GenerationRecord<DNA, G : Gene<DNA, G>>(
    val generation: Int,
)
