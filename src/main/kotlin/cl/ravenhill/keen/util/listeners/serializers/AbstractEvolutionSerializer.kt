/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.listeners.serializers

import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.AbstractEvolutionListener
import cl.ravenhill.keen.util.listeners.EvolutionListener
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import cl.ravenhill.keen.util.listeners.records.IndividualRecord
import kotlin.time.ExperimentalTime

@ExperimentalTime
abstract class AbstractEvolutionSerializer<DNA, G : Gene<DNA, G>> :
    AbstractEvolutionListener<DNA, G>() {

    /**
     * This method is invoked at the start of each new generation in the evolution process.
     * It records the start time of the generation which will be used to calculate the
     * duration of the generation when it finishes.
     *
     * @param generation The number of the generation that is starting.
     * @param population The population of the generation that is starting.
     */
    @ExperimentalTime
    override fun onGenerationStarted(generation: Int, population: Population<DNA, G>) {
        _currentGeneration = GenerationRecord(generation).apply {
            startTime = timeSource.markNow()
        }
    }

    /**
     * Called when the current generation finishes, records the duration of the generation.
     */
    override fun onGenerationFinished(population: Population<DNA, G>) {
        // Calculate duration
        _currentGeneration.duration = _currentGeneration.startTime.elapsedNow().inWholeNanoseconds
        // Sort population and set resulting
        val sorted = optimizer.sort(population)
        _currentGeneration.population.resulting = List(sorted.size) {
            IndividualRecord("${sorted[it].genotype}", sorted[it].fitness)
        }
        generations.lastOrNull()?.let { lastGeneration ->
            EvolutionListener.computeSteadyGenerations(lastGeneration, _currentGeneration)
        }
        // Add current generation to the list of generations
        _currentGeneration.also { evolution.generations += it }
    }

    /**
     * Called when the initialization of the population starts, marks the start time.
     */
    override fun onInitializationStarted() {
        evolution.initialization.startTime = timeSource.markNow()
    }

    /**
     * Called when the initialization of the population finishes, records the duration.
     */
    override fun onInitializationFinished() {
        evolution.initialization.duration =
            evolution.initialization.startTime.elapsedNow().inWholeNanoseconds
    }

    /**
     * Called when the evaluation of the population starts, marks the start time.
     */
    override fun onEvaluationStarted() {
        _currentGeneration.evaluation.startTime = timeSource.markNow()
    }

    /**
     * Called when the evaluation of the population finishes, records the duration.
     */
    override fun onEvaluationFinished() {
        _currentGeneration.evaluation.duration =
            _currentGeneration.evaluation.startTime.elapsedNow().inWholeNanoseconds
    }

    /**
     * Called when the offspring selection process starts, marks the start time.
     */
    override fun onOffspringSelectionStarted() {
        _currentGeneration.offspringSelection.startTime = timeSource.markNow()
    }

    /**
     * Called when the offspring selection process finishes, records the duration.
     */
    override fun onOffspringSelectionFinished() {
        _currentGeneration.offspringSelection.duration =
            _currentGeneration.offspringSelection.startTime.elapsedNow().inWholeNanoseconds
    }

    /**
     * Called when the survivor selection process starts, marks the start time.
     */
    override fun onSurvivorSelectionStarted() {
        _currentGeneration.survivorSelection.startTime = timeSource.markNow()
    }

    /**
     * Called when the survivor selection process finishes, records the duration.
     */
    override fun onSurvivorSelectionFinished() {
        _currentGeneration.survivorSelection.duration =
            _currentGeneration.survivorSelection.startTime.elapsedNow().inWholeNanoseconds
    }

    /**
     * Called when the alteration of the population starts, marks the start time.
     */
    override fun onAlterationStarted() {
        _currentGeneration.alteration.startTime = timeSource.markNow()
    }

    /**
     * Called when the alteration of the population finishes, records the duration.
     */
    override fun onAlterationFinished() {
        _currentGeneration.alteration.duration =
            _currentGeneration.alteration.startTime.elapsedNow().inWholeNanoseconds
    }

    /**
     * Called when the evolution starts, marks the start time.
     */
    override fun onEvolutionStart() {
        evolution.startTime = timeSource.markNow()
    }

    /**
     * Called when the evolution finishes, records the duration.
     */
    override fun onEvolutionFinished() {
        evolution.duration = evolution.startTime.elapsedNow().inWholeNanoseconds
    }
}
