/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.ga.tsp

import cl.ravenhill.keen.dsl.chromosomeOf
import cl.ravenhill.keen.dsl.evolutionEngine
import cl.ravenhill.keen.dsl.genotypeOf
import cl.ravenhill.keen.limits.MaxGenerations
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.operators.alteration.crossover.OrderedCrossover
import cl.ravenhill.keen.operators.alteration.mutation.InversionMutator
import cl.ravenhill.keen.ranking.FitnessMinRanker

/**
 * An object that encapsulates the setup and execution of a genetic algorithm for solving the Traveling Salesman Problem
 * (TSP).
 *
 * This object leverages a genetic algorithm approach to find the shortest possible route that visits a set of cities
 * and returns to the origin city. It aims to minimize the total distance of the route. The algorithm uses evolutionary
 * techniques such as selection, crossover, and mutation to evolve solutions over generations.
 *
 * ## Key Features:
 * - Encapsulates the evolutionary algorithm configuration and execution within a single object.
 * - Uses the `invoke` operator function as an entry point to start the evolution process, making it easy to use.
 * - Supports the attachment of external observers (listeners) to monitor the evolutionary process.
 *
 * ## Evolutionary Algorithm Configuration:
 * - [evolutionEngine]: Configures and initializes the genetic algorithm engine for evolving TSP solutions.
 * - [fitnessFunction]: A function that evaluates the fitness of each candidate solution, based on the total distance of
 *   the route.
 * - [genotypeOf] Defines the genetic representation of solutions, where each solution (genotype) represents a sequence
 *   of cities.
 * - `populationSize`: Determines the number of solutions in each generation.
 * - `alterers`: Genetic operators that modify solutions over generations, including mutators and crossover strategies.
 * - `limits`: Criteria for terminating the evolution process, such as maximum generations or reaching a steady state.
 * - `observers`: Optional listeners that track and respond to different stages in the evolution process.
 *
 * ## Usage:
 * The object is designed to be invoked with a set of `EvolutionListener<Pair<Int, Int>, RoutePointGene>` objects, which
 * can monitor various events and states during the evolution process. The `invoke` operator simplifies starting the
 * genetic algorithm with the desired configuration.
 *
 * ## Example:
 * ```kotlin
 * TravelingSalesmanProblem(myListener)
 * ```
 * Here, `myListener` is an instance of `EvolutionListener` that will monitor the evolutionary process. The algorithm
 * evolves solutions to minimize the total distance of a route that visits all cities.
 *
 * @property POPULATION_SIZE The size of the population in each generation, set to 1000 by default.
 * @property MAX_GENERATIONS The maximum number of generations the algorithm will run, set to 200 by default.
 * @property cities A predefined list of city coordinates involved in the TSP.
 */
data object TravelingSalesmanProblem {
    private const val POPULATION_SIZE = 1000
    private const val MAX_GENERATIONS = 200

    operator fun invoke(vararg observers: EvolutionListener<Pair<Int, Int>, RoutePointGene>) {
        val engine = evolutionEngine(TravelingSalesmanProblem::fitnessFunction, genotypeOf {
            chromosomeOf {
                RouteChromosome.Factory()
            }
        }) {
            populationSize = POPULATION_SIZE
            limits += MaxGenerations(MAX_GENERATIONS)
            alterers += listOf(InversionMutator(individualRate = 0.1), OrderedCrossover(chromosomeRate = 0.3))
            ranker = FitnessMinRanker()
            listeners += observers
        }
        engine.evolve()
    }

    val cities = listOf(
        Pair(60, 200),
        Pair(180, 200),
        Pair(80, 180),
        Pair(140, 180),
        Pair(20, 160),
        Pair(100, 160),
        Pair(200, 160),
        Pair(140, 140),
        Pair(40, 120),
        Pair(100, 120),
        Pair(180, 100),
        Pair(60, 80),
        Pair(120, 80),
        Pair(180, 60),
        Pair(20, 40),
        Pair(100, 40),
        Pair(200, 40),
        Pair(20, 20),
        Pair(60, 20),
        Pair(160, 20)
    )
}
