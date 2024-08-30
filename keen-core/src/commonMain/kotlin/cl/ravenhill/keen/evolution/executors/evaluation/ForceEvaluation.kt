/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.executors.evaluation

/**
 * Enum representing the evaluation strategy in an evolutionary algorithm.
 *
 * The `ForceEvaluation` enum defines different strategies for evaluating individuals within a population in the context
 * of an evolutionary algorithm. This is particularly useful when deciding which individuals need to be evaluated based
 * on their current state, whether they are new, have changed, or whether a full re-evaluation of all individuals is
 * required.
 *
 * ## Evaluation Strategies:
 * - **ALL**: Forces the evaluation of all individuals in the population, regardless of whether they have been evaluated
 *   before. This strategy ensures that every individual is reassessed, which can be useful when there are changes in
 *   the evaluation criteria or when complete consistency is required across the population.
 * - **NEW**: Evaluates only new individuals or those that have changed since the last evaluation. This strategy is
 *   efficient when most of the population remains unchanged and only a subset of individuals needs to be evaluated.
 * - **NONE**: Skips the evaluation process entirely. This can be useful in scenarios where evaluation is optional or
 *   has already been performed by another process.
 *
 * @see EvaluationExecutor
 */
enum class ForceEvaluation {
    /**
     * Forces the evaluation of all individuals in the population, regardless of their current evaluation status.
     */
    ALL,

    /**
     * Evaluates only new or changed individuals since the last evaluation.
     */
    NEW,

    /**
     * Skips the evaluation process entirely.
     */
    NONE
}
