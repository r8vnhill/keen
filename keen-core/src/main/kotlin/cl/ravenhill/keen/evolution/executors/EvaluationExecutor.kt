/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution.executors

import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * An interface defining an executor responsible for evaluating the fitness of individuals in an evolutionary algorithm.
 *
 * `EvaluationExecutor` is a key component in evolutionary algorithms, responsible for assessing the fitness of each
 * individual in a population. The fitness evaluation is a crucial step, as it determines how well each individual
 * performs in the given environment or problem context.
 *
 * ## Key Concepts:
 * - **Fitness Evaluation**: The primary function of this executor is to evaluate and assign fitness scores to
 *   individuals based on their genotypes.
 * - **Optional Force Evaluation**: The executor can optionally force re-evaluation of individuals, which can be
 *   useful in dynamic environments where fitness might change over time or after certain operations like mutation.
 *
 * ## Usage:
 * Implement this interface to define the logic for evaluating the fitness of individuals in a specific
 * problem domain. The executor is typically used in each generation of the evolutionary process to assess
 * and update the fitness scores of the population.
 *
 * ### Example:
 * Implementing a fitness evaluation executor for a specific problem:
 * ```kotlin
 * class MyEvaluationExecutor : EvaluationExecutor<MyDataType, MyGene> {
 *     override fun invoke(state: EvolutionState<MyDataType, MyGene>, force: Boolean) {
 *         // Fitness evaluation logic for the state's population
 *     }
 * }
 *
 * // Factory for creating instances of MyEvaluationExecutor
 * class MyEvaluationExecutorFactory : EvaluationExecutor.Factory<MyDataType, MyGene> {
 *     override var creator: ((Genotype<MyDataType, MyGene>) -> Double) -> EvaluationExecutor<MyDataType, MyGene> =
 *         { fitnessFunction -> MyEvaluationExecutor(fitnessFunction) }
 * }
 *
 * // Usage in an evolutionary algorithm
 * val executorFactory = MyEvaluationExecutorFactory()
 * val fitnessFunction = { genotype: Genotype<MyDataType, MyGene> -> /* Calculate fitness */ }
 * val executor = executorFactory.creator(fitnessFunction)
 * executor(currentState)
 * ```
 * In this example, `MyEvaluationExecutor` defines the fitness evaluation logic, while `MyEvaluationExecutorFactory`
 * provides a way to create instances of the executor.
 *
 * @param T The type of data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 */
interface EvaluationExecutor<T, G> : KeenExecutor where G : Gene<T, G> {

    /**
     * Evaluates the fitness of each individual in the given [GeneticEvolutionState].
     *
     * See [EvaluationExecutor] for more information.
     *
     * @param state The current state of the evolution process, including the population to be evaluated.
     * @param force A flag indicating whether to force re-evaluation of fitness. Useful in dynamic environments.
     * @return The updated population with the fitness scores of each individual updated.
     */
    operator fun invoke(state: GeneticEvolutionState<T, G>, force: Boolean = false): GeneticEvolutionState<T, G>

