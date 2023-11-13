/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.evolution.EvolutionResult
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.records.EvolutionRecord
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import cl.ravenhill.keen.util.optimizer.FitnessMaximizer
import cl.ravenhill.keen.util.optimizer.IndividualOptimizer
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

/**
 * Calculates statistics about a population.
 *
 * @param DNA  The type of the gene's value.
 *
 * @property population The population to calculate statistics from.
 * @property optimizer The optimizer used to calculate the fitness.
 * @property fittest The fittest individual.
 * @property generation The current generation.
 */
abstract class AbstractEvolutionListener<DNA, G: Gene<DNA, G>> : EvolutionListener<DNA, G> {
    override var optimizer: IndividualOptimizer<DNA, G> = FitnessMaximizer()
    override var generation: Int = 0
    override var evolution: EvolutionRecord<DNA, G> = EvolutionRecord()
    protected val generations by lazy { evolution.generations }
    protected lateinit var _currentGeneration: GenerationRecord
    override val currentGeneration by lazy { _currentGeneration }
    @ExperimentalTime
    override var timeSource: TimeSource = TimeSource.Monotonic

    override fun onResultUpdated() {
        generation = currentGeneration.generation
    }

    fun display() = println(toString())
}
