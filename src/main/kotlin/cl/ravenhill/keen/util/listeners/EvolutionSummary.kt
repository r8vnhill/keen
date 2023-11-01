package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import cl.ravenhill.keen.util.listeners.records.PhenotypeRecord
import kotlin.time.ExperimentalTime

/**
 * This class is an implementation of [EvolutionListener] that captures and reports statistics
 * related to the execution of a genetic algorithm.
 *
 * [EvolutionSummary] is designed to monitor the performance of the algorithm over the course
 * of its execution, providing comprehensive and detailed insights on various parameters like
 * initialization time, evaluation time, selection times, alteration times, and results of the evolution.
 *
 * This class can be used in any context where tracking and reporting the performance of the
 * genetic algorithm is required for analysis and fine-tuning.
 *
 * @param DNA represents the type of the gene's value.
 * @param G specifies the type of the gene.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
@OptIn(ExperimentalTime::class)
class EvolutionSummary<DNA, G : Gene<DNA, G>> : AbstractEvolutionListener<DNA, G>() {

    /**
     * Returns a string representation of the evolution summary, formatted for display.
     */
    override fun toString(): String {
        return """
        ------------ Evolution Summary ---------------
        |--> Initialization time: ${evolution.initialization.duration} ms
        ------------- Evaluation Times ----------------
        |--> Average: ${generations.map { it.evaluation.duration }.average()} ms
        |--> Max: ${generations.maxOfOrNull { it.evaluation.duration }} ms
        |--> Min: ${generations.minOfOrNull { it.evaluation.duration }} ms
        -------------- Selection Times ----------------
        |--> Offspring Selection
        |   |--> Average: ${
            generations.map { it.offspringSelection.duration }.average()
        } ms
        |   |--> Max: ${
            generations.maxOfOrNull { it.offspringSelection.duration }
        } ms
        |   |--> Min: ${
            generations.minOfOrNull { it.offspringSelection.duration }
        } ms
        |--> Survivor Selection
        |   |--> Average: ${
            generations.map { it.survivorSelection.duration }.average()
        } ms
        |   |--> Max: ${
            generations.maxOfOrNull { it.survivorSelection.duration }
        } ms
        |   |--> Min: ${
            generations.minOfOrNull { it.survivorSelection.duration }
        } ms
        --------------- Alteration Times --------------
        |--> Average: ${generations.map { it.alteration.duration }.average()} ms
        |--> Max: ${generations.maxOfOrNull { it.alteration.duration }} ms
        |--> Min: ${generations.minOfOrNull { it.alteration.duration }} ms
        -------------- Evolution Results --------------
        |--> Total time: ${evolution.duration} ms
        |--> Average generation time: ${
            generations.map { it.duration }.average()
        } ms
        |--> Max generation time: ${
            generations.maxOfOrNull { it.duration }
        } ms
        |--> Min generation time: ${
            generations.minOfOrNull { it.duration }
        } ms
        |--> Generation: ${evolution.generations.last().generation}
        |--> Steady generations: ${evolution.generations.last().steady}
        |--> Fittest: ${generations.last().population.resulting.first().genotype}
        |--> Best fitness: ${generations.last().population.resulting.first().fitness}
        """.trimIndent()
    }

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
            PhenotypeRecord("${sorted[it].genotype}", sorted[it].fitness)
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
