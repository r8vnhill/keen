/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines.ga

import cl.ravenhill.keen.evolution.config.AlterationConfiguration
import cl.ravenhill.keen.evolution.config.EvolutionConfiguration
import cl.ravenhill.keen.evolution.config.GeneticPopulationConfiguration
import cl.ravenhill.keen.evolution.config.SelectionConfiguration
import cl.ravenhill.keen.evolution.engines.AbstractGeneBasedEvolutionaryAlgorithm
import cl.ravenhill.keen.evolution.engines.EvaluationEngine
import cl.ravenhill.keen.evolution.engines.InitializerEngine
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.listeners.mixins.InitializationListener
import cl.ravenhill.keen.operators.selection.Selector

class GeneticAlgorithm<T, G, L>(
    populationConfiguration: GeneticPopulationConfiguration<T, G>,
    val selectionConfiguration: SelectionConfiguration<T, G, Genotype<T, G>>,
    val alterationConfiguration: AlterationConfiguration<T, G>,
    evolutionConfiguration: EvolutionConfiguration<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>
) : AbstractGeneBasedEvolutionaryAlgorithm<T, G, L>(
    populationConfiguration,
    evolutionConfiguration
), InitializerEngine<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> by GeneticInitializer(
    populationConfiguration,
    evolutionConfiguration
), EvaluationEngine<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> by GeneticEvaluator(
    populationConfiguration,
    evolutionConfiguration
)
        where G : Gene<T, G>, L : EvolutionListener<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> {

    override var state: GeneticEvolutionState<T, G> = evolutionConfiguration.initialState

    override suspend fun iterateGeneration(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G> {
        val interceptedStart = interceptor.before(state)
        val initializedState = initialize(interceptedStart)
        val evaluatedState = evaluate(initializedState)
        val parents = selectParents(evaluatedState)
        val survivors = selectSurvivors(evaluatedState)
        val offspring = alter(parents)
        val nextPopulation = survivors.population + offspring.population
        val nextGeneration = evaluate(evaluatedState.makeCopy(population = nextPopulation))
        return interceptor.after(nextGeneration).makeCopy(generation = nextGeneration.generation + 1)
    }

    private fun alter(parents: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G> {
        TODO("Not yet implemented")
    }

    private fun selectSurvivors(evaluatedState: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G> {
        TODO("Not yet implemented")
    }

    private fun selectParents(evaluatedState: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G> {
        TODO("Not yet implemented")
    }

    override val survivalRate: Double
        get() = TODO("Not yet implemented")
    override val parentSelector: Selector<T, G, Genotype<T, G>>
        get() = TODO("Not yet implemented")
    override val offspringSelector: Selector<T, G, Genotype<T, G>>
        get() = TODO("Not yet implemented")
}
