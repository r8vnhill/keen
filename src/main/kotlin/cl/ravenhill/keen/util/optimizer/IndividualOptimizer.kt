/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.optimizer

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene

/**
 * Generic optimization strategy to determine which of two individuals is better.
 *
 * @property comparator The comparator to use to determine which of two individuals is better.
 */
interface IndividualOptimizer<DNA, G : Gene<DNA, G>> {
    val comparator
        get() = Comparator(::compare)

    /**
     * Compares two individuals and returns a negative integer, zero, or a positive integer as the
     * first phenotype is less than, equal to, or greater than the second.
     */
    operator fun invoke(a: Individual<*, *>, b: Individual<*, *>): Int = compare(a, b)

    /**
     * Compares two individuals and returns a negative integer, zero, or a positive integer as the
     * first phenotype is less than, equal to, or greater than the second.
     */
    fun compare(p1: Individual<*, *>, p2: Individual<*, *>): Int

    /**
     * Sorts the given list of individuals using this optimizer.
     */
    fun sort(population: Population<DNA, G>) =
        population.sortedWith(comparator.reversed())
}
