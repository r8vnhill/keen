/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.constraints.doubles.BeEqualTo
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.incremental
import cl.ravenhill.keen.util.optimizer.IndividualOptimizer

/**
 * Represents a selection strategy based on probabilities derived from the fitness of individuals
 * within a population. In such strategies, the likelihood of an individual being selected for
 * reproduction correlates with its fitness.
 *
 * Implementations of this interface should define how these probabilities are calculated
 * and how selection based on these probabilities is performed.
 *
 * @param DNA The genetic representation of the individuals.
 * @param G The type of the genes which make up the DNA.
 * @property sorted flag that determines whether the input population should be sorted by
 *           fitness
 *
 * @see Selector
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface ProbabilitySelector<DNA, G> : Selector<DNA, G> where G : Gene<DNA, G> {

    val sorted: Boolean

    /**
     * Calculates the probabilities of selecting each member of the population for reproduction,
     * based on their fitness.
     *
     * @param population the population to calculate probabilities for
     * @param count the number of probabilities to calculate
     * @param optimizer the individual optimizer used to calculate the fitness of the population
     * @return an array of probabilities, one for each member of the population
     */
    fun probabilities(
        population: Population<DNA, G>,
        count: Int,
        optimizer: IndividualOptimizer<DNA, G>,
    ): DoubleArray

    /**
     * Selects individuals from a population based on fitness using fitness-proportionate selection.
     * The likelihood of an individual's selection is proportional to its fitness.
     *
     * The population may be sorted by fitness if required. Probabilities are assigned to each individual,
     * and any rounding discrepancies are corrected before selection.
     *
     * @param population The source population.
     * @param count Number of individuals to select.
     * @param optimizer Used to rank and sort the population by fitness.
     *
     * @return A new population of selected individuals.
     *
     * @throws CompositeException if the summed probabilities aren't 1.0.
     */
    override fun select(
        population: Population<DNA, G>,
        count: Int,
        optimizer: IndividualOptimizer<DNA, G>,
    ): Population<DNA, G> {
        val pop = sortIfRequired(population, optimizer)
        val probabilities = probabilities(population, count, optimizer)
        validateProbabilities(probabilities)
        probabilities.incremental()

        return selectByProbabilities(pop, probabilities, count)
    }

    /**
     * Sorts the population by fitness if required. If sorting isn't needed, it simply returns the
     * given population as-is.
     *
     * @param population The source population.
     * @param optimizer The optimizer used to rank and sort the population by fitness.
     *
     * @return The sorted population or the original population if sorting isn't required.
     */
    private fun sortIfRequired(
        population: Population<DNA, G>,
        optimizer: IndividualOptimizer<DNA, G>
    ): Population<DNA, G> {
        return if (sorted) optimizer.sort(population) else population
    }

    /**
     * Validates that the computed probabilities sum to 1.0.
     * Throws an exception if the sum is not close to 1.0.
     *
     * @param probabilities Array of computed probabilities.
     *
     * @throws CompositeException if the summed probabilities aren't 1.0.
     */
    private fun validateProbabilities(probabilities: DoubleArray) {
        if (probabilities.all { it == 0.0 }) {
            for (i in probabilities.indices) {
                probabilities[i] = 1.0 / probabilities.size
            }
        }
        constraints {
            "Probabilities must sum 1.0" {
                probabilities.sum() must BeEqualTo(1.0)
            }
        }
    }

    /**
     * Selects individuals from the population based on their associated probabilities.
     * The method uses fitness-proportionate selection, where each individual's chance of being selected
     * corresponds to its probability in the array.
     *
     * @param population The source population.
     * @param probabilities Array of probabilities associated with each individual in the population.
     * @param count Number of individuals to select.
     *
     * @return A new population of selected individuals.
     */
    private fun selectByProbabilities(
        population: Population<DNA, G>,
        probabilities: DoubleArray,
        count: Int
    ): Population<DNA, G> {
        return List(count) {
            population[probabilities.indexOfFirst { Core.random.nextDouble() <= it }]
        }
    }

}
