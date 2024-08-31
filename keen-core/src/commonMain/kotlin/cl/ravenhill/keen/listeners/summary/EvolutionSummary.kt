/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.summary

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.consoleWidth
import cl.ravenhill.keen.listeners.fittest
import cl.ravenhill.keen.listeners.mixins.AlterationListener
import cl.ravenhill.keen.listeners.mixins.EvaluationListener
import cl.ravenhill.keen.listeners.mixins.GenerationListener
import cl.ravenhill.keen.listeners.mixins.InitializationListener
import cl.ravenhill.keen.listeners.mixins.ParentSelectionListener
import cl.ravenhill.keen.listeners.mixins.SurvivorSelectorListener
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * A comprehensive listener that summarizes the evolution process, including initialization, evaluation, selection, and
 * alteration phases.
 *
 * The `EvolutionSummary` class aggregates and summarizes key metrics and times for each phase of the evolutionary
 * algorithm. It implements various listeners for different stages of the algorithm and consolidates their data into a
 * single summary. The summary is displayed as a formatted table, showing statistics like time taken for initialization,
 * evaluation, selection, and alterations, as well as the best fitness and steady generations.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 */
class EvolutionSummary<T, F, R, S> private constructor(
    private val configuration: ListenerConfiguration<T, F, R> = ListenerConfiguration()
) : EvolutionListener<T, F, R, S>,
    GenerationListener<T, F, R, S> by GenerationSummary(configuration),
    InitializationListener<T, F, R, S> by InitializationSummary(configuration),
    EvaluationListener<T, F, R, S> by EvaluationSummary(configuration),
    ParentSelectionListener<T, F, R, S> by ParentSelectionSummary(configuration),
    SurvivorSelectorListener<T, F, R, S> by SurvivorSelectionSummary(configuration),
    AlterationListener<T, F, R, S> by AlterationSummary(configuration)
        where F : Feature<T, F>,
              R : Representation<T, F>,
              S : EvolutionState<T, F, R, S> {

    private val evolution by lazy { configuration.evolution }

    private val timeUnit by lazy { configuration.precision.unit }

    private val withPrecision by lazy { configuration.precision.withPrecision }

    private val timeSource by lazy { configuration.timeSource }

    private val ranker by lazy { configuration.ranker }

    /**
     * Triggered at the start of the evolutionary process.
     */
    override fun onEvolutionStart() {
        evolution.startTime = timeSource.markNow()
    }

    /**
     * Triggered at the end of the evolutionary process, recording the total duration.
     *
     * @param state The final evolutionary state.
     */
    override fun onEvolutionEnd(state: S) {
        evolution.duration = evolution.startTime.elapsedNow().withPrecision()
    }

    /**
     * Displays a summary of the evolution process, including times for each stage and results.
     */
    override suspend fun display() {
        val consoleWidth = consoleWidth()
        val tableWidth = minOf(consoleWidth, Domain.defaultConsoleWidth) // Maximum width, adjust as needed

        val content = generateContent()
        val adjustedWidth = calculateTableWidth<T, F, R, S>(content, tableWidth)
        val border = generateBorder<T, F, R, S>(adjustedWidth)
        val borderedContent = formatContent<T, F, R, S>(content, adjustedWidth)

        val table = assembleTable<T, F, R, S>(border, borderedContent, adjustedWidth)
        println(table)
    }

    private suspend fun generateContent(): List<String> {
        val generations = evolution.generations
        val lastGeneration = generations.lastOrNull()

        return buildList {
            add(generateInitializationTime())
            addAll(generateEvaluationTimes(generations))
            addAll(generateSelectionTimes(generations))
            addAll(generateAlterationTimes(generations))
            addAll(generateEvolutionResults(generations, lastGeneration))
        }
    }

    private fun generateInitializationTime(): String {
        return "Initialization time: ${evolution.initialization.duration} $timeUnit"
    }

    private fun generateEvaluationTimes(generations: List<GenerationRecord<T, F, R>>) = listOf(
        "Evaluation Times",
        "   Average: ${generations.map { it.evaluation.duration }.average()} $timeUnit",
        "   Max: ${generations.maxOfOrNull { it.evaluation.duration }} $timeUnit",
        "   Min: ${generations.minOfOrNull { it.evaluation.duration }} $timeUnit"
    )

    private fun generateSelectionTimes(generations: List<GenerationRecord<T, F, R>>) = listOf(
        "Selection Times",
        "   Offspring Selection",
        "     Average: ${generations.map { it.parentSelection.duration }.average()} $timeUnit",
        "     Max: ${generations.maxOfOrNull { it.parentSelection.duration }} $timeUnit",
        "     Min: ${generations.minOfOrNull { it.parentSelection.duration }} $timeUnit",
        "   Survivor Selection",
        "     Average: ${generations.map { it.survivorSelection.duration }.average()} $timeUnit",
        "     Max: ${generations.maxOfOrNull { it.survivorSelection.duration }} $timeUnit",
        "     Min: ${generations.minOfOrNull { it.survivorSelection.duration }} $timeUnit"
    )

    private fun generateAlterationTimes(generations: List<GenerationRecord<T, F, R>>) = listOf(
        "Alteration Times",
        "   Average: ${generations.map { it.alteration.duration }.average()} $timeUnit",
        "   Max: ${generations.maxOfOrNull { it.alteration.duration }} $timeUnit",
        "   Min: ${generations.minOfOrNull { it.alteration.duration }} $timeUnit"
    )

    private suspend fun generateEvolutionResults(
        generations: List<GenerationRecord<T, F, R>>,
        lastGeneration: GenerationRecord<T, F, R>?
    ) = listOf(
        "Evolution Results",
        "   Total time: ${evolution.duration} $timeUnit",
        "   Average generation time: ${generations.map { it.duration }.average()} $timeUnit",
        "   Max generation time: ${generations.maxOfOrNull { it.duration }} $timeUnit",
        "   Min generation time: ${generations.minOfOrNull { it.duration }} $timeUnit",
        "   Generation: ${lastGeneration?.generation}",
        "   Steady generations: ${lastGeneration?.steady}",
        "   Fittest: ${fittest(ranker, evolution).representation}",
        "   Best fitness: ${fittest(ranker, evolution).fitness}"
    )

    /**
     * Creates a copy of the `EvolutionSummary` with the same configuration.
     */
    override fun copy() = EvolutionSummary<T, F, R, S>(configuration)

    /**
     * Companion object to provide a factory function for creating an `EvolutionSummary` listener.
     *
     * @see EvolutionSummary.Companion.invoke
     */
    companion object {

        /**
         * Factory function to create an `EvolutionSummary` listener.
         *
         * The `invoke` function provides a convenient way to instantiate an `EvolutionSummary` listener using a
         * functional approach. It returns a lambda function that takes a `ListenerConfiguration` and produces a new
         * `EvolutionSummary` instance.
         *
         * @param T The type of the value held by the features.
         * @param F The type of the feature, which must extend [Feature].
         * @param R The type of the representation, which must extend [Representation].
         * @param S The type of the evolutionary state, which must extend [EvolutionState].
         * @return A lambda function that takes a `ListenerConfiguration` and returns an `EvolutionSummary` instance.
         */
        operator fun <T, F, R> invoke(): (
            ListenerConfiguration<T, F, R>
        ) -> EvolutionSummary<T, F, R, out EvolutionState<T, F, R, *>>
                where F : Feature<T, F>,
                      R : Representation<T, F> =
            { configuration -> EvolutionSummary(configuration) }
    }
}

