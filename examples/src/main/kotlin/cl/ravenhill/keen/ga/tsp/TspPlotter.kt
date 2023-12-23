/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.tsp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cl.ravenhill.keen.listeners.EvolutionPlotter
import org.jetbrains.letsPlot.Figure
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.geom.geomPoint
import org.jetbrains.letsPlot.letsPlot
import org.jetbrains.letsPlot.sampling.samplingNone
import org.jetbrains.letsPlot.skia.compose.PlotPanel

class TspPlotter : EvolutionPlotter<Pair<Int, Int>, RoutePointGene>() {

    override fun display() = application {
        val (best, worst, average) = computeFitnessTriplet(evolution.generations)
        Window(onCloseRequest = ::exitApplication, title = "Traveling Salesman Problem") {

            val figures = listOf(
                "Fitness Plot" to createFitnessFigure(best, worst, average),
                "Best Route" to createRouteFigure()
            )

            val preserveAspectRatio = remember { mutableStateOf(false) }
            val figureIndex = remember { mutableStateOf(0) }

            MaterialTheme {
                Row {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .align(CenterVertically)
                            .width(IntrinsicSize.Max)
                    ) {
                        Text("Plots:", fontWeight = FontWeight.Bold)
                        PlotList(
                            options = figures.unzip().first,
                            selectedIndex = figureIndex,
                        )
                        Row {
                            Text(
                                text = "Keep ratio:",
                                modifier = Modifier
                                    .align(CenterVertically)
                            )
                            Checkbox(preserveAspectRatio.value, onCheckedChange = { preserveAspectRatio.value = it })
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxSize()
                            .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                                .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp),
                        ) {
                            PlotPanel(
                                figure = figures[figureIndex.value].second,
                                preserveAspectRatio = preserveAspectRatio.value,
                                modifier = Modifier.fillMaxSize()
                            ) { computationMessages ->
                                computationMessages.forEach { println("[DEMO APP MESSAGE] $it") }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun createRouteFigure(): Figure {
        val best = fittest.genotype.flatten()
        val data = mapOf(
            "x" to best.map { it.first.toDouble() },
            "y" to best.map { it.second.toDouble() },
        )
        return letsPlot(data) + geomPoint(size = 8, alpha = 0.3, sampling = samplingNone) {
            x = "x"
            y = "y"
        } + geomLine(sampling = samplingNone) {
            x = "x"
            y = "y"
        }
    }
}


@Suppress("FunctionName")
@Composable
fun PlotList(
    options: List<String>,
    selectedIndex: MutableState<Int>,
) {
    Box {
        Column(modifier = Modifier.width(IntrinsicSize.Max)) {
            options.forEachIndexed { index, name ->

                Row(
                    verticalAlignment = CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .selectable(
                            selected = selectedIndex.value == index,
                            onClick = { selectedIndex.value = index }
                        )
                ) {
                    RadioButton(
                        onClick = { selectedIndex.value = index },
                        selected = index == selectedIndex.value,
                    )
                    Text(name)
                }
            }
        }
    }
}

