/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners.records

import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.repr.Representation


/**
 * Represents a record of the evolutionary process.
 *
 * The `EvolutionRecord` data class encapsulates the records of all generations in an evolutionary algorithm. It also
 * includes a record for the initialization phase.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @property generations The list of generation records.
 * @constructor Creates an instance of `EvolutionRecord` with an optional list of generation records.
 */
data class EvolutionRecord<T, F, R>(val generations: MutableList<GenerationRecord<T, F, R>> = mutableListOf()) :
    AbstractTimedRecord() where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * The record for the initialization phase of the evolutionary process.
     *
     * The `InitializationRecord` class extends `AbstractTimedRecord` to provide detailed timing information for
     * the initialization phase.
     */
    val initialization = InitializationRecord()

    /**
     * Represents a timed record for the initialization phase in the evolutionary process.
     */
    class InitializationRecord : AbstractTimedRecord()
}
