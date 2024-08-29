/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.config.AlterationConfiguration
import cl.ravenhill.keen.evolution.config.EvolutionConfiguration
import cl.ravenhill.keen.evolution.config.GeneticPopulationConfiguration
import cl.ravenhill.keen.evolution.config.SelectionConfiguration
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.operators.selection.Selector

class GeneticAlgorithm<T, G, L>(
    populationConfiguration: GeneticPopulationConfiguration<T, G>,
    val selectionConfiguration: SelectionConfiguration<T, G, Genotype<T, G>>,
    val alterationConfiguration: AlterationConfiguration<T, G>,
    evolutionConfiguration: EvolutionConfiguration<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>
) : AbstractGeneBasedEvolutionaryAlgorithm<T, G, L>(
    populationConfiguration,
    evolutionConfiguration
) where G : Gene<T, G>, L : EvolutionListener<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> {
    override var state: GeneticEvolutionState<T, G> = TODO()
    override fun iterateGeneration(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G> {
        TODO("Not yet implemented")
    }

    override val survivalRate: Double
        get() = TODO("Not yet implemented")
    override val parentSelector: Selector<T, G, Genotype<T, G>>
        get() = TODO("Not yet implemented")
    override val offspringSelector: Selector<T, G, Genotype<T, G>>
        get() = TODO("Not yet implemented")
}
