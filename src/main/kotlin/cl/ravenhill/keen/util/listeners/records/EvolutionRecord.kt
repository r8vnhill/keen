/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util.listeners.records

import cl.ravenhill.keen.genetic.genes.Gene
import kotlinx.serialization.Serializable

@Serializable
data class EvolutionRecord<DNA, G : Gene<DNA, G>>(
    val generations: MutableList<GenerationRecord> = mutableListOf(),
) {
    val generationTimes: List<Long> get() = generations.map { it.duration.inWholeMilliseconds }
}