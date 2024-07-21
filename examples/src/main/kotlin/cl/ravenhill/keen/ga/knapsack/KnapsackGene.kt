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
 * knapsack problem. Each instance of `KnapsackGene` holds a value represented as a `Pair<Int, Int>`, where the
 * first element signifies the weight (w) and the second element denotes the value (v) of an item in the knapsack.
 *
 * @property value A [Pair]<[Int], [Int]> representing the weight and value of the item in the knapsack problem.
 */
data class KnapsackGene(override val value: Pair<Int, Int>) : Gene<Pair<Int, Int>, KnapsackGene> {

    /**
     * Creates a new instance of `KnapsackGene` with the specified value.
     *
     * @param value A `Pair<Int, Int>` representing the weight and value of the new gene.
     * @return A new `KnapsackGene` instance with the specified value.
     */
    override fun duplicateWithValue(value: Pair<Int, Int>) = KnapsackGene(value)

    /**
     * Generates a random `KnapsackGene` from the available items in the unbounded knapsack problem.
     *
     * @return A randomly generated `KnapsackGene`.
     */
    override fun generator() = UnboundedKnapsackProblem.items.random(Domain.random)

    /**
     * Returns a string representation of the `KnapsackGene`, showing the weight and value of the item.
     *
     * @return A string representation of the gene in the format "(w=weight, v=value)".
     */
    override fun toString() = "(w=${value.first}, v=${value.second})"
}
