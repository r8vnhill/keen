/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.examples.ga

import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.builders.ints
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import cl.ravenhill.keen.operators.mutator.strategies.RandomMutator
import cl.ravenhill.keen.util.listeners.EvolutionPlotter
import cl.ravenhill.keen.util.listeners.EvolutionSummary
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer

/**
 * Represents a meeting with a start and end time.
 *
 * @property start The start time of the meeting.
 * @property end The end time of the meeting.
 */
data class Meeting(val start: Int, val end: Int)

/**
 * A list of Meeting objects representing various meetings scheduled with start and end times.
 */
private val meetings =
    listOf(
        Meeting(1, 3),
        Meeting(2, 3),
        Meeting(5, 6),
        Meeting(7, 9),
        Meeting(4, 7),
        Meeting(8, 10),
        Meeting(2, 7),
        Meeting(3, 4),
        Meeting(1, 5),
        Meeting(3, 6),
        Meeting(4, 5)
    )

/**
 * The fitness function for the room scheduling problem.
 * It is calculated as the number of rooms needed to schedule all the meetings plus the number of
 * meetings that overlap.
 */
/**
 * Calculates the fitness score of a given genotype.
 * The fitness score represents how good a solution is.
 * A lower score is better.
 *
 * @param genotype the genotype to evaluate.
 * @return the fitness score.
 */
private fun fitnessFn(genotype: Genotype<Int, IntGene>): Double {
    // Create a map to represent the rooms, where the key is the room number and the value is
    // a list of meetings in that room.
    val rooms =
        meetings.groupBy { genotype.chromosomes[meetings.indexOf(it)].genes[0].dna }
    // Calculate the number of conflicts in each room.
    val conflicts = rooms.values.sumOf { meetingList ->
        val table = IntArray(10) // Create an array to represent the time slots in a day.
        meetingList.forEach { meeting ->
            // Increment the time slots for each meeting.
            for (i in meeting.start until meeting.end) {
                table[i]++
            }
        }
        // Count the number of time slots with more than one meeting.
        table.count { it > 1 }
    }
    // The fitness score is the number of rooms used plus the number of conflicts.
    // We add 1 to the number of rooms used to avoid a fitness score of 0.
    return rooms.size.toDouble() + conflicts
}

/**
 * Solves the room scheduling problem for a list of meetings, where each meeting has a start and end
 * time and must be assigned to a room.
 * The goal is to minimize the number of conflicts, defined as the number of time slots where two or
 * more meetings are scheduled in the same room.
 */
fun main() {
    // Create a genetic algorithm engine with the fitness function and genotype for the problem.
    val engine = engine(
        ::fitnessFn,
        genotype {
            repeat(meetings.size) {
                chromosome {
                    ints {
                        size = 1
                        ranges += meetings.indices.first..meetings.indices.last
                    }
                }
            }
        }
    ) {
        // Set the parameters for the genetic algorithm.
        populationSize = 100
        optimizer = FitnessMinimizer()
        alterers = listOf(RandomMutator(0.06), SinglePointCrossover(0.2))
        limits = listOf(SteadyGenerations(20), GenerationCount(100))
        listeners += listOf(EvolutionSummary(), EvolutionPlotter())
    }
    // Evolve the population and get the best result.
    val result = engine.evolve()
    // Print the statistics of the genetic algorithm.
    println(engine.listeners.first())
    // Create a schedule based on the best genotype.
    val schedule = MutableList(result.best.genotype.size) { mutableListOf<Meeting>() }
    meetings.forEachIndexed { index, meeting ->
        val room = result.best.genotype.chromosomes[index].genes[0].dna
        schedule[room].add(meeting)
    }
    // Print the schedule for each room.
    schedule.forEachIndexed { index, room ->
        println("Room $index: $room")
    }
    // Display a plot of the fitness values over time.
    (engine.listeners.last() as EvolutionPlotter).displayFitness()
}
