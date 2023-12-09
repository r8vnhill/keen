/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners.records

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Represents a record of a generation in an evolutionary algorithm.
 *
 * This class holds detailed information about a specific generation, including its number, various
 * process timings, and population data. It extends from `AbstractTimedRecord` to include timing
 * information for different stages of the evolutionary process within the generation.
 *
 * ## Key Components:
 * - **Generation Number**: The sequential number of the generation.
 * - **Alteration Record**: Timing information for the alteration process.
 * - **Evaluation Record**: Timing information for the fitness evaluation process.
 * - **Offspring Selection Record**: Timing information for the offspring selection process.
 * - **Survivor Selection Record**: Timing information for the survivor selection process.
 * - **Population Record**: Information about the parents and offspring in the generation.
 * - **Steady**: A counter for the number of generations without significant change, used in some
 *    termination conditions.
 *
 * ## Constraints:
 * - The generation number and the steady counter must not be negative.
 *
 * ## Usage:
 * `GenerationRecord` is particularly useful in scenarios where detailed analysis and logging of the
 * evolutionary process are required. It provides a structured way to capture and store timings and
 * population details for each generation, facilitating deeper insights into the algorithm's behavior
 * and performance.
 *
 * ### Example:
 * ```
 * val generationRecord = GenerationRecord<Int, MyGeneType>(generationNumber)
 * // ... Perform evolutionary processes
 * // Set records for each process
 * generationRecord.alteration.startTime = //...
 * // ... and so on
 * ```
 * In this example, `GenerationRecord` is used to track and record details of a specific generation
 * in an evolutionary algorithm.
 *
 * @param T The type of the genetic data or information.
 * @param G The type of gene encapsulated within the individuals.
 * @param generation The sequential number of the generation being recorded.
 *
 * @constructor Creates a new instance of `GenerationRecord` with the provided generation number.
 *   Ensures that the generation number is non-negative.
 *
 * @see AbstractTimedRecord for the base class providing timing functionalities.
 */
class GenerationRecord<T, G>(val generation: Int) : AbstractTimedRecord() where G : Gene<T, G> {

    init {
        constraints {
            "The generation number [$generation] must not be negative" { generation mustNot BeNegative }
        }
    }

    // Detailed records for various stages of the evolutionary process within the generation.
    val alteration = AlterationRecord()
    val evaluation = EvaluationRecord()
    val parentSelection = SelectionRecord()
    val survivorSelection = SelectionRecord()
    val population = PopulationRecord<T, G>()
    var steady: Int = 0
        set(value) {
            constraints {
                "The steady counter [$value] must not be negative" { value mustNot BeNegative }
            }
            field = value
        }

    // Inner classes representing timed records for specific stages of the evolutionary process.
    class AlterationRecord : AbstractTimedRecord()
    class EvaluationRecord : AbstractTimedRecord()
    class SelectionRecord : AbstractTimedRecord()

    /**
     * A record of the population within the generation, including both parents and offspring.
     *
     * @param parents The list of parent individuals in the generation.
     * @param offspring The list of offspring individuals generated in the generation.
     */
    data class PopulationRecord<T, G>(
        var parents: List<IndividualRecord<T, G>> = emptyList(),
        var offspring: List<IndividualRecord<T, G>> = emptyList(),
    ) where G : Gene<T, G>
}
