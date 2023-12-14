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
import cl.ravenhill.keen.utils.nextIntInRange
import java.util.*

/**
 * Represents a gene with an integer value, range, and a custom filter function.
 *
 * The `IntGene` class encapsulates a single integer value within a specified range and is subject to a custom filtering
 * condition. It is a concrete implementation of the `NumberGene` and `ComparableGene` interfaces and
 * is commonly used in gene-centric evolutionary algorithms where integer values are required.
 *
 * ## Key Features:
 * - **Value Range**: Each `IntGene` has a value that falls within a specified `range`, ensuring that the gene's
 *   value is always valid and meaningful in the context of the specific problem being solved.
 * - **Custom Filter**: A `filter` function can be applied to impose additional constraints or conditions on the
 *   gene's value.
 * - **Averaging**: The `average` method enables the calculation of an average value from a list of `IntGene` instances,
 *   useful in crossover operations in genetic algorithms.
 * - **Conversion Methods**: Provides methods to convert the gene's value to `Double` or `Int`, enhancing flexibility
 *   and ease of use in various computational contexts.
 *
 * ## Usage Scenarios:
 * - **Evolutionary Algorithms**: Ideal for use in genetic algorithms where the genetic representation involves
 *   integers, such as scheduling problems, optimization problems, or any scenario where discrete values are more
 *   appropriate than continuous ones.
 * - **Constraint Handling**: The ability to define a range and custom filter function allows for sophisticated
 *   constraint handling, making it suitable for problems with strict rules or conditions.
 * - **Simulation of Natural Processes**: Can be used in simulations where integer-based genetic material is more
 *   representative of the natural process being modeled.
 *
 * ### Example:
 * ```kotlin
 * // Creating an IntGene with a value within the range 0 to 10
 * val gene = IntGene(value = 5, range = 0..10)
 *
 * // Using a custom filter function to allow only even numbers
 * val evenGene = IntGene(value = 4, range = 0..10) { it % 2 == 0 }
 *
 * // Averaging a list of IntGenes
 * val genes = listOf(IntGene(2, 0..10), IntGene(4, 0..10), IntGene(6, 0..10))
 * val averageGene = genes[0].average(genes.drop(1)) // Results in an IntGene with the average value
 * ```
 * In these examples, `IntGene` is used to represent integer-based genetic material, with an optional filter for
 * custom constraints and the ability to compute an average from a collection of genes.
 *
 * @param value The integer value of the gene.
 * @param range The range within which the gene's value must fall.
 * @param filter An optional filter function for additional validation of the gene's value.
 */
