/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ExperimentalKeen
import cl.ravenhill.keen.genetic.genes.Gene

/**
 * A crossover operator that randomly selects genes from parent chromosomes to create offspring.
 *
 * `UniformCrossover` is a type of [CombineCrossover] specifically designed for scenarios where a random selection
 * of genes from a set of parent chromosomes is desirable. In this crossover method, each gene in the offspring
 * chromosome is randomly chosen from the corresponding genes of the parent chromosomes. This approach is useful in
 * maintaining genetic diversity within the population and is commonly used in various genetic algorithms.
 *
 * ## Usage:
 * This operator is typically employed in genetic algorithms where random, yet uniform, gene selection is crucial
 * for exploring a wide range of genetic combinations. It is effective in scenarios where avoiding bias towards any
 * particular parent's genetic makeup is important.
 *
 * ### Example:
 * ```kotlin
 * val uniformCrossover = UniformCrossover<MyType, MyGene>(
 *     chromosomeRate = 0.5, // 50% chance for each chromosome to be involved in crossover
 *     geneRate = 0.5        // 50% chance for each gene to be selected
 * )
 * val offspringChromosome = uniformCrossover.crossoverChromosomes(listOf(parentChromosome1, parentChromosome2))
 * ```
 * In this example, `UniformCrossover` is utilized to combine two parent chromosomes. The genes of the offspring
 * chromosome are randomly selected from the genes of the parent chromosomes, based on the specified crossover rates.
 *
 * @param T The type parameter representing the value type in the gene.
 * @param G The gene type, extending `Gene<T, G>`.
 * @param chromosomeRate The probability of a chromosome undergoing crossover, typically in the range [0.0, 1.0].
 */
@ExperimentalKeen
class UniformCrossover<T, G>(numParents: Int = 2, chromosomeRate: Double = 1.0, exclusivity: Boolean = false) :
    CombineCrossover<T, G>(
        { genes: List<G> ->
            genes.random(Domain.random)
        },
        chromosomeRate,
        1.0,
        numParents,
        exclusivity
    ) where G : Gene<T, G>
