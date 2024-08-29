/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines.ga

import cl.ravenhill.keen.evolution.states.GeneticEvolutionFailureState
import cl.ravenhill.keen.evolution.states.GeneticEvolutionSuccessState
import cl.ravenhill.keen.genetics.GenotypeFactory
import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.evolution.config.EvolutionConfiguration
import cl.ravenhill.keen.evolution.config.GeneticPopulationConfiguration
import cl.ravenhill.keen.evolution.engines.InitializerEngine
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.listeners.mixins.InitializationListener

/**
 * Initializes a genetic algorithm's population by creating individuals based on a genotype factory.
 *
 * The `GeneticInitializer` class implements the [InitializerEngine] interface to set up an initial population of
 * individuals if the given [GeneticEvolutionState] is empty. It uses a provided [GenotypeFactory] to create genotypes
 * for the individuals and notifies listeners about the initialization process.
 *
 * @param T The type of the value held by the genes in the genetic algorithm.
 * @param G The type of the gene, which must extend [Gene].
 * @param populationConfiguration The configuration for the genetic population, including population size and other
 *   related parameters.
 * @param evolutionConfiguration The configuration for the evolutionary process, including listeners, limits, and the
 *   evaluator.
 * @return The updated [GeneticEvolutionState] with the newly created population if the state was initially empty;
 *         otherwise, returns the original state.
 */
internal class GeneticInitializer<T, G>(
    populationConfiguration: GeneticPopulationConfiguration<T, G>,
    evolutionConfiguration: EvolutionConfiguration<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>,
) : InitializerEngine<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> where G : Gene<T, G> {

    /**
     * The factory used to create genotypes for individuals.
     */
    private val genotypeFactory = populationConfiguration.genotypeFactory

    /**
     * The number of individuals to create in the initial population.
     */
    private val populationSize = populationConfiguration.populationSize

    /**
     * Listeners that are triggered during the initialization process.
     */
    private val listeners = (evolutionConfiguration.listeners + evolutionConfiguration.limits.map { it.listener })
        .filterIsInstance<InitializationListener<*, *, *, GeneticEvolutionState<T, G>>>()

    /**
     * Initializes the [GeneticEvolutionState] with a new population of individuals if the state is empty.
     *
     * This method checks if the provided `GeneticEvolutionState` is empty. If it is, the method will:
     * 1. Notify all registered listeners that initialization has started.
     * 2. Create a new list of individuals by generating genotypes using the provided `GenotypeFactory`.
     * 3. Add each individual to the list and then update the state with the new population.
     * 4. Notify all registered listeners that initialization has completed.
     *
     * If the state is not empty, the method returns the existing state without modification.
     *
     * The process is wrapped in a [runCatching] block to handle any exceptions that may occur during initialization.
     * If an exception is thrown, the state is updated to reflect the failure using [GeneticEvolutionState.asFailure].
     *
     * @param state The current [GeneticEvolutionState] that may be updated with a new population.
     * @return The updated [GeneticEvolutionState] with a newly created population if the initial state was empty;
     *         otherwise, returns the original state.
     * @see GeneticAlgorithm
     * @see GeneticEvolutionFailureState
     * @see GeneticEvolutionSuccessState
     */
    override suspend fun initialize(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G> = runCatching {
        if (state.isEmpty()) {
            listeners.forEach { it.onInitializationStart(state) }
            val individuals = mutableListOf<Individual<T, G, Genotype<T, G>>>()
            repeat(populationSize) {
                val genotype = genotypeFactory().getOrThrow()
                individuals.add(Individual(genotype))
            }
            state.also {
                listeners.forEach { it.onInitializationEnd(state) }
            }
        } else {
            state
        }
    }.fold(
        onSuccess = { it },
        onFailure = { GeneticEvolutionState.asFailure(state, it) }
    )
}
