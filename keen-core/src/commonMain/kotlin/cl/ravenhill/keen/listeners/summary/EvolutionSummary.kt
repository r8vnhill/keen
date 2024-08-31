/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.summary

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.consoleWidth
import cl.ravenhill.keen.listeners.fittest
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

class EvolutionSummary<T, F, R, S>(
    private val configuration: ListenerConfiguration<T, F, R> = ListenerConfiguration()
) : EvolutionListener<T, F, R, S>
        where F : Feature<T, F>,
              R : Representation<T, F>,
              S : EvolutionState<T, F, R, S> {

    private val evolution by lazy { configuration.evolution }

    private val timeUnit by lazy { configuration.precision.unit }

    private val withPrecision by lazy { configuration.precision.withPrecision }

    private val timeSource by lazy { configuration.timeSource }

    private val ranker by lazy { configuration.ranker }

    override fun onEvolutionStart() {
        evolution.startTime = timeSource.markNow()
    }

    override fun onEvolutionEnd(state: S) {
        evolution.duration = evolution.startTime.elapsedNow().withPrecision()
    }

    override suspend fun display() {
        val consoleWidth = consoleWidth()
        val tableWidth = minOf(consoleWidth, 80) // Maximum width, adjust as needed

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

    override fun copy() = EvolutionSummary<T, F, R, S>(configuration)
}

private fun <T, F, R, S> calculateTableWidth(
    content: List<String>,
    tableWidth: Int
): Int where F : Feature<T, F>,
             R : Representation<T, F>,
             S : EvolutionState<T, F, R, S> {
    val maxContentWidth = content.maxOf { it.length }
    return minOf(maxContentWidth + 4, tableWidth) // Adjusted width with padding
}

private fun <T, F, R, S> generateBorder(width: Int) where F : Feature<T, F>,
                                                          R : Representation<T, F>,
                                                          S : EvolutionState<T, F, R, S> = "-".repeat(width)

private fun <T, F, R, S> formatContent(
    content: List<String>,
    width: Int
) where F : Feature<T, F>,
        R : Representation<T, F>,
        S : EvolutionState<T, F, R, S> = content.joinToString("\n") { "| ${it.padEnd(width - 2)} |" }

private fun <T, F, R, S> assembleTable(
    border: String,
    content: String,
    width: Int
): String where F : Feature<T, F>,
                R : Representation<T, F>,
                S : EvolutionState<T, F, R, S> {
    val header = "Evolution Summary"
    return """
        +${border}+
        | ${header.padEnd(width - 2)} |
        +${border}+
        $content
        +${border}+
        """.trimIndent()
}
