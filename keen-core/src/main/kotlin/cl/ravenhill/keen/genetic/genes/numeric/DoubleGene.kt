/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.genes.numeric

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.genetic.genes.ComparableGene
import cl.ravenhill.keen.mixins.Ranged
import cl.ravenhill.keen.utils.nextDoubleInRange
import java.util.*


/**
 * Represents a gene with a double value within a specified range.
 *
 * `DoubleGene` is a data class that implements both `NumberGene` and `ComparableGene` interfaces, tailored specifically
 * for genes with double values. It also implements `Ranged` to define a valid range for the gene's value. This class is
 * suitable for evolutionary algorithms where genes represent floating-point numbers within certain bounds.
 *
 * ## Usage:
 * This class can be used in scenarios where genes are represented by floating-point numbers and certain constraints
 * (like range limits) are necessary.
 *
 * ### Example:
 * ```
 * val myGene = DoubleGene(5.0, 0.0..10.0) // A DoubleGene with a value of 5.0, within the range 0.0 to 10.0
 * val mutatedGene = myGene.mutate() // Mutates within the specified range
 * ```
 * In this example, `myGene` represents a `DoubleGene` with specific constraints. The `mutate` method will generate
 * a new `DoubleGene` with a mutated value that respects the defined range and filter criteria.
 *
 * @property value The double value encapsulated by the gene.
 * @property range The range within which the gene's value is valid. Defaults to the full range of double values.
 * @property filter A filter function that defines additional constraints on the gene's value. Defaults to allowing
 *   all values.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @version 2.0.0
 * @since 1.0.0
 */
