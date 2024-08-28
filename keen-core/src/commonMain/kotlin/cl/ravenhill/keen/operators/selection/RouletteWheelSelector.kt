package cl.ravenhill.keen.operators.selection

import cl.ravenhill.keen.Population
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

    override fun select(
        population: Population<T, F, R>,
        count: Int,
        ranker: IndividualRanker<T, F, R>,
        random: Random
    ): Result<Population<T, F, R>> {
        TODO("Not yet implemented")
    }

    /**
     * Computes the selection probabilities for a population of individuals based on their fitness values.
     *
     * The `probabilities` function calculates the probability of each individual in a population being selected
     * for the next generation in an evolutionary algorithm. This is done using a fitness-proportional selection
     * strategy, where individuals with higher fitness values have a greater chance of being selected.
     *
     * ## Special Cases:
     * - If the total fitness is NaN, Infinite, or zero, the function will return a uniform distribution where each
     *   individual has an equal probability of being selected.
     *
     * @param population The population of individuals whose selection probabilities are to be computed.
     * @param ranker The ranker used to evaluate and rank the individuals based on their fitness.
     * @return A list of selection probabilities for each individual in the population. The probabilities are normalized
     *   to sum to 1.
     */
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
