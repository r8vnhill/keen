/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.config.EvolutionConfiguration
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.listeners.Listener

/**
 * Abstract base class for gene-based evolutionary algorithms.
 *
 * The `AbstractGeneBasedEvolutionaryAlgorithm` class provides a foundation for implementing evolutionary algorithms
 * that operate on genes as the primary unit of evolution. It combines initialization, evaluation, and evolutionary
 * operations into a cohesive framework that can be extended to create specific genetic algorithms. This class extends
 * the [AbstractEvolver] and implements both the [InitializerEngine] and [EvaluationEngine] interfaces, providing the
 * necessary infrastructure to manage the lifecycle of an evolutionary algorithm.
 *
 * ## Usage:
 * This abstract class is intended to be extended by concrete implementations of gene-based evolutionary algorithms.
 * Subclasses should implement the necessary logic for initializing populations, evaluating individuals, and iterating
 * through generations. By inheriting from this class, developers can focus on the specific details of their algorithm
 * while leveraging the common functionality provided here.
 *
 * ### Example: Extending `AbstractGeneBasedEvolutionaryAlgorithm`
 * Suppose you want to implement a genetic algorithm that focuses on evolving a population of integer genes. You could
 * extend this class as follows:
 *
 * ```kotlin
 * class IntGeneBasedAlgorithm(
 *     populationConfig: GeneticPopulationConfiguration<Int, IntGene>,
 *     evolutionConfig: EvolutionConfiguration<Int, IntGene, Genotype<Int, IntGene>, GeneticEvolutionState<Int, IntGene>>
 * ) : AbstractGeneBasedEvolutionaryAlgorithm<Int, IntGene, MyListener>(populationConfig, evolutionConfig) {
 *     // Implement the initialization, evaluation, and generation iteration logic here
 * }
 * ```
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @param L The type of the listener used in the evolutionary process, which must extend [Listener].
 * @param evolutionConfiguration The overall configuration for the evolutionary algorithm, including interceptors,
 *   limits, and listeners.
 */
abstract class AbstractGeneBasedEvolutionaryAlgorithm<T, G, L>(
    evolutionConfiguration: EvolutionConfiguration<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>
) : AbstractEvolver<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>(evolutionConfiguration),
    InitializerEngine<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>,
    EvaluationEngine<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> where G : Gene<T, G>, L : Listener {

    /**
     * The interceptor that modifies or monitors the state before and after key evolutionary processes.
     *
     * The `interceptor` is used to inject additional behavior into the evolutionary process, such as logging,
     * performance monitoring, or state manipulation. This can be particularly useful for debugging or customizing
     * the evolutionary process.
     */
    protected val interceptor = evolutionConfiguration.interceptor
}
