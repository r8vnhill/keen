/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.knapsack

import cl.ravenhill.keen.genetic.genes.BooleanGene
import cl.ravenhill.keen.listeners.EvolutionPlotter
import cl.ravenhill.keen.listeners.EvolutionSummary


/**
 * Main function to execute the Zero-One Knapsack Problem using a genetic algorithm.
 *
 * This function demonstrates the use of the [ZeroOneKnapsackProblem] object to solve the knapsack problem with a
 * genetic algorithm. It includes the creation of summary and plotter tools to monitor and visualize the evolutionary
 * process and outcomes.
 *
 * ## Process:
 * - Initializes `EvolutionSummary` and `EvolutionPlotter` instances for monitoring and visualizing the genetic
 *   algorithm's progress.
 * - Invokes the `ZeroOneKnapsackProblem` with these monitoring tools to start the evolutionary process.
 * - Displays the summary and plots the evolution results using the respective tools.
 * - Extracts and prints the fittest genotype from the summary, showing the best solution found by the algorithm.
 *
 * ### Usage:
 * Run this function to solve the Zero-One Knapsack Problem using a genetic algorithm and to visualize the process and
 * results. It provides insights into the algorithm's performance and the quality of the solutions it generates.
 *
 * ### Example Output:
 * An example output of this function includes a summary of the evolutionary process, a plot visualizing the progress,
 * and a printout of the fittest solution in the format of item pairs (value, weight).
 */
fun main() {
    val summary = EvolutionSummary<Boolean, BooleanGene>()
    val plotter = EvolutionPlotter<Boolean, BooleanGene>()

    ZeroOneKnapsackProblem(summary, plotter)

    summary.display()
    val fittestSolution = summary.fittest.genotype.flatten()
        .mapIndexedNotNull { index, isInBag ->
            if (isInBag) ZeroOneKnapsackProblem.items[index] else null
        }
        .joinToString { (value, weight) -> "($value, $weight)" }

    println(fittestSolution)
    plotter.display()
}
