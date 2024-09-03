/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.executors.evaluation

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.toPopulation

/**
 * A sequential evaluator for evaluating individuals in an evolutionary algorithm.
 *
 * The `SequentialEvaluator` class implements the [EvaluationExecutor] interface, providing a mechanism to evaluate
 * individuals within a population in a sequential manner. It applies a given evaluation function to each individual
 * based on the specified [ForceEvaluation] strategy. This class is suitable for scenarios where evaluations need to be
 * performed in a single-threaded, step-by-step fashion.
 *
 * ## Usage:
 * This class is intended to be used in evolutionary algorithms where the evaluation of individuals is required. It
 * allows for flexible control over which individuals are evaluated through the [ForceEvaluation] parameter.
 *
 * ### Example: Using `SequentialEvaluator` to Evaluate a Population
 * ```kotlin
 * val evaluationFunction: (MyRepresentation) -> Double = { representation -> // fitness calculation logic }
 * val evaluator = SequentialEvaluator(evaluationFunction)
 * val evaluatedState = evaluator(currentState, ForceEvaluation.NEW)
 * ```
 *
 * @param T The type of the value held by the features within the individuals.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param S The type of evolutionary state, which must extend [EvolutionState].
 * @property evaluationFunction The function used to evaluate the fitness of each individual's representation.
 */
class SequentialEvaluator<T, F, R, S>(
    private val evaluationFunction: (R) -> Double
) : EvaluationExecutor<T, F, R, S> where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {

    /**
     * Executes the evaluation process on the given evolutionary state.
     *
     * This function evaluates the individuals in the population according to the specified [ForceEvaluation] strategy.
     * The population is updated with the newly evaluated individuals, and the state is returned with this updated
     * population.
     *
     * @param state The current evolutionary state to be evaluated.
     * @param force The evaluation strategy to use, determining which individuals are evaluated.
     * @return The updated evolutionary state after evaluation.
     */
    override suspend fun invoke(state: S, force: ForceEvaluation): S = state.makeCopy(
        population = evaluateAndAddToPopulation(
            selectAndCreateEvaluators(state.population, evaluationFunction, force),
            state.population
        ) { evaluators -> evaluators.forEach { evaluator -> evaluator() } }
    )

    companion object {

        /**
         * Selects individuals from the population based on the given [ForceEvaluation] strategy and creates
         * corresponding evaluators for them.
         *
         * @param population The population from which individuals are selected.
         * @param function The evaluation function to be applied to each individual's representation.
         * @param force The evaluation strategy used to determine which individuals are selected.
         * @return A list of [IndividualEvaluator]s for the selected individuals.
         */
        private fun <T, F, R> selectAndCreateEvaluators(
            population: Population<T, F, R>,
            function: (R) -> Double,
            force: ForceEvaluation
        ): List<IndividualEvaluator<T, F, R>> where F : Feature<T, F>, R : Representation<T, F> {
            val individuals = when (force) {
                ForceEvaluation.ALL -> population
                ForceEvaluation.NEW -> population.filterNot { it.isEvaluated() }
                ForceEvaluation.NONE -> emptyList()
            }
            return individuals.map { IndividualEvaluator(it, function) }
        }

        /**
         * Evaluates the selected individuals and adds them to the population.
         *
         * @param toEvaluate The list of evaluators for the individuals to be evaluated.
         * @param population The current population of individuals.
         * @param evaluationStrategy The strategy used to perform the evaluation of individuals.
         * @return The updated population with the evaluated individuals included.
         */
        private fun <T, F, R> evaluateAndAddToPopulation(
            toEvaluate: List<IndividualEvaluator<T, F, R>>,
            population: Population<T, F, R>,
            evaluationStrategy: (List<IndividualEvaluator<T, F, R>>) -> Unit
        ): Population<T, F, R> where F : Feature<T, F>, R : Representation<T, F> = if (toEvaluate.isNotEmpty()) {
            evaluationStrategy(toEvaluate)
            if (toEvaluate.size == population.size) {
                toEvaluate.map { it.individual }
            } else {
                population.filter { it.isEvaluated() } + toEvaluate.map { it.individual }
            }.toPopulation()
        } else {
            population
        }
    }
}
