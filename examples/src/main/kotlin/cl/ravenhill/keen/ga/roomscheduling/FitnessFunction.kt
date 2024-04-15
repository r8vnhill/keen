/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.roomscheduling

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.IntGene

/**
 * Calculates the fitness of a given genotype in the context of the room scheduling problem.
 *
 * This function is an integral part of the RoomSchedulingProblem, where it evaluates the efficiency and viability of a
 * given room allocation strategy represented by a genotype. The fitness score is determined based on the number of
 * rooms used and the number of scheduling conflicts between meetings.
 *
 * ## Process:
 * 1. **Room Grouping**: The function groups meetings into rooms based on the allocation strategy suggested by the
 *   genotype.
 * 2. **Conflict Calculation**: For each room, the function calculates the number of scheduling conflicts. A conflict
 *   occurs when two or more meetings overlap in time within the same room.
 * 3. **Fitness Score Computation**: The fitness score is computed as the sum of the total number of rooms used and the
 *   total number of conflicts. The goal is to minimize this score, where a lower score indicates a more efficient and
 *   conflict-free room allocation.
 *
 * ## Implementation Details:
 * - The genotype is treated as a matrix, where each value represents the room assigned to a meeting.
 * - Conflicts are identified by iterating over the time slots and counting overlaps.
 * - The fitness score penalizes solutions with more rooms and more conflicts, guiding the evolutionary algorithm
 *   towards more optimal solutions.
 *
 * ## Usage:
 * This function is typically used by the genetic algorithm's engine to evaluate and rank genotypes during
 * the evolutionary process. It's not intended for standalone use, but as part of the optimization process
 * within the [RoomSchedulingProblem].
 *
 * @param genotype The genotype representing a room allocation strategy. It's a structure where each gene
 *   corresponds to the room assignment for a specific meeting.
 * @return The fitness score as a `Double`. Lower scores indicate better solutions.
 */
internal fun RoomSchedulingProblem.fitnessFunction(genotype: Genotype<Int, IntGene>): Double {
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
