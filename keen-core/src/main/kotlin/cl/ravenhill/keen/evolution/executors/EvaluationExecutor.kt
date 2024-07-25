/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution.executors

import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.features.Representation
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.Population


/**
 * Interface for executing fitness evaluations in an evolutionary algorithm.
 *
 * The `EvaluationExecutor` interface defines the contract for executing fitness evaluations on a population within
 * an evolutionary algorithm. It extends the `KeenExecutor` interface and provides methods for invoking the evaluation
 * process and handling the creation and execution of individual evaluators.
 *
 * ## Usage:
 * Implement this interface to define custom evaluation strategies for evolutionary algorithms.
 *
 * ### Example:
 * ```
 * class MyEvaluationExecutor<T, F : Feature<T, F>> : EvaluationExecutor<T, F> {
 *     override fun invoke(state: EvolutionState<T, F>, force: Boolean): EvolutionState<T, F> {
 *         // Custom evaluation logic
 *     }
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 */
interface EvaluationExecutor<T, F> : KeenExecutor where F : Feature<T, F> {

    /**
     * Invokes the evaluation process on the given evolution state.
     *
     * @param state The current evolution state to be evaluated.
     * @param force Whether to force re-evaluation of already evaluated individuals.
     * @return The updated evolution state with evaluated fitness values.
     */
    operator fun invoke(state: EvolutionState<T, F>, force: Boolean = false): EvolutionState<T, F>

    companion object {

        /**
         * Selects and creates individual evaluators for the given population.
         *
         * @param population The population to be evaluated.
         * @param function The fitness function used to evaluate the individual's representation.
         * @param force Whether to force re-evaluation of already evaluated individuals.
         * @return A list of individual evaluators.
         */
        internal fun <T, F, R> selectAndCreateEvaluators(
            population: Population<T, F, R>, function: (R) -> Double, force: Boolean = false,
        ) where F : Feature<T, F>, R : Representation<T, F> = if (force) {
            population
        } else {
            population.filterNot { it.isEvaluated() }
        }.map { IndividualEvaluator(it, function) }

        /**
         * Evaluates the individuals and adds them to the population.
         *
         * @param toEvaluate The list of individual evaluators to be evaluated.
         * @param population The current population.
         * @param evaluationStrategy The strategy used to evaluate the individuals.
         * @return The updated population with evaluated fitness values.
         */
        internal fun <T, F, R> evaluateAndAddToPopulation(
            toEvaluate: List<IndividualEvaluator<T, F, R>>,
            population: Population<T, F, R>,
            evaluationStrategy: (List<IndividualEvaluator<T, F, R>>) -> Unit,
        ) where F : Feature<T, F>, R : Representation<T, F> = if (toEvaluate.isNotEmpty()) {
            evaluationStrategy(toEvaluate)
            // Handling population update based on evaluation results
            if (toEvaluate.size == population.size) {
                toEvaluate.map { it.individual }
            } else {
                population.filter { it.isEvaluated() }.toMutableList().apply {
                    addAll(toEvaluate.map { it.individual })
                }
            }
        } else {
            population
        }
    }

    /**
     * Factory class for creating `EvaluationExecutor` instances.
     *
     * The `Factory` class provides a way to create `EvaluationExecutor` instances using a specified fitness function.
     *
     * @param T The type of the value held by the features.
     * @param F The type of the feature, which must extend [Feature].
     */
    open class Factory<T, F, R> : KeenExecutor.Factory<(R) -> Double, EvaluationExecutor<T, F>>
            where F : Feature<T, F>, R : Representation<T, F> {

        /**
         * The creator function for producing `EvaluationExecutor` instances.
         */
        override lateinit var creator: ((R) -> Double) -> EvaluationExecutor<T, F>
    }
}

/**
 * Evaluates the fitness of an individual in an evolutionary algorithm.
 *
 * The `IndividualEvaluator` class is responsible for evaluating the fitness of an individual using a provided
 * fitness function. It encapsulates the logic for applying the fitness function to the individual's representation and
 * storing the resulting fitness value.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param individual The individual to be evaluated.
 * @param fitnessFunction The function used to evaluate the fitness of the individual's representation.
 * @constructor Creates an instance of `IndividualEvaluator` with the specified individual and fitness function.
 */
internal class IndividualEvaluator<T, F, R>(
    individual: Individual<T, F, R>,
    private val fitnessFunction: (R) -> Double,
) where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * The fitness value of the individual, initially set to `Double.NaN`.
     */
    private var fitness = Double.NaN

    /**
     * The individual to be evaluated.
     */
    private val _individual = individual

    /**
     * Gets a copy of the individual with the evaluated fitness value.
     */
    val individual: Individual<T, F, R>
        get() = _individual.copy(fitness = fitness)

    /**
     * Invokes the fitness evaluation function and updates the fitness value.
     */
    operator fun invoke() {
        fitness = fitnessFunction(_individual.representation)
    }
}
