/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.genes.numeric

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.mixins.Filterable


/**
 * An interface representing a gene with a numeric value in genetic algorithms.
 *
 * `NumberGene` extends the [Gene] interface, specifically tailored for numeric types. It includes additional
 * functionalities suitable for numeric genetic material, such as averaging and numeric conversions. It also implements
 * [Filterable] to provide a filtering capability based on numeric criteria.
 *
 * ## Features:
 * - **Numeric Operations**: Provides methods like `average`, `toDouble`, and `toInt` for numeric manipulations.
 * - **Filterable**: Inherits the `filter` property from [Filterable], allowing genes to be filtered based on a numeric
 *   condition.
 * - **Mutation**: Overrides the `mutate` method to ensure that the mutated value adheres to the specified filter
 *   condition.
 *
 * ## Usage:
 * This interface is useful for evolutionary algorithms where the genes represent numeric values, such as in
 * optimization problems. Implementations can specify the numeric type ([T]) and provide custom logic for numeric
 * operations and mutation behaviors.
 *
 * ### Example:
 * Implementing a `NumberGene` for integer values:
 * ```kotlin
 * class IntGene(override val value: Int) : NumberGene<Int, IntGene>, Gene<Int, IntGene> {
 *     override val filter: (Int) -> Boolean = { it in 0..100 } // A filter condition
 *
 *     override fun average(genes: List<IntGene>): IntGene {
 *         val avg = genes.sumOf { it.value } / genes.size
 *         return IntGene(avg)
 *     }
 *
 *     override fun toDouble() = value.toDouble()
 *     override fun toInt() = value
 * }
 * ```
 * In this example, `IntGene` represents a gene with an integer value. It provides an average calculation for
 * a list of `IntGene` and conversion methods. The mutation logic ensures that mutated values adhere to the
 * filter condition.
 *
 * @param T The numeric type of the gene's value.
 * @param G The specific type of `NumberGene`.
 * @property filter A function defining the condition for filtering numeric values.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface NumberGene<T, G> : Gene<T, G>, Filterable<T> where T : Number, G : NumberGene<T, G> {
    override val filter: (T) -> Boolean

    /**
     * Mutates the gene's value, producing a new gene instance that complies with the defined filter condition.
     *
     * This method is crucial for introducing genetic variations while ensuring that the mutations remain within
     * acceptable boundaries as specified by the `filter` property. It generates a sequence of potential mutated values
     * using the [generator] function and then selects the first value that passes the filter criteria. This approach
     * guarantees that the mutated gene adheres to the predefined constraints, maintaining the integrity and validity
     * of the genetic material in the evolutionary process.
     *
     * ## Process:
     * 1. **Mutation Generation**: Utilizes the [generator] to produce a sequence of potential mutated values.
     * 2. **Filter Application**: Applies the [filter] to this sequence, ensuring that only acceptable mutations are
     *    considered.
     * 3. **Mutation Selection**: Selects the first value from the filtered sequence as the new gene value.
     *
     * ## Usage:
     * This method is typically invoked during the mutation phase of an evolutionary algorithm, where genes are randomly
     * altered to explore new genetic variations. Implementations of this method can vary based on the nature of the
     * gene and the specific requirements of the evolutionary algorithm.
     *
     * ### Example:
     * Assuming a gene representing an integer value with a filter for positive numbers:
     * ```kotlin
     * class PositiveIntGene(override val value: Int) : NumberGene<Int, PositiveIntGene> {
     *     override val filter: (Int) -> Boolean = { it > 0 }
     *     override fun generator() = Random.nextInt()
     *     // Other implementations...
     * }
     *
     * val gene = PositiveIntGene(-5)
     * val mutatedGene = gene.mutate() // Ensures the mutated gene has a positive value
     * ```
     * In this example, `mutate` method ensures that the new `PositiveIntGene` instance has a positive value, adhering
     * to the filter condition.
     *
     * @return A new gene instance of type [G] with the mutated value, complying with the specified filter condition.
     */
    override fun mutate() = duplicateWithValue(
        generateSequence { generator() }
            .filter(filter)
            .first()
    )

    /**
     * Computes the arithmetic mean of a collection of genes and returns a new gene instance representing this average.
     *
     * This method aggregates the numeric values from a list of genes and calculates their arithmetic mean.
     * It is particularly useful in evolutionary algorithms for operations like crossover or mutation where a new gene
     * is needed that represents the average characteristics of a group of parent genes. This approach can help in
     * maintaining genetic diversity or converging towards optimal solutions.
     *
     * The method encapsulates the calculated average in a new gene instance, ensuring immutability and preserving the
     * integrity of the original genes. The type of the returned gene is the same as the type of the genes in the input
     * list.
     *
     * ## Usage:
     * This method is often used during the genetic operations that require combining the traits of multiple genes,
     * such as during crossover or population initialization.
     *
     * ### Example:
     * Assuming a list of genes representing integer values:
     * ```kotlin
     * val genes = listOf(IntGene(10), IntGene(20), IntGene(30))
     * val averageGene = genes.average() // Returns an IntGene with the average value (20 in this case)
     * ```
     * In this example, `average` calculates the mean of the values (10, 20, 30) and returns a new `IntGene`
     * with this average value.
     *
     * @param genes A list of genes of type [G] from which to calculate the average. Assumes that the gene type [G]
     *   encapsulates a numeric value.
     * @return A new gene instance of type [G] that represents the average value of the genes provided in the input
     *   list.
     */
    fun average(genes: List<G>): G

    /**
     * Converts the gene's value to a double.
     *
     * @return The double representation of the gene's value.
     */
    fun toDouble(): Double

    /**
     * Converts the gene's value to an integer.
     *
     * @return The integer representation of the gene's value.
     */
    fun toInt(): Int

    /**
     * Converts the gene's numeric value to its simple string representation.
     *
     * This method overrides the default `toString` behavior to provide a more concise and focused string representation
     * of a `NumberGene`. It outputs the string form of the gene's numeric value, which can be useful for logging,
     * debugging, or displaying the gene in a readable format.
     *
     * @return A string representation of the gene's numeric value.
     */
    override fun toSimpleString() = value.toString()
}