/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.selection

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.fitness
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.utils.eq
import cl.ravenhill.keen.utils.incremental
import cl.ravenhill.keen.utils.sub
import kotlin.math.min


/**
 * A selector implementation based on the Roulette Wheel selection method in evolutionary algorithms.
 *
 * `RouletteWheelSelector` simulates a roulette wheel where the probability of an individual being selected is
 * proportional to its fitness. Individuals with higher fitness have a greater chance of being selected.
 *
 * ## Process:
 * 1. **Fitness Adjustment**: Adjusts the fitness values of the population to ensure they are all positive.
 * 2. **Total Fitness Calculation**: Computes the total fitness of the adjusted population.
 * 3. **Probability Calculation**: Assigns selection probabilities to each individual based on their proportion
 *   of the total fitness.
 * 4. **Fallback for Invalid Total Fitness**: In cases where the total fitness is not a valid number (NaN, Infinite,
 *   or Zero), the selection probability is uniformly distributed across the population, making this equivalent to
 *   a [RandomSelector].
 *
 * ## Usage:
 * This selector is typically used in genetic algorithms where it's essential to favor individuals with higher
 * fitness but still give a chance to less fit individuals.
 *
 * ### Example:
 * ```kotlin
 * val state = EvolutionState<MyDataType, MyGene>(/* ... */)
 * val rouletteSelector = RouletteWheelSelector<MyDataType, MyGene>(sorted = false)
 * val selected = rouletteSelector(state, 5)
 * ```
 * In this example, `RouletteWheelSelector` is used to select 5 individuals from the population, with the probability
 * of each individual being selected proportional to its fitness.
 *
 * @param T The type of data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 * @param sorted Specifies whether the population should be sorted before applying the selection. Some variations of
 *   roulette wheel selection might require a sorted population based on fitness.
 * @property sorted A boolean indicating whether the population should be sorted based on the ranking before
 *   selection. This is relevant for certain variations of roulette wheel selection.
 */
class RouletteWheelSelector<T, G>(val sorted: Boolean = false) : Selector<T, G> where G : Gene<T, G> {

    /**
     * Calculates selection probabilities for each individual in the population based on their fitness.
     *
     * This method adjusts the fitness values of individuals in the population to ensure they are positive and then
     * calculates the probability of each individual being selected. The probability is proportional to the individual's
     * share of the total fitness of the population.
     *
     * ## Process:
     * 1. **Fitness Adjustment**: Converts all fitness values to positive numbers by subtracting the minimum fitness
     *   value from all fitness values, if necessary.
     * 2. **Total Fitness Calculation**: Computes the sum of the adjusted fitness values.
     * 3. **Probability Assignment**: Assigns each individual a selection probability based on their proportion of the
     *    total fitness. If the total fitness is invalid (NaN, Infinite, or Zero), a uniform probability is assigned
     *    to all individuals.
     *
     * ## Usage:
     * This method is typically used internally by the `RouletteWheelSelector` to calculate selection probabilities
     * before performing the actual selection.
     *
     * ### Example:
     * ```kotlin
     * val population: Population<MyDataType, MyGene> = /* ... */
     * val ranker: IndividualRanker<MyDataType, MyGene> = /* ... */
     * val selector = RouletteWheelSelector<MyDataType, MyGene>(sorted = false)
     * val probabilities = selector.probabilities(population, count = 5, ranker)
     * ```
     * In this example, `probabilities` provides the likelihood of each individual in the population being selected
     * based on their fitness.
     *
     * @param population The population from which individuals are to be selected.
     * @return A list of probabilities corresponding to each individual in the population.
     */
    fun probabilities(population: Population<T, G>, ranker: IndividualRanker<T, G>): List<Double> {
        // Adjust fitness values to ensure they're positive.
        val adjustedFitness = ranker.fitnessTransform(population.fitness).let {
            it sub min(it.min(), 0.0)
        }.toMutableList()

        // Compute total adjusted fitness.
        val totalFitness = adjustedFitness.sum()

        // Compute probabilities based on adjusted fitness.
        if (totalFitness.isNaN() || totalFitness.isInfinite() || totalFitness eq 0.0) {
            return List(population.size) { 1.0 / population.size }
        }

        for (i in adjustedFitness.indices) {
            adjustedFitness[i] /= totalFitness
        }

        return adjustedFitness
    }

    override fun select(population: Population<T, G>, count: Int, ranker: IndividualRanker<T, G>): Population<T, G> {
        val pop = if (sorted) {
            ranker.sort(population)
        } else {
            population
        }
        val probabilities = probabilities(pop, ranker).incremental()
        val selected = mutableListOf<Individual<T, G>>()
        while (selected.size < count) {
            val random = Domain.random.nextDouble()
            val index = probabilities.indexOfFirst { it >= random }
            selected += population[index]
        }
        return selected
    }
}
