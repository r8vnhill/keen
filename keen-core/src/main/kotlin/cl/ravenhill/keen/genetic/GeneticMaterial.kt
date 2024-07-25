/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.mixins.FlatMappable
import cl.ravenhill.keen.mixins.MultiStringFormat
import cl.ravenhill.keen.mixins.Verifiable


/**
 * Represents genetic material in an evolutionary algorithm.
 *
 * The `GeneticMaterial` interface extends the [Verifiable] and [FlatMappable] interfaces, providing a contract
 * for types that encapsulate genetic material and support verification and flat mapping operations. It includes
 * a default implementation of the `flatMap` method.
 *
 * ## Usage:
 * This interface is intended to be implemented by classes that manage genetic material, such as chromosomes or
 * genotypes, in evolutionary algorithms. It ensures that the implementing types can be verified for integrity and
 * support flat mapping of their elements.
 *
 * ### Example:
 * ```
 * class MyGeneticMaterial<T, G : Gene<T, G>>(val genes: List<G>) : GeneticMaterial<T, G> {
 *     override fun verify(): Boolean {
 *         // Verification logic
 *         return true
 *     }
 *
 *     override fun flatten(): List<T> {
 *         // Flattening logic
 *         return genes.map { it.value }
 *     }
 * }
 * ```
 * In this example, `MyGeneticMaterial` implements the `GeneticMaterial` interface, providing specific logic for
 * verification and flattening of genetic material.
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 */
interface GeneticMaterial<T, G> : Verifiable, FlatMappable<T> where G : Gene<T, G> {

    /**
     * Applies a transformation function to each element in the genetic material and returns a list of the results.
     *
     * This default implementation of `flatMap` flattens the genetic material and then applies the transformation
     * function to each element.
     *
     * ### Example:
     * ```
     * val geneticMaterial = MyGeneticMaterial(...)
     * val transformedElements = geneticMaterial.flatMap { geneValue ->
     *     // Transformation logic
     *     geneValue.toString()
     * }
     * ```
     * In this example, the `flatMap` method is used to convert each gene value to its string representation.
     *
     * @param U The type of elements in the resulting list.
     * @param transform The transformation function to apply to each element.
     * @return A list of transformed elements.
     */
    override fun <U> flatMap(transform: (T) -> U): List<U> = flatten().map(transform)
}
