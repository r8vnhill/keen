/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.roomscheduling

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import cl.ravenhill.keen.listeners.plotter.EvolutionPlotter
import cl.ravenhill.keen.listeners.summary.EvolutionSummary
import kotlin.random.Random

/**
 * The main entry point for the room scheduling application using an evolutionary algorithm.
 *
 * This function sets up the necessary components for running a genetic algorithm to find an optimal or near-optimal
 * solution for scheduling meetings into rooms. The goal is to minimize room usage and scheduling conflicts.
 *
 * ## Process:
 * 1. **Initialization**: Sets the random seed for reproducibility and initializes summary and plotter objects for
 *   tracking the evolution process.
 * 2. **Problem Setup**: Initializes the [RoomSchedulingProblem] with the summary and plotter.
 * 3. **Evolution Process**: The `RoomSchedulingProblem` instance runs the evolutionary algorithm.
 * 4. **Result Display**: After the evolution, the best solution is extracted, and the schedule along with
 *    a summary of the evolutionary process is displayed.
 * 5. **Visualization**: The plotter displays the progress of the evolutionary algorithm.
 *
 * ## Key Components:
 * - [Domain.random]: Configures the random number generator with a specified seed for consistent behavior.
 * - [EvolutionSummary] & [EvolutionPlotter]: Objects used for monitoring and visualizing the evolutionary process.
 * - `RoomSchedulingProblem`: Represents the specific problem instance, including the setup and execution of the
 *   genetic algorithm.
 * - `schedule`: A mutable list that stores the best room allocation solution.
 *
 * ## Output:
 * The function outputs the room allocation schedule to the standard console and visualizes the evolution process.
 */
fun main() {
    Domain.random = Random(seed = 420)
    val summary = EvolutionSummary<Int, IntGene>()
    val plotter = EvolutionPlotter<Int, IntGene>()
    RoomSchedulingProblem(summary, plotter)
    summary.display()
    val schedule = MutableList(RoomSchedulingProblem.meetings.size) { mutableListOf<Meeting>() }
    RoomSchedulingProblem.meetings.forEachIndexed { index, meeting ->
        val room = summary.fittest.genotype[index][0].value
        schedule[room] += meeting
    }
    schedule.forEachIndexed { index, meetings ->
        println("Room $index: $meetings")
    }
    plotter.display()
}
