/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines.ga

import cl.ravenhill.keen.evolution.config.AlterationConfiguration
import cl.ravenhill.keen.evolution.config.EvolutionConfiguration
import cl.ravenhill.keen.evolution.config.GeneticPopulationConfiguration
import cl.ravenhill.keen.evolution.engines.AlterationEngine
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.listeners.mixins.AlterationListener

class GeneticAlterationEngine<T, G>(
    populationConfiguration: GeneticPopulationConfiguration<T, G>,
    evolutionConfiguration: EvolutionConfiguration<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>,
    alterationConfiguration: AlterationConfiguration<T, G>,
) : AlterationEngine<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> where G : Gene<T, G> {

    private val listeners = (evolutionConfiguration.listeners + evolutionConfiguration.limits.map { it.listener })
        .filterIsInstance<AlterationListener<*, *, *, GeneticEvolutionState<T, G>>>()

    override suspend fun alter(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G> {
        TODO("Not yet implemented")
    }
}
