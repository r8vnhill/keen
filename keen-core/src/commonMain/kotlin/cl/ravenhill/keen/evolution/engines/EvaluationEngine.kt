/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines

import arrow.core.Either
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.exceptions.EvaluationException
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Interface representing an evaluation engine in an evolutionary algorithm.
 *
 * The `EvaluationEngine` interface defines the contract for engines responsible for evaluating the population within
 * an evolutionary state. Evaluation is a crucial step in evolutionary algorithms, as it determines the fitness of
 * individuals in the population based on their representations. This interface is designed to be flexible and supports
 * asynchronous operations by utilizing Kotlin's `suspend` functions, making it suitable for environments where
 * non-blocking operations are essential.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 */
interface EvaluationEngine<T, F, R, S> where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> {

    /**
     * Evaluates the population within the given evolutionary state.
     *
     * This function is responsible for evaluating the fitness of each individual in the population based on their
     * representation. The evaluation process is typically dependent on the specific problem being solved by the
     * evolutionary algorithm. The function returns the updated evolutionary state after all individuals have been
     * evaluated.
     *
     * ## Asynchronous Execution:
     * The `evaluate` function is marked as `suspend`, allowing it to be executed asynchronously. This is particularly
     * beneficial in environments where non-blocking operations are necessary, such as in Kotlin/JS or when dealing with
     * large-scale populations.
     *
     * @param state The current evolutionary state containing the population to be evaluated.
     * @return The updated evolutionary state after the evaluation process is complete.
     */
    suspend fun evaluate(state: S): Either<EvaluationException, S>
}
