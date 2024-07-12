/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.tsp

import cl.ravenhill.keen.listeners.summary.EvolutionSummary

/**
 * The entry point of the application for solving the Traveling Salesman Problem (TSP) using a genetic algorithm.
 *
 * This function sets up and runs the genetic algorithm for the TSP and visualizes the results. It utilizes the
 * `TravelingSalesmanProblem` object for the algorithm's configuration and execution, and leverages `EvolutionSummary`
 * and `TspPlotter` for monitoring and visualizing the process and outcomes.
 *
 * ## Process:
 * - Initializes `EvolutionSummary` to track the summary of the genetic algorithm's evolution process.
 * - Instantiates `TspPlotter` to visualize the fitness evolution and the best route found.
 * - Invokes the `TravelingSalesmanProblem` with the summary and plotter as observers.
 * - Displays the results using the `display` methods of both `EvolutionSummary` and `TspPlotter`.
 *
 * ## Usage:
 * Run this function to start the TSP solution process. It is designed to provide insights into the genetic algorithm's
 * performance and the efficiency of the solution found.
 */
fun main() {
    val summary = EvolutionSummary<Pair<Int, Int>, RoutePointGene>()
    val plotter = TspPlotter()
    TravelingSalesmanProblem(summary, plotter)
    summary.display()
    plotter.display()
}
