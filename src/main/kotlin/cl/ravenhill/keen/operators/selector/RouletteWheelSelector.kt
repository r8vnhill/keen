/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.eq
import cl.ravenhill.keen.util.sub
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer
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
class RouletteWheelSelector<DNA, G: Gene<DNA, G>>(
    sorted: Boolean = false
) : AbstractProbabilitySelector<DNA, G>(sorted) {

    /// Documentation inherited from [AbstractProbabilitySelector]
    override fun probabilities(
        population: Population<DNA, G>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA, G>
    ): DoubleArray {
        // Subtract the minimum fitness from all fitness values to ensure they are all positive.
        // This prevents negative fitness values from causing problems later on when computing the
        // probabilities.
        val fitness = population.fitness.let {
            it sub min(it.min(), 0.0)
        }
        // Compute the sum of all fitness values.
        val cums = fitness.reduce { acc, d -> acc + d }
        // If the sum of fitness values is 0, return a uniform probability distribution.
        // Otherwise, compute the probability of each individual by dividing its fitness by the sum
        // of all fitness values.
        return if (cums eq 0.0) {
            List(population.size) { 1.0 / population.size }.toDoubleArray()
        } else {
            fitness.map { it / cums }.toDoubleArray()
        }
    }

    /// Documentation inherited from [Any]
    override fun toString() = "RouletteWheelSelector(sorted=$sorted)"
}

/**
 * Returns a list of the fitness values of all the individuals in this population.
 *
 * The `fitness` property is a computed property that maps over all individuals in this population
 * and returns a list of their fitness values.
 *
 * @param DNA The type of the genetic data of the individuals.
 * @param G The type of the genes that make up the individuals.
 * @return A list of the fitness values of all the individuals in this population.
 */
private val <DNA, G: Gene<DNA, G>> Population<DNA, G>.fitness: List<Double>
    get() = this.map { it.fitness }
