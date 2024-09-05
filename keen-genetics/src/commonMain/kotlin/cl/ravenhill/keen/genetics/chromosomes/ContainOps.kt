/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics.chromosomes

import cl.ravenhill.keen.genetics.genes.Gene

/**
 * Provides containment operations for chromosomes in an evolutionary algorithm.
 *
 * The `ContainOps` interface defines methods for checking the presence of genes within a chromosome. It offers basic
 * operations for verifying whether the chromosome contains a specific gene or a collection of genes. This interface is
 * useful in genetic algorithms where chromosomes are composed of multiple genes, and operations need to be performed to
 * verify the integrity or structure of the chromosome.
 *
 * @param T The type of value held by the genes.
 * @param G The type of gene within the chromosome, which must extend [Gene].
 *
 * @property genes The list of genes contained in the chromosome.
 */
interface ContainOps<T, G> : Collection<G> where G : Gene<T, G> {

    val genes: List<G>

    /**
     * Checks if the chromosome contains all the specified genes.
     *
     * The `containsAll` method verifies whether all genes in the provided collection are present in the chromosome.
     * This is useful for checking if a specific set of genes exists in the chromosome.
     *
     * @param elements The collection of genes to check for.
     * @return `true` if the chromosome contains all the specified genes, `false` otherwise.
     */
    override fun containsAll(elements: Collection<G>) = genes.containsAll(elements)

    /**
     * Checks if the chromosome contains the specified gene.
     *
     * The `contains` method checks whether a specific gene is present in the chromosome. It returns `true` if the
     * gene is found, and `false` otherwise.
     *
     * @param element The gene to check for.
     * @return `true` if the chromosome contains the specified gene, `false` otherwise.
     */
    override fun contains(element: G) = genes.contains(element)
}
