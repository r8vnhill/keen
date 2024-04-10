/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.ga.knapsack

import cl.ravenhill.keen.dsl.booleans
import cl.ravenhill.keen.dsl.chromosomeOf
import cl.ravenhill.keen.dsl.evolutionEngine
import cl.ravenhill.keen.dsl.genotypeOf
import cl.ravenhill.keen.ga.knapsack.ZeroOneKnapsackProblem.MAX_GENERATIONS
import cl.ravenhill.keen.ga.knapsack.ZeroOneKnapsackProblem.MAX_WEIGHT
import cl.ravenhill.keen.ga.knapsack.ZeroOneKnapsackProblem.POPULATION_SIZE
import cl.ravenhill.keen.ga.knapsack.ZeroOneKnapsackProblem.TRUE_RATE
import cl.ravenhill.keen.ga.knapsack.ZeroOneKnapsackProblem.items
import cl.ravenhill.keen.genetic.genes.BooleanGene
import cl.ravenhill.keen.limits.MaxGenerations
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.operators.alteration.crossover.SinglePointCrossover
import cl.ravenhill.keen.operators.alteration.mutation.BitFlipMutator


/**
 * Defines the zero-one knapsack problem for use in a genetic algorithm.
 *
 * This object encapsulates the configuration and setup necessary for solving the zero-one knapsack problem using
 * evolutionary computation. It includes constants that define various parameters of the problem and the genetic
 * algorithm.
 *
 * ### Usage:
 * This object is used to solve instances of the zero-one knapsack problem by configuring and running a genetic
 * algorithm. Observers can be attached to monitor the evolutionary process.
 *
 * ### Example:
 * ```kotlin
 * ZeroOneKnapsackProblem.invoke(myListOfObservers)
 * ```
 * In this example, `ZeroOneKnapsackProblem` is invoked with a list of observers to start the evolutionary process for
 * solving the problem.
 *
 * @property MAX_WEIGHT The maximum weight that the knapsack can hold, set to 30.
 * @property items The list of items to be used in the knapsack problem. Each item is a pair of integers, representing
 *   the value and weight of the item, respectively.
 * @property TRUE_RATE The rate at which Boolean genes are initialized to `true`, set to 0.5 by default.
 * @property POPULATION_SIZE The number of solutions in each generation, set to 50.
 * @property MAX_GENERATIONS The maximum number of generations to evolve, set to 100.
 */
object ZeroOneKnapsackProblem {
    const val MAX_WEIGHT = 30

    val items = listOf(4 to 12, 2 to 1, 2 to 2, 1 to 1, 10 to 4, 2 to 2, 1 to 2, 2 to 1, 5 to 15, 5 to 10)

    private const val TRUE_RATE = 0.5
    private const val POPULATION_SIZE = 50
    private const val MAX_GENERATIONS = 100

    /**
     * Initializes and executes the genetic algorithm for the Zero-One Knapsack Problem.
     *
     * This function sets up and runs an evolutionary computation process to solve the zero-one knapsack problem. It
     * configures the evolutionary engine with specified parameters, genetic operators, and constraints. Observers can
     * be attached to monitor the evolution process.
     *
     * ## Process:
     * - Initializes the evolutionary engine using a custom fitness function and genetic structure.
     * - Configures genetic operators such as BitFlipMutator and SinglePointCrossover.
     * - Sets population size, and evolution limits like the maximum number of generations.
     * - Attaches provided observers to the evolution process.
     * - Executes the evolutionary algorithm by calling `engine.evolve()`.
     *
     * ### Usage:
     * Call this function to start the evolutionary process for solving the Zero-One Knapsack Problem. Attach observers
     * as needed to monitor or intervene in the evolution process.
     *
     * @param observers A list of `EvolutionListener<Boolean, BooleanGene>` instances to monitor the evolution process.
     */
    operator fun invoke(vararg observers: EvolutionListener<Boolean, BooleanGene>) {
        val engine = evolutionEngine(ZeroOneKnapsackProblem::fitnessFunction, genotypeOf {
            chromosomeOf {
                booleans {
                    size = items.size
                    trueRate = TRUE_RATE
                }
            }
        }) {
            populationSize = POPULATION_SIZE
            alterers += listOf(BitFlipMutator(individualRate = 0.1), SinglePointCrossover(chromosomeRate = 0.1))
            limits += listOf(MaxGenerations(MAX_GENERATIONS))
            listeners += observers
        }
        engine.evolve()
    }
}
