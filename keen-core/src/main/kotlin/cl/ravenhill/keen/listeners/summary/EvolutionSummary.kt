/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners.summary

import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.AbstractEvolutionListener
import cl.ravenhill.keen.listeners.mixins.GenerationListener
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.listeners.records.GenerationRecord
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource


/**
 * Provides a summary of the evolutionary process, including timing and performance metrics.
 *
 * `EvolutionSummary` is an `EvolutionListener` that captures detailed timing information throughout the evolution
 * process. It logs durations for various stages, such as initialization, evaluation, selection, and alteration. It also
 * tracks overall evolution time, generation times, and the final results of the evolutionary process.
 *
 * ## Key Metrics:
 * - Initialization time
 * - Average, maximum, and minimum evaluation times
 * - Average, maximum, and minimum selection times for offspring and survivors
 * - Average, maximum, and minimum alteration times
 * - Total evolution time and individual generation times
 * - Generation number, steady generations, and fitness of the fittest individual
 *
 * ## Usage:
 * Attach this listener to an evolutionary algorithm to gather performance and timing metrics. It can help
 * in analyzing the efficiency and effectiveness of different components of the evolutionary process.
 *
 * ### Example:
 * ```kotlin
 * val summaryListener = EvolutionSummary<MyDataType, MyGene>()
 * val engine = evolutionEngine(/* ... */).apply {
 *     listeners += summaryListener
 * }
 * engine.run()
 * summaryListener.display()
 * ```
 * In this example, `EvolutionSummary` is used as a listener for an evolutionary algorithm. After the evolution
 * process is completed, `display()` is called to print a comprehensive summary of the evolution.
 *
 * @param T The type of data encapsulated by the genes.
 * @param G The type of gene, conforming to the [Gene] interface.
 * @property fittest The fittest individual in the final generation.
 */
@OptIn(ExperimentalTime::class)
class EvolutionSummary<T, G>(
    val precision: Duration.() -> Long = Duration::inWholeMilliseconds,
    evolution: EvolutionRecord<T, G> = EvolutionRecord(),
    timeSource: TimeSource = TimeSource.Monotonic,
    generationSummary: GenerationSummary<T, G> = GenerationSummary(evolution, timeSource, ptecision)
) : AbstractEvolutionListener<T, G>(),
        GenerationListener<T, G> by generationSummary
        where G : Gene<T, G> {

    /**
     * Displays a detailed summary of the evolutionary process on the console.
     *
     * This method outputs a comprehensive report of the evolution, covering various aspects such as initialization
     * time, evaluation, selection, alteration times, overall evolution duration, generation details, and the final
     * results. It's designed to provide a quick and informative overview of the evolutionary process and its
     * efficiency.
     *
     * ## Output Format:
     * The summary is structured in sections, each focusing on different aspects of the evolutionary process:
     * - Initialization Time: Duration taken for the initial setup of the evolution.
     * - Evaluation Times: Statistical data (average, maximum, minimum) for the time taken in the evaluation phase
     *   across generations.
     * - Selection Times: Detailed timing for offspring and survivor selection processes.
     * - Alteration Times: Statistical data for the alteration phase, including mutation and crossover operations.
     * - Evolution Results: Overall metrics such as total time, generation times, and final evolutionary outcomes like
     *   the fittest individual and its fitness.
     *
     * ## Example Output:
     * ```
     * ------------ Evolution Summary ---------------
     * |--> Initialization time: [Initialization time] ms
     * ------------- Evaluation Times ----------------
     * |--> Average: [Average evaluation time] ms
     * |--> Max: [Maximum evaluation time] ms
     * |--> Min: [Minimum evaluation time] ms
     * ...
     * |--> Best fitness: [Best fitness value]
     * ```
     *
     * ## Usage:
     * This method is typically called after the completion of the evolutionary process to analyze and understand the
     * performance and outcomes.
     *
     * ```kotlin
     * val summaryListener = EvolutionSummary<MyDataType, MyGene>()
     * //... evolution process
     * summaryListener.display() // Call this method to print the summary
     * ```
     */
    override fun display() = println(
        """
    ------------ Evolution Summary ---------------
    |--> Initialization time: ${evolution.initialization.duration} ms
    ------------- Evaluation Times ----------------
    |--> Average: ${generations.map { it.evaluation.duration }.average()} ms
    |--> Max: ${generations.maxOfOrNull { it.evaluation.duration }} ms
    |--> Min: ${generations.minOfOrNull { it.evaluation.duration }} ms
    -------------- Selection Times ----------------
    |   |--> Offspring Selection
    |   |   |--> Average: ${generations.map { it.parentSelection.duration }.average()} ms
    |   |   |--> Max: ${generations.maxOfOrNull { it.parentSelection.duration }} ms
    |   |   |--> Min: ${generations.minOfOrNull { it.parentSelection.duration }} ms
    |   |--> Survivor Selection
    |   |   |--> Average: ${generations.map { it.survivorSelection.duration }.average()} ms
    |   |   |--> Max: ${generations.maxOfOrNull { it.survivorSelection.duration }} ms
    |   |   |--> Min: ${generations.minOfOrNull { it.survivorSelection.duration }} ms
    --------------- Alteration Times --------------
    |--> Average: ${generations.map { it.alteration.duration }.average()} ms
    |--> Max: ${generations.maxOfOrNull { it.alteration.duration }} ms
    |--> Min: ${generations.minOfOrNull { it.alteration.duration }} ms
    -------------- Evolution Results --------------
    |--> Total time: ${evolution.duration} ms
    |--> Average generation time: ${generations.map { it.duration }.average()} ms
    |--> Max generation time: ${generations.maxOfOrNull { it.duration }} ms
    |--> Min generation time: ${generations.minOfOrNull { it.duration }} ms
    |--> Generation: ${evolution.generations.last().generation}
    |--> Steady generations: ${evolution.generations.last().steady}
    |--> Fittest: ${fittest.genotype.toSimpleString()}
    |--> Best fitness: ${fittest.fitness}
    """.trimIndent()
    )

    /**
     * Callback function triggered at the start of the initialization phase in the evolutionary process.
     *
     * This method is invoked when the initialization phase of an evolutionary algorithm begins. Initialization is a
     * crucial stage where the initial population and setup for the evolutionary process are established. This method
     * marks the start time of the initialization phase, allowing for tracking the duration of this initial setup.
     *
     * ## Functionality:
     * - **Initialization Start Time Recording**: Captures the start time of the initialization phase using the provided
     *   time source. This timestamp is essential for measuring the duration of the initialization phase, which includes
     *   creating the initial population and setting up the environment for the evolution process.
     *
     * ## Usage:
     * This method is automatically called by the evolutionary algorithm framework at the beginning of the
     * initialization phase. It is not intended for direct invocation in typical use cases.
     *
     * ```kotlin
     * // Within the evolutionary algorithm framework
     * evolutionListeners.forEach { it.onInitializationStarted(currentState) }
     * ```
     *
     * In this snippet, `onInitializationStarted` is called for each listener in the evolutionary algorithm, signifying
     * the start of the initialization phase.
     *
     * @param state The current [EvolutionState] at the start of initialization. It provides context for the
     *   initialization phase, including the initial settings and parameters for the evolutionary process.
     */
    override fun onInitializationStarted(state: EvolutionState<T, G>) {
        evolution.initialization.startTime = timeSource.markNow()
    }

    /**
     * Callback function triggered at the end of the initialization phase in the evolutionary process.
     *
     * This method is called when the initialization phase of an evolutionary algorithm concludes. The initialization
     * phase typically involves setting up the initial population and other necessary configurations for the
     * evolutionary process. This method calculates and records the duration of this phase, providing insights into the
     * time taken for initialization.
     *
     * ## Functionality:
     * - **Initialization Duration Calculation**: Determines the duration of the initialization phase by calculating the
     *   elapsed time since its start. This duration is crucial for performance analysis and optimization of the
     *   evolutionary process.
     *
     * ## Usage:
     * This method is automatically invoked by the evolutionary algorithm framework at the conclusion of the
     * initialization phase. It is typically not called directly in user code.
     *
     * ```kotlin
     * // Within the evolutionary algorithm framework
     * evolutionListeners.forEach { it.onInitializationEnded(currentState) }
     * ```
     *
     * In this example, `onInitializationEnded` is invoked for each listener, signaling the end of the initialization
     * phase and enabling the calculation of its duration.
     *
     * @param state The current [EvolutionState] at the end of the initialization. It encapsulates the state of the
     *   evolutionary process immediately after initialization, ready for the next phase of evolution.
     */
    override fun onInitializationEnded(state: EvolutionState<T, G>) {
        evolution.initialization.duration = evolution.initialization.startTime.elapsedNow().precision()
    }

    /**
     * Callback function triggered at the start of the evaluation phase in the evolutionary process.
     *
     * This method is invoked when the evaluation phase of an evolutionary algorithm begins. The evaluation phase is
     * crucial as it involves assessing the fitness of each individual in the current population. This method records
     * the start time of the evaluation phase, which is essential for tracking the performance and efficiency of the
     * evolutionary process.
     *
     * ## Functionality:
     * - **Evaluation Start Time Recording**: Captures the moment when the evaluation phase starts, allowing for precise
     *   measurement of the duration of this phase.
     *
     * ## Usage:
     * This method is automatically called by the evolutionary algorithm framework at the beginning of the evaluation
     * phase. It is not typically invoked directly in user code.
     *
     * ```kotlin
     * // Within the evolutionary algorithm framework
     * evolutionListeners.forEach { it.onEvaluationStarted(currentState) }
     * ```
     *
     * In this example, `onEvaluationStarted` is invoked for each listener at the start of the evaluation phase,
     * marking the time when the fitness assessment of the population begins.
     *
     * @param state The current [EvolutionState] at the start of the evaluation. This state includes the population that
     *   will undergo fitness evaluation.
     */
    override fun onEvaluationStarted(state: EvolutionState<T, G>) {
        currentGeneration.evaluation.startTime = timeSource.markNow()
    }

    /**
     * Callback function triggered at the end of the evaluation phase in the evolutionary process.
     *
     * This method is called when the evaluation phase of an evolutionary algorithm concludes. During the evaluation
     * phase, each individual in the population is assessed for fitness. This method calculates and records the
     * duration of the evaluation phase, which is vital for analyzing the performance and time efficiency of the
     * evolutionary process.
     *
     * ## Functionality:
     * - **Evaluation Duration Calculation**: Computes the total time taken for the evaluation phase by measuring the
     *   interval between the start and end times.
     *
     * ## Usage:
     * This method is automatically invoked by the evolutionary algorithm framework at the end of the evaluation phase.
     * It is not intended for direct invocation in typical use cases.
     *
     * ```kotlin
     * // Within the evolutionary algorithm framework
     * evolutionListeners.forEach { it.onEvaluationEnded(currentState) }
     * ```
     * In this example, `onEvaluationEnded` is called for each listener at the conclusion of the evaluation phase. It
     * calculates the duration of the evaluation phase, which is crucial for performance analysis.
     *
     * @param state The current [EvolutionState] at the end of the evaluation phase. This state includes the population
     *              that has just been evaluated for fitness.
     */
    override fun onEvaluationEnded(state: EvolutionState<T, G>) {
        currentGeneration.evaluation.duration = currentGeneration.evaluation.startTime.elapsedNow().precision()
    }

    /**
     * Callback function triggered at the beginning of the parent selection phase in the evolutionary process.
     *
     * This method is called when the parent selection phase of an evolutionary algorithm begins. During this phase,
     * a subset of individuals from the current population is selected to act as parents for generating offspring.
     * The method marks the start time of the parent selection phase, which is essential for tracking the duration
     * and performance of this phase.
     *
     * ## Functionality:
     * - **Start Time Recording**: Records the start time of the parent selection phase, enabling the calculation of
     *   the total duration of this phase once it concludes.
     *
     * ## Usage:
     * This method is automatically invoked by the evolutionary algorithm framework at the start of the parent selection
     * phase. It is not intended for direct invocation in general use cases.
     *
     * ```kotlin
     * // Within the evolutionary algorithm framework
     * evolutionListeners.forEach { it.onParentSelectionStarted(currentState) }
     * ```
     * In this example, `onParentSelectionStarted` is invoked for each listener at the commencement of the parent
     * selection phase. It logs the start time of the phase, which is later used to calculate the total duration of
     * parent selection.
     *
     * @param state The current [EvolutionState] at the beginning of the parent selection phase. This state includes the
     *              population from which parents will be selected.
     */
    override fun onParentSelectionStarted(state: EvolutionState<T, G>) {
        currentGeneration.parentSelection.startTime = timeSource.markNow()
    }

    /**
     * Callback function triggered at the conclusion of the parent selection phase in the evolutionary process.
     *
     * This method is invoked at the end of the parent selection phase of an evolutionary algorithm. During this phase,
     * parents are selected from the current population for the purpose of producing offspring. The method captures the
     * end time of the parent selection phase and calculates its total duration, contributing to a comprehensive
     * performance analysis of the evolutionary process.
     *
     * ## Functionality:
     * - **Duration Calculation**: Computes the total time taken for the parent selection phase by measuring the
     *   interval between the start and end times recorded.
     *
     * ## Usage:
     * This method is automatically called by the evolutionary algorithm framework upon the completion of the parent
     * selection phase. It is generally not intended for manual invocation.
     *
     * ```kotlin
     * // Within the evolutionary algorithm framework
     * evolutionListeners.forEach { it.onParentSelectionEnded(currentState) }
     * ```
     * In this example, `onParentSelectionEnded` is called for each listener at the conclusion of the parent selection
     * phase. It calculates and logs the duration of this phase, providing valuable data for analyzing the efficiency
     * and performance of the selection process.
     *
     * @param state The current [EvolutionState] at the end of the parent selection phase. This state includes the
     *              population from which parents have been selected for offspring production.
     */
    override fun onParentSelectionEnded(state: EvolutionState<T, G>) {
        currentGeneration.parentSelection.duration =
            currentGeneration.parentSelection.startTime.elapsedNow().precision()
    }

    /**
     * Callback function triggered at the beginning of the survivor selection phase in the evolutionary process.
     *
     * This method is invoked at the start of the survivor selection phase of an evolutionary algorithm. During this
     * phase, individuals from the current population or the newly created offspring are selected to form the next
     * generation's population. The method records the start time of the survivor selection phase, which is crucial for
     * monitoring the duration and efficiency of this process.
     *
     * ## Functionality:
     * - **Start Time Recording**: Marks the beginning of the survivor selection phase, facilitating subsequent
     *   calculation of its duration.
     *
     * ## Usage:
     * This method is automatically called by the evolutionary algorithm framework as the survivor selection phase
     * begins. It is not typically used directly in user-defined code.
     *
     * ```kotlin
     * // Within the evolutionary algorithm framework
     * evolutionListeners.forEach { it.onSurvivorSelectionStarted(currentState) }
     * ```
     * In this example, `onSurvivorSelectionStarted` is called for each listener at the start of the survivor selection
     * phase. It records the start time, which will later be used to calculate the total duration of this phase, aiding
     * in performance analysis and optimization.
     *
     * @param state The current [EvolutionState] at the beginning of the survivor selection phase. This state includes
     *              the population from which survivors will be chosen for the next generation.
     */
    override fun onSurvivorSelectionStarted(state: EvolutionState<T, G>) {
        currentGeneration.survivorSelection.startTime = timeSource.markNow()
    }

    /**
     * Callback function triggered at the end of the survivor selection phase in the evolutionary process.
     *
     * This method is called upon the conclusion of the survivor selection phase in an evolutionary algorithm. Survivor
     * selection is a crucial stage where individuals are chosen based on their fitness to continue into the next
     * generation. This method calculates and records the duration of the survivor selection phase, providing valuable
     * insights into the time efficiency of this stage in the evolutionary process.
     *
     * ## Functionality:
     * - **Duration Calculation**: Computes the total time taken for the survivor selection phase by measuring the
     *   elapsed time from its start to the end.
     *
     * ## Usage:
     * This method is automatically invoked by the evolutionary algorithm framework at the completion of the survivor
     * selection phase. It is typically not called directly by user code.
     *
     * ```kotlin
     * // Within the evolutionary algorithm framework
     * evolutionListeners.forEach { it.onSurvivorSelectionEnded(currentState) }
     * ```
     * In this example, `onSurvivorSelectionEnded` is invoked for each listener at the end of the survivor selection
     * phase. The method's primary role is to calculate the duration of this phase, contributing to the overall
     * performance analysis of the algorithm.
     *
     * @param state The current [EvolutionState] at the end of the survivor selection phase. This state reflects the
     *              population that has been selected to survive into the next generation.
     */
    override fun onSurvivorSelectionEnded(state: EvolutionState<T, G>) {
        currentGeneration.survivorSelection.duration =
            currentGeneration.survivorSelection.startTime.elapsedNow().precision()
    }

    /**
     * Callback function triggered at the start of the alteration phase in the evolutionary process.
     *
     * This method marks the beginning of the alteration phase in an evolutionary algorithm. The alteration phase
     * typically involves genetic operations such as mutation and crossover. This method records the start time of this
     * phase, which is crucial for analyzing the time taken by these genetic operations within the evolutionary cycle.
     *
     * ## Functionality:
     * - **Start Time Recording**: Captures the start time of the alteration phase, marking the beginning of genetic
     *   operations on the population.
     *
     * ## Usage:
     * This method is automatically invoked by the evolutionary algorithm framework at the start of the alteration
     * phase. It is not usually called directly by user code.
     *
     * ```kotlin
     * // Within the evolutionary algorithm framework
     * evolutionListeners.forEach { it.onAlterationStarted(currentState) }
     * ```
     * In this example, `onAlterationStarted` is called for each listener at the beginning of the alteration phase,
     * setting the start time for this crucial stage in the evolutionary process.
     *
     * @param state The current [EvolutionState] at the start of the alteration phase. This state includes the
     *   population that is about to undergo genetic operations like mutation or crossover.
     */
    override fun onAlterationStarted(state: EvolutionState<T, G>) {
        currentGeneration.alteration.startTime = timeSource.markNow()
    }

    /**
     * Callback function triggered at the end of the alteration phase in the evolutionary process.
     *
     * This method is invoked when the alteration phase of an evolutionary algorithm concludes. The alteration phase,
     * which includes genetic operations such as mutation and crossover, is crucial for introducing genetic diversity
     * and new traits into the population. This method calculates and records the duration of the alteration phase,
     * providing valuable insights into the time efficiency of these genetic operations.
     *
     * ## Functionality:
     * - **Duration Calculation**: Computes the total time taken for the alteration phase by measuring the interval
     *   between the start and end times. This duration is crucial for performance analysis and optimization of the
     *   genetic operations.
     *
     * ## Usage:
     * This method is automatically triggered by the evolutionary algorithm framework at the conclusion of the
     * alteration phase. It is not typically called directly by user code.
     *
     * ```kotlin
     * // Within the evolutionary algorithm framework
     * evolutionListeners.forEach { it.onAlterationEnded(currentState) }
     * ```
     * In this example, `onAlterationEnded` is called for each listener after the alteration phase is completed. It
     * calculates the duration of this phase, aiding in the assessment and improvement of the algorithm's performance.
     *
     * @param state The current [EvolutionState] at the end of the alteration phase. This state includes the population
     *   that has undergone genetic operations.
     */
    override fun onAlterationEnded(state: EvolutionState<T, G>) {
        currentGeneration.alteration.duration = currentGeneration.alteration.startTime.elapsedNow().precision()
    }

    /**
     * Callback function triggered at the start of the evolution process.
     *
     * This method is invoked at the beginning of the evolutionary algorithm's execution. It marks the commencement of
     * the entire evolutionary process, setting the starting point for time measurement of the evolution. This is
     * crucial for tracking the duration and performance of the evolutionary algorithm as a whole.
     *
     * ## Functionality:
     * - **Start Time Recording**: Establishes the starting time for the evolution process. This initial timestamp is
     *   used to calculate the total duration of the evolution once it concludes.
     *
     * ## Usage:
     * This method is automatically triggered by the evolutionary algorithm framework at the start of the evolution
     * process. It is not typically invoked directly in user code.
     *
     * ```kotlin
     * // Within the evolutionary algorithm framework
     * evolutionListeners.forEach { it.onEvolutionStarted(currentState) }
     * ```
     * In this example, `onEvolutionStarted` is called for each listener at the beginning of the evolution process. It
     * records the start time, which is essential for later calculating the total duration of the evolutionary cycle.
     *
     * @param state The [EvolutionState] at the start of the evolution process. This state includes the initial
     *   population and other relevant setup details for the evolutionary algorithm.
     */
    override fun onEvolutionStarted(state: EvolutionState<T, G>) {
        evolution.startTime = timeSource.markNow()
    }

    /**
     * Callback function triggered at the end of the evolution process.
     *
     * This method is called upon the completion of an evolutionary algorithm's execution. It marks the end of the
     * evolution process and is responsible for calculating the total duration of the evolutionary cycle. This
     * information is vital for analyzing the performance, efficiency, and overall time consumption of the evolutionary
     * algorithm.
     *
     * ## Functionality:
     * - **Total Duration Calculation**: Computes the total time taken for the entire evolution process by measuring the
     *   interval between the start and end times.
     *
     * ## Usage:
     * This method is automatically invoked by the evolutionary algorithm framework at the conclusion of the evolution
     * process. It is not intended for direct invocation in typical use cases.
     *
     * ```kotlin
     * // Within the evolutionary algorithm framework
     * evolutionListeners.forEach { it.onEvolutionEnded(currentState) }
     * ```
     * In this example, `onEvolutionEnded` is called for each listener at the end of the evolution process. It
     * calculates the total duration of the evolution, which is essential for performance analysis and time efficiency
     * evaluation.
     *
     * @param state The final [EvolutionState] at the conclusion of the evolution process. This state includes the
     *   final population and the results of the evolutionary algorithm.
     */
    override fun onEvolutionEnded(state: EvolutionState<T, G>) {
        evolution.duration = evolution.startTime.elapsedNow().precision()
    }
}
