/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


@file:OptIn(ExperimentalTime::class)

package cl.ravenhill.keen.util.listeners.records

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.records.EvolutionRecord.InitializationRecord
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.time.ExperimentalTime

/**
 * This class represents a record of an evolutionary process, which can be serialized for later use
 * or analysis.
 * It extends from [AbstractRecord], meaning it also includes start time and duration properties.
 *
 * @param DNA The type parameter representing the genetic information used in this evolution
 *            process.
 * @param G The type parameter representing a gene which contains the [DNA].
 *          It must be a subtype of [Gene].
 *
 * @property generations The list of [GenerationRecord]s which document each generation in the
 *                       evolution process.
 *                       By default, this list is empty and can be added to during the evolution
 *                       process.
 *
 * @property initialization This is an [InitializationRecord] instance which documents the initial
 *                          setup or configuration of the evolution process.
 *                          The exact details of what is recorded depend on how the evolution
 *                          process is defined and may include information like the initial
 *                          population, parameters of the evolution process, etc.
 *
 * @constructor Creates an empty [EvolutionRecord] with an empty list of [generations] and a new
 *              [InitializationRecord].
 */
@Serializable
data class EvolutionRecord<DNA, G : Gene<DNA, G>>(
    val generations: MutableList<GenerationRecord> = mutableListOf(),
) : AbstractTimedRecord() {
    val initialization = InitializationRecord()

    /**
     * This class represents a record of the initialization phase of the evolution process.
     * It extends from [AbstractRecord], meaning it also includes start time and duration properties.
     * The exact details of what is recorded in this class depend on the evolution process and how
     * the initialization phase is defined.
     */
    @Serializable
    class InitializationRecord : AbstractTimedRecord()
}
