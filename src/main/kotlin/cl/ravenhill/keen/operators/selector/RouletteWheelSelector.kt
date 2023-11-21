/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.fitness
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.eq
import cl.ravenhill.keen.util.optimizer.IndividualOptimizer
import cl.ravenhill.keen.util.sub
import kotlin.math.min

/**
 * A selector that chooses individuals from a population using a roulette wheel selection mechanism.
 *
 * The probability of each individual being selected is proportional to its fitness. This means that
 * individuals with higher fitness values are more likely to be selected, but all individuals have
 * some non-zero probability of being chosen.
 *
 * @param DNA The type of data that the genes represent.
 * @param G The type of gene.
 * @property sorted Whether the population should be sorted before selecting individuals.
 * If `true`, the population will be sorted in decreasing order of fitness before selecting
 * individuals.
 * This can increase the selection pressure on fitter individuals and may lead to faster
 * convergence, but may also lead to premature convergence and reduced diversity.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
class RouletteWheelSelector<DNA, G : Gene<DNA, G>>(override val sorted: Boolean = false) :
    AbstractSelector<DNA, G>(),
    ProbabilitySelector<DNA, G> {

    /* Documentation inherited from [AbstractProbabilitySelector] */
    override fun probabilities(
        population: Population<DNA, G>,
        count: Int,
        optimizer: IndividualOptimizer<DNA, G>,
    ): DoubleArray {
        // Adjust fitness values to ensure they're positive.
        val adjustedFitness = population.fitness.let {
            it sub min(it.min(), 0.0)
        }.toMutableList()

        // Compute total adjusted fitness.
        val totalFitness = adjustedFitness.sum()

        // Compute probabilities based on adjusted fitness.
        if (totalFitness.isNaN() || totalFitness.isInfinite() || totalFitness eq 0.0) {
            return DoubleArray(population.size) { 1.0 / population.size }
        }

        for (i in adjustedFitness.indices) {
            adjustedFitness[i] /= totalFitness
        }

        return adjustedFitness.toDoubleArray()
    }
}
