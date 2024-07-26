/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.selection

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.fitness
import cl.ravenhill.keen.ranking.Ranker
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.utils.eq
import cl.ravenhill.keen.utils.incremental
import cl.ravenhill.keen.utils.sub
import kotlin.math.min


/**
 * A selector that performs roulette wheel selection in an evolutionary algorithm.
 *
 * The `RouletteWheelSelector` class selects individuals based on their fitness probabilities, simulating the
 * roulette wheel selection process. The selection can optionally be performed on a sorted population.
 *
 * ## Usage:
 * Use this selector to implement roulette wheel selection in evolutionary algorithms. This selector assigns
 * selection probabilities to individuals based on their fitness values, allowing fitter individuals a higher chance
 * of being selected.
 *
 * ### Example:
 * ```kotlin
 * val selector = RouletteWheelSelector<MyType, MyFeature, MyRepresentation>(sorted = true)
 * val selectedState = selector(state, 10) { selectedPopulation ->
 *     state.copy(population = selectedPopulation)
 * }
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @property sorted Indicates whether the population should be sorted by fitness before selection.
 * @constructor Creates an instance of `RouletteWheelSelector` with the specified sorted option.
 */
class RouletteWheelSelector<T, F, R>(val sorted: Boolean = false) :
    Selector<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * Calculates the selection probabilities for the population based on their fitness.
     *
     * @param population The population of individuals.
     * @param ranker The ranker used to evaluate and compare individuals.
     * @return A list of selection probabilities for the population.
     */
    fun probabilities(population: Population<T, F, R>, ranker: Ranker<T, F, R>): List<Double> {
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

    /**
     * Selects individuals from the population using roulette wheel selection.
     *
     * @param population The population of individuals.
     * @param count The number of individuals to select.
     * @param ranker The ranker used to evaluate and compare individuals.
     * @return The selected subset of individuals.
     */
    override fun select(population: Population<T, F, R>, count: Int, ranker: Ranker<T, F, R>): Population<T, F, R> {
        val pop = if (sorted) {
            ranker.sort(population)
        } else {
            population
        }
        val probabilities = probabilities(pop, ranker).incremental()
        val selected = mutableListOf<Individual<T, F, R>>()
        while (selected.size < count) {
            val random = Domain.random.nextDouble()
            val index = probabilities.indexOfFirst { it >= random }
            selected += population[index]
        }
        return selected
    }
}
