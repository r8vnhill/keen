/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines.ga

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import cl.ravenhill.keen.evolution.config.AlterationConfiguration
import cl.ravenhill.keen.evolution.config.EvolutionConfiguration
import cl.ravenhill.keen.evolution.config.GeneticPopulationConfiguration
import cl.ravenhill.keen.evolution.config.SelectionConfiguration
import cl.ravenhill.keen.evolution.engines.AbstractGeneBasedEvolutionaryAlgorithm
import cl.ravenhill.keen.evolution.engines.AlterationEngine
import cl.ravenhill.keen.evolution.engines.EvaluationEngine
import cl.ravenhill.keen.evolution.engines.InitializerEngine
import cl.ravenhill.keen.evolution.engines.ParentSelectionEngine
import cl.ravenhill.keen.evolution.engines.SurvivorSelectionEngine
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.exceptions.EvolutionException
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.listeners.EvolutionListener

/**
 * Implementation of a genetic algorithm within Keen.
 *
 * The `GeneticAlgorithm` class represents a comprehensive implementation of a genetic algorithm (GA), a popular
 * optimization technique inspired by the process of natural selection. This class is designed to manage the entire
 * lifecycle of a genetic algorithm, including initialization, evaluation, selection of parents, alteration (crossover
 * and mutation), and survivor selection. It integrates multiple components through delegation, ensuring a modular and
 * flexible structure that can be easily customized or extended.
 *
 * ## Theoretical Framework:
 * Genetic algorithms are a subset of evolutionary algorithms, which are inspired by the biological processes of
 * evolution, such as natural selection, mutation, and recombination. GAs work by evolving a population of candidate
 * solutions (individuals) over successive generations. The key processes in a genetic algorithm include:
 *
 * 1. **Initialization**: The process starts with an initial population of individuals, each representing a potential
 * solution to the problem.
 * 2. **Evaluation**: Each individual is evaluated to determine its fitness, which reflects how well it solves the
 * problem at hand.
 * 3. **Selection**: Based on fitness, individuals are selected to act as parents for the next generation. Various
 * selection strategies exist, such as roulette wheel selection, tournament selection, etc.
 * 4. **Alteration**: Genetic operators such as crossover (recombination of genes from parents) and mutation (random
 * alterations to genes) are applied to generate offspring.
 * 5. **Survivor Selection**: The offspring compete with the existing population to form the next generation.
 * 6. **Termination**: The algorithm continues until a stopping condition is met, such as a maximum number of
 * generations or convergence to a solution.
 *
 * The `GeneticAlgorithm` class implements these processes in a structured manner, leveraging a series of engines that
 * handle specific aspects of the algorithm. By combining these engines, the algorithm can evolve the population toward
 * an optimal solution.
 *
 * ## Usage:
 * This class is intended to be used in scenarios where a genetic algorithm is an appropriate method for solving
 * optimization or search problems. The algorithm is highly configurable, allowing for customization of the population
 * size, selection methods, genetic operators, and evolutionary parameters.
 *
 * ### Example: Running a Genetic Algorithm
 * ```kotlin
 * val populationConfig = GeneticPopulationConfiguration(...)
 * val selectionConfig = SelectionConfiguration(...)
 * val alterationConfig = AlterationConfiguration(...)
 * val evolutionConfig = EvolutionConfiguration(...)
 *
 * val geneticAlgorithm = GeneticAlgorithm(
 *     populationConfiguration = populationConfig,
 *     selectionConfiguration = selectionConfig,
 *     alterationConfiguration = alterationConfig,
 *     evolutionConfiguration = evolutionConfig
 * )
 *
 * runBlocking { // Remove this line if you're running the algorithm in JS
 *     val finalState = geneticAlgorithm.evolve()
 *     println("Final population: ${finalState.population}")
 * } // Remove this line if you're running the algorithm in JS
 * ```
 *
 * @param T The type of value held by the genes.
 * @param G The type of gene, which must extend [Gene].
 * @param L The type of listener, which must extend [EvolutionListener].
 * @param populationConfiguration The configuration for the genetic population, defining parameters like population size.
 * @param selectionConfiguration The configuration for selection strategies, including parent and survivor selection.
 * @param alterationConfiguration The configuration for genetic alterations, such as crossover and mutation.
 * @param evolutionConfiguration The overall configuration for the evolutionary algorithm, including listeners and
 *   other settings.
 */
class GeneticAlgorithm<T, G, L>(
    populationConfiguration: GeneticPopulationConfiguration<T, G>,
    selectionConfiguration: SelectionConfiguration<T, G, Genotype<T, G>>,
    alterationConfiguration: AlterationConfiguration<T, G>,
    evolutionConfiguration: EvolutionConfiguration<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>
) : AbstractGeneBasedEvolutionaryAlgorithm<T, G, L>(
    evolutionConfiguration
), InitializerEngine<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> by GeneticInitializer(
    populationConfiguration,
    evolutionConfiguration
), EvaluationEngine<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> by GeneticEvaluator(
    populationConfiguration,
    evolutionConfiguration
), ParentSelectionEngine<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> by GeneticParentSelector(
    populationConfiguration,
    evolutionConfiguration,
    selectionConfiguration
), SurvivorSelectionEngine<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> by GeneticSurvivorSelector(
    populationConfiguration,
    evolutionConfiguration,
    selectionConfiguration
), AlterationEngine<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> by GeneticAlterationEngine(
    populationConfiguration,
    evolutionConfiguration,
    alterationConfiguration
)
        where G : Gene<T, G>, L : EvolutionListener<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> {

    override var state: GeneticEvolutionState<T, G> = evolutionConfiguration.initialState

    override suspend fun iterateGeneration(
        state: GeneticEvolutionState<T, G>
    ): Either<EvolutionException, GeneticEvolutionState<T, G>> {
        val interceptedStart = interceptor.before(state)
        val initializedState = initialize(interceptedStart)
            .getOrElse { return EvolutionException("Initialization failed", it).left() }
        val evaluatedState = evaluate(initializedState)
            .getOrElse { return EvolutionException("Evaluation failed", it).left() }
        val parents = selectParents(evaluatedState)
            .getOrElse { return EvolutionException("Parent selection failed", it).left() }
        val survivors = selectSurvivors(evaluatedState)
            .getOrElse { return EvolutionException("Survivor selection failed", it).left() }
        val offspring = alter(parents)
        val nextPopulation = survivors.population + offspring.population
        val nextGeneration = evaluate(evaluatedState.makeCopy(population = nextPopulation))
            .getOrElse { return EvolutionException("Evaluation failed", it).left() }
        return interceptor.after(nextGeneration)
            .makeCopy(generation = nextGeneration.generation + 1)
            .right()    // Right is the success case (the "right" choice)
    }
}