data class IntGene(
    override val value: Int,
    override val range: ClosedRange<Int> = Int.MIN_VALUE..Int.MAX_VALUE,
    override val filter: (Int) -> Boolean = { true },
) : NumberGene<Int, IntGene>, ComparableGene<Int, IntGene>, Ranged<Int> {

    /**
     * Generates a random integer within the specified range of the gene.
     *
     * This function is a part of the `IntGene` class and is responsible for producing a random integer value that falls
     * within the gene's defined range. It utilizes the global random number generator from the [Domain] to ensure
     * consistency and potential reproducibility in the random number generation process.
     *
     * ## Usage:
     * The `generator` function is typically used in evolutionary algorithms during the initialization of the population
     * or during genetic operations like mutation, where new gene values need to be created randomly within the
     * specified constraints of the gene.
     *
     * ### Example:
     * ```kotlin
     * val intGene = IntGene(5, 1..10) // IntGene with a value range of 1 to 10
     * val randomValue = intGene.generator() // Generates a random integer within 1 to 10
     * ```
     * In this example, `generator` is called on an instance of `IntGene` with a value range of 1 to 10. The function
     * returns a random integer within this range, which can be used for initializing new genes or applying mutations.
     *
     * @return A randomly generated integer value within the range specified by the `IntGene`.
     */
    override fun generator() = Domain.random.nextIntInRange(range)

    /**
     * Creates a duplicate of the current gene with the specified value.
     *
     * This function is a part of the `IntGene` class. It allows for the creation of a new `IntGene` instance that is a
     * duplicate of the current gene but with a different value. This is particularly useful in genetic operations where
     * a gene's value needs to be modified while preserving the other properties (like the range and filter function) of
     * the gene.
     *
     * ## Usage:
     * The `duplicateWithValue` function is typically used in evolutionary algorithms during mutation or crossover
     * operations where new gene values are created based on existing genes.
     *
     * ### Example:
     * ```kotlin
     * val originalGene = IntGene(5, 1..10)
     * val mutatedGene = originalGene.duplicateWithValue(7) // Creates a new gene with value 7
     * ```
     * In this example, `duplicateWithValue` is used to create a new `IntGene` instance from `originalGene`
     * with a new value (7), while retaining the same range and filter function as the original gene.
     *
     * @param value The new integer value for the duplicated gene.
     * @return A new `IntGene` instance with the specified value and the same range and filter function as
     *   the original gene.
     */
    override fun duplicateWithValue(value: Int) = copy(value = value)

    /**
     * Verifies whether the gene's value is valid within its defined constraints.
     *
     * This function is an integral part of the `IntGene` class. It checks if the gene's value is within the specified
     * range and satisfies the additional filter conditions, ensuring that the gene represents a valid and acceptable
     * state within the context of the genetic algorithm.
     *
     * ## Key Checks Performed:
     * - **Range Validation**: Confirms that the gene's value falls within the specified closed range.
     * - **Filter Function Application**: Applies the custom filter function to the gene's value, allowing for
     *   additional, user-defined validation criteria.
     *
     * ## Usage:
     * This method is primarily used internally within the evolutionary computation framework to ensure the validity of
     * genes, especially during the creation, mutation, and crossover phases.
     *
     * ### Example:
     * ```kotlin
     * val gene = IntGene(5, 1..10, filter = { it % 2 == 0 })
     * val isValid = gene.verify() // Returns false as 5 is not an even number
     * ```
     * In this example, `verify` checks if the `IntGene` instance's value is within its defined range (1 to 10)
     * and satisfies the custom filter condition (being an even number). The result is `false` since the value 5
     * does not meet the filter criteria.
     *
     * @return `true` if the gene's value is within the range and satisfies the filter function, `false` otherwise.
     */
    override fun verify() = value in range && filter(value)

    /**
     * Computes the average of a list of `IntGene` instances, including the current gene.
     *
     * This method calculates the average value of a collection of `IntGene` instances and returns a new `IntGene`
     * instance with this average value. It's a fundamental operation in evolutionary algorithms, particularly in
     * crossover and mutation processes, where blending or averaging genetic information from multiple genes is
     * essential.
     *
     * ## Constraints:
     * - The list of genes provided for averaging must not be empty.
     *
     * ## Usage:
     * This method is often used in the crossover phase of genetic algorithms to combine genetic information from
     * multiple parents. It can also be utilized in mutation operations to create variations based on the average
     * values of certain genes.
     *
     * ### Example:
     * ```kotlin
     * val genes = listOf(IntGene(2, 1..10), IntGene(4, 1..10), IntGene(6, 1..10))
     * val averageGene = genes[0].average(genes.drop(1)) // Results in an IntGene with the average value of 4
     * ```
     * In this example, `average` is used to calculate the average value of a list of `IntGene` instances. The resulting
     * `IntGene` has a value that is the average of the values from the list of genes.
     *
     * @param genes A list of `IntGene` instances to average along with the current gene.
     * @return A new `IntGene` instance with the average value calculated from the provided list of genes.
     * @throws CompositeException containing a [CollectionConstraintException] if the list of genes is empty.
     */
    @Throws(CompositeException::class, CollectionConstraintException::class)
    override fun average(genes: List<IntGene>): IntGene {
        constraints { "The list of genes cannot be empty" { genes mustNot BeEmpty } }
        return copy(
            value = genes.fold(value.toDouble() / (genes.size + 1)) { acc, gene ->
                acc + gene.toDouble() / (genes.size + 1)
            }.toInt()
        )
    }

    /**
     * Converts the current value of the object to a double.
     *
     * @return The double representation of the value.
     */
    override fun toDouble() = value.toDouble()

    /**
     * Converts the given value to an integer.
     *
     * @return The integer representation of the value.
     */
    override fun toInt() = value

    /**
     * Provides a string representation of the `IntGene` instance.
     *
     * ## Format:
     * The string representation is formatted as:
     * ```
     * "IntGene(value=<value>, range=<range>)"
     * ```
     * where `<value>` is replaced with the actual integer value of the gene, and `<range>` is replaced with the
     * string representation of the gene's permissible range.
     *
     * @return A string representation of the `IntGene` instance, including its value and range.
     */
    override fun toString() = "IntGene(value=$value, range=$range)"


    /**
     * Provides a detailed string representation of the `IntGene` instance.
     *
     * ## Format:
     * The string representation includes the gene's value, range, and a representation of the filter function. The
     * format is as follows:
     * ```
     * "IntGene(value=<value>, range=<range>, filter=<filter@hashcode>)"
     * ```
     * - `<value>` is the actual integer value of the gene.
     * - `<range>` is the string representation of the gene's permissible range.
     * - `<filter@hashcode>` is a representation of the filter function, including its identity hash code for
     *   uniqueness.
     *
     * @return A detailed string representation of the `IntGene` instance, including its value, range, and filter
     *   function.
     */
    override fun toDetailedString() =
        "IntGene(value=$value, range=$range, filter=$filter@${System.identityHashCode(filter)})"

    /**
     * Determines whether another object is equal to this `IntGene` instance.
     *
     * The `equals` method in `IntGene` is overridden to provide a custom equality check. This method determines
     * equality based on the gene's value, range, and class type. It ensures that two `IntGene` instances are considered
     * equal only if they have the same value and fall within the same range.
     *
     * ## Process:
     * 1. **Identity Check**: If the compared object is the same instance as this object, they are immediately
     *   considered equal.
     * 2. **Type Check**: The method checks if the other object is an instance of `IntGene`. If not, they are not equal.
     * 3. **Class Equality Check**: Verifies that the other object is exactly the same class as `IntGene`.
     * 4. **Value and Range Check**: Compares the value and the range (both start and end) of the two `IntGene`
     *   instances.
     *
     * @param other The object to be compared for equality with this `IntGene`.
     * @return `true` if the other object is an `IntGene` with the same value and range, `false` otherwise.
     */
    override fun equals(other: Any?) = when {
        other === this -> true
        other !is IntGene -> false
        other::class != IntGene::class -> false
        other.value != value -> false
        other.range.start != range.start -> false
        other.range.endInclusive != range.endInclusive -> false
        else -> true
    }

    /**
     * Computes the hash code for this `IntGene` instance.
     *
     * ## Importance:
     * Overriding `hashCode` is crucial for `IntGene` instances to function correctly in hash-based collections, such as
     * `HashSet` or `HashMap`. This method ensures that `IntGene` instances can be reliably used as keys in maps or
     * elements in sets while maintaining the expected behavior of these collections.
     *
     * ## Calculation:
     * The hash code is calculated using `Objects.hash`, which combines the hash codes of the class type
     * (`IntGene::class`), the gene's value, and the start and end values of its range. This combination ensures a
     * distribution of hash codes that reduces the likelihood of collisions in hash tables.
     *
     * @return The hash code value for this `IntGene` instance.
     */
    override fun hashCode() = Objects.hash(IntGene::class, value, range.start, range.endInclusive)
}
