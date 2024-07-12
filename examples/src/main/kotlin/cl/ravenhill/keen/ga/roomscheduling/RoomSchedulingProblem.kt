/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.roomscheduling

import cl.ravenhill.keen.dsl.chromosomeOf
import cl.ravenhill.keen.dsl.evolutionEngine
import cl.ravenhill.keen.dsl.genotypeOf
import cl.ravenhill.keen.dsl.integers
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import cl.ravenhill.keen.limits.MaxGenerations
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.operators.alteration.crossover.SinglePointCrossover
import cl.ravenhill.keen.operators.alteration.mutation.RandomMutator
import cl.ravenhill.keen.ranking.FitnessMinRanker

/**
 * This internal object encapsulates the evolutionary algorithm setup and execution for solving the room scheduling
 * problem. It leverages a genetic algorithm approach to find an efficient allocation of a set of meetings into a
 * limited number of rooms, aiming to minimize room usage and scheduling conflicts.
 *
 * ## Key Features:
 * - Encapsulates the evolutionary algorithm configuration and execution in a single object.
 * - Utilizes the `invoke` operator function for a concise and clear entry point to start the evolution process.
 * - Allows external observers (listeners) to be attached to monitor the evolution process.
 *
 * ## Evolutionary Algorithm Configuration:
 * - [evolutionEngine]: Configures and initiates the genetic algorithm's engine for evolving solutions.
 * - [fitnessFunction]: The function used to evaluate the fitness of each potential solution.
 * - [genotypeOf]: Sets up the genetic representation of solutions, where each solution (genotype) represents a room
 *   allocation strategy.
 * - `populationSize`: Defines the number of solutions in each generation.
 * - `ranker`: A mechanism for ranking solutions based on their fitness.
 * - `alterers`: Genetic operators, such as mutation and crossover, that modify solutions over generations.
 * - `limits`: Criteria for terminating the evolution process, such as a steady state or maximum generations.
 * - `observers`: Optional listeners for tracking and responding to the evolution process.
 *
 * ## Usage:
 * This object is designed to be invoked with a set of `EvolutionListener` objects. These listeners can be used to
 * monitor and respond to various events and states in the evolution process. The invoke operator (`operator fun
 * invoke(...)`) simplifies* the process of starting the genetic algorithm with the desired configuration.
 *
 * ## Example:
 * ```kotlin
 * RoomSchedulingProblem(myListener)
 * ```
 * Here, `myListener` is an instance of `EvolutionListener` that will monitor the evolutionary process.
 *
 * @property POPULATION_SIZE The number of solutions in each generation. Set to 100 by default.
 */
internal object RoomSchedulingProblem {
    private const val POPULATION_SIZE = 100

    operator fun invoke(vararg observers: EvolutionListener<Int, IntGene>) {
        val engine = evolutionEngine(
            ::fitnessFunction,
            genotypeOf {
                repeat(meetings.size) {
                    chromosomeOf {
                        integers {
                            size = 1
                            ranges += meetings.indices.first..meetings.indices.last
                        }
                    }
                }
            }) {
            populationSize = POPULATION_SIZE
            ranker = FitnessMinRanker()
            alterers += listOf(RandomMutator(individualRate = 0.3), SinglePointCrossover(chromosomeRate = 0.2))
            limits += listOf(SteadyGenerations(generations = 20), MaxGenerations(generations = 100))
            listeners += observers
        }
        engine.evolve()
    }
}
