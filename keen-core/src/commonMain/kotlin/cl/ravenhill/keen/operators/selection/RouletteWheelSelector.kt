/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selection

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.exceptions.SelectionException
import cl.ravenhill.keen.fitness
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.utils.SortingStrategy
import cl.ravenhill.keen.utils.sub
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlin.math.min

/**
 * Typealias for a [RouletteWheelSelector], representing the fitness-proportionate selection strategy.
 *
 * The `FitnessProportionateSelector` is a typealias for the `RouletteWheelSelector`, which implements the
 * fitness-proportionate selection strategy, commonly known as roulette wheel selection, in evolutionary algorithms.
 * This strategy selects individuals based on their relative fitness within the population, with higher fitness
 * individuals having a greater probability of being selected.
 *
 * @param T The type of value held by the features.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 */
typealias FitnessProportionateSelector<T, F, R> = RouletteWheelSelector<T, F, R>

/**
 * A selector implementing the roulette wheel selection mechanism in an evolutionary algorithm.
 *
 * The `RouletteWheelSelector` class provides a method to select individuals from a population based on their fitness
 * values using the roulette wheel (also known as fitness-proportionate) selection strategy. This method is inspired by
 * the idea of a roulette wheel where the probability of selecting an individual is proportional to its fitness relative
 * to the rest of the population. The higher an individual's fitness, the greater its chance of being selected.
 *
 * ## Theoretical Framework:
 * Roulette wheel selection is a common technique used in genetic algorithms and other evolutionary computation methods
 * to maintain diversity in the population while favoring individuals with higher fitness. It is part of the selection
 * phase, which is critical for guiding the evolutionary process toward optimal solutions.
 *
 * ### Key Concepts:
 * - **Fitness Proportionate Selection**: The probability of selecting an individual is directly proportional to its
 *   fitness. Individuals with higher fitness have a higher probability of being selected.
 * - **Diversity Maintenance**: This method ensures that even individuals with lower fitness have a chance of being
 *   selected, which helps to maintain genetic diversity in the population and prevents premature convergence.
 * - **Sorting Strategy**: The population can be sorted according to their fitness values before selection, which can
 *   influence the efficiency and outcome of the selection process. The `RouletteWheelSelector` supports three sorting
 *   strategies: `ASCENDING`, `DESCENDING`, and `UNSORTED`.
 *
 * ## Usage:
 * The recommended way to use this selector is through its [invoke] operator, which is designed to be intuitive and
 * straightforward for most use cases. The [select] method is also public but is intended primarily for fine-tuning new
 * algorithms or for scenarios where more control over the selection process is needed. Directly using `select` should
 * be reserved for these specific contexts.
 *
 * ### Example: Using `RouletteWheelSelector`
 * ```kotlin
 * val selector = RouletteWheelSelector<Int, MyFeature, MyRepresentation>(sorted = SortingStrategy.ASCENDING)
 * val selectedIndividuals = selector(population, count = 10, ranker = myRanker)
 * println(selectedIndividuals)
 * ```
 *
 * @param T The type of value held by the features.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param sorted The sorting strategy to apply before selection. Default is [SortingStrategy.UNSORTED].
 */
class RouletteWheelSelector<T, F : Feature<T, F>, R : Representation<T, F>>(
    private val sorted: SortingStrategy = SortingStrategy.UNSORTED
) : Selector<T, F, R> {

    /**
     * Selects individuals from the population based on the roulette wheel selection mechanism.
     *
     * The `select` method selects a specified number of individuals from the population based on their fitness values.
     * The selection process can be influenced by the sorting strategy provided during initialization.
     *
     * **Note**: This method is public to allow fine-tuning in the development of new algorithms and should be used
     * primarily in that context. The recommended way to use this selector is through its [invoke] operator.
     *
     * @param population The current population of individuals from which to select.
     * @param count The number of individuals to select.
     * @param ranker The ranker used to evaluate and rank individuals within the population.
     * @return A list of selected individuals wrapped in an [Either] type, with a [SelectionException] on failure.
     * @throws SelectionException if the population is empty or if the selection process encounters an error.
     */
    override suspend fun select(
        population: Population<T, F, R>,
        count: Int,
        ranker: IndividualRanker<T, F, R>
    ): Either<SelectionException, Population<T, F, R>> = withContext(Domain.dispatcher) {
        try {
            sortPopulationIfNeeded(population, ranker)
            val probabilitiesDeferred = async {
                calculateProbabilities(population, ranker)
            }
            val probabilities = probabilitiesDeferred.await()
            val selectedIndividuals = coroutineScope {
                (1..count).map {
                    async {
                        val r = Domain.random.nextDouble()
                        val index = probabilities.indexOfFirst { prob -> prob > r }.takeIf { it != -1 }
                            ?: (probabilities.size - 1)
                        population[index]
                    }
                }.awaitAll()
            }
            selectedIndividuals.right()
        } catch (e: CompositeException) {
            SelectionException("Failed to select individuals from the population", e).left()
        }
    }

    /**
     * Sorts the population if needed, based on the selected sorting strategy.
     *
     * @param population The population to be sorted.
     * @param ranker The ranker used to evaluate and rank individuals within the population.
     */
    private suspend fun sortPopulationIfNeeded(population: Population<T, F, R>, ranker: IndividualRanker<T, F, R>) {
        withContext(Domain.dispatcher) {
            when (sorted) {
                SortingStrategy.ASCENDING -> ranker.sort(population)
                SortingStrategy.DESCENDING -> ranker.sort(population, sortOrder = SortingStrategy.DESCENDING)
                SortingStrategy.UNSORTED -> Unit
            }
        }
    }

    /**
     * Calculates the selection probabilities for each individual in the population.
     *
     * This method adjusts the fitness values to ensure they are positive and computes the probability for each
     * individual based on their fitness relative to the population.
     *
     * @param population The population of individuals for which probabilities are calculated.
     * @param ranker The ranker used to evaluate and rank individuals.
     * @return A list of selection probabilities corresponding to each individual in the population.
     */
    private fun calculateProbabilities(
        population: Population<T, F, R>,
        ranker: IndividualRanker<T, F, R>
    ): List<Double> {
        // Adjust fitness values to ensure they're positive.
        val adjustedFitness = ranker.fitnessTransform(population.fitness).let {
            it sub min(it.minOrNull() ?: 0.0, 0.0)
        }

        val totalFitness = adjustedFitness.sum()

        // Handle edge cases where total fitness is zero or invalid.
        if (totalFitness <= 0.0) {
            return List(population.size) { 1.0 / population.size }
        }

        return adjustedFitness.map { it / totalFitness }
    }
}
