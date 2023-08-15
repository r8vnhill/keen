/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer


/**
 * Class that implements a random selection strategy for a population in a genetic algorithm.
 *
 * This class extends [AbstractProbabilitySelector] with a strategy to randomly select elements
 * from a population. The selection is unbiased, meaning each element in the population has
 * an equal chance to be selected. This is ensured by assigning the same selection probability
 * (1.0 / population size) to every element in the population.
 *
 * ## Examples
 * ### Example 1: Creating a RandomSelector and selecting individuals
 * ```kotlin
 * // Assume we have a defined population of Individuals
 * val population: Population<MyDNA, MyGene> = ...
 * val randomSelector = RandomSelector<MyDNA, MyGene>()
 *
 * // Select 5 individuals
 * val selected = randomSelector.select(population, 5)
 * ```
 *
 * @property DNA The type parameter indicating the type of the DNA.
 * @property G The type parameter indicating the type of the Gene. G is a Gene of type DNA.
 *
 * @constructor Creates a new RandomSelector.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class RandomSelector<DNA, G : Gene<DNA, G>> : AbstractProbabilitySelector<DNA, G>(false) {
    /**
     * Computes the selection probabilities for each individual in the population.
     *
     * The probabilities are uniformly distributed, i.e., every individual has the same
     * probability of being selected, which equals 1.0 / population size.
     *
     * @param population The population of individuals.
     * @param count The number of individuals to be selected.
     * @param optimizer The phenotype optimizer.
     * @return A DoubleArray with the computed probabilities.
     */
    override fun probabilities(
        population: Population<DNA, G>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA, G>,
    ) = DoubleArray(population.size) { 1.0 / population.size }
}