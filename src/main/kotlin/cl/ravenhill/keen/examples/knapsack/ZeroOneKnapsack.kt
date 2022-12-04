/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.examples.knapsack

import cl.ravenhill.keen.Builders.engine
import cl.ravenhill.keen.Builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.BoolChromosome
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.operators.Mutator
import cl.ravenhill.keen.operators.crossover.SinglePointCrossover
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPrinter

/**
 * The maximum weight that the knapsack can hold.
 */
private const val MAX_WEIGHT = 15

/**
 * The possible items that can be put in the knapsack.
 */
private val items = listOf(4 to 12, 2 to 1, 2 to 2, 1 to 1, 10 to 4, 0 to 0)

/**
 * The fitness function for the knapsack problem.
 * It calculates the fitness of a given genotype by summing the values of the items in the knapsack.
 * If the weight of the knapsack is greater than the maximum weight, the fitness is reduced by the
 * difference between the weight and the maximum weight.
 *
 * @param genotype The genotype to calculate the fitness for.
 * @return The fitness of the genotype.
 */
private fun fitnessFn(genotype: Genotype<Boolean>) =
    (genotype.flatten() zip items)
        .sumOf { (isInBag, item) -> if (isInBag) item.first else 0 }
        .let { if (it > MAX_WEIGHT) it - MAX_WEIGHT else it }
        .toDouble()

/**
 * The 0-1 knapsack problem is a classic problem in combinatorial optimization.
 * The problem is to fill a knapsack with items of different weights and values so that the total
 * weight is less than or equal to a given limit and the total value is as large as possible.
 * The 0-1 knapsack problem is a special case of the knapsack problem where the number of each item
 * is either 0 or 1.
 */
fun main() {
    val engine = engine(::fitnessFn, genotype {
        chromosomes = listOf(BoolChromosome.Factory(items.size, 0.5))
    }) {
        populationSize = 10000
        alterers = listOf(Mutator(0.03), SinglePointCrossover(0.06))
        limits = listOf(SteadyGenerations(10))
        statistics = listOf(StatisticCollector(), StatisticPrinter(1))
    }
    val result = engine.run()
    println(engine.statistics.first())
    result.best?.flatten()
        ?.mapIndexed { index, b -> if (b) items[index] else null }
        ?.filterNotNull()
        .let { println("Items: $it") }
}