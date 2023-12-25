/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.tsp

import cl.ravenhill.keen.listeners.EvolutionSummary

fun main() {
    val summary = EvolutionSummary<Pair<Int, Int>, RoutePointGene>()
    val plotter = TspPlotter()
    TravelingSalesmanProblem(summary, plotter)
    summary.display()
    plotter.display()
}