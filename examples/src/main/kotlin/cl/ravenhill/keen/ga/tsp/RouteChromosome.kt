/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.tsp

import cl.ravenhill.keen.genetic.chromosomes.Chromosome


/**
 * Represents a chromosome used in genetic algorithms for encoding a route in the Traveling Salesman Problem
 * ([TravelingSalesmanProblem])
 *
 * This data class models a chromosome where each chromosome represents a complete route, composed of multiple
 * [RoutePointGene] objects. Each gene in the chromosome represents a point in the route. It is a specialized
 * implementation of the `Chromosome` interface, tailored for scenarios where routes are optimized in problems like
 * the Traveling Salesman Problem.
 *
 * ## Usage:
 * `RouteChromosome` is used in genetic algorithms to represent potential solutions to route optimization problems like
 * the Traveling Salesman Problem. It encapsulates a sequence of route points that collectively define a route.
 *
 * ### Example:
 * ```kotlin
 * val routeChromosome = RouteChromosome(listOf(RoutePointGene(3 to 4), RoutePointGene(5 to 6)))
 * println(routeChromosome) // Output: "(3, 4) -> (5, 6)"
 * ```
 * In this example, a `RouteChromosome` is created to represent a route comprising two points. The `toString` method
 * outputs a human-readable representation of this route.
 *
 * @property genes A list of `RoutePointGene` objects representing the points in the route.
 */
data class RouteChromosome(override val genes: List<RoutePointGene>) : Chromosome<Pair<Int, Int>, RoutePointGene> {

    /**
     * Creates a new instance of `RouteChromosome` with the specified genes.
     *
     * ## Functionality:
     * - Creates a new instance of `RouteChromosome` with the provided list of `RoutePointGene` objects.
     * - This method is useful in genetic operations such as mutation and crossover where the genetic composition
     *   of a chromosome is altered.
     *
     * ## Usage:
     * This method is typically used during genetic algorithm operations to create new chromosomes with modified genes.
     *
     * ### Example:
     * ```kotlin
     * val originalChromosome = RouteChromosome(...)
     * val mutatedGenes = ...
     * val mutatedChromosome = originalChromosome.duplicateWithGenes(mutatedGenes)
     * ```
     * In this example, `originalChromosome` is duplicated with a new set of genes, resulting in `mutatedChromosome`.
     *
     * @param genes The new list of `RoutePointGene` objects for the chromosome.
     * @return A new `RouteChromosome` instance with the updated list of genes.
     */
    override fun duplicateWithGenes(genes: List<RoutePointGene>) = copy(genes = genes)

    /**
     * Returns a string representation of the `RouteChromosome`.
     *
     * The returned string represents the route as a sequence of points, with each point formatted as "(x, y)" and
     * separated by " -> ". This format provides a clear and intuitive representation of the route encoded by the
     * chromosome.
     *
     * @return the string representation of the chromosome
     */
    override fun toString() = genes.joinToString(" -> ")

    /**
     * Factory class for creating instances of `RouteChromosome`.
     *
     * This factory is responsible for generating new `RouteChromosome` instances. It is particularly useful in the
     * initialization phase of a genetic algorithm where initial populations of chromosomes are created.
     *
     * ## Usage:
     * The factory creates `RouteChromosome` instances with a shuffled list of `RoutePointGene` objects, representing
     * a random route. This approach is used to generate diverse initial solutions for the genetic algorithm.
     *
     * ### Example:
     * ```kotlin
     * val chromosomeFactory = RouteChromosome.Factory()
     * val newChromosome = chromosomeFactory.make()
     * ```
     * In this example, `chromosomeFactory` creates a new instance of `RouteChromosome` with a random sequence of route
     * points.
     */
    class Factory : Chromosome.AbstractFactory<Pair<Int, Int>, RoutePointGene>() {
        override fun make() = RouteChromosome(TravelingSalesmanProblem.cities.shuffled().map { RoutePointGene(it) })
    }
}
