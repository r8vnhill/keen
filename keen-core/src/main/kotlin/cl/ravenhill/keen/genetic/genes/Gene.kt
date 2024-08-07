/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.genetic.GeneticMaterial
import cl.ravenhill.keen.mixins.MultiStringFormat
import cl.ravenhill.keen.mixins.SelfReferential


/**
 * Represents a basic unit of genetic material in a genetic algorithm.
 *
 * This interface extends `GeneticMaterial`, `SelfReferential`, and `MultiStringFormat`,
 * providing functionalities relevant to genetic operations. It's designed to encapsulate
 * a single piece of genetic information, commonly known as a gene, in genetic algorithms.
 *
 * ## Key Features:
 * - **Genetic Value**: Holds the genetic data (`value`) of a specific type `T`.
 * - **Copy Mechanism**: Allows for creating a copy of the gene, potentially with a modified value.
 * - **Transformation and String Representations**: Inherits capabilities from `GeneticMaterial`
 *   and `MultiStringFormat`, allowing for transformation of genetic data and flexible string representations.
 *
 * ## Usage:
 * Implement this interface to create custom gene types for use in genetic algorithms. Each gene
 * holds a piece of genetic information and can be manipulated or evaluated as part of the algorithm's
 * evolutionary process.
 *
 * ### Example:
 * Implementing a custom gene:
 * ```kotlin
 * class MyGene(val myValue: Int) : Gene<Int, MyGene> {
 *     override val value = myValue
 *
 *     override fun duplicateWithValue(value: Int) = MyGene(value)
 *
 *     // Optional: Override other methods as needed
 * }
 * ```
 * In this example, `MyGene` implements `Gene` to hold an integer value. The `copy` method
 * allows creating new instances of `MyGene` with modified values.
 *
 * @param T The type of the genetic data held by the gene.
 * @param G The specific type of `Gene`, following the Curiously Recurring Template Pattern (CRTP).
 *
 * @property value The genetic data stored in the gene.
 *
 * @see GeneticMaterial
 * @see SelfReferential
 * @see MultiStringFormat
 * @author <https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @version 2.0.0
 * @since 2.0.0
 */
interface Gene<T, G> : GeneticMaterial<T, G>, SelfReferential<G>, MultiStringFormat where G : Gene<T, G> {
    val value: T

    /**
     * Generates a new value for the gene. By default, returns the current value.
     * Override to provide custom generation logic.
     *
     * @return A new value of type [T] for the gene.
     */
    fun generator(): T = value

    /**
     * Performs a mutation operation on the gene.
     *
     * This method is a key aspect of evolutionary algorithms, enabling genetic diversity and exploration of the search
     * space. It mutates the current gene's value using a predefined mutation strategy, encapsulated within the
     * [generator] method.
     *
     * The mutation process typically involves altering the gene's underlying data (DNA) to produce a new gene variant.
     * This can range from simple modifications, like flipping a boolean value or changing a number within a range, to
     * more complex transformations based on the specific requirements of the genetic algorithm.
     *
     * The method returns a new instance of the gene type [G] with the mutated value. This ensures immutability of
     * genes, where each mutation results in a new gene instance rather than modifying the existing one.
     *
     * ## Usage:
     * This method is usually called during the mutation phase of a genetic algorithm. Implementing classes
     * may override this method and/or the [generator] method to provide custom mutation logic.
     *
     * ### Example:
     * Assuming a gene representing a numeric value:
     * ```kotlin
     * class NumericGene(val number: Int) : Gene<Int, NumericGene> {
     *     override fun generator(): Int = number + Random.nextInt(-5, 6)
     *     override fun withGenes(value: Int): NumericGene = NumericGene(value)
     * }
     *
     * val gene = NumericGene(10)
     * val mutatedGene = gene.mutate() // Could result in a gene with value between 5 and 15
     * ```
     * In this example, `NumericGene` mutates by randomly adding or subtracting a value to its number.
     * The `mutate` method creates a new `NumericGene` with this updated value.
     *
     * @return A new instance of [G], representing the mutated gene.
     */
    fun mutate(): G = duplicateWithValue(generator())

    /**
     * Creates a new gene instance with a specified value.
     *
     * This method is crucial in evolutionary algorithms for creating new gene instances with specific values,
     * often used during crossover or mutation operations. It takes a given value of type [T] and produces
     * a new gene instance ([G]) that encapsulates this value. This process is commonly referred to as
     * duplication, but it's essential to note that it goes beyond mere copying; it involves creating a new
     * gene instance with potentially different characteristics or behaviors, depending on the implementation.
     *
     * ## Usage:
     * - In a crossover operation, this method can be used to create offspring genes with values derived from
     *   parent genes.
     * - In mutation operations, it can be used to create a new gene with a mutated value.
     *
     * ### Example:
     * Implementing `duplicateWithValue` in a simple gene class:
     * ```kotlin
     * class SimpleGene(val data: Int) : Gene<Int, SimpleGene> {
     *     override fun duplicateWithValue(value: Int): SimpleGene = SimpleGene(value)
     *     // Other methods
     * }
     *
     * val originalGene = SimpleGene(5)
     * val duplicatedGene = originalGene.duplicateWithValue(10)
     * // duplicatedGene is a new SimpleGene instance with data value 10
     * ```
     * In this example, `SimpleGene` implements the method to create a new `SimpleGene` instance with
     * the provided integer value. This allows for easy duplication of genes with modified values.
     *
     * @param value The value to be encapsulated in the new gene instance. This value is of type [T], which
     *   corresponds to the gene's data type.
     * @return A new instance of type [G] (the gene type) that contains the specified value.
     */
    fun duplicateWithValue(value: T): G

    /**
     * Flattens the gene into a list containing its genetic value.
     *
     * This function is part of the `Gene` interface and is used to transform the gene's value into a list format.
     * It is particularly useful in contexts where genes need to be processed or analyzed as a collection of values,
     * even if they contain only a single piece of genetic data. The `flatten` method ensures that genes can be
     * uniformly treated as collections, simplifying operations that work on genetic structures.
     *
     * ## Usage:
     * The `flatten` function can be used in various genetic algorithm operations, such as fitness evaluation, where
     * genes need to be aggregated or compared as collections of values.
     *
     * ### Example:
     * Assuming a gene representing a numeric value:
     * ```kotlin
     * class NumericGene(val number: Int) : Gene<Int, NumericGene> {
     *     // Implementation of other methods...
     * }
     *
     * val gene = NumericGene(10)
     * val flattenedValue = gene.flatten() // Returns [10]
     * ```
     * In this example, the `flatten` method transforms the `NumericGene`'s value into a list containing the single
     * integer value. This allows the gene's value to be treated as a collection, which can be useful in various
     * genetic algorithm contexts.
     *
     * @return A list containing the single genetic value of the gene.
     */
    override fun flatten(): List<T> = listOf(value)

    /**
     * Converts the value of the object to a simple string representation.
     *
     * @return The string representation of the object value.
     */
    override fun toSimpleString() = value.toString()

    /**
     * Returns a string representation of the object with additional details.
     *
     * @return A string representation of the object with its class name and the current value.
     */
    override fun toDetailedString() = "${this::class.simpleName}(value=$value)"
}