    companion object {
        /**
         * Constructs evaluators for a subset of individuals in a population based on their evaluation needs and a
         * fitness function.
         *
         * This method is designed to efficiently select individuals from a population that require fitness evaluation
         * and then create `IndividualEvaluator` instances for each. The selection of individuals can be controlled by
         * the `force` parameter. If `force` is true, all individuals in the population are selected for re-evaluation.
         * Otherwise, only those individuals whose fitness has not been previously evaluated (i.e., fitness is NaN) are
         * selected.
         *
         * @param T The type of data encapsulated by the genes in the individuals' genotypes.
         * @param G The type of gene in the individuals' genotypes.
         * @param population The population from which individuals are selected for evaluation.
         * @param function The fitness function used to evaluate the genotypes of the individuals. It takes a [Genotype]
         *   as input and returns a [Double] representing the fitness score.
         * @param force A boolean flag that, when set to true, forces the re-evaluation of all individuals in the
         *   population. If false, only individuals that have not been evaluated (fitness is NaN) will be selected for
         *   evaluation.
         *
         * @return A list of `IndividualEvaluator` instances, each corresponding to an individual selected for
         * evaluation. These evaluators can then be used to compute and update the fitness scores of their respective
         * individuals.
         */
        internal fun <T, G> selectAndCreateEvaluators(
            population: Population<T, G>, function: (Genotype<T, G>) -> Double, force: Boolean = false,
        ) where G : Gene<T, G> = if (force) {
            population
        } else {
            population.filterNot { it.isEvaluated() }
        }.map { IndividualEvaluator(it, function) }


        /**
         * Evaluates a subset of individuals and integrates them into the existing population.
         *
         * This function is designed to handle the evaluation of individuals in an evolutionary algorithm.
         * It takes a list of `IndividualEvaluator` instances, each associated with an individual that needs to be
         * evaluated, and a strategy for performing the evaluations. After evaluation, the updated individuals are
         * integrated back into the population.
         *
         * ## Behavior:
         * - If [toEvaluate] is not empty, the method executes the [evaluationStrategy] on the [toEvaluate] list.
         * - If all individuals in `population` are evaluated, the method returns a new population consisting of the
         *   evaluated individuals.
         * - If only a subset of the population is evaluated, the evaluated individuals are added back into the original
         *   population, maintaining those that were already evaluated.
         * - If [toEvaluate] is empty, indicating no individuals require evaluation, the original population is returned
         *   unchanged.
         *
         * @param T The type of data encapsulated by the genes in the individuals' genotypes.
         * @param G The type of gene in the individuals' genotypes.
         * @param toEvaluate A list of [IndividualEvaluator]<[T], [G]> instances, each corresponding to an individual
         *   that requires fitness evaluation.
         * @param population The current population of individuals.
         * @param evaluationStrategy A function that defines how the list of individuals is to be evaluated. This
         *   strategy takes a list of [IndividualEvaluator]<[T], [G]> and applies the necessary evaluations.
         */
        internal fun <T, G> evaluateAndAddToPopulation(
            toEvaluate: List<IndividualEvaluator<T, G>>,
            population: Population<T, G>,
            evaluationStrategy: (List<IndividualEvaluator<T, G>>) -> Unit,
        ) where G : Gene<T, G> = if (toEvaluate.isNotEmpty()) {
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
     * Factory interface for creating [EvaluationExecutor] instances.
     *
     * The factory allows for the dynamic creation of evaluation executors, with customizable fitness
     * evaluation functions.
     *
     * See [EvaluationExecutor] for more information.
     *
     * @param T The type of data encapsulated by the genes within the individuals.
     * @param G The type of gene in the individuals, conforming to the [Gene] interface.
     */
    open class Factory<T, G> :
        KeenExecutor.Factory<(Genotype<T, G>) -> Double, EvaluationExecutor<T, G>> where G : Gene<T, G> {

        /**
         * A creator function for constructing [EvaluationExecutor] instances.
         *
         * @property creator A lambda function that takes a fitness evaluation function and returns an instance
         *                   of [EvaluationExecutor].
         */
        override lateinit var creator: ((Genotype<T, G>) -> Double) -> EvaluationExecutor<T, G>

        override fun toString() = "EvaluationExecutor.Factory(creator=$creator)"

        override fun equals(other: Any?) = when {
            this === other -> true
            other !is Factory<*, *> -> false
            creator != other.creator -> false
            else -> true
        }
    }
}

/**
 * A class dedicated to evaluating the fitness of an individual in an evolutionary algorithm.
 *
 * `IndividualEvaluator` encapsulates the logic for calculating the fitness of a single individual. It uses a
 * specified fitness function that operates on the individual's genotype to determine its fitness score.
 *
 * @param T The type of data encapsulated by the genes in the individual's genotype.
 * @param G The type of gene in the individual's genotype.
 * @param individual The individual whose fitness is to be evaluated.
 * @param fitnessFunction The function used to calculate the fitness of the individual.
 */
internal class IndividualEvaluator<T, G>(
    individual: Individual<T, G>,
    private val fitnessFunction: (Genotype<T, G>) -> Double,
) where G : Gene<T, G> {

    private var fitness = Double.NaN

    private val _individual = individual

    /**
     * Returns a copy of the individual with updated fitness.
     *
     * @return The individual with updated fitness.
     */
    val individual: Individual<T, G>
        get() = _individual.copy(fitness = fitness)

    /**
     * Invokes the fitness evaluation process.
     *
     * When this method is called, it applies the fitness function to the individual's genotype to calculate
     * the fitness score. This score is then assigned to the individual's fitness property.
     */
    operator fun invoke() {
        fitness = fitnessFunction(_individual.genotype)
    }
}
