/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.tsp

import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Represents a gene used in genetic algorithms for encoding route points.
 *
 * This data class models a gene where each gene represents a point in a route as a pair of integers (e.g.,
 * coordinates). It is a specialized implementation of the `Gene` interface, tailored for scenarios where routes are
 * optimized or analyzed using genetic algorithms.
 *
 * @property value A pair of integers representing the route point's coordinates.
 */
data class RoutePointGene(override val value: Pair<Int, Int>) : Gene<Pair<Int, Int>, RoutePointGene> {

    /**
     * Creates a new instance of the gene with the specified value.
     *
     * @param value The new value for the gene, provided as a `Pair<Int, Int>`.
     * @return A new instance of the gene class with the updated value.
     */
    override fun duplicateWithValue(value: Pair<Int, Int>) = copy(value = value)

    /**
     * Returns a string representation of the object.
     *
     * The returned string has the format "(x, y)" where x and y are the values of the first and second components of
     * the object's value.
     *
     * @return the string representation of the object
     */
    override fun toString() = "(${value.first}, ${value.second})"
}
