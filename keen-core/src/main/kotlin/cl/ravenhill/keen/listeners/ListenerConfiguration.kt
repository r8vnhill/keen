package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.ranking.FitnessMaxRanker
import cl.ravenhill.keen.ranking.Ranker
import cl.ravenhill.keen.utils.Box
import kotlin.time.Duration
import kotlin.time.TimeSource

/**
 * Configuration settings for creating listeners in the evolutionary algorithm.
 *
 * The `ListenerConfiguration` data class encapsulates various configuration settings used to create and configure
 * listeners for monitoring and responding to events in the evolutionary process. This includes settings for the ranker,
 * the evolution record, the time source, and the precision of time measurements.
 *
 * ## Usage:
 * Use this class to configure listeners with specific settings before attaching them to the evolutionary algorithm.
 * This configuration allows listeners to have access to important context and utility functions.
 *
 * ### Example:
 * ```kotlin
 * val listenerConfig = ListenerConfiguration(
 *     ranker = FitnessMaxRanker(),
 *     evolution = EvolutionRecord(),
 *     timeSource = TimeSource.Monotonic,
 *     precision = Duration::inWholeMilliseconds
 * )
 * val listeners = listOf<ListenerFactory<MyGeneType, MyFeatureType>> { config ->
 *     EvolutionSummary(config)
 * }.map { it(listenerConfig) }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @property ranker The ranker used to evaluate and compare individuals in the population.
 * @property evolution The record of the evolutionary process.
 * @property timeSource The source of time used for measuring durations and timestamps.
 * @property precision The function used to determine the precision of time measurements.
 * @constructor Creates an instance of `ListenerConfiguration` with the specified settings.
 */
data class ListenerConfiguration<T, F>(
    val ranker: Ranker<T, F> = FitnessMaxRanker(),
    val evolution: EvolutionRecord<T, F> = EvolutionRecord(),
    val timeSource: TimeSource = TimeSource.Monotonic,
    val precision: Duration.() -> Long = Duration::inWholeMilliseconds,
) where F : Feature<T, F> {

    /**
     * The current generation record, stored in a mutable box.
     *
     * This property allows listeners to access and modify the current generation record during the evolutionary process.
     */
    val currentGeneration = Box.mutable<GenerationRecord<T, F>?>(null)
}
