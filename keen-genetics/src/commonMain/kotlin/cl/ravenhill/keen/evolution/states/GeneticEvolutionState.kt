/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.states

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.ranking.IndividualRanker

/**
 * Represents the state of the genetic evolutionary process.
 *
 * The `GeneticEvolutionState` class models the state of an evolutionary algorithm specifically tailored for genetic
 * algorithms. It encapsulates the population of individuals, the ranker used to evaluate these individuals, and the
 * current generation number. This state is essential in tracking the progress of the evolutionary process and is used
 * to determine the next steps in the algorithm.
 *
 * @param T The type of value held by the genes in the population.
 * @param G The type of gene, which must extend [Gene].
 * @param population The current population of individuals in the evolutionary process.
 * @param ranker The ranker used to evaluate and compare individuals within the population.
 * @param generation The current generation number in the evolutionary process.
 */
data class GeneticEvolutionState<T, G>(
    override val population: Population<T, G, Genotype<T, G>>,
    override val ranker: IndividualRanker<T, G, Genotype<T, G>>,
    override val generation: Int
) : EvolutionState<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> where G : Gene<T, G> {

    /**
     * Creates a copy of the current evolutionary state with the provided population, ranker, and generation number.
     *
     * This method is useful for advancing the evolutionary process by creating a new state that reflects changes in the
     * population, ranker, or generation number. It ensures that the evolutionary algorithm can proceed with an updated
     * state without mutating the original state.
     *
     * @param population The updated population for the new state.
     * @param ranker The updated ranker for the new state.
     * @param generation The updated generation number for the new state.
     * @return A new `GeneticEvolutionState` instance with the updated parameters.
     */
    override fun makeCopy(
        population: Population<T, G, Genotype<T, G>>,
        ranker: IndividualRanker<T, G, Genotype<T, G>>,
        generation: Int
    ) = copy(population = population, ranker = ranker, generation = generation)

    companion object {
        /**
         * Creates an empty `GeneticEvolutionState` instance with the provided ranker.
         *
         * This method is useful for initializing an evolutionary state without any population or generation
         * information. It can be used at the beginning of an evolutionary process or when resetting the process to its
         * initial state.
         *
         * @param ranker The ranker for evaluating and comparing the fitness of individuals.
         * @param G The type of gene used in the `Genotype`.
         * @param T The type of value held by the gene.
         */
        fun <T, G> empty(ranker: IndividualRanker<T, G, Genotype<T, G>>) where G : Gene<T, G> =
            GeneticEvolutionState(emptyPopulation(), ranker, 0)
    }
}
