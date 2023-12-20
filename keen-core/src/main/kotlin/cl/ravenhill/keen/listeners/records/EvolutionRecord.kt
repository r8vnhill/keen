/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners.records

import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Records the complete evolutionary process in an evolutionary algorithm.
 *
 * `EvolutionRecord` captures detailed information about each generation within an evolutionary algorithm run. It
 * extends from `AbstractTimedRecord` to include overall timing information for the entire evolution process. The
 * primary purpose of this class is to provide a comprehensive log of the evolutionary process, capturing details from
 * initialization through each generation.
 *
 * ## Key Components:
 * - **Generations**: A mutable list of `GenerationRecord` objects, each representing a specific generation in the
 *   evolutionary process.
 * - **Initialization Record**: Timing and additional information about the initialization phase of the evolutionary
 *   algorithm.
 *
 * ## Usage:
 * This class is particularly useful for analysis, debugging, and optimization of evolutionary algorithms. It allows
 * developers and researchers to track the evolution process in detail, including the duration of specific stages and
 * changes in population over generations.
 *
 * ### Example:
 * ```
 * val evolutionRecord = EvolutionRecord<Int, MyGeneType>()
 * // Initialize the algorithm
 * evolutionRecord.initialization.startTime = //...
 * // Add generation records as the algorithm progresses
 * evolutionRecord.generations.add(generationRecord)
 * //... Continue for each generation
 * ```
 * In this example, `EvolutionRecord` is used to keep a log of the entire evolutionary process, from
 * initialization to the final generation.
 *
 * @param T The type of the genetic data or information.
 * @param G The type of gene encapsulated within the individuals.
 * @param generations The list of generation records, each documenting a specific generation in the evolution.
 * @property initialization A record of the initialization phase in the evolutionary process.
 *
 * @constructor Creates a new instance of `EvolutionRecord` with an optional list of generation records.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @version 2.0.0
 * @since 2.0.0
 */
data class EvolutionRecord<T, G>(val generations: MutableList<GenerationRecord<T, G>> = mutableListOf()) :
    AbstractTimedRecord() where G : Gene<T, G> {

    val initialization = InitializationRecord()

    /**
     * A subclass of `AbstractTimedRecord` that specifically records the timing information
     * related to the initialization phase of an evolutionary algorithm.
     */
    class InitializationRecord : AbstractTimedRecord()
}
