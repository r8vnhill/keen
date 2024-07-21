/*
 * Copyright (c) 2024, Ignacio Slater M.
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
 * @property genes A list of `RoutePointGene` objects representing the points in the route.
 */
data class RouteChromosome(override val genes: List<RoutePointGene>) : Chromosome<Pair<Int, Int>, RoutePointGene> {

    /**
     * Creates a new instance of `RouteChromosome` with the specified genes.
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
     */
    class Factory : Chromosome.AbstractFactory<Pair<Int, Int>, RoutePointGene>() {
        override fun make() = RouteChromosome(TravelingSalesmanProblem.cities.shuffled().map { RoutePointGene(it) })
    }
}
