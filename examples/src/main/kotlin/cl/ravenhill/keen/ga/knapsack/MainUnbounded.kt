/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.knapsack

import cl.ravenhill.keen.listeners.plotter.EvolutionPlotter
import cl.ravenhill.keen.listeners.summary.EvolutionSummary

/**
 * The main function that sets up and runs the genetic algorithm for the unbounded knapsack problem.
 *
 * The unbounded knapsack problem is a variation of the classic knapsack problem. In this version, there is an unlimited
 * supply of each type of item, and the goal is to maximize the total value of the items in the knapsack without
 * exceeding its weight capacity. Each item has a specific weight and value, and the objective is to determine the
 * optimal combination of items that yields the highest value.
 *
 * This function initializes the evolutionary engine with listeners for summarizing and plotting the evolution process.
 * It retrieves the `EvolutionSummary` and `EvolutionPlotter` listeners from the engine, displays the summary, and
 * prints the fittest genotype that contains non-zero weight items. Finally, it displays the plot of the evolution
 * process.
 */
fun main() {
    val engine = UnboundedKnapsackProblem(::EvolutionSummary, ::EvolutionPlotter)
    val summary = engine.listeners.filterIsInstance<EvolutionSummary<Pair<Int, Int>, KnapsackGene>>().first()
    val plotter = engine.listeners.filterIsInstance<EvolutionPlotter<Pair<Int, Int>, KnapsackGene>>().first()
    summary.display()
    summary.fittest.genotype.flatten().filter { it.first != 0 }.also(::println)
    plotter.display()
}
