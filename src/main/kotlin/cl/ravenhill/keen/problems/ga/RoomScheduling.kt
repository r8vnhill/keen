package cl.ravenhill.keen.problems.ga

import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.builders.ints
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPlotter

/**
 * Information of a meeting.
 *
 * @property start The start time of the meeting.
 * @property end The end time of the meeting.
 */
data class Meeting(val start: Int, val end: Int)

/**
 * The meetings to schedule.
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
private fun fitnessFn(genotype: Genotype<Int, IntGene>): Double {
    // We create a list to represent the rooms.
    // The size of the list is the number meetings, since the worst case scenario is that each
    // meeting is in a different room.
    val rooms = MutableList(genotype.size) { mutableListOf<Meeting>() }
    meetings.forEachIndexed { index, meeting ->
        val room = genotype.chromosomes[index].genes[0].dna
        rooms[room].add(meeting)
    }
    var conflicts = 0
    rooms.forEach { meetingList ->
        val table = IntArray(10)
        meetingList.forEach { meeting ->
            for (i in meeting.start until meeting.end) {
                table[i]++
            }
        }
        conflicts += table.count { it > 1 }
    }
    return rooms.filter { it.isNotEmpty() }.size.toDouble() + conflicts
}

/**
 * The meeting room scheduling problem is a combinatorial optimization problem that consists of
 * scheduling meetings in a set of rooms so that no meetings overlap.
 * This example uses a genetic algorithm to find the optimal solution.
 */
fun main() {
    val engine = engine(::fitnessFn, genotype {
        repeat(meetings.size) {
            chromosome {
                ints { size = 1; range = meetings.indices.first to meetings.indices.last }
            }
        }
    }) {
        populationSize = 100
        optimizer = FitnessMinimizer()
        alterers = listOf(Mutator(0.06), SinglePointCrossover(0.06))
        limits = listOf(SteadyGenerations(20), GenerationCount(100))
        statistics = listOf(StatisticCollector(), StatisticPlotter())
    }
    val result = engine.evolve()
    println(engine.statistics.first())
    val schedule = MutableList(result.best.genotype.size) { mutableListOf<Meeting>() }
    meetings.forEachIndexed { index, meeting ->
        val room = result.best.genotype.chromosomes[index].genes[0].dna
        schedule[room].add(meeting)
    }
    schedule.forEachIndexed { index, room ->
        println("Room $index: $room")
    }
    (engine.statistics.last() as StatisticPlotter).displayFitness()
}