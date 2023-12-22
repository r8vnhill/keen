/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.ranking.FitnessMaxRanker
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.listeners.records.GenerationRecord
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource


abstract class AbstractEvolutionListener<T, G> : EvolutionListener<T, G> where G : Gene<T, G> {
    override var ranker: IndividualRanker<T, G> = FitnessMaxRanker()
    override var evolution: EvolutionRecord<T, G> = EvolutionRecord()
    protected val generations by lazy { evolution.generations }
    protected lateinit var currentGeneration: GenerationRecord<T, G>
    @ExperimentalTime
    override var timeSource: TimeSource = TimeSource.Monotonic
}