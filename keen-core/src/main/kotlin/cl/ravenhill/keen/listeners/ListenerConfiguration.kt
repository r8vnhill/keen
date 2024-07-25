package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.ranking.FitnessMaxRanker
import cl.ravenhill.keen.ranking.FitnessRanker
import cl.ravenhill.keen.utils.Box
import kotlin.time.Duration
import kotlin.time.TimeSource

/**
 * Configuration data class for setting up listeners in the evolutionary computation process.
 *
 * ## Usage:
 * This class provides configuration for listeners, including the ranker used for ranking individuals, the record of the
 * evolution process, the time source, and the precision for measuring duration.
 *
 * ### Example 1: Default Configuration
 * ```
 * val config = ListenerConfiguration<Int, MyGene>()
 * ```
 *
 * ### Example 2: Custom Configuration
 * ```
 * val customRanker = MyCustomRanker<Int, MyGene>()
 * val customEvolution = MyCustomEvolutionRecord<Int, MyGene>()
 * val customTimeSource = MyCustomTimeSource()
 * val config = ListenerConfiguration(
 *     ranker = customRanker,
 *     evolution = customEvolution,
 *     timeSource = customTimeSource,
 *     precision = Duration::inWholeSeconds
 * )
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @property ranker the ranker used for ranking individuals, defaults to [FitnessMaxRanker]
 * @property evolution the record of the evolution process, defaults to [EvolutionRecord]
 * @property timeSource the source of time, defaults to [TimeSource.Monotonic]
 * @property precision the function used to measure the duration, defaults to [Duration.inWholeMilliseconds]
 * @property currentGeneration a box that holds the current generation record, initialized to null
 */
data class ListenerConfiguration<T, G>(
    val ranker: FitnessRanker<T, G> = FitnessMaxRanker(),
    val evolution: EvolutionRecord<T, G> = EvolutionRecord(),
    val timeSource: TimeSource = TimeSource.Monotonic,
    val precision: Duration.() -> Long = Duration::inWholeMilliseconds,
) where G : Gene<T, G> {
    val currentGeneration = Box.mutable<GenerationRecord<T, G>?>(null)
}
