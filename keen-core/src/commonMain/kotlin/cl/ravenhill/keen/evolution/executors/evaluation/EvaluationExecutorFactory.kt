/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.executors.evaluation

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Factory class for creating instances of [EvaluationExecutor] in evolutionary algorithms.
 *
 * The `EvaluationExecutorFactory` class is responsible for providing a way to create instances of `EvaluationExecutor`
 * that can evaluate individuals within a population in an evolutionary algorithm. This factory allows for flexibility
 * in choosing the evaluation strategy by configuring the `creator` function.
 *
 * ## Usage:
 * The factory uses a [creator] function to generate `EvaluationExecutor` instances based on the provided evaluation
 * function. By default, it creates a [CoroutineConcurrentEvaluator], which evaluates individuals concurrently using
 * Kotlin coroutines. This setup is ideal for environments where concurrent processing can improve performance.
 *
 * ### Example: Creating an Evaluation Executor
 * ```kotlin
 * val factory = EvaluationExecutorFactory<MyType, MyFeature, MyRepresentation, MyState>()
 * val evaluationExecutor = factory.creator { representation ->
 *     // Custom evaluation logic for the representation
 *     evaluateFitness(representation)
 * }
 * ```
 *
 * In this example, the factory is used to create an `EvaluationExecutor` with a custom evaluation function. The
 * resulting executor can then be used within the evolutionary algorithm to evaluate the fitness of individuals.
 *
 * @param T The type of value held by the features.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param S The type of evolutionary state, which must extend [EvolutionState].
 * @property creator A function that takes an evaluation function `(R) -> Double` and returns an [EvaluationExecutor]
 *   capable of evaluating individuals in the evolutionary process.
 */
class EvaluationExecutorFactory<T, F, R, S> where F : Feature<T, F>,
                                                  R : Representation<T, F>,
                                                  S : EvolutionState<T, F, R, S> {
    /**
     * A function that creates an [EvaluationExecutor] based on the provided evaluation function.
     *
     * By default, this function creates a `CoroutineConcurrentEvaluator` which evaluates individuals concurrently.
     */
    val creator: ((R) -> Double) -> EvaluationExecutor<T, F, R, S> = {
        CoroutineConcurrentEvaluator(it)
    }
}
