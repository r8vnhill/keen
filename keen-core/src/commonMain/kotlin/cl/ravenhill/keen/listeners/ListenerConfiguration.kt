/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.ranking.FitnessMaxRanker
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import kotlin.time.Duration
import kotlin.time.TimeSource

/**
 * Configuration for a listener that manages evolution and timing within an evolutionary algorithm.
 *
 * @param T The type of value stored by the feature.
 * @param F The kind of feature stored in a representation, which must implement [Feature].
 * @param R The type of representation used by the individual, which must implement [Representation].
 *
 * @property ranker The [IndividualRanker] used to evaluate and rank individuals. Defaults to [FitnessMaxRanker].
 * @property evolution The [EvolutionRecord] that tracks the evolution process. Defaults to a new instance of [EvolutionRecord].
 * @property timeSource The [TimeSource] providing time-related functionalities. Defaults to [TimeSource.Monotonic].
 * @property precision A lambda function that takes a [Duration] and returns a [Long] value representing the precision.
 *   Defaults to [Duration.inWholeMilliseconds].
 */
data class ListenerConfiguration<T, F, R>(
    val ranker: IndividualRanker<T, F, R> = FitnessMaxRanker(),
    val evolution: EvolutionRecord<T, F, R> = EvolutionRecord(),
    val timeSource: TimeSource = TimeSource.Monotonic,
    val precision: Duration.() -> Long = Duration::inWholeMilliseconds
) where F : Feature<T, F>, R : Representation<T, F>
