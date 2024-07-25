package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.config.EvolutionConfig
import cl.ravenhill.keen.evolution.config.PopulationConfig
import cl.ravenhill.keen.evolution.config.SelectionConfig
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.selection.Selector

/**
 * Abstract class representing a gene-based evolutionary algorithm.
 *
 * The `AbstractGeneBasedEvolutionaryAlgorithm` class provides a framework for implementing evolutionary algorithms
 * based on genes like genetic algorithms or evolutionary strategies. It manages the evolution process, including
 * initialization, evaluation, parent selection, survivor selection, and offspring alteration. Subclasses must implement
 * the abstract methods for each step of the evolutionary process.
 *
 * ## Usage:
 * This class is intended to be extended by specific implementations of gene-based evolutionary algorithms. Subclasses
 * should provide concrete implementations for initializing the population, evaluating fitness, selecting parents and
 * survivors, and altering offspring.
 *
 * ### Example:
 * ```kotlin
 * class MyGeneBasedAlgorithm<T, G>(
 *     populationConfig: PopulationConfig<T, G>,
 *     evolutionConfig: EvolutionConfig<T, G>,
 *     selectionConfig: SelectionConfig<T, G>
 * ) : AbstractGeneBasedEvolutionaryAlgorithm<T, G>(populationConfig, evolutionConfig, selectionConfig)
 *         where G : Gene<T, G> {
 *
 *     override fun startEvolution(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G> {
 *         // Implementation of population initialization
 *     }
 *
 *     override fun evaluatePopulation(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G> {
 *         // Implementation of population evaluation
 *     }
 *
 *     override fun selectParents(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G> {
 *         // Implementation of parent selection
 *     }
 *
 *     override fun selectSurvivors(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G> {
 *         // Implementation of survivor selection
 *     }
 *
 *     override fun alterOffspring(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G> {
 *         // Implementation of offspring alteration
 *     }
 * }
 * ```
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @param evolutionConfig The configuration settings for the evolutionary algorithm.
 * @param populationConfig The configuration settings for the population.
 * @param selectionConfig The configuration settings for the selection process.
 * @property state The current state of the evolutionary process.
 * @property listeners The listeners to notify during the evolution process.
 * @property limits The termination conditions for the evolution process.
 * @property ranker The ranker used to evaluate individuals in the population.
 * @property evaluator The evaluator used to calculate the fitness of individuals.
 * @property interceptor The interceptor used to pre-process and post-process the state.
 * @property genotypeFactory The factory used to generate genotypes for individuals.
 * @property populationSize The size of the population.
 * @property survivalRate The rate of individuals that survive to the next generation.
 * @property parentSelector The selector used to choose parents for offspring production.
 * @property survivorSelector The selector used to choose survivors for the next generation.
 * @constructor Creates an instance of the gene-based evolutionary algorithm with the specified configurations.
 */
abstract class AbstractGeneBasedEvolutionaryAlgorithm<T, G>(
    populationConfig: PopulationConfig<T, G>,
    evolutionConfig: EvolutionConfig<T, G>,
    selectionConfig: SelectionConfig<T, G>
) : Evolver<T, G> where G : Gene<T, G> {

    private var state = GeneticEvolutionState.empty(evolutionConfig.ranker)

    val listeners = evolutionConfig.listeners.toMutableList()
    val limits = evolutionConfig.limits
    val ranker = evolutionConfig.ranker
    val evaluator = evolutionConfig.evaluator
    val interceptor = evolutionConfig.interceptor
    val genotypeFactory: Genotype.Factory<T, G> = populationConfig.genotypeFactory
    override val populationSize: Int = populationConfig.populationSize
    override val survivalRate: Double = selectionConfig.survivalRate
    val parentSelector: Selector<T, G> = selectionConfig.parentSelector
    val survivorSelector: Selector<T, G> = selectionConfig.survivorSelector

    /**
     * Starts the evolution process and runs it until one of the limits is reached.
     *
     * This method manages the main evolutionary loop, notifying listeners at the start and end of the evolution,
     * as well as at the start and end of each generation. It applies the configured limits to determine when to
     * terminate the evolution.
     *
     * @return The final state of the evolution process.
     */
    override fun evolve(): GeneticEvolutionState<T, G> {
        // Notify listeners of evolution start
        listeners.forEach { it.onEvolutionStarted(state) }
        // Main evolutionary loop
        do {
            // Notify listeners of generation start
            listeners.forEach { it.onGenerationStarted(state) }
            state = iterateGeneration(state)
            // Notify listeners of generation end
            listeners.forEach { it.onGenerationEnded(state) }
        } while (limits.none { it(state) })
        // Notify listeners of evolution end
        listeners.forEach { it.onEvolutionEnded(state) }
        return state
    }

    /**
     * Performs a single generation iteration, including all phases of the evolutionary process.
     *
     * This method applies the following steps:
     * 1. Pre-process the state using the interceptor.
     * 2. Initialize or continue the population.
     * 3. Evaluate the population's fitness.
     * 4. Select parents for producing offspring.
     * 5. Select survivors for the next generation.
     * 6. Alter the offspring through genetic operations.
     * 7. Merge the offspring and survivors to form the next generation.
     * 8. Evaluate the next generation.
     * 9. Post-process the final state using the interceptor.
     *
     * @param state The current state of the evolution process.
     * @return The updated state after one generation iteration.
     */
    fun iterateGeneration(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G> {
        // Apply pre-processing to the state
        val interceptedStart = interceptor.before(state)
        // Initialize or continue population
        val initialPopulation = startEvolution(interceptedStart)
        // Evaluate population fitness
        val evaluatedPopulation = evaluatePopulation(initialPopulation)
        // Select parents for offspring production
        val parents = selectParents(evaluatedPopulation)
        // Select survivors for the next generation
        val survivors = selectSurvivors(evaluatedPopulation)
        // Alter offspring through genetic operations
        val offspring = alterOffspring(parents)
        // Merge offspring and survivors to form the next generation
        val nextPopulation = survivors.copy(population = survivors.population + offspring.population)
        // Evaluate the next generation
        val nextGeneration = evaluatePopulation(nextPopulation)
        // Apply post-processing to the final state
        val interceptedEnd = interceptor.after(nextGeneration)
        return interceptedEnd.copy(generation = interceptedEnd.generation + 1)
    }

    /**
     * Initializes or continues the population for the evolution process.
     *
     * @param state The current state of the evolution process.
     * @return The updated state with the initialized or continued population.
     */
    abstract fun startEvolution(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G>

    /**
     * Evaluates the fitness of the population in the given state.
     *
     * @param state The current state of the evolution process.
     * @return The updated state with evaluated fitness values.
     */
    abstract fun evaluatePopulation(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G>

    /**
     * Selects parents from the population in the given state.
     *
     * @param state The current state of the evolution process.
     * @return The updated state with selected parents.
     */
    abstract fun selectParents(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G>

    /**
     * Selects survivors from the population in the given state.
     *
     * @param state The current state of the evolution process.
     * @return The updated state with selected survivors.
     */
    abstract fun selectSurvivors(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G>

    /**
     * Alters the offspring in the population in the given state.
     *
     * @param state The current state of the evolution process.
     * @return The updated state with altered offspring.
     */
    abstract fun alterOffspring(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G>
}
