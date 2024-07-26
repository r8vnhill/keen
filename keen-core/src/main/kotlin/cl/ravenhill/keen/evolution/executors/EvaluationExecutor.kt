/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution.executors

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.repr.Representation


/**
 * An executor for evaluating the fitness of a population in an evolutionary algorithm.
 *
 * The `EvaluationExecutor` interface defines the contract for executing the fitness evaluation process on a population
 * of individuals within an evolutionary state. It extends the [KeenExecutor] interface and provides methods for
 * selecting and creating evaluators, as well as for evaluating and updating the population.
 *
 * ## Recommendation:
 * It is recommended to use the provided [Factory] class to create instances of `EvaluationExecutor`. The factory
 * ensures that the executors are correctly configured and simplifies the creation process.
 *
 * ### Example:
 * Here's how to extend the `EvaluationExecutor` interface to create a custom executor:
 * ```kotlin
 * class MyEvaluationExecutor<T, F, R>(
 *     private val fitnessFunction: (R) -> Double
 * ) : EvaluationExecutor<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {
 *
 *     override fun invoke(state: EvolutionState<T, F, R>, force: Boolean): EvolutionState<T, F, R> {
 *         ...
 *     }
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 */
interface EvaluationExecutor<T, F, R> : KeenExecutor where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * Executes the fitness evaluation process on the given evolutionary state.
     *
     * @param state The current evolutionary state.
     * @param force Whether to force re-evaluation of already evaluated individuals.
     * @return The updated evolutionary state with evaluated fitness values.
     */
    operator fun invoke(state: EvolutionState<T, F, R>, force: Boolean = false): EvolutionState<T, F, R>

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
     * It ensures that the executors are properly configured and simplifies the creation process.
     *
     * ### Example:
     * ```kotlin
     * class MyEvaluationExecutor<T, F, R>(
     *     private val fitnessFunction: (R) -> Double
     * ) : EvaluationExecutor<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {
     *
     *     override fun invoke(state: EvolutionState<T, F, R>, force: Boolean): EvolutionState<T, F, R> {
     *         ...
     *     }
     * }
     *
     * val evaluatorFactory = EvaluationExecutor.Factory<MyType, MyFeature, MyRepresentation>()
     * evaluatorFactory.creator = { fitnessFunction -> MyEvaluationExecutor(fitnessFunction) }
     * val evaluator = evaluatorFactory.creator(fitnessFunction)
     * ```
     *
     * @param T The type of the value held by the features.
     * @param F The type of the feature, which must extend [Feature].
     * @param R The type of the representation, which must extend [Representation].
     */
    open class Factory<T, F, R> : KeenExecutor.Factory<(R) -> Double, EvaluationExecutor<T, F, R>>
            where F : Feature<T, F>, R : Representation<T, F> {

        /**
         * The creator function for producing `EvaluationExecutor` instances.
         */
        override lateinit var creator: ((R) -> Double) -> EvaluationExecutor<T, F, R>
    }
}


/**
 * Evaluates the fitness of an individual in the evolutionary algorithm.
 *
 * The `IndividualEvaluator` class is responsible for evaluating the fitness of a given individual using the provided
 * fitness function. It stores the evaluated fitness value and allows access to the individual with its updated fitness.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @property fitnessFunction The function used to evaluate the fitness of the individual's representation.
 * @constructor Creates an instance of `IndividualEvaluator` with the specified individual and fitness function.
 */
internal class IndividualEvaluator<T, F, R>(
    individual: Individual<T, F, R>,
    private val fitnessFunction: (R) -> Double,
) where F : Feature<T, F>, R : Representation<T, F> {

    // Holds the evaluated fitness value.
    private var fitness = Double.NaN

    // Stores the original individual.
    private val _individual = individual

    /**
     * Gets the individual with the updated fitness value.
     */
    val individual: Individual<T, F, R>
        get() = _individual.copy(fitness = fitness)

    /**
     * Evaluates the fitness of the individual's representation using the fitness function.
     */
    operator fun invoke() {
        fitness = fitnessFunction(_individual.representation)
    }
}
