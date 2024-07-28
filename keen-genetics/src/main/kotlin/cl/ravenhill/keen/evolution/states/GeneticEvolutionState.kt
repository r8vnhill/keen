/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.states

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.ranking.IndividualRanker

data class GeneticEvolutionState<T, G>(
    override val generation: Int,
    override val ranker: IndividualRanker<T, G, Genotype<T, G>>,
    override val population: Population<T, G, Genotype<T, G>>
) : EvolutionState<T, G, Genotype<T, G>> where G : Gene<T, G>
