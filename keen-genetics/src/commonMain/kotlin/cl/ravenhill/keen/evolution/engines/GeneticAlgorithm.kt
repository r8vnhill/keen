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

data class GeneticAlgorithm<T, G, L>(
    override val populationConfiguration: GeneticPopulationConfiguration<T, G>,
    val selectionConfiguration: SelectionConfiguration<T, G, Genotype<T, G>>,
    val alterationConfiguration: AlterationConfiguration<T, G>,
    val evolutionConfiguration: EvolutionConfiguration<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>, L>
) : AbstractGeneBasedEvolutionaryAlgorithm<T, G, L>(
    populationConfiguration
) where G : Gene<T, G>, L : EvolutionListener<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> {
    override val listeners: List<L>
        get() = TODO("Not yet implemented")
    override val populationSize: Int
        get() = TODO("Not yet implemented")
    override val survivalRate: Double
        get() = TODO("Not yet implemented")
    override val parentSelector: Selector<T, G, Genotype<T, G>>
        get() = TODO("Not yet implemented")
    override val offspringSelector: Selector<T, G, Genotype<T, G>>
        get() = TODO("Not yet implemented")
    override val limits: List<Limit<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>, L>>
        get() = TODO("Not yet implemented")
    override val currentState: GeneticEvolutionState<T, G>
        get() = TODO("Not yet implemented")

    override fun evolve(): GeneticEvolutionState<T, G> {
        TODO("Not yet implemented")
    }
}