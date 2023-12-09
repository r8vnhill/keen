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
 * - **Transformation**: Includes a `flatMap` method, enabling transformations of the genetic
 *   material.
 *
 * ## Usage:
 * This interface is meant to be implemented by classes that represent genetic material, such as
 * chromosomes or genes. Implementors can provide custom logic for verification and string representation,
 * as well as define how the genetic material can be transformed.
 *
 * @param T The type of the genetic data.
 * @param G The specific type of `Gene` that encapsulates the genetic data.
 *
 * @author <https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @version 2.0.0
 * @since 2.0.0
 */
interface GeneticMaterial<T, G> : Verifiable, MultiStringFormat where G : Gene<T, G> {

    /**
     * Transforms the genetic material using a specified function and returns a list of the transformed elements.
     *
     * The `flatMap` method applies a transformation function to the genetic material and returns the results
     * as a list. This method is useful for manipulating or analyzing the genetic data.
     *
     * @param transform A transformation function to be applied to the genetic data. Defaults to the identity function.
     * @return A list containing the transformed genetic data.
     */
    fun flatMap(transform: (T) -> T = { it }): List<T>
}
