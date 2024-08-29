/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.config.EvolutionConfiguration
import cl.ravenhill.keen.evolution.config.GeneticPopulationConfiguration
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.listeners.Listener
import cl.ravenhill.keen.listeners.mixins.InitializationListener
import cl.ravenhill.keen.operators.selection.Selector

abstract class AbstractGeneBasedEvolutionaryAlgorithm<T, G, L>(
    populationConfiguration: GeneticPopulationConfiguration<T, G>,
    evolutionConfiguration: EvolutionConfiguration<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>
) : AbstractEvolver<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>(evolutionConfiguration) where G : Gene<T, G>,
                                                                                                     L : Listener {

    protected val genotypeFactory = populationConfiguration.genotypeFactory

    protected val populationSize = populationConfiguration.populationSize

    protected val interceptor = evolutionConfiguration.interceptor

    protected val initializationListeners =
        listeners.filterIsInstance<InitializationListener<*, *, *, GeneticEvolutionState<T, G>>>()

    abstract val survivalRate: Double
    abstract val parentSelector: Selector<T, G, Genotype<T, G>>
    abstract val offspringSelector: Selector<T, G, Genotype<T, G>>
}
