/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord
import org.jetbrains.letsPlot.Figure
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.letsPlot
import org.jetbrains.letsPlot.skia.compose.PlotPanel

class EvolutionPlotter<T, G> : AbstractEvolutionListener<T, G>() where G : Gene<T, G> {
    override fun display() = application {
        val generations = evolution.generations
        val sorted = generations.map { ranker.sort(it.population.offspring.map { record -> record.toIndividual() }) }
        val bestFitness = sorted.map { it.first().fitness }
        val worstFitness = sorted.map { it.last().fitness }
        val averageFitness = sorted.map { population -> population.map { it.fitness }.average() }

        Window(onCloseRequest = ::exitApplication, title = "Lets-Plot in Compose Desktop (min)") {
            MaterialTheme {
                Column(
                    modifier = Modifier.fillMaxSize().padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp),
                ) {

                    PlotPanel(
                        figure = createFigure(bestFitness, worstFitness, averageFitness),
                        modifier = Modifier.fillMaxSize()
                    ) { computationMessages ->
                        computationMessages.forEach { println("[DEMO APP MESSAGE] $it") }
                    }
                }
            }
        }
    }

    private fun createFigure(best: List<Double>, worst: List<Double>, average: List<Double>): Figure {
        val categoryLabel = "Fitness"
        val data = mapOf<String, Any>(
            categoryLabel to List(best.size) { "Best" } + List(worst.size) { "Worst" }
                  + List(average.size) { "Average" },
            "fitness" to best + worst + average,
            "generations" to best.indices + worst.indices + average.indices
        )

        return letsPlot(data) {
            x = "generations"
            color = "Fitness"
        } + geomLine { y = "fitness" }
    }

    override fun onGenerationStarted(state: EvolutionState<T, G>) {
        currentGeneration = GenerationRecord(state.generation)
        evolution.generations += currentGeneration
    }

    override fun onGenerationEnded(state: EvolutionState<T, G>) {
        currentGeneration.population.offspring = List(state.population.size) {
            IndividualRecord(
                state.population[it].genotype,
                state.population[it].fitness
            )
        }
    }
}
