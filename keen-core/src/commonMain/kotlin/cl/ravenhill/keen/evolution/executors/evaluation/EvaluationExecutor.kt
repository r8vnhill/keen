/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.executors.evaluation

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Interface for executing the evaluation process in an evolutionary algorithm.
 *
 * The `EvaluationExecutor` interface defines the contract for evaluating a population of individuals within an
 * evolutionary state. This evaluation process typically involves calculating the fitness of each individual based on
 * a provided fitness function or evaluation criteria. The result of the evaluation influences the selection, mutation,
 * and crossover processes in subsequent generations.
 *
 * ## Key Features:
 * - **Evaluation Process**: The interface supports flexible evaluation strategies, allowing the evaluation of
 *   individuals that are newly created, those that have changed, or forcing the re-evaluation of all individuals.
 * - **Force Evaluation**: The `force` parameter allows control over whether to evaluate only new or changed
 *   individuals, or to force the evaluation of the entire population, regardless of whether they have been evaluated
 *   previously.
 *
 * ## Usage:
 * This interface is intended to be implemented by classes that manage the evaluation process in an evolutionary
 * algorithm. Implementations should define how the evaluation is performed and how the results are stored or used
 * within the evolutionary state.
 *
 * ### Example: Implementing a Custom EvaluationExecutor
 * ```kotlin
 * class MyEvaluationExecutor<T, F, R, S> : EvaluationExecutor<T, F, R, S>
 *         where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {
 *
 *     override fun invoke(state: S, force: ForceEvaluation = ForceEvaluation.NEW): S {
 *         // Implement the evaluation logic here
 *         // For example, evaluate the fitness of each individual in the state
 *         state.population.forEach { individual ->
 *             if (force == ForceEvaluation.ALL || !individual.isEvaluated) {
 *                 individual.fitness = calculateFitness(individual.representation)
 *             }
 *         }
 *         return state // Return the updated state with evaluated individuals
 *     }
 *     // ... Additional methods and logic ...
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 * @return The updated evolutionary state after the evaluation process.
 */
interface EvaluationExecutor<T, F, R, S>
        where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {

    /**
     * Executes the evaluation process on the evolutionary state.
     *
     * This operator function is responsible for evaluating the population of individuals within the provided
     * evolutionary state. The `force` parameter controls the evaluation strategy:
     * - [ForceEvaluation.NEW]: Evaluate only individuals that have not been evaluated before or have changed.
     * - [ForceEvaluation.ALL]: Force the evaluation of all individuals, regardless of their current evaluation status.
     * - [ForceEvaluation.NONE]: Skip the evaluation process.
     *
     * The function returns the updated evolutionary state with individuals that have been evaluated according to the
     * specified strategy.
     *
     * @param state The current evolutionary state containing the population to be evaluated.
     * @param force The evaluation strategy to apply. Defaults to [ForceEvaluation.NEW].
     * @return The updated evolutionary state after the evaluation process is complete.
     */
    operator fun invoke(state: S, force: ForceEvaluation = ForceEvaluation.NEW): S
}