/**
 * Calculates the appropriate width for a table based on its content and the specified maximum width.
 *
 * The `calculateTableWidth` function determines the optimal width for a table by evaluating the longest string in the
 * content list and adding padding. The calculated width is then compared to a provided maximum table width to ensure
 * the table does not exceed the desired size.
 *
 * @param content A list of strings representing the content of the table.
 * @param tableWidth The maximum allowed width for the table.
 * @return The calculated width for the table, considering content length and padding, without exceeding the maximum
 *         specified width.
 */
private fun <T, F, R, S> calculateTableWidth(
    content: List<String>,
    tableWidth: Int
): Int where F : Feature<T, F>,
             R : Representation<T, F>,
             S : EvolutionState<T, F, R, S> {
    val maxContentWidth = content.maxOf { it.length }
    return minOf(maxContentWidth + 4, tableWidth) // Adjusted width with padding
}

/**
 * Generates a horizontal border for a table with the specified width.
 *
 * The `generateBorder` function creates a horizontal border for a table by repeating the `-` character for the given
 * width. This border is typically used at the top or bottom of a table to visually separate the content from the
 * surrounding text.
 *
 * @param width The total width of the border to be generated.
 * @return A string of `-` characters repeated to match the specified width.
 */
private fun <T, F, R, S> generateBorder(width: Int) where F : Feature<T, F>,
                                                          R : Representation<T, F>,
                                                          S : EvolutionState<T, F, R, S> = "-".repeat(width)

/**
 * Formats a list of strings as table rows with consistent width.
 *
 * The `formatContent` function takes a list of strings and formats each string as a row in a table. Each row is padded
 * to ensure that it matches the specified width, with padding added to the right of the content. The formatted rows are
 * then joined into a single string with each row separated by a newline character.
 *
 * @param content A list of strings representing the content to be formatted as table rows.
 * @param width The total width of each row, including padding and borders.
 * @return A single string with all the formatted rows, ready for display in a table.
 */
private fun <T, F, R, S> formatContent(
    content: List<String>,
    width: Int
): String where F : Feature<T, F>,
                R : Representation<T, F>,
                S : EvolutionState<T, F, R, S> {
    val adjustedWidth = minOf(width - 4, 80) // Account for borders and padding

    // Format the content with borders
    return content.joinToString("\n") { line ->
        val truncatedLine = if (line.length > adjustedWidth) {
            "${line.take(adjustedWidth - 3)}..." // Truncate and add ellipses
        } else {
            line.padEnd(adjustedWidth)
        }
        "|| $truncatedLine   |"
    }
}

/**
 * Assembles a formatted table to display an evolution summary.
 *
 * The `assembleTable` function creates a formatted table as a string, designed to display an evolution summary in a
 * visually organized manner. The table consists of a header, content, and borders, all formatted according to the
 * specified width. The header of the table is centered and padded to align with the width, ensuring a clean and
 * consistent presentation.
 *
 * @param border A string used to create the border of the table. This defines the horizontal line pattern.
 * @param content The main content to be displayed inside the table. It is typically aligned under the header.
 * @param width The overall width of the table, including the border and padding around the header and content.
 * @return A formatted string representing the table, including the header, content, and borders.
 */
private fun <T, F, R, S> assembleTable(
    border: String,
    content: String,
    width: Int
): String where F : Feature<T, F>,
                R : Representation<T, F>,
                S : EvolutionState<T, F, R, S> {
    val header = "Evolution Summary"
    return """
        |+${border}+
        || ${header.padEnd(width - 2)} |
        |+${border}+
        $content
        |+${border}+
        """.trimMargin()
}
