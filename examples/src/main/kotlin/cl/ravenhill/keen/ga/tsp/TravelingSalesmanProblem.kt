/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.ga.tsp

import cl.ravenhill.keen.dsl.chromosomeOf
import cl.ravenhill.keen.dsl.evolutionEngine
import cl.ravenhill.keen.dsl.genotypeOf
import cl.ravenhill.keen.evolution.EvolutionEngine
import cl.ravenhill.keen.limits.steadyGenerations
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.operators.alteration.crossover.OrderedCrossover
import cl.ravenhill.keen.operators.alteration.mutation.InversionMutator
import cl.ravenhill.keen.ranking.FitnessMinRanker

private typealias TspListenerFactory =
    (ListenerConfiguration<Pair<Int, Int>, RoutePointGene>) -> EvolutionListener<Pair<Int, Int>, RoutePointGene>

/**
 * An object that encapsulates the setup and execution of a genetic algorithm for solving the Traveling Salesman Problem
 * (TSP).
 *
 * This object leverages a genetic algorithm approach to find the shortest possible route that visits a set of cities
 * and returns to the origin city. It aims to minimize the total distance of the route. The algorithm uses evolutionary
 * techniques such as selection, crossover, and mutation to evolve solutions over generations.
 *
 * @property POPULATION_SIZE The size of the population in each generation, set to 1000 by default.
 * @property STEADY_GENERATIONS The number of generations with no improvement in fitness after which the genetic
 *  algorithm will terminate, set to 200 by default.
 * @property cities A predefined list of city coordinates involved in the TSP.
 */
data object TravelingSalesmanProblem {
    private const val POPULATION_SIZE = 1000
    private const val STEADY_GENERATIONS = 500

    /**
     * Sets up and runs the genetic algorithm for the Traveling Salesman Problem (TSP) with the specified observers.
     *
     * @param observers a vararg parameter of observer factories that create listeners for the evolution process
     */
    operator fun invoke(vararg observers: TspListenerFactory): EvolutionEngine<Pair<Int, Int>, RoutePointGene> {
        val engine = evolutionEngine(TravelingSalesmanProblem::fitnessFunction, genotypeOf {
            chromosomeOf {
                RouteChromosome.Factory()
            }
        }) {
            populationSize = POPULATION_SIZE
            limitFactories += steadyGenerations(STEADY_GENERATIONS)
            alterers += listOf(InversionMutator(individualRate = 0.3), OrderedCrossover(chromosomeRate = 0.3))
            ranker = FitnessMinRanker()
            listenerFactories += observers
        }
        engine.evolve()
        return engine
    }

    val cities = listOf(
        Pair(first = 60, second = 200),
        Pair(first = 180, second = 200),
        Pair(first = 80, second = 180),
        Pair(first = 140, second = 180),
        Pair(first = 20, second = 160),
        Pair(first = 100, second = 160),
        Pair(first = 200, second = 160),
        Pair(first = 140, second = 140),
        Pair(first = 40, second = 120),
        Pair(first = 100, second = 120),
        Pair(first = 180, second = 100),
        Pair(first = 60, second = 80),
        Pair(first = 120, second = 80),
        Pair(first = 180, second = 60),
        Pair(first = 20, second = 40),
        Pair(first = 100, second = 40),
        Pair(first = 200, second = 40),
        Pair(first = 20, second = 20),
        Pair(first = 60, second = 20),
        Pair(first = 160, second = 20)
    )
}
