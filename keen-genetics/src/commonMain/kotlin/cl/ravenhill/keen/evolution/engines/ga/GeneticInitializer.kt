/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines.ga

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.evolution.config.EvolutionConfiguration
import cl.ravenhill.keen.evolution.config.GeneticPopulationConfiguration
import cl.ravenhill.keen.evolution.engines.InitializerEngine
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.exceptions.InitializationException
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.GenotypeFactory
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
     * This method is responsible for generating the initial population in a genetic evolutionary algorithm when the
     * provided state is empty. The process involves the following steps:
     *
     * 1. **Start Initialization**: Notify all registered initialization listeners that the initialization process has
     *    begun.
     * 2. **Population Creation**: A list of new individuals is created. For each individual:
     *    - A genotype is generated using the [GenotypeFactory].
     *    - If genotype creation fails, an [InitializationException] is returned.
     *    - Otherwise, the genotype is wrapped in an [Individual] and added to the population.
     * 3. **State Update**: The state is updated with the newly created population.
     * 4. **End Initialization**: Notify all registered initialization listeners that the initialization process has
     *    completed.
     *
     * If the state is not empty, the method simply returns the existing state without making any modifications.
     *
     * The method uses [Either] to handle potential errors during the initialization process, ensuring that any issues
     * (such as failures in genotype creation) are properly encapsulated in an [InitializationException].
     *
     * @param state The current [GeneticEvolutionState] which may be updated with a new population.
     * @return An [Either] containing:
     *   - The updated [GeneticEvolutionState] with a newly created population if the initial state was empty.
     *   - The original state if it was not empty.
     *   - An [InitializationException] in case of errors during genotype creation.
     */
    override suspend fun initialize(
        state: GeneticEvolutionState<T, G>
    ): Either<InitializationException, GeneticEvolutionState<T, G>> =
        if (state.isEmpty()) {
            listeners.forEach { it.onInitializationStart(state) }
            val individuals = mutableListOf<Individual<T, G, Genotype<T, G>>>()
            repeat(populationSize) {
                val genotype = genotypeFactory().getOrElse {
                    return InitializationException("Genotype creation failed", it).left()
                }
                individuals.add(Individual(genotype))
            }
            state.copy(population = individuals).also {
                listeners.forEach { l -> l.onInitializationEnd(it) }
            }
        } else {
            state
        }.right()
}
