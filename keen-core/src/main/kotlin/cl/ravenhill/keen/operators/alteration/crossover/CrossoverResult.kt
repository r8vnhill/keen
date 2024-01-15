/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.keen.genetic.GeneticMaterial
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene


sealed interface CrossoverResult<T, G, M> where G : Gene<T, G>, M : GeneticMaterial<T, G> {
    val subject: List<M>
    val crosses: Int
}

data class GenotypeCrossoverResult<T, G>(
    override val subject: List<Genotype<T, G>>,
    override val crosses: Int,
) : CrossoverResult<T, G, Genotype<T, G>> where G : Gene<T, G>
