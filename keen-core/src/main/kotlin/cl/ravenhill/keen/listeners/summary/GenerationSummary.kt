package cl.ravenhill.keen.listeners.summary

import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.EvolutionListener.Companion.computeSteadyGenerations
import cl.ravenhill.keen.listeners.mixins.GenerationListener
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord
import cl.ravenhill.keen.ranking.IndividualRanker
import kotlin.time.Duration
import kotlin.time.TimeSource

class GenerationSummary<T, G>(
    private val evolution: EvolutionRecord<T, G>,
    private val timeSource: TimeSource,
    private val ranker: IndividualRanker<T, G>,
    private val precision: Duration.() -> Long
) : GenerationListener<T, G> where G : Gene<T, G> {

    private lateinit var currentGeneration: GenerationRecord<T, G>

    override fun onGenerationStarted(state: EvolutionState<T, G>) {
        currentGeneration = GenerationRecord<T, G>(evolution.generations.size + 1).apply {
            startTime = timeSource.markNow()
            this.population.parents = List(state.population.size) {
                IndividualRecord(state.population[it].genotype, state.population[it].fitness)
            }
        }
        evolution.generations += currentGeneration
    }

    override fun onGenerationEnded(state: EvolutionState<T, G>) {
        currentGeneration.apply {
            duration = currentGeneration.startTime.elapsedNow().precision()
            population.offspring = List(state.population.size) {
                IndividualRecord(state.population[it].genotype, state.population[it].fitness)
            }
            steady = computeSteadyGenerations(ranker, evolution)
        }
    }
}