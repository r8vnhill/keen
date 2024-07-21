/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.ga.knapsack

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.dsl.chromosomeOf
import cl.ravenhill.keen.dsl.evolutionEngine
import cl.ravenhill.keen.dsl.genotypeOf
import cl.ravenhill.keen.evolution.Evolver
import cl.ravenhill.keen.ga.knapsack.UnboundedKnapsackProblem.CHROMOSOME_SIZE
import cl.ravenhill.keen.ga.knapsack.UnboundedKnapsackProblem.MAX_GENERATIONS
import cl.ravenhill.keen.ga.knapsack.UnboundedKnapsackProblem.MAX_WEIGHT
import cl.ravenhill.keen.ga.knapsack.UnboundedKnapsackProblem.MUTATION_RATE
import cl.ravenhill.keen.ga.knapsack.UnboundedKnapsackProblem.PENALTY_MULTIPLIER
import cl.ravenhill.keen.ga.knapsack.UnboundedKnapsackProblem.POPULATION_SIZE
import cl.ravenhill.keen.ga.knapsack.UnboundedKnapsackProblem.STEADY_GENERATIONS
import cl.ravenhill.keen.ga.knapsack.UnboundedKnapsackProblem.items
import cl.ravenhill.keen.limits.maxGenerations
import cl.ravenhill.keen.limits.steadyGenerations
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.operators.alteration.crossover.UniformCrossover
import cl.ravenhill.keen.operators.alteration.mutation.RandomMutator

private typealias UnboundedKnapsackListenerFactory =
            (ListenerConfiguration<Pair<Int, Int>, KnapsackGene>) -> EvolutionListener<Pair<Int, Int>, KnapsackGene>

/**
 * Defines the unbounded knapsack problem for use in a genetic algorithm.
 *
 * This object encapsulates the configuration and setup necessary for solving the knapsack problem using evolutionary
 * computation. It includes constants that define various parameters of the problem and the genetic algorithm.
 *
 * @property MAX_WEIGHT The maximum weight that the knapsack can hold. Set to 15 by default.
 * @property PENALTY_MULTIPLIER The penalty multiplier for exceeding the maximum weight. Set to 50.0 by default.
 * @property CHROMOSOME_SIZE The number of genes in each chromosome. Set to 15 by default.
 * @property POPULATION_SIZE The number of solutions in each generation. Set to 100 by default.
 * @property MUTATION_RATE The probability of mutation occurring on an individual. Set to 0.1 by default.
 * @property MAX_GENERATIONS The maximum number of generations to evolve. Set to 100 by default.
 * @property STEADY_GENERATIONS The number of generations with no improvement in fitness after which the genetic
 *   algorithm will terminate. Set to 20 by default.
 * @property items The list of items to be used in the knapsack problem. Each item is a pair of integers, representing
 *   the value and weight of the item, respectively.
 */
object UnboundedKnapsackProblem {
    const val MAX_WEIGHT = 15
    const val PENALTY_MULTIPLIER = 50.0
    private const val CHROMOSOME_SIZE = 15
    private const val POPULATION_SIZE = 100
    private const val MUTATION_RATE = 0.1
    private const val MAX_GENERATIONS = 100
    private const val STEADY_GENERATIONS = 20

    val items = listOf(4 to 12, 2 to 1, 2 to 2, 1 to 1, 10 to 4, 0 to 0)

    /**
     * Initializes and executes the genetic algorithm for the Unbounded Knapsack Problem.
     *
     * This function sets up and runs an evolutionary computation process to solve the knapsack problem. It configures
     * the evolutionary engine with specified parameters, genetic operators, and constraints. Observers can be
     * attached to monitor the evolution process.
     *
     * @param observers A variable number of `EvolutionListener<Pair<Int, Int>, KnapsackGene>` instances that can be
     *   used to monitor the evolution process.
     */
    operator fun invoke(vararg observers: UnboundedKnapsackListenerFactory): Evolver<Pair<Int, Int>, KnapsackGene> {
        val engine = evolutionEngine(UnboundedKnapsackProblem::fitnessFunction, genotypeOf {
            chromosomeOf {
                KnapsackChromosome.Factory(CHROMOSOME_SIZE) { KnapsackGene(items.random(Domain.random)) }
            }
        }) {
            populationSize = POPULATION_SIZE
            alterers += listOf(RandomMutator(MUTATION_RATE), UniformCrossover())
            limitFactories += listOf(maxGenerations(MAX_GENERATIONS), steadyGenerations(STEADY_GENERATIONS))
            listenerFactories += observers.toList()
        }
        engine.evolve()
        return engine
    }
}
