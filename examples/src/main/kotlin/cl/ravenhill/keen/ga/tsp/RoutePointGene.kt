/*
 * Copyright (c) 2023, Ignacio Slater M.
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
 * ## Usage:
 * `RoutePointGene` is used in genetic algorithms where route optimization or pathfinding is involved. Each gene
 * encapsulates a coordinate or a point along a route, and a collection of these genes can represent a complete route.
 *
 * ### Example:
 * ```kotlin
 * val routePointGene = RoutePointGene(3 to 4)
 * println(routePointGene) // Output: (3, 4)
 * ```
 * In this example, a `RoutePointGene` is created to represent a route point at coordinates (3, 4). The gene's value
 * can be used in the context of a genetic algorithm to evolve and optimize routes.
 *
 * @property value A pair of integers representing the route point's coordinates.
 */
data class RoutePointGene(override val value: Pair<Int, Int>) : Gene<Pair<Int, Int>, RoutePointGene> {

    /**
     * Creates a new instance of the gene with the specified value.
     *
     * ## Functionality:
     * - Creates a new instance of the gene (or `RoutePointGene` in this context) with the provided `value`.
     * - The `value` parameter is a `Pair<Int, Int>`, representing new coordinates or data for the gene.
     * - Utilizes the `copy` function, a feature of Kotlin data classes, ensuring that the new instance retains the
     *   same properties as the original except for the updated value.
     *
     * ## Usage:
     * This method is typically invoked during the mutation process in a genetic algorithm. When a gene's value needs
     * to be changed, `duplicateWithValue` is called with the new value, resulting in a new gene instance with this
     * updated value.
     *
     * ### Example:
     * ```kotlin
     * val originalGene = RoutePointGene(5, 10)
     * val mutatedGene = originalGene.duplicateWithValue(7, 15)
     * ```
     * In this example, `originalGene` represents a route point at coordinates (5, 10). `mutatedGene` is a new
     * instance of `RoutePointGene` with updated coordinates (7, 15), created using `duplicateWithValue`.
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
