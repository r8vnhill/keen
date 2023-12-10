/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.dsl.chromosomeOf
import cl.ravenhill.keen.dsl.evolutionEngine
import cl.ravenhill.keen.dsl.genotypeOf
import cl.ravenhill.keen.dsl.integers
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import cl.ravenhill.keen.limits.MaxGenerations
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.listeners.EvolutionPlotter
import cl.ravenhill.keen.listeners.EvolutionSummary
import cl.ravenhill.keen.operators.crossover.SinglePointCrossover
import cl.ravenhill.keen.operators.mutation.RandomMutator
import cl.ravenhill.keen.ranking.FitnessMinRanker
import kotlin.random.Random

/**
 * Represents a time-bound meeting with a defined start and end time.
 *
 * The `Meeting` class encapsulates the concept of a scheduled meeting by storing its start and end times.
 * This class is particularly useful for managing and organizing meetings in scheduling applications or
 * time management systems. The time values are represented as integers, which could correspond to various
 * time formats such as hours of the day, minutes since midnight, or other domain-specific representations.
 *
 * ## Characteristics:
 * - **Start Time**: The beginning time of the meeting. Represented as an integer.
 * - **End Time**: The time when the meeting concludes. Also represented as an integer.
 *   It's assumed that the end time is always equal to or later than the start time.
 * - **Duration**: Can be calculated as the difference between the end and start times.
 *
 * ## Usage:
 * `Meeting` can be used in applications where scheduling or time management is a key feature.
 * For instance, in a calendar app to represent the timeslot of a meeting or in an algorithm
 * for meeting room scheduling.
 *
 * ### Example:
 * Creating a `Meeting` instance to represent a meeting from 10:00 AM to 11:00 AM (assuming time is in hours):
 * ```kotlin
 * val morningMeeting = Meeting(10, 11)
 * ```
 *
 * @property start The start time of the meeting.
 * @property end The end time of the meeting. It should be greater than or equal to `start`.
 * @constructor Creates a new `Meeting` instance with the specified start and end times.
 */
private data class Meeting(val start: Int, val end: Int)

/**
 * List of meetings.
 *
 * This variable is a list of Meeting objects that represent scheduled meetings.
 */
private val meetings =
    listOf(
        Meeting(start = 1, end = 3),
        Meeting(start = 2, end = 3),
        Meeting(start = 5, end = 6),
        Meeting(start = 7, end = 9),
        Meeting(start = 4, end = 7),
        Meeting(start = 8, end = 10),
        Meeting(start = 2, end = 7),
        Meeting(start = 3, end = 4),
        Meeting(start = 1, end = 5),
        Meeting(start = 3, end = 6),
        Meeting(start = 4, end = 5)
    )

/**
 * Evaluates the fitness of a given genotype in the context of a meeting scheduling problem.
 *
 * This function calculates the fitness of a `Genotype`, representing a potential solution to scheduling
 * a series of meetings in a limited number of rooms. The fitness is evaluated based on two criteria:
 * the number of rooms used and the number of conflicts in the schedule.
 *
 * ## Functionality:
 * - **Room Allocation**: Distributes meetings across available rooms based on the genes in the genotype.
 * - **Conflict Calculation**: Counts the number of time conflicts within each room. A conflict occurs when
 *   two or more meetings overlap in time within the same room.
 *
 * ## Fitness Score:
 * The fitness score is a composite measure calculated as the sum of the total number of rooms used
 * and the total number of meeting time conflicts. A lower fitness score indicates a more efficient
 * and conflict-free allocation of meetings to rooms.
 *
 * @param genotype The genotype to be evaluated. Each chromosome in the genotype represents a room, and
 *   each gene in a chromosome represents the allocation of a specific meeting to that room.
 * @return The fitness score as a `Double`, combining the number of rooms used and the number of conflicts.
 */
private fun fitnessFunction(genotype: Genotype<Int, IntGene>): Double {
    // We can access the genotype components by index as it is a matrix.
    val rooms = meetings.groupBy { genotype[meetings.indexOf(it)][0].value }
    val conflicts = rooms.values.sumOf { meetingList ->
        val table = IntArray(size = 10)
        meetingList.forEach { meeting ->
            // The ..< operator is equivalent to the range: [start, end)
            for (i in meeting.start..<meeting.end) {
                table[i]++
            }
        }
        table.count { it > 1 }
    }
    // Fitness is penalized by the number of conflicts.
    return rooms.size.toDouble() + conflicts
}

private const val POPULATION_SIZE = 100

fun main() {
    Domain.random = Random(seed = 420)
    val summary = EvolutionSummary<Int, IntGene>()
    val plotter = EvolutionPlotter<Int, IntGene>()
    val engine = evolutionEngine(
        ::fitnessFunction,
        genotypeOf {
            repeat(meetings.size) {
                chromosomeOf {
                    integers {
                        size = 1
                        ranges  += meetings.indices.first..meetings.indices.last
                    }
                }
            }
        }) {
        populationSize = POPULATION_SIZE
        ranker = FitnessMinRanker()
        alterers += listOf(RandomMutator(individualRate = 0.06), SinglePointCrossover(chromosomeRate = 0.2))
        limits += listOf(SteadyGenerations(generations = 20), MaxGenerations(generations = 100))
        listeners += plotter + summary  // Add both listeners to the engine
    }
    engine.evolve()
    summary.display()
    val schedule = MutableList(meetings.size) { mutableListOf<Meeting>() }
    meetings.forEachIndexed { index, meeting ->
        val room = summary.fittest.genotype[index][0].value
        schedule[room] += meeting
    }
    schedule.forEachIndexed { index, meetings ->
        println("Room $index: $meetings")
    }
    plotter.display()
}
