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


class RouletteWheelSelector<T, F, R>(val sorted: Boolean = false) :
    Selector<T, F> where F : Feature<T, F>, R : Representation<T, F> {

    fun probabilities(population: Population<T, F>, ranker: Ranker<T, F>): List<Double> {
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

    override fun select(population: Population<T, F>, count: Int, ranker: Ranker<T, F>): Population<T, F> {
        val pop = if (sorted) {
            ranker.sort(population)
        } else {
            population
        }
        val probabilities = probabilities(pop, ranker).incremental()
        val selected = mutableListOf<Individual<T, F>>()
        while (selected.size < count) {
            val random = Domain.random.nextDouble()
            val index = probabilities.indexOfFirst { it >= random }
            selected += population[index]
        }
        return selected
    }
}
