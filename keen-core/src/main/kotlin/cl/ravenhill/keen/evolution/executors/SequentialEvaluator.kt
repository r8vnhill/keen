/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution.executors

import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * A sequential implementation of the [EvaluationExecutor] for evaluating individuals in an evolutionary algorithm.
 *
 * The `SequentialEvaluator` evaluates the fitness of individuals in a population one by one, using a provided fitness
 * function. It is called "sequential" because it processes each individual in turn, rather than evaluating multiple
 * individuals concurrently.
 *
 * ## Usage:
 * The evaluator is typically used in the evaluation phase of an evolutionary algorithm, where the fitness of each
 * individual in the population needs to be determined.
 *
 * ### Example:
 * ```kotlin
 * // Define a fitness function
 * val fitnessFunction: (Genotype<MyType, MyGene>) -> Double = { genotype -> /* Calculate fitness */ }
 *
 * // Create a sequential evaluator
 * val evaluator = SequentialEvaluator(fitnessFunction)
 *
 * // Evaluate the population
 * val state = EvolutionState(/* ... */)
 * evaluator(state, force = false)
 * ```
 * In this example, the `SequentialEvaluator` is created with a fitness function and then used to evaluate the
 * population within an [EvolutionState]. The `force` parameter can be set to `true` to force re-evaluation of all
 * individuals, even if they have already been evaluated.
 *
 * @param T The type of data encapsulated by the genes within the individuals' genotypes.
 * @param G The type of gene in the individuals' genotypes.
 *
 * @param function A function that takes a [Genotype] and returns a [Double] representing the fitness value. This
 *   function defines how each individual's fitness is calculated.
 */
class SequentialEvaluator<T, G>(private val function: (Genotype<T, G>) -> Double) :
    EvaluationExecutor<T, G> where G : Gene<T, G> {

    /**
     * Evaluates the population of an [EvolutionState] using the provided fitness function.
     *
     * See [SequentialEvaluator]'s class documentation for an example of how to use this method.
     *
     * @param state The current state of the evolution, including the population to be evaluated.
     * @param force If `true`, forces re-evaluation of all individuals in the population. If `false`, only individuals
     *   that have not been evaluated will be assessed.
     */
    override fun invoke(state: EvolutionState<T, G>, force: Boolean) =
        state.copy(population = EvaluationExecutor.evaluateAndAddToPopulation(
            EvaluationExecutor.selectAndCreateEvaluators(state.population, function, force),
            state.population
        ) { evaluators -> evaluators.forEach { it() } })
}
