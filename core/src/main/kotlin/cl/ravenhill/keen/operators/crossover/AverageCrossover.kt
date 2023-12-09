/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.genetic.genes.numeric.NumberGene


/**
 * A crossover operator that averages the genes of parent chromosomes to create offspring.
 *
 * `AverageCrossover` is a specialized form of `CombineCrossover` tailored for numeric genes. It averages the values of
 * corresponding genes from multiple parent chromosomes to produce a new set of genes for the offspring chromosome.
 * This type of crossover is particularly useful in scenarios where a blend of traits from the parents is desirable.
 *
 * ## Usage:
 * This operator is often used in numerical optimization problems or in situations where an intermediate value between
 * parents is more likely to yield a better solution.
 *
 * ### Example:
 * ```kotlin
 * val crossover = AverageCrossover<MyNumberType, MyNumberGene>(
 *     chromosomeRate = 0.5, // 50% chance for each chromosome to be involved in crossover
 *     geneRate = 0.5        // 50% chance for each gene to be averaged
 * )
 * val offspringChromosome = crossover.crossoverChromosomes(listOf(parentChromosome1, parentChromosome2))
 * ```
 * In this example, `AverageCrossover` is applied to two parent chromosomes. Each gene in the offspring chromosome is
 * an average of the corresponding genes in the parent chromosomes, depending on the crossover rates.
 *
 * @param T The numeric type of the gene's value.
 * @param G The specific type of `NumberGene`.
 * @param chromosomeRate The probability of a chromosome undergoing crossover, in the range [0.0, 1.0].
 * @param geneRate The probability of an individual gene within a chromosome being recombined, in the range [0.0, 1.0].
 */
class AverageCrossover<T, G>(
    chromosomeRate: Double = 1.0,
    geneRate: Double = 1.0,
) : CombineCrossover<T, G>(
    { genes: List<G> ->
        genes[0].average(genes.drop(1))
    },
    chromosomeRate,
    geneRate
) where T : Number, G : NumberGene<T, G>
