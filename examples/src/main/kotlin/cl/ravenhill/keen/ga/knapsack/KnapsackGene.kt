/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.knapsack

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Represents a gene for the knapsack problem in a genetic algorithm.
 *
 * The `KnapsackGene` class is a specialized implementation of the [Gene] interface, tailored specifically for the
 * knapsack problem.
 * Each instance of `KnapsackGene` holds a value represented as a [Pair]<[Int], [Int]>, where the first element
 * signifies the weight (w) and the second element denotes the value (v) of an item in the knapsack.
 *
 * ## Usage:
 * This class is utilized in genetic algorithms that solve the knapsack problem, where the goal is to maximize the total
 * value of items in a knapsack without exceeding its weight capacity. `KnapsackGene` facilitates the representation and
 * manipulation of items during the genetic algorithm process.
 *
 * ### Example:
 * ```kotlin
 * val gene = KnapsackGene(Pair(10, 100))
 * println(gene) // Outputs: (w=10, v=100)
 * ```
 * In this example, `gene` represents an item with a weight of 10 and a value of 100. The `toString` method outputs the
 * gene's characteristics in a readable format.
 *
 * @property value A [Pair]<[Int], [Int]> representing the weight and value of the item in the knapsack problem.
 */
data class KnapsackGene(override val value: Pair<Int, Int>) : Gene<Pair<Int, Int>, KnapsackGene> {
    override fun duplicateWithValue(value: Pair<Int, Int>) = KnapsackGene(value)
    override fun generator() = UnboundedKnapsackProblem.items.random(Domain.random)
    override fun toString() = "(w=${value.first}, v=${value.second})"
}
