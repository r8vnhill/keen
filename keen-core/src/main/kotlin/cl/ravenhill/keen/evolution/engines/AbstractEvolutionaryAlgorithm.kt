package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.EvolutionInterceptor
import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.evolution.config.EvolutionConfig
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.features.Representation
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.ranking.IndividualRanker

abstract class AbstractEvolutionaryAlgorithm<T, F, R>(val evolutionConfig: EvolutionConfig<T, F, R>) :
    Evolver<T, F> where F : Feature<T, F>, R : Representation<T, F> {

    private var state: EvolutionState<T, F, R> = EvolutionState.empty(evolutionConfig.ranker)

    override val listeners: MutableList<EvolutionListener<T, F>> = evolutionConfig.listeners.toMutableList()
    val limits: List<Limit<T, F>> = evolutionConfig.limits
    val ranker: IndividualRanker<T, F> = evolutionConfig.ranker
    val evaluator: EvaluationExecutor<T, F> = evolutionConfig.evaluator
    val interceptor: EvolutionInterceptor<T, F> = evolutionConfig.interceptor

    override fun evolve(): EvolutionState<T, F> {
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

    fun iterateGeneration(state: EvolutionState<T, F>): EvolutionState<T, F> {
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

    abstract fun startEvolution(state: EvolutionState<T, F>): EvolutionState<T, F>
    abstract fun evaluatePopulation(state: EvolutionState<T, F>): EvolutionState<T, F>
    abstract fun selectParents(state: EvolutionState<T, F>): EvolutionState<T, F>
    abstract fun selectSurvivors(state: EvolutionState<T, F>): EvolutionState<T, F>
    abstract fun alterOffspring(state: EvolutionState<T, F>): EvolutionState<T, F>
}
