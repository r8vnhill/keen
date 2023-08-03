package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import kotlin.time.ExperimentalTime

/**
 * An implementation of [EvolutionListener] that collects various statistics from an evolutionary
 * algorithm run and provides a comprehensive summary.
 *
 * It collects and calculates various metrics such as initialization time, evaluation times,
 * selection times, alteration times, and evolution results.
 * These metrics are captured for every generation and can be used to track and analyze the
 * performance of the genetic algorithm over time.
 *
 * @param DNA The type of the gene's value.
 * @param G The type of the gene.
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
        val generations = evolution.generations
        return """
        ------------ Evolution Summary ---------------
        |--> Initialization time: ${evolution.initialization.duration} ms
        ------------- Evaluation Times ----------------
        |--> Average: ${generations.map { it.evaluation.duration.inWholeMilliseconds }.average()} ms
        |--> Max: ${generations.maxOfOrNull { it.evaluation.duration.inWholeMilliseconds }} ms
        |--> Min: ${generations.minOfOrNull { it.evaluation.duration.inWholeMilliseconds }} ms
        -------------- Selection Times ----------------
        |--> Offspring Selection
        |   |--> Average: ${
            generations.map { it.offspringSelection.duration.inWholeMilliseconds }.average()
        } ms
        |   |--> Max: ${
            generations.maxOfOrNull { it.offspringSelection.duration.inWholeMilliseconds }
        } ms
        |   |--> Min: ${
            generations.minOfOrNull { it.offspringSelection.duration.inWholeMilliseconds }
        } ms
        |--> Survivor Selection
        |   |--> Average: ${
            generations.map { it.survivorSelection.duration.inWholeMilliseconds }.average()
        } ms
        |   |--> Max: ${
            generations.maxOfOrNull { it.survivorSelection.duration.inWholeMilliseconds }
        } ms
        |   |--> Min: ${
            generations.minOfOrNull { it.survivorSelection.duration.inWholeMilliseconds }
        } ms
        --------------- Alteration Times --------------
        |--> Average: ${generations.map { it.alteration.duration.inWholeMilliseconds }.average()} ms
        |--> Max: ${generations.maxOfOrNull { it.alteration.duration.inWholeMilliseconds }} ms
        |--> Min: ${generations.minOfOrNull { it.alteration.duration.inWholeMilliseconds }} ms
        -------------- Evolution Results --------------
        |--> Total time: ${evolution.duration.inWholeMilliseconds} ms
        |--> Average generation time: ${
            generations.map { it.duration.inWholeMilliseconds }.average()
        } ms
        |--> Max generation time: ${
            generations.maxOfOrNull { it.duration.inWholeMilliseconds }
        } ms
        |--> Min generation time: ${
            generations.minOfOrNull { it.duration.inWholeMilliseconds }
        } ms
        |--> Generation: ${evolution.generations.last().generation}
        |--> Steady generations: $steadyGenerations
        |--> Fittest: ${population.firstOrNull().toString().replace("\n", "; ")}
        |--> Best fitness: ${bestFitness.lastOrNull()}
        """.trimIndent()
    }

    /**
     * Called when a new generation starts, marks the start time of the generation.
     */
    @ExperimentalTime
    override fun onGenerationStarted(generation: Int) {
        _currentGeneration = GenerationRecord(generation).apply {
            startTime = timeSource.markNow()
        }
    }

    /**
     * Called when the current generation finishes, records the duration of the generation.
     */
    override fun onGenerationFinished() {
        _currentGeneration.duration = _currentGeneration.startTime.elapsedNow()
        evolution.generations += _currentGeneration
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
            evolution.initialization.startTime.elapsedNow()
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
            _currentGeneration.evaluation.startTime.elapsedNow()
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
            _currentGeneration.offspringSelection.startTime.elapsedNow()
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
            _currentGeneration.survivorSelection.startTime.elapsedNow()
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
            _currentGeneration.alteration.startTime.elapsedNow()
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
        evolution.duration = evolution.startTime.elapsedNow()
    }
}