package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import cl.ravenhill.keen.util.listeners.records.IndividualRecord
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
    override fun display() {
        println("""
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
        """.trimIndent())
    }

    override fun toString() =
        "EvolutionSummary(optimizer=$optimizer, generation=$generation, evolution=$evolution, " +
            "currentGenerationRecord=$currentGenerationRecord)"

    /**
     * Called at the beginning of each generation in the evolutionary process.
     * This method sets up a new [GenerationRecord] for the current generation, capturing its initial state
     * and marking its start time. This plays a vital role in tracking the evolution of each generation.
     *
     * ## Workflow
     * 1. **Generation Initialization**: Assigns a unique identifier to the generation, usually its sequence number.
     * 2. **Time Tracking**: Records the start time of the generation using a high-resolution time source. This is
     *    essential for measuring the generation's duration accurately.
     * 3. **Initial Population Recording**: Documents the initial state of the population. Each individual in the
     *    population is logged as an `IndividualRecord`, encompassing their genotype and initial fitness. This
     *    initial snapshot is crucial for later analysis and comparison with the final state of the generation.
     *
     * ## Usage Scenario
     * This method is a key part of the evolutionary cycle, marking the commencement of each generation's evolution.
     * It is typically followed by various evolutionary operations (such as selection, crossover, and mutation)
     * and concludes with the `onGenerationFinished` method, which finalizes the generation's results.
     *
     * The method is integral to the evolution tracking process, as it defines the starting point for each generation,
     * allowing for detailed analysis of how populations evolve and change over time.
     *
     * ```kotlin
     * override fun evolve(): EvolutionResult<DNA, G> {
     *     listeners.forEach { it.onGenerationStarted(generation, listOf()) }
     *     // Additional steps in the evolution process...
     *     val result = someEvolutionaryOperation()
     *     listeners.forEach { it.onGenerationFinished(result) }
     *     return result
     * }
     * ```
     *
     * In the example above, the `onGenerationStarted` method is called at the beginning of each evolutionary cycle,
     * setting up the initial state of the generation for tracking purposes.
     *
     * @param generation The numerical identifier of the current generation.
     * @param population The initial population at the beginning of the generation.
     *
     * @see [onGenerationFinished] for handling the end of a generation.
     * @see [GenerationRecord] for details on the structure used for recording generation data.
     * @see [Engine.evolve]
     */
    @ExperimentalTime
    override fun onGenerationStarted(generation: Int, population: Population<DNA, G>) {
        currentGenerationRecord = GenerationRecord<DNA, G>(generations.size + 1).apply {
            startTime = timeSource.markNow()
            this.population.initial = List(population.size) {
                IndividualRecord(population[it].genotype, population[it].fitness)
            }
        }
        evolution.generations += currentGenerationRecord
    }

    /**
     * Called at the conclusion of each generation during the evolutionary process. This method plays a
     * pivotal role in updating the [EvolutionSummary] with comprehensive details about the recently
     * completed generation.
     *
     * ## Workflow Overview
     * This method encompasses several key steps in processing and documenting the evolution of a generation:
     * 1. **Duration Calculation**: Measures the time elapsed since the commencement of the current generation.
     *    This metric is instrumental in assessing the time efficiency of the evolutionary process.
     * 2. **Recording Resulting Population**: Records the state of the population at the end of the generation.
     *    Each individual in the population is represented as an `IndividualRecord`, capturing their genotype
     *    and fitness metrics for record-keeping.
     * 3. **Computing Steady Generations**: Evaluates and records the count of steady generations, a critical
     *    metric for detecting potential convergence or stagnation within the evolutionary trajectory. It reflects
     *    the number of consecutive generations that have not exhibited significant changes in fitness.
     *
     * ## Context of Use
     * In the overarching evolutionary loop, this method is invoked post-evolution of each generation to
     * finalize and document the outcomes. It acts as a sequential follow-up to the `onGenerationStarted`
     * method, which establishes the initial setup for a generation's evolution.
     *
     * @param population The population at the conclusion of a generation, which is documented for
     *                   evolutionary analysis.
     *
     * @see [onGenerationStarted] for initializing generation parameters.
     * @see [Engine.evolve] for understanding the context within the overall evolutionary process.
     */
    override fun onGenerationFinished(population: Population<DNA, G>) {
        // Calculate duration
        currentGenerationRecord.duration = currentGenerationRecord.startTime.elapsedNow().inWholeNanoseconds
        // Record resulting population
        currentGenerationRecord.population.resulting = List(population.size) {
            IndividualRecord(population[it].genotype, population[it].fitness)
        }
        // Compute steady generations
        currentGenerationRecord.steady = EvolutionListener.computeSteadyGenerations(optimizer, evolution)
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
        currentGenerationRecord.evaluation.startTime = timeSource.markNow()
    }

    /**
     * Called when the evaluation of the population finishes, records the duration.
     */
    override fun onEvaluationFinished() {
        currentGenerationRecord.evaluation.duration =
            currentGenerationRecord.evaluation.startTime.elapsedNow().inWholeNanoseconds
    }

    /**
     * Called when the offspring selection process starts, marks the start time.
     */
    override fun onOffspringSelectionStarted() {
        currentGenerationRecord.offspringSelection.startTime = timeSource.markNow()
    }

    /**
     * Called when the offspring selection process finishes, records the duration.
     */
    override fun onOffspringSelectionFinished() {
        currentGenerationRecord.offspringSelection.duration =
            currentGenerationRecord.offspringSelection.startTime.elapsedNow().inWholeNanoseconds
    }

    /**
     * Called when the survivor selection process starts, marks the start time.
     */
    override fun onSurvivorSelectionStarted() {
        currentGenerationRecord.survivorSelection.startTime = timeSource.markNow()
    }

    /**
     * Called when the survivor selection process finishes, records the duration.
     */
    override fun onSurvivorSelectionFinished() {
        currentGenerationRecord.survivorSelection.duration =
            currentGenerationRecord.survivorSelection.startTime.elapsedNow().inWholeNanoseconds
    }

    /**
     * Called when the alteration of the population starts, marks the start time.
     */
    override fun onAlterationStarted() {
        currentGenerationRecord.alteration.startTime = timeSource.markNow()
    }

    /**
     * Called when the alteration of the population finishes, records the duration.
     */
    override fun onAlterationFinished() {
        currentGenerationRecord.alteration.duration =
            currentGenerationRecord.alteration.startTime.elapsedNow().inWholeNanoseconds
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
