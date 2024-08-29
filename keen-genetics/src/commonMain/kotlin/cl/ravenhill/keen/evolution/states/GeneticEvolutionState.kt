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
 * Represents the state of the evolutionary process in a genetic algorithm.
 *
 * The `GeneticEvolutionState` interface defines a sealed hierarchy of states that can occur during the evolutionary
 * process in a genetic algorithm. This interface extends the general [EvolutionState] interface, specializing it for
 * genetic algorithms where the population consists of genotypes. By sealing the interface, it ensures that all possible
 * states of the evolutionary process are known and can be handled appropriately, providing better safety and clarity in
 * the design of evolutionary algorithms.
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 */
sealed interface GeneticEvolutionState<T, G> : EvolutionState<T, G, Genotype<T, G>> where G : Gene<T, G> {

    /**
     * Creates a new instance of `GeneticEvolutionState` with the specified properties, while preserving other
     * properties from the current instance. This function is overridden to change the return type to
     * `GeneticEvolutionState` specifically for genetic algorithms.
     *
     * @param population The new population for the state. If not provided, the current population is used.
     * @param ranker The new ranker for evaluating individuals. If not provided, the current ranker is used.
     * @param generation The new generation number. If not provided, the current generation number is used.
     * @return A new `GeneticEvolutionState` instance with the updated properties.
     */
    override fun makeCopy(
        population: Population<T, G, Genotype<T, G>>,
        ranker: IndividualRanker<T, G, Genotype<T, G>>,
        generation: Int
    ): GeneticEvolutionState<T, G>

    companion object {
        /**
         * Creates a failure state for the `GeneticEvolutionState` with the provided error.
         *
         * This function is useful for handling error conditions by transforming an existing state into a failure state.
         * The resulting `GeneticEvolutionFailureState` will encapsulate the provided error along with the original
         * state information, facilitating error handling in the evolutionary process.
         *
         * @param state The current state to transform into a failure state.
         * @param error The error to associate with the failure state.
         * @return A `GeneticEvolutionFailureState` representing the failure condition.
         */
        fun <T, G> asFailure(
            state: GeneticEvolutionState<T, G>,
            error: Throwable
        ): GeneticEvolutionFailureState<T, G> where G : Gene<T, G> =
            GeneticEvolutionFailureState(state.population, state.ranker, state.generation, error)
    }
}

/**
 * A private implementation of the `GeneticEvolutionState` interface.
 *
 * The `GeneticEvolutionStateImpl` class serves as the internal implementation of the `GeneticEvolutionState` interface,
 * encapsulating common properties and behaviors shared by all concrete states. This implementation is not exposed
 * outside of this file, ensuring that the state management is handled consistently across different states while
 * providing flexibility for specialized states to be created.
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @param population The current population of individuals in the evolutionary process.
 * @param ranker The ranker used to evaluate and compare individuals within the population.
 * @param generation The current generation number in the evolutionary process.
 */
private data class GeneticEvolutionStateImpl<T, G>(
    override val population: Population<T, G, Genotype<T, G>>,
    override val ranker: IndividualRanker<T, G, Genotype<T, G>>,
    override val generation: Int
) : GeneticEvolutionState<T, G> where G : Gene<T, G> {
    override fun makeCopy(
        population: Population<T, G, Genotype<T, G>>,
        ranker: IndividualRanker<T, G, Genotype<T, G>>,
        generation: Int
    ) = copy(population = population, ranker = ranker, generation = generation)
}

/**
 * Represents a successful state in the genetic evolutionary process.
 *
 * The `GeneticEvolutionSuccessState` class models a state where the evolutionary process is proceeding normally
 * without errors. This state indicates that the population has evolved to the next generation successfully, and it
 * provides the current population, the ranker used to evaluate individuals, and the generation number. This class is
 * a concrete implementation of the [GeneticEvolutionState] interface, using delegation to `GeneticEvolutionStateImpl`
 * for common functionality.
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @property population The current population of individuals.
 * @property ranker The ranker used to evaluate and compare individuals within the population.
 * @property generation The current generation number in the evolutionary process.
 */
data class GeneticEvolutionSuccessState<T, G>(
    override val population: Population<T, G, Genotype<T, G>>,
    override val ranker: IndividualRanker<T, G, Genotype<T, G>>,
    override val generation: Int
) : GeneticEvolutionState<T, G> by GeneticEvolutionStateImpl(population, ranker, generation)
        where G : Gene<T, G> {

    companion object {
        /**
         * Creates an empty `GeneticEvolutionSuccessState` with an initial generation of 0.
         *
         * This companion object function provides a convenient way to create a `GeneticEvolutionSuccessState` when
         * starting a new evolutionary process or when you need to initialize an empty state. The population is
         * initialized as an empty list, and the generation is set to 0.
         *
         * @param ranker The ranker used to evaluate and compare individuals within the population.
         * @return An instance of `GeneticEvolutionSuccessState` with an empty population and a generation number of 0.
         */
        fun <T, G> empty(ranker: IndividualRanker<T, G, Genotype<T, G>>) where G : Gene<T, G> =
            GeneticEvolutionSuccessState(emptyList(), ranker, 0)
    }
}


/**
 * Represents a failure state in the genetic evolutionary process.
 *
 * The `GeneticEvolutionFailureState` class models a state where an error has occurred during the evolutionary process.
 * This state captures the population, the ranker, the generation number, and the error that caused the process to fail.
 * It provides a mechanism to handle and report failures in the evolutionary algorithm, ensuring that such situations
 * are properly managed and logged.
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @param population The current population of individuals.
 * @param ranker The ranker used to evaluate and compare individuals within the population.
 * @param generation The current generation number in the evolutionary process.
 * @param error The exception or error that caused the evolutionary process to fail.
 */
data class GeneticEvolutionFailureState<T, G>(
    override val population: Population<T, G, Genotype<T, G>>,
    override val ranker: IndividualRanker<T, G, Genotype<T, G>>,
    override val generation: Int,
    val error: Throwable
) : GeneticEvolutionState<T, G> by GeneticEvolutionStateImpl(population, ranker, generation)
        where G : Gene<T, G>
