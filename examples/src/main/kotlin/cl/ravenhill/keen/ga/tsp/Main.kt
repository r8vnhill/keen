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
 */
fun main() {
    val engine = TravelingSalesmanProblem(::EvolutionSummary, ::TspPlotter)
    val summary = engine.listeners.filterIsInstance<EvolutionSummary<Pair<Int, Int>, RoutePointGene>>().first()
    summary.display()
    val plotter = engine.listeners.filterIsInstance<TspPlotter>().first()
    plotter.display()
}
