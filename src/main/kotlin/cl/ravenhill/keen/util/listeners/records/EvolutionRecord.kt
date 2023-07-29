/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


@file:OptIn(ExperimentalTime::class)

package cl.ravenhill.keen.util.listeners.records

import cl.ravenhill.keen.genetic.genes.Gene
import kotlinx.serialization.Serializable
import kotlin.properties.Delegates
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark

@Serializable
data class EvolutionRecord<DNA, G : Gene<DNA, G>>(
    val generations: MutableList<GenerationRecord> = mutableListOf(),
) {
    val initialization = InitializationRecord()
    val generationTimes: List<Long> get() = generations.map { it.duration.inWholeMilliseconds }

    @Serializable
    class InitializationRecord {
        lateinit var startTime: TimeMark
        var duration: Duration by Delegates.notNull()
    }
}