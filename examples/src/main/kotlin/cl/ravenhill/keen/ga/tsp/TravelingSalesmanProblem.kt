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

data object TravelingSalesmanProblem {
    private const val POPULATION_SIZE = 1000

    operator fun invoke(vararg observers: EvolutionListener<Pair<Int, Int>, RoutePointGene>) {
        val engine = evolutionEngine(TravelingSalesmanProblem::fitnessFunction, genotypeOf {
            chromosomeOf {
                RouteChromosome.Factory()
            }
        }) {
            populationSize = POPULATION_SIZE
            limits += MaxGenerations(generations = 200)
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