/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.mixins.MultiStringFormat
import cl.ravenhill.keen.mixins.Verifiable


/**
 * Interface representing the encapsulation of genetic information in a genetic algorithm.
 *
 * `GeneticMaterial` serves as a foundational element in genetic algorithms, combining aspects of verification and
 * diverse string representation capabilities. It's designed for classes that encapsulate genetic data, like genes or
 * chromosomes, providing essential operations and transformation capabilities for such data.
 *
 * ## Key Features:
 * - **Verification**: Inherits from [Verifiable], enabling implementation of custom verification logic to ensure
 *   the integrity and validity of the genetic data.
 * - **String Representation**: Inherits from [MultiStringFormat], offering methods [toSimpleString] and
 *   [toDetailedString] for versatile textual representation of genetic material.
 * - **Flattening Genetic Data**: Includes a [flatten] method, which transforms structured genetic material into a list
 *   of basic elements, facilitating individual-level operations in genetic algorithms.
 * - **Mapping Elements**: Provides a [flatMap] method, allowing for transformation of the genetic material's elements
 *   into a list of another type, enhancing the versatility in processing genetic data.
 *
 * ## Applicability:
 * Ideal for use in classes that model genetic elements, enabling them to align with the operational needs of genetic
 * algorithms, such as verification, transformation, and diverse representation for analysis or debugging purposes.
 *
 * @param T The type of data encapsulated within the genetic material.
 * @param G The specific gene type, extending `Gene<T, G>`, representing the genetic data.
 */
interface GeneticMaterial<T, G> : Verifiable, MultiStringFormat where G : Gene<T, G> {

    /**
     * Transforms the genetic material into a list of its basic genetic elements.
     * Useful for processing or evaluating genetic data at the individual element level.
     *
     * @return A list of type [T], representing the flattened genetic elements.
     */
    fun flatten(): List<T>

    /**
     * Maps each element of the flattened genetic material to a different type, creating a transformed list.
     * This method extends the functionality of [flatten], enabling custom transformations on genetic elements.
     *
     * @param transform A transformation function applied to each flattened element of type [T].
     * @return A list of transformed elements of type [U].
     */
    fun <U> flatMap(transform: (T) -> U): List<U> = flatten().map(transform)
}

