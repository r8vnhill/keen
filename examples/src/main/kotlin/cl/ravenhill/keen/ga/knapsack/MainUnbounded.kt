/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.knapsack

import cl.ravenhill.keen.listeners.EvolutionPlotter
import cl.ravenhill.keen.listeners.summary.EvolutionSummary

fun main() {
    val summary = EvolutionSummary<Pair<Int, Int>, KnapsackGene>()
    val plotter = EvolutionPlotter<Pair<Int, Int>, KnapsackGene>()
    UnboundedKnapsackProblem(summary, plotter)
    summary.display()
    summary.fittest.genotype.flatten().filter { it.first != 0 }.also(::println)
    plotter.display()
}