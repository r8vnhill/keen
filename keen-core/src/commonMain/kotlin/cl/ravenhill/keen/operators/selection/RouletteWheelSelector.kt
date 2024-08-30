package cl.ravenhill.keen.operators.selection

import arrow.core.Either
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.exceptions.SelectionException
import cl.ravenhill.keen.fitness
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.utils.SortingStrategy
import cl.ravenhill.keen.utils.eq
import cl.ravenhill.keen.utils.sub
import kotlin.math.min
import kotlin.random.Random

class RouletteWheelSelector<T, F : Feature<T, F>, R : Representation<T, F>>(
    sorted: SortingStrategy = SortingStrategy.UNSORTED
) : Selector<T, F, R> {

    override suspend fun select(
        population: Population<T, F, R>,
        count: Int,
        ranker: IndividualRanker<T, F, R>
    ): Either<SelectionException, Population<T, F, R>> {
        TODO("Not yet implemented")
    }

    internal fun probabilities(population: Population<T, F, R>, ranker: IndividualRanker<T, F, R>): List<Double> {
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
}
