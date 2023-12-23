/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


@file:Suppress("FunctionName")

package cl.ravenhill.keen.ga.tsp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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

/**
 * A class that extends [EvolutionPlotter] to visualize the progress and results of an evolutionary algorithm solving
 * the Traveling Salesman Problem (TSP).
 *
 * `TspPlotter` is designed to provide a graphical interface for displaying the fitness evolution and the best route
 * found by the evolutionary algorithm. It uses the Lets-Plot library for plotting and provides an interactive
 * interface for users to view and analyze the algorithm's performance.
 *
 * ## Key Features:
 * - Visualizes the fitness trend over generations, showing best, worst, and average fitness values.
 * - Displays the best route found by the algorithm in a coordinate-based plot.
 * - Provides an interactive UI to switch between different plots and control the aspect ratio of the displayed plots.
 *
 * ## Functionality:
 * - The `display` method sets up the application window and UI layout, incorporating the plots and controls.
 * - Utilizes composable functions like `Sidebar` and `PlotArea` for structuring the UI.
 * - Includes methods like `createRouteFigure` to generate specific plots based on the algorithm's output.
 *
 * ## Usage:
 * Instantiate `TspPlotter` as part of an evolutionary algorithm implementation for the TSP. The plotter can be used to
 * visually monitor the algorithm's progress and analyze the results.
 *
 * @see TravelingSalesmanProblem
 */
class TspPlotter : EvolutionPlotter<Pair<Int, Int>, RoutePointGene>() {

    /**
     * Displays the main application window for visualizing the Traveling Salesman Problem (TSP) solution.
     *
     * This method sets up the graphical user interface for the TSP plotter. It initializes and displays a window
     * containing various plots related to the TSP solution, such as the fitness trend over generations and the best
     * route found by the evolutionary algorithm. The method leverages the Lets-Plot library for plotting and Compose
     * for the UI layout.
     *
     * ## Functionality:
     * - **Window Initialization**: Sets up and displays the main window of the application with the title "Traveling
     *   Salesman Problem".
     * - **Plot Generation**: Generates the plots for fitness trend and best route using [createFitnessFigure] and
     *   `createRouteFigure` methods.
     * - **UI Layout**: Arranges the sidebar (for plot selection and aspect ratio control) and the main plot area in a
     *   row layout.
     * - **Interactive Elements**: Includes interactive UI components, such as a plot selection list and an aspect
     *   ratio toggle, to enhance user engagement and control.
     *
     * ## Usage:
     * This method is called to initiate the display of the TSP solution visualization. It is typically invoked after
     * setting up the evolutionary algorithm and attaching the `TspPlotter` as an evolution listener.
     */
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
                Row(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                    Sidebar(figures, figureIndex, preserveAspectRatio)
                    PlotArea(figures, figureIndex, preserveAspectRatio)
                }
            }
        }
    }

    /**
     * Creates a graphical representation of the best route found by the evolutionary algorithm.
     *
     * This method visualizes the optimal route discovered in the context of the Traveling Salesman Problem (TSP). It
     * uses the Lets-Plot library to create a plot that depicts the sequence of points in the best route, both as a
     * series of connected lines and as individual points.
     *
     * ## Functionality:
     * - **Data Preparation**: Extracts the coordinates of the route points from the best genotype and prepares them
     *   for plotting. The `x` and `y` coordinates are mapped to corresponding values in the plot.
     * - **Plot Configuration**:
     *   - [geomPoint]: Adds points to the plot for each location in the route, making the individual stops visible.
     *   - [geomLine]: Connects the points with lines to illustrate the path of the route.
     * - **Styling**:
     *   - The points are styled with a specific size and transparency (alpha) value for visual clarity.
     *   - The lines are drawn to connect the points in the order they are visited in the route.
     *
     * ## Usage:
     * This method is used to generate a visual output for the best route found in a TSP solution. The resulting
     * `Figure` object can be displayed in a UI or exported as an image file.
     *
     * ### Example Usage:
     * ```kotlin
     * val tspPlotter = TspPlotter()
     * // Assuming tspPlotter.fittest is already defined
     * val routeFigure = tspPlotter.createRouteFigure()
     * ```
     * In this example, `createRouteFigure` is used within a `TspPlotter` instance to generate a plot of the best
     * route.
     *
     * @return A `Figure` object representing the plotted route.
     */
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

/**
 * A composable function that creates a list of selectable plot options with radio buttons.
 *
 * `PlotList` generates a vertically aligned list of options where each option is represented by a radio button and
 * a label. It is designed for scenarios where a user needs to select one option from a list. The current selection
 * is tracked and can be updated by the user's interaction.
 *
 * ## Parameters:
 * - `options`: A list of strings representing the plot options to be displayed.
 * - `selectedIndex`: A [MutableState] holding the index of the currently selected option.
 *
 * ## Functionality:
 * - The function iterates over the provided list of options, creating a row for each item.
 * - Each row contains a radio button and a text label corresponding to the option.
 * - Selection logic is implemented such that clicking on an option updates the `selectedIndex`.
 * - The current selection is visually represented by the active state of the radio button.
 *
 * ## Usage:
 * This function can be used in UIs where a selection is required from a list of options, such as choosing a plot type
 * in a data visualization tool.
 *
 * ### Example:
 * ```kotlin
 * val plotOptions = listOf("Line Chart", "Bar Chart", "Pie Chart")
 * val selectedPlot = remember { mutableStateOf(0) }
 *
 * PlotList(plotOptions, selectedPlot)
 * ```
 * In this example, `PlotList` is used to display three plot options. The selection is stored in `selectedPlot`, which
 * is a mutable state holding the index of the selected option.
 *
 * @see [RadioButton] for details on the radio button implementation.
 * @see [Column] and [Row] for layout structures.
 */
