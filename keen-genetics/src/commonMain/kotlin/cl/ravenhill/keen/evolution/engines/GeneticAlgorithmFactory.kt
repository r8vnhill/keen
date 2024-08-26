package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.config.GeneticPopulationConfiguration
import cl.ravenhill.keen.evolution.config.SelectionConfiguration
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.listeners.ListenerConfiguration

private typealias ListenerFactory<T, G> =
            (ListenerConfiguration<T, G, Genotype<T, G>>) ->
        EvolutionListener<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>

private typealias PopulationConfig<T, G> = GeneticPopulationConfiguration<T, G>

private typealias SelectionConfig<T, G> = SelectionConfiguration<T, G, Genotype<T, G>>

class GeneticAlgorithmFactory<T, G>
