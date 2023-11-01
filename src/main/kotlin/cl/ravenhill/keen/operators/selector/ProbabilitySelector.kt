/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.EnforcementException
import cl.ravenhill.enforcer.requirements.DoubleRequirement
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Population
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
     * Selects a specified number of individuals from a given population based on their fitness.
     * The method employs fitness-proportionate selection, where the likelihood of an individual
     * being selected correlates with its fitness.
     *
     * During the process, individuals may be sorted based on their fitness (if required) and the
     * probabilities are calculated for each individual. Any minor discrepancies in probability
     * values due to rounding errors are corrected before proceeding with the selection.
     *
     * @param population The population from which individuals are to be selected.
     * @param count The number of individuals to select from the population.
     * @param optimizer The optimizer used to rank and sort the individuals based on their phenotypes.
     *
     * @return A new population consisting of the selected individuals.
     *
     * @throws EnforcementException if the computed probabilities do not sum up to 1.0.
     */
    override fun select(
        population: Population<DNA, G>,
        count: Int,
        optimizer: IndividualOptimizer<DNA, G>,
    ): Population<DNA, G> {
        // Sort the population if necessary
        val pop = if (sorted) {
            optimizer.sort(population)
        } else {
            population
        }
        // Calculate the probabilities for each phenotype
        val probabilities = probabilities(population, count, optimizer)
        // Check that the probabilities sum to 1.0
        enforce {
            "Probabilities must sum 1.0" {
                probabilities.sum() must DoubleRequirement.BeEqualTo(1.0)
            }
        }
        // Convert the probabilities to incremental probabilities
        probabilities.incremental()
        // Select the individuals using fitness-proportionate selection
        return List(count) {
            pop[probabilities.indexOfFirst { Core.random.nextDouble() <= it }]
        }
    }
}
