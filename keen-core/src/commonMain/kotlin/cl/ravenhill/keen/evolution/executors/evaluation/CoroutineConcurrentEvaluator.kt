/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.executors.evaluation

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.toPopulation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * A concurrent evaluator that uses Kotlin coroutines to evaluate individuals in an evolutionary algorithm.
 *
 * The `CoroutineConcurrentEvaluator` class implements the [EvaluationExecutor] interface, providing a mechanism to
 * evaluate individuals within a population concurrently using Kotlin coroutines. This approach is well-suited for
 * scenarios where evaluations are computationally expensive and can benefit from parallel execution.
 *
 * ## Usage:
 * This class is intended for use in evolutionary algorithms where concurrent evaluation of individuals is required.
 * It leverages the power of Kotlin coroutines to achieve parallelism, making it ideal for large-scale or
 * computationally intensive evaluations.
 *
 * ### Example: Using `CoroutineConcurrentEvaluator` to Evaluate a Population Concurrently
 * ```kotlin
 * val evaluationFunction: (MyRepresentation) -> Double = { representation -> // fitness calculation logic }
 * val evaluator = CoroutineConcurrentEvaluator(evaluationFunction)
 * val evaluatedState = evaluator(currentState, ForceEvaluation.NEW)
 * ```
 *
 * @param T The type of the value held by the features within the individuals.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param S The type of evolutionary state, which must extend [EvolutionState].
 * @property evaluationFunction The function used to evaluate the fitness of each individual's representation.
 * @property coroutineContext The coroutine context in which the evaluation operations are executed. Defaults to
 *   [Dispatchers.Default].
 */
class CoroutineConcurrentEvaluator<T, F, R, S>(
    private val evaluationFunction: (R) -> Double,
    private val coroutineContext: CoroutineContext = Dispatchers.Default
) : EvaluationExecutor<T, F, R, S> where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {

    /**
     * Executes the evaluation process on the given evolutionary state concurrently.
     *
     * This function evaluates the individuals in the population concurrently according to the specified
     * [ForceEvaluation] strategy. The population is updated with the newly evaluated individuals, and the state is
     * returned with this updated population.
     *
     * @param state The current evolutionary state to be evaluated.
     * @param force The evaluation strategy to use, determining which individuals are evaluated.
     * @return The updated evolutionary state after evaluation.
     */
    override suspend fun invoke(state: S, force: ForceEvaluation): S = withContext(coroutineContext) {
        val evaluators = selectAndCreateEvaluators(state.population, evaluationFunction, force)

        // Concurrently evaluate individuals
        val evaluatedIndividuals = evaluators.map { evaluator ->
            async { evaluator() }
        }.awaitAll()

        // Create the new population with the evaluated individuals
        val newPopulation = evaluateAndAddToPopulation(
            evaluatedIndividuals,
            state.population
        )

        state.makeCopy(population = newPopulation)
    }

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
         * @param evaluated The list of evaluated individuals.
         * @param population The current population of individuals.
         * @return The updated population with the evaluated individuals included.
         */
        private fun <T, F, R> evaluateAndAddToPopulation(
            evaluated: List<Individual<T, F, R>>,
            population: Population<T, F, R>
        ): Population<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {
            return if (evaluated.isNotEmpty()) {
                if (evaluated.size == population.size) {
                    evaluated
                } else {
                    population.filter { it.isEvaluated() } + evaluated
                }.toPopulation()
            } else {
                population
            }
        }
    }
}
