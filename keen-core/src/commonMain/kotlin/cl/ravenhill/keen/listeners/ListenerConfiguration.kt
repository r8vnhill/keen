/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.listeners.ListenerConfiguration.Companion.defaultEvolutionRecord
import cl.ravenhill.keen.listeners.ListenerConfiguration.Companion.defaultPrecision
import cl.ravenhill.keen.listeners.ListenerConfiguration.Companion.defaultRanker
import cl.ravenhill.keen.listeners.ListenerConfiguration.Companion.defaultTimeSource
import cl.ravenhill.keen.listeners.precision.TimePrecision
import cl.ravenhill.keen.listeners.precision.WholeMilliseconds
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.ranking.FitnessMaxRanker
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.utils.box.MutableBox
import kotlin.time.TimeSource

/**
 * Configuration class for initializing and managing listeners in an evolutionary algorithm.
 *
 * The `ListenerConfiguration` class encapsulates the necessary configurations for listeners used in an evolutionary
 * algorithm. It provides defaults for various components, such as the ranker, evolution record, time source, and time
 * precision, ensuring that listeners have the required context and resources to operate effectively.
 *
 * @param T The type of the value held by the genes in the individuals.
 * @param F The type of the feature used in the individual's representation.
 * @param R The type of the representation used by the individual.
 * @property ranker The [IndividualRanker] used to evaluate and compare individuals in the population. Defaults to
 *   [defaultRanker].
 * @property evolution The [EvolutionRecord] that tracks the state and progress of the evolutionary process. Defaults to
 *   [defaultEvolutionRecord].
 * @property timeSource The [TimeSource] used for timing operations within the evolutionary algorithm. Defaults to
 *   [defaultTimeSource].
 * @property precision The [TimePrecision] that defines the precision level for timing operations. Defaults to
 *   [defaultPrecision].
 * @property currentGeneration A [MutableBox] that holds the current generation's record, allowing listeners to access
 *   and modify the generation data.
 */
data class ListenerConfiguration<T, F, R>(
    val ranker: IndividualRanker<T, F, R> = defaultRanker(),
    val evolution: EvolutionRecord<T, F, R> = defaultEvolutionRecord(),
    val timeSource: TimeSource = defaultTimeSource,
    val precision: TimePrecision = defaultPrecision,
) where F : Feature<T, F>, R : Representation<T, F> {
    val currentGeneration = MutableBox<GenerationRecord<T, F, R>?>(null)

    internal companion object {
        /**
         * Provides a default ranker for evaluating individuals in the evolutionary algorithm.
         *
         * The `defaultRanker` function returns a synchronous fitness maximization ranker, which is used to evaluate and
         * compare individuals based on their fitness values. This ranker is suitable for scenarios where the goal is to
         * maximize fitness, and the evaluation process is performed sequentially.
         *
         * @return A default instance of [FitnessMaxRanker] configured for synchronous operation.
         * @param T The type of value held by the genes in the individuals.
         * @param F The type of feature used in the individual's representation.
         * @param R The type of representation used by the individual.
         */
        fun <T, F, R> defaultRanker()
                where F : Feature<T, F>,
                      R : Representation<T, F> = FitnessMaxRanker.sync<T, F, R>()

        /**
         * Provides a default evolution record for tracking the state of the evolutionary process.
         *
         * The `defaultEvolutionRecord` function returns a new instance of [EvolutionRecord], which is used to store
         * and manage the state and progress of the evolutionary algorithm. This record includes information about
         * generations, individuals, and their fitness, enabling detailed tracking and analysis of the evolution
         * process.
         *
         * @return A default instance of `EvolutionRecord` initialized for the specified types.
         * @param T The type of value held by the genes in the individuals.
         * @param F The type of feature used in the individual's representation.
         * @param R The type of representation used by the individual.
         */
        fun <T, F, R> defaultEvolutionRecord()
                where F : Feature<T, F>,
                      R : Representation<T, F> = EvolutionRecord<T, F, R>()

        /**
         * Provides a default time source for measuring durations in the evolutionary process.
         *
         * The `defaultTimeSource` property represents a monotonic time source, which is typically used for measuring
         * elapsed time without being affected by changes in the system clock. This time source ensures that timing
         * measurements remain consistent and reliable throughout the evolutionary process.
         *
         * @return The default monotonic time source.
         */
        val defaultTimeSource: TimeSource = TimeSource.Monotonic


        /**
         * Provides a default time precision for timing operations in the evolutionary algorithm.
         *
         * The `defaultPrecision` property defines the precision level for timing operations, with the default being
         * [WholeMilliseconds]. This setting is used to control how durations are measured and reported during the
         * evolutionary process, ensuring that the timing data is consistent and appropriate for the algorithm's needs.
         *
         * @return The default time precision set to whole milliseconds.
         */
        val defaultPrecision: TimePrecision = WholeMilliseconds
    }
}
