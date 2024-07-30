/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.states

import cl.ravenhill.jakt.Jakt
import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.ranking.IndividualRanker

/**
 * Represents the state of evolution in a genetic algorithm, including the current generation, the individual ranking
 * strategy, and the population of individuals.
 *
 * @param T The type of value stored by the gene.
 * @param G The type of gene, which must implement the [Gene] interface.
 *
 * @property generation The current generation number in the evolutionary process. Must not be negative.
 * @property ranker The [IndividualRanker] used to rank individuals in the population.
 * @property population The [Population] of individuals participating in the evolutionary process.
 *
 * @constructor Initializes the class and checks that the generation number is non-negative.
 * @throws CompositeException If any of the constraints are violated.
 * @throws IntConstraintException If the generation number is negative and [Jakt.shortCircuit] is set to `true`.
 */
data class GeneticEvolutionState<T, G>(
    override val generation: Int,
    override val ranker: IndividualRanker<T, G, Genotype<T, G>>,
    override val population: Population<T, G, Genotype<T, G>>
) : EvolutionState<T, G, Genotype<T, G>> where G : Gene<T, G> {
    init {
        constraints {
            "Generation ($generation) must not be negative" { generation mustNot BeNegative }
        }
    }
}