data class DoubleGene(
    override val value: Double,
    override val range: ClosedRange<Double> = -Double.MAX_VALUE..Double.MAX_VALUE,
    override val filter: (Double) -> Boolean = { true },
) : NumberGene<Double, DoubleGene>, ComparableGene<Double, DoubleGene>, Ranged<Double> {

    /**
     * Calculates the arithmetic mean of the values of a list of `DoubleGene` objects, including the calling gene.
     *
     * This method computes the average value of a collection of `DoubleGene` instances by summing their values and
     * dividing by the total number of genes, including the gene on which this method is invoked. It provides a way to
     * combine the genetic information from a group of genes into a single representative value, which can be useful
     * in various evolutionary algorithm operations like crossover or selection.
     *
     * ## Constraints:
     * - The list of genes provided must not be empty. An attempt to calculate the average with an empty list will
     *   result in a [CompositeException] containing a [CollectionConstraintException].
     *
     * ## Usage:
     * This method is typically used in genetic operations where a representative value from a group of genes is needed.
     * For instance, during crossover, the average value of parent genes might be used to create a new offspring gene.
     *
     * ### Example:
     * ```
     * val genes = listOf(DoubleGene(2.0), DoubleGene(4.0), DoubleGene(6.0))
     * val currentGene = DoubleGene(8.0)
     * val averageGene = currentGene.average(genes) // Returns a DoubleGene with average value (5.0 in this case)
     * ```
     * In this example, the average value of the genes (including the current gene) is calculated, resulting in a
     * `DoubleGene` instance with the average value.
     *
     * @param genes A list of `DoubleGene` objects from which to calculate the average.
     * @return A new `DoubleGene` instance representing the average value of the genes in the list, including the
     *   calling gene's value.
     * @throws CompositeException containing [CollectionConstraintException] if the list of genes is empty.
     */
    @Throws(CompositeException::class)
    override fun average(genes: List<DoubleGene>): DoubleGene {
        constraints { "The list of genes must not be empty" { genes mustNot BeEmpty } }
        return duplicateWithValue((value + genes.sumOf { it.value }) / (genes.size + 1))
    }


    /**
     * Converts the value to a [Double].
     *
     * @return the [Double] representation of the value.
     */
    override fun toDouble() = value

    /**
     * Converts the value of the object to an integer.
     *
     * @return the integer representation of the value
     */
    override fun toInt() = value.toInt()

    /**
     * Generates a random double value within the specified range of the gene.
     *
     * This method is an essential part of the genetic mutation process. It produces a new random value that falls
     * within the predefined range of the gene, facilitating the exploration of the genetic search space. The
     * randomness is sourced from the [Domain.random] object, ensuring variability in the evolutionary algorithm's
     * evolution process.
     *
     * ## Range:
     * The range within which the random value is generated is determined by the `range` property of the gene.
     * This range is a [ClosedRange]<[Double]>, which includes both its endpoints, providing control over the
     * span of possible values that the gene can assume.
     *
     * ## Usage:
     * This method is typically called during gene mutation operations where a new, random but constrained value
     * is required. The generated value respects the gene's range constraints, ensuring that the new value remains
     * valid within the context of the gene's defined characteristics.
     *
     * ### Example:
     * ```
     * val gene = DoubleGene(2.5, 1.0..4.0)
     * val randomValue = gene.generator() // Generates a random double between 1.0 and 4.0 (inclusive)
     * ```
     * In this example, `generator` produces a random double value within the range of 1.0 to 4.0, as defined
     * by the `DoubleGene` instance.
     *
     * @return A random double value that is within the gene's defined range.
     */
    override fun generator() = Domain.random.nextDoubleInRange(range)

    /**
     * Creates a new [DoubleGene] instance by replicating the current gene's properties, but with a specified value.
     *
     * This method is pivotal in genetic operations where a gene's structure is to be preserved while altering its
     * value. The method maintains the original gene's range and filter constraints, ensuring that the new gene adheres
     * to the same genetic rules as the original. This is crucial in maintaining consistency in the evolutionary
     * algorithm's process, particularly during mutation and crossover operations.
     *
     * ## Process:
     * - A new [DoubleGene] instance is created with the given [value].
     * - The [range] and [filter] properties from the current gene are carried over to the new gene.
     *
     * ## Usage:
     * This method can be used in scenarios where a new gene variant is needed, with a specific value, but it is
     * essential to retain the original gene's constraints.
     *
     * ### Example:
     * ```
     * val originalGene = DoubleGene(2.5, 1.0..4.0)
     * val newGene = originalGene.duplicateWithValue(3.5) // New gene with value 3.5, same range and filter as original
     * ```
     * In this example, `duplicateWithValue` is used to create a new `DoubleGene` with a different value while
     * preserving the range and filter of the original gene.
     *
     * @param value The new value for the duplicated gene. This value should ideally comply with the gene's range and
     *   filter constraints, although this method does not enforce these constraints directly.
     * @return A new [DoubleGene] instance with the specified value, and the same range and filter as the original gene.
     */
    override fun duplicateWithValue(value: Double) = copy(value = value)


    /**
     * Validates the gene's value against its defined range and filter criteria.
     *
     * This method is essential for maintaining the integrity of the genetic material in an evolutionary algorithm.
     * It checks two primary conditions:
     * 1. The gene's value falls within the specified [range].
     * 2. The gene's value satisfies the specified [filter] function.
     *
     * The method returns `true` only if both conditions are met, ensuring that the gene's value is not only within
     * the acceptable numerical range but also adheres to any additional constraints imposed by the [filter].
     *
     * ## Usage:
     * `verify` is typically used in scenarios where it's crucial to ascertain the validity of a gene's value before
     * proceeding with genetic operations like selection, crossover, or mutation. This ensures that only genes with
     * valid values contribute to the evolutionary process.
     *
     * ### Example:
     * ```
     * val gene = DoubleGene(5.0, 1.0..10.0) { it % 2 == 0 } // Gene with value 5, range 1..10, and even-number filter
     * val isValid = gene.verify() // Returns false as 5 is not an even number
     * ```
     * In this example, the `verify` method checks if the value of `gene` (5.0) is within the range 1.0 to 10.0 and
     * passes the filter condition (being an even number). As the value 5.0 does not satisfy the filter condition,
     * `verify` returns `false`.
     *
     * @return `true` if the gene's value is within the defined `range` and satisfies the `filter` condition; `false`
     *   otherwise.
     */
    override fun verify() = value in range && filter(value)


    /**
     * Returns a string representation of the DoubleGene object.
     *
     * @return A string representation of the DoubleGene object, including its value and range.
     */
    override fun toString() = "DoubleGene(value=$value, range=$range)"

    /**
     * Returns a detailed string representation of the DoubleGene object.
     *
     * @return A string representation of the DoubleGene object including its value,
     *   range, and filter.
     */
    override fun toDetailedString() = "DoubleGene(value=$value, range=$range, filter=$filter)"

    /**
     * Determines whether the provided object is equal to this `DoubleGene`.
     *
     * Equality for a `DoubleGene` is defined based on the gene's value and its range. This method compares the
     * current gene with another object to ascertain if they are equivalent. Two `DoubleGene` instances are considered
     * equal if they have the same value and the same range.
     *
     * The method follows these checks in order:
     * 1. Identity Check: Returns `true` if the current instance and the `other` object are the same instance.
     * 2. Type Check: Returns `false` if the `other` object is not a `DoubleGene`.
     * 3. Value Equality: Compares the value of the current gene with the value of the `other` gene.
     * 4. Range Equality: Compares the range of the current gene with the range of the `other` gene.
     *
     * ## Usage:
     * This method is generally used implicitly in collections, sorting algorithms, or whenever equality checks
     * between two gene instances are necessary.
     *
     * ### Example:
     * ```
     * val gene1 = DoubleGene(5.0, 1.0..10.0)
     * val gene2 = DoubleGene(5.0, 1.0..10.0)
     * val isEqual = gene1.equals(gene2) // Returns true as both have the same value and range
     * ```
     * In this example, `gene1` and `gene2` are considered equal because they share the same value (5.0) and
     * the same range (1.0 to 10.0).
     *
     * @param other The object to be compared with this `DoubleGene` for equality.
     * @return `true` if the provided object is a `DoubleGene` with the same value and range as this gene; `false`
     *   otherwise.
     */
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is DoubleGene -> false
        value != other.value -> false
        else -> range == other.range
    }


    /**
     * Calculates the hash code for the instance of `DoubleGene`.
     *
     * This method calculates the hash code for the `DoubleGene` object based on the values of its properties.
     * The hash code is computed using the [Objects.hash] method, which combines the hash codes of its arguments.
     * The arguments used for computing the hash code are the `DoubleGene` class, the `value` property, and the `range`
     * property.
     *
     * @return The hash code for the instance of `DoubleGene`.
     */
    override fun hashCode() = Objects.hash(DoubleGene::class, value, range)
}