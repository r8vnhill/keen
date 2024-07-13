/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.knapsack

import cl.ravenhill.keen.listeners.plotter.EvolutionPlotter
import cl.ravenhill.keen.listeners.summary.EvolutionSummary

fun main() {
    val engine = UnboundedKnapsackProblem(::EvolutionSummary, ::EvolutionPlotter)
    val summary = engine.listeners.filterIsInstance<EvolutionSummary<Pair<Int, Int>, KnapsackGene>>().first()
    val plotter = engine.listeners.filterIsInstance<EvolutionPlotter<Pair<Int, Int>, KnapsackGene>>().first()
    summary.display()
    summary.fittest.genotype.flatten().filter { it.first != 0 }.also(::println)
    plotter.display()
}