@Composable
private fun PlotList(
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
                        .padding(vertical = 4.dp)
                        .selectable(
                            selected = selectedIndex.value == index,
                            onClick = { selectedIndex.value = index }
                        )
                ) {
                    RadioButton(
                        onClick = { selectedIndex.value = index },
                        selected = index == selectedIndex.value,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(name)
                }
            }
        }
    }
}

/**
 * A composable function that creates the area for displaying plots in a user interface.
 *
 * `PlotArea` is responsible for rendering the selected plot from a list of figures. It forms the central part of the
 * UI where the visual representation of data (such as fitness trends or routes in an evolutionary algorithm) is
 * displayed. The function uses [PlotPanel] from the Lets-Plot library to handle the actual plot rendering.
 *
 * ## Functionality:
 * - Displays the plot corresponding to the currently selected index (`figureIndex.value`).
 * - The plot's aspect ratio is determined based on the value of `preserveAspectRatio`.
 * - If there are any computation messages from the plot rendering process, they are printed to the console.
 *
 * ## Usage:
 * This function is used within a UI layout where multiple plots are available for selection, and the user can choose
 * which plot to view. It is typically used in data analysis or visualization tools where different representations
 * of data are provided.
 *
 * ### Example:
 * ```kotlin
 * val figures = listOf("Plot 1" to figure1, "Plot 2" to figure2)
 * val selectedPlot = remember { mutableStateOf(0) }
 * val preserveAspect = remember { mutableStateOf(true) }
 *
 * PlotArea(figures, selectedPlot, preserveAspect)
 * ```
 * In this example, `PlotArea` is used to display the plot currently selected by the user. The user can switch between
 * `figure1` and `figure2` using a separate control mechanism that updates `selectedPlot`.
 *
 * @param figures A list of pairs, each containing a string label and a [Figure] object representing a plot.
 * @param figureIndex A [MutableState] holding the index of the currently selected plot to be displayed.
 * @param preserveAspectRatio A [MutableState] indicating whether the aspect ratio of the plot should be preserved.
 */
@Composable
private fun PlotArea(
    figures: List<Pair<String, Figure>>,
    figureIndex: MutableState<Int>,
    preserveAspectRatio: MutableState<Boolean>
) {
    Column(modifier = Modifier.fillMaxSize().padding(all = 10.dp)) {
        PlotPanel(
            figure = figures[figureIndex.value].second,
            preserveAspectRatio = preserveAspectRatio.value,
            modifier = Modifier.fillMaxSize()
        ) { computationMessages ->
            computationMessages.forEach { println("Computation message: $it") }
        }
    }
}

/**
 * A composable function that creates a sidebar for plot selection and aspect ratio control in a user interface.
 *
 * `Sidebar` is designed to provide control elements for the user to select different plots and to toggle the aspect
 * ratio preservation of the displayed plot. It's a part of a larger UI, typically used in conjunction with a main
 * area that displays the selected plot (`PlotArea`).
 *
 * ## Layout and Functionality:
 * - The function creates a vertical column layout that includes:
 *   - A header text labeling the plot selection area.
 *   - A list of selectable plot options (`PlotList`), allowing the user to choose which plot to display.
 *   - A row containing a checkbox to toggle the preservation of the aspect ratio of the plot.
 *
 * ## Usage:
 * This function is typically used in data visualization tools or any application where multiple plots are available
 * for viewing, and the user needs to select which plot to display. It provides an intuitive way to switch between
 * different plots and control their display properties.
 *
 * ### Example Usage:
 * ```kotlin
 * val figures = listOf("Plot 1" to figure1, "Plot 2" to figure2)
 * val selectedPlot = remember { mutableStateOf(0) }
 * val preserveAspect = remember { mutableStateOf(true) }
 *
 * Sidebar(figures, selectedPlot, preserveAspect)
 * ```
 * In this example, `Sidebar` is part of a UI layout where users can select between `figure1` and `figure2` and
 * toggle the aspect ratio preservation of the displayed plot.
 *
 * @param figures A list of pairs, each containing a string label and a [Figure] object representing a plot.
 * @param figureIndex A [MutableState] holding the index of the currently selected plot to be displayed.
 * @param preserveAspectRatio A [MutableState] indicating whether the aspect ratio of the plot should be preserved.
 */
@Composable
private fun Sidebar(
    figures: List<Pair<String, Figure>>,
    figureIndex: MutableState<Int>,
    preserveAspectRatio: MutableState<Boolean>
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.width(IntrinsicSize.Max).padding(end = 10.dp)
    ) {
        Text("Plots:", fontWeight = FontWeight.Bold)
        PlotList(
            options = figures.map { it.first },
            selectedIndex = figureIndex,
        )
        Row(verticalAlignment = CenterVertically) {
            Text("Keep ratio:")
            Checkbox(
                checked = preserveAspectRatio.value,
                onCheckedChange = { preserveAspectRatio.value = it }
            )
        }
    }
}
