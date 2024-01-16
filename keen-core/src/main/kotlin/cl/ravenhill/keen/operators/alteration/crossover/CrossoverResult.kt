/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.keen.genetic.GeneticMaterial
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Represents the result of a crossover operation in genetic algorithms.
 *
 * ## Overview
 * `CrossoverResult` is a sealed interface that encapsulates the outcome of a crossover process between
 * genetic materials. It is designed to provide a generic, extendable structure for representing
 * crossover results, offering essential information such as the resulting genetic materials (subjects)
 * and the number of crossover events (crosses) that occurred. Being a sealed interface, it allows for
 * controlled expansion in types representing different kinds of crossover results.
 *
 * @param T The type of value that the genes represent.
 * @param G The gene type, must extend [Gene]<[T], [G]>.
 * @param M The type of the genetic material, such as a chromosome or a gene, extending
 *   [GeneticMaterial]<[T], [G]>.
 * @property subject The genetic materials (such as chromosomes or genes) that are the outcome of the
 *   crossover process.
 * @property crosses The total number of crossover events that occurred during the operation.
 */
sealed interface CrossoverResult<T, G, M> where G : Gene<T, G>, M : GeneticMaterial<T, G> {
    val subject: List<M>
    val crosses: Int
}

/**
 * Represents the result of a genotype crossover operation in a genetic algorithm.
 *
 * ## Overview
 * `GenotypeCrossoverResult` encapsulates the outcomes of crossover operations involving genotypes, a
 * core concept in genetic algorithms. This data class, conforming to the [CrossoverResult] interface,
 * provides both the resultant genotypes and the count of crossover events. It's essential in scenarios
 * where tracking the effects of crossover on genotypes and the extent of these operations is crucial
 * for the analysis and evolution of genetic algorithms.
 *
 * @param T The type of value that the genes represent.
 * @param G The gene type, extending `Gene<T, G>`.
 * @param subject The list of resulting genotypes from the crossover operation.
 * @param crosses The number of crossover events that occurred.
 */
data class GenotypeCrossoverResult<T, G>(
    override val subject: List<Genotype<T, G>>,
    override val crosses: Int,
) : CrossoverResult<T, G, Genotype<T, G>> where G : Gene<T, G>
