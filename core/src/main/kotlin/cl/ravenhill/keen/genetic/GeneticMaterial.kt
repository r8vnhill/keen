/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.mixins.MultiStringFormat
import cl.ravenhill.keen.mixins.Verifiable


/**
 * An interface representing the genetic material in a genetic algorithm.
 *
 * This interface extends both `Verifiable` and `MultiStringFormat`, combining the capabilities
 * of verification and multiple string representations. It is specifically designed for use in
 * genetic algorithms, encapsulating genetic information and operations on it.
 *
 * ## Features:
 * - **Verification**: Inherits the `verify` method from `Verifiable`, allowing for custom
 *   verification logic.
 * - **String Representations**: Inherits `toSimpleString` and `toDetailedString` from
 *   `MultiStringFormat`, providing flexibility in how genetic material is represented as a string.
 * - **Flattening**: Provides a method to flatten the genetic material into a list of its basic elements.
 *
 * ## Usage:
 * This interface is meant to be implemented by classes that represent genetic material, such as
 * chromosomes or genes. Implementors can provide custom logic for verification and string representation,
 * as well as define how the genetic material can be transformed.
 *
 * @param T The type of the genetic data.
 * @param G The specific type of `Gene` that encapsulates the genetic data.
 */
interface GeneticMaterial<T, G> : Verifiable, MultiStringFormat where G : Gene<T, G> {

    /**
     * Flattens the genetic material into a list of its basic elements.
     *
     * This method provides a way to transform the structured genetic material, such as a chromosome or a collection of
     * genes, into a flat list of its basic elements (e.g., the individual genetic values). It's particularly useful in
     * scenarios where operations or evaluations on the genetic algorithm need to be performed at the level of
     * individual genetic elements, rather than on the structured genetic material as a whole.
     *
     * ## Usage:
     * In the context of a genetic algorithm, this method can be used to access the individual elements of the genetic
     * material for processes like fitness evaluation, mutation, or crossover.
     *
     * ### Example:
     * ```kotlin
     * val geneticMaterial: GeneticMaterial<Char, CharGene> = // Some genetic material
     * val flattenedGenes = geneticMaterial.flatten() // Returns a List<Char> representing individual genes
     * ```
     * In this example, `flattenedGenes` will be a list of characters, each representing an individual gene within the
     * genetic material.
     *
     * @return A list of type [T] elements representing the flattened genetic material.
     */
    fun flatten(): List<T>

    fun <U> flatMap(f: (T) -> U): List<U> = flatten().map(f)
}
