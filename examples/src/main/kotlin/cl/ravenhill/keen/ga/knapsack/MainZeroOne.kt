/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.knapsack

import cl.ravenhill.keen.genetic.genes.BooleanGene
import cl.ravenhill.keen.listeners.plotter.EvolutionPlotter
import cl.ravenhill.keen.listeners.summary.EvolutionSummary


/**
 * The main function that sets up and runs the genetic algorithm for the 0/1 knapsack problem.
 *
 * The 0/1 knapsack problem is a combinatorial optimization problem where you have a set of items, each with a specific
 * weight and value, and a knapsack with a limited capacity. The goal is to maximize the total value of the items in the
 * knapsack without exceeding its weight capacity. Each item can either be included in the knapsack (1) or excluded (0),
 * hence the name 0/1 knapsack problem.
 *
 * This function initializes the evolutionary engine with listeners for summarizing and plotting the evolution process.
 * It retrieves the `EvolutionSummary` and `EvolutionPlotter` listeners from the engine, displays the summary, and
 * prints the fittest genotype that contains items in the knapsack. Finally, it displays the plot of the evolution
 * process.
 */
fun main() {
    val plotter = EvolutionPlotter<Boolean, BooleanGene>()

    val engine = ZeroOneKnapsackProblem(::EvolutionSummary, ::EvolutionPlotter)
    val summary = engine.listeners.filterIsInstance<EvolutionSummary<Boolean, BooleanGene>>().first()
    summary.display()
    val fittestSolution = summary.fittest.genotype.flatten()
        .mapIndexedNotNull { index, isInBag ->
            if (isInBag) ZeroOneKnapsackProblem.items[index] else null
        }
        .joinToString { (value, weight) -> "($value, $weight)" }

    println(fittestSolution)
    plotter.display()
}
