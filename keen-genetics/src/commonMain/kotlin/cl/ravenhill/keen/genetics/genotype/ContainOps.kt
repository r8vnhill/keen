/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics.genotype

import cl.ravenhill.keen.genetics.chromosomes.Chromosome
import cl.ravenhill.keen.genetics.genes.Gene

/**
 * Provides operations for checking the containment of chromosomes within a genotype.
 *
 * The `ContainOps` interface extends the [Collection] interface and provides basic functionality to check whether
 * a genotype contains specific chromosomes or a collection of chromosomes. It operates on a list of [Chromosome]
 * instances, offering methods for efficient containment checks.
 *
 * ## Usage:
 * This interface is typically mixed into classes representing genotypes or genetic structures in evolutionary
 * algorithms. It allows you to verify the presence of certain chromosomes within the genotype.
 *
 * ### Example: Implementing ContainOps in a Genotype
 * ```kotlin
 * class Genotype<T, G>(override val chromosomes: List<Chromosome<T, G>>) : ContainOps<T, G> where G : Gene<T, G> {
 *     // Other Genotype-specific methods and properties
 * }
 *
 * val genotype = Genotype(listOf(chromosome1, chromosome2))
 * println(genotype.contains(chromosome1))  // Output: true
 * ```
 *
 * @param T The type of value held by the genes in the chromosomes.
 * @param G The type of gene within the chromosomes, which must extend [Gene].
 *
 * @property chromosomes The list of chromosomes within the genotype.
 */
interface ContainOps<T, G> : Collection<Chromosome<T, G>> where G : Gene<T, G> {

    val chromosomes: List<Chromosome<T, G>>

    /**
     * Checks if the genotype contains all the specified chromosomes.
     *
     * The `containsAll` method verifies whether all chromosomes in the provided collection are present within the
     * genotype. This is useful for checking if a specific subset of chromosomes exists in the genotype.
     *
     * @param elements The collection of chromosomes to check for containment.
     * @return `true` if the genotype contains all the specified chromosomes, `false` otherwise.
     */
    override fun containsAll(elements: Collection<Chromosome<T, G>>) = chromosomes.containsAll(elements)

    /**
     * Checks if the genotype contains the specified chromosome.
     *
     * The `contains` method checks whether a specific chromosome is present in the genotype. It returns `true` if
     * the chromosome is found, and `false` otherwise.
     *
     * @param element The chromosome to check for containment.
     * @return `true` if the genotype contains the specified chromosome, `false` otherwise.
     */
    override fun contains(element: Chromosome<T, G>) = chromosomes.contains(element)
}
