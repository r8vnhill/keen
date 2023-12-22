/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.EvolutionListener.Companion.computeSteadyGenerations
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
class EvolutionPrinter<T, G>(val every: Int) : AbstractEvolutionListener<T, G>() where G : Gene<T, G> {
    override fun onGenerationStarted(state: EvolutionState<T, G>) {
        currentGeneration = GenerationRecord<T, G>(state.generation).apply {
            startTime = timeSource.markNow()
        }
    }

    override fun onGenerationEnded(state: EvolutionState<T, G>) {
        with(currentGeneration) {
            duration = startTime.elapsedNow().inWholeNanoseconds
            evolution.generations += this
            currentGeneration.population.offspring = List(state.population.size) {
                IndividualRecord(
                    state.population[it].genotype,
                    state.population[it].fitness
                )
            }
            steady = computeSteadyGenerations(ranker, evolution)
        }
        if (state.generation % every == 0) {
            display()
        }
    }


    override fun display() = println(
        if (evolution.generations.isEmpty()) {
            "No generations have been processed yet."
        } else {
            """ === Generation ${evolution.generations.size} ===
            |--> Average generation time: ${
                evolution.generations.map { it.duration }.average()
            } ns
            |--> Max generation time: ${
                evolution.generations.maxOfOrNull { it.duration }
            } ns
            |--> Min generation time: ${
                evolution.generations.minOfOrNull { it.duration }
            } ns
            |--> Steady generations: ${generations.last().steady}
            |--> Best fitness: ${generations.last().population.offspring.first().fitness}
            |--> Worst fitness: ${generations.last().population.offspring.last().fitness}
            |--> Average fitness: ${
                generations.last().population.offspring.map { it.fitness }.average()
            }
            |--> Fittest: ${generations.last().population.offspring.first().genotype}
            |<<<>>>
            """.trimMargin()
        }
    )
}