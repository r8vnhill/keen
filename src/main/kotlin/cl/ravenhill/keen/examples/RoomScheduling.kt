package cl.ravenhill.keen.examples

import cl.ravenhill.keen.Builders.engine
import cl.ravenhill.keen.Builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.operators.Mutator
import cl.ravenhill.keen.operators.crossover.SinglePointCrossover
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPrinter

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
    listOf(Meeting(1, 3), Meeting(2, 3), Meeting(5, 6), Meeting(7, 9), Meeting(4, 7))

private fun fitnessFn(genotype: Genotype<Int>): Double {
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

fun main() {
    val engine = engine(::fitnessFn, genotype {
        chromosomes = List(meetings.size) {
            IntChromosome.Factory(1, meetings.indices)
        }
    }) {
        populationSize = 20000
        optimizer = FitnessMinimizer()
        alterers = listOf(Mutator(0.03), SinglePointCrossover(0.1))
        limits = listOf(SteadyGenerations(20), GenerationCount(100))
        statistics = listOf(StatisticPrinter(1), StatisticCollector())
    }
    val result = engine.run()
    println(engine.statistics.last())
    val schedule = MutableList(result.best!!.genotype.size) { mutableListOf<Meeting>() }
    meetings.forEachIndexed { index, meeting ->
        val room = result.best.genotype.chromosomes[index].genes[0].dna
        schedule[room].add(meeting)
    }
    schedule.forEachIndexed { index, room ->
        println("Room $index: $room")
    }
}