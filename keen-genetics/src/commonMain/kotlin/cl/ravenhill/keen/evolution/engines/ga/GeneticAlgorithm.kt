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
    evolutionConfiguration,
    alterationConfiguration
)
        where G : Gene<T, G>, L : EvolutionListener<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> {

    override var state: GeneticEvolutionState<T, G> = evolutionConfiguration.initialState

    /**
     * Advances the evolutionary process by one generation.
     *
     * The `iterateGeneration` function guides the algorithm through a complete cycle of evolutionary steps for one
     * generation. This method actively manages the process, from initialization to generation advancement, ensuring
     * each phase is executed in the correct sequence. If any phase fails, the function stops and returns an
     * [EvolutionException].
     *
     * ## Workflow:
     * 1. **Pre-Initialization Interception**: The function intercepts and potentially modifies the state before
     *   initialization begins.
     * 2. **Initialization**: If the population is empty, the function initializes it, creating a well-defined starting
     *   point. Any errors during this step cause the generation to halt.
     * 3. **Evaluation**: The function evaluates the population based on the fitness criteria. If the evaluation fails,
     *   the function stops the process.
     * 4. **Parent Selection**: The function selects parents from the evaluated population for reproduction. If the
     *   selection fails, the function halts the process.
     * 5. **Survivor Selection**: The function chooses which individuals will survive to the next generation. Any
     *   failure during this phase stops the generation.
     * 6. **Alteration**: The selected parents undergo genetic operations (e.g., crossover, mutation) to produce
     *   offspring. If this process fails, the function stops the generation.
     * 7. **Population Update**: The function forms the next generation by combining the survivors with the newly
     *   created offspring and then re-evaluates the population.
     * 8. **Post-Evaluation Interception**: After the evaluation, the function intercepts the state again, allowing for
     *   any final adjustments before proceeding to the next generation.
     * 9. **Generation Advancement**: The function increments the generation counter and returns the updated state.
     *
     * ## Error Handling:
     * The function carefully monitors each step for errors. If an error occurs, it returns an [EvolutionException]
     * within an [Either.Left] value. On success, it returns the updated [GeneticEvolutionState] within an
     * [Either.Right] value.
     *
     * @param state The current [GeneticEvolutionState] representing the evolutionary state at the start of the
     *   generation.
     * @return An [Either] containing the updated [GeneticEvolutionState] after one generation on success, or an
     *   [EvolutionException] on failure.
     */
    override suspend fun iterateGeneration(
        state: GeneticEvolutionState<T, G>
    ): Either<EvolutionException, GeneticEvolutionState<T, G>> = runCatching {
        val interceptedStart = interceptor.before(state)
        val initializedState = initialize(interceptedStart).getOrElse { throw it }
        val evaluatedState = evaluate(initializedState).getOrElse { throw it }
        val parents = selectParents(evaluatedState).getOrElse { throw it }
        val survivors = selectSurvivors(evaluatedState).getOrElse { throw it }
        val offspring = alter(parents).getOrElse { throw it }
        val nextPopulation = survivors.population + offspring.population
        val nextGeneration = evaluate(evaluatedState.makeCopy(population = nextPopulation)).getOrElse { throw it }
        interceptor.after(nextGeneration).makeCopy(generation = nextGeneration.generation + 1)
    }.fold(
        onSuccess = { it.right() },
        onFailure = { throwable -> EvolutionException("An error occurred -- ${throwable.message}", throwable).left() }
    )
}
