package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.mixins.Filterable
import cl.ravenhill.keen.mixins.Ranged
import cl.ravenhill.keen.utils.nextChar
import java.util.Objects

/**
 * Represents a gene with a character value in a genetic algorithm.
 *
 * This data class encapsulates a single character gene, with functionalities to generate, duplicate, and verify the
 * gene.
 * It also provides methods to represent the gene value as different data types. The gene operates within a specified
 * range of characters and can be filtered based on custom criteria.
 *
 * ## Usage:
 * ```
 * val charGene = CharGene('A')
 * val randomGene = charGene.generator()
 * val duplicateGene = charGene.duplicateWithValue('B')
 * val isValid = charGene.verify() // Checks if 'A' is within the range and passes the filter
 * val charValue = charGene.toChar()
 * val intValue = charGene.toInt()
 * val simpleString = charGene.toSimpleString()
 * ```
 * In this example, a `CharGene` is created and used to demonstrate its various functionalities, including
 * gene generation, duplication, verification, and representation.
 *
 * @property value The character value of the gene.
 * @property range The range of valid characters for the gene.
 * @property filter The lambda function for filtering valid characters.
 */
data class CharGene(
    override val value: Char,
    override val range: CharRange = ' '..'z',
    override val filter: (Char) -> Boolean = { true },
) : ComparableGene<Char, CharGene>, Ranged<Char>, Filterable<Char> {

    /**
     * Generates a random character within the specified range and adhering to the filter criteria.
     *
     * This method overrides the `generator` function ([Gene.generator]) in the context of a `CharGene`. It utilizes the
     * [Domain.random] object to produce a random character. The generated character will fall within the specified
     * [range] and satisfy the conditions specified by the [filter] lambda function. This functionality is crucial in
     * genetic algorithms for creating new gene instances or during mutation operations, where new, valid gene values
     * are required.
     *
     * ## Usage:
     * ```
     * val charGene = CharGene('A', 'a'..'z', { it.isLowerCase() })
     * val randomChar = charGene.generator() // Generates a random lowercase character
     * ```
     * In this example, `randomChar` will be a randomly generated lowercase character, adhering to the range and filter
     * provided in `charGene`.
     *
     * @return A random character within the defined range and filter criteria.
     */
    override fun generator() = Domain.random.nextChar(range, filter)


    /**
     * Creates a new instance of `CharGene` with the specified character value.
     *
     * This method overrides the `duplicateWithValue` function from the [Gene] interface. It allows for the creation
     * of a new `CharGene` instance with a specific character value. This functionality is particularly useful in
     * genetic algorithm operations such as mutation or crossover, where it is necessary to create a new gene instance
     * based on an existing gene but with a modified value.
     *
     * ## Usage:
     * ```
     * val originalGene = CharGene('A')
     * val newGene = originalGene.duplicateWithValue('B')
     * ```
     * In this example, `newGene` is a duplicate of `originalGene`, but with the character value 'B' instead of 'A'.
     *
     * Prefer using [copy] instead of this method when possible.
     *
     * @param value The character value for the new `CharGene` instance. This value should fall within the range and
     *   satisfy the filter criteria defined in the original gene.
     * @return A new `CharGene` instance with the specified character value.
     */
    override fun duplicateWithValue(value: Char) = copy(value = value)

    /**
     * Verifies if the gene's value is within the defined range and passes the filter criteria.
     *
     * This method checks whether the gene's character value falls within the specified `range` and satisfies the
     * conditions defined by the `filter` lambda function. Ensuring that the gene holds a valid value as per its
     * defined constraints is crucial for maintaining the integrity of the genetic algorithm process.
     *
     * ## Usage:
     * ```
     * val charGene = CharGene('A', 'a'..'z', { it.isLowerCase() })
     * val isValid = charGene.verify() // Returns false as 'A' is not a lowercase letter
     * ```
     * In this example, `isValid` will be `false` because the gene's value 'A' does not satisfy the filter criterion
     * (being a lowercase letter).
     *
     * @return `true` if the gene's value is within the range and passes the filter criteria; `false` otherwise.
     */
    override fun verify() = value in range && filter(value)

    /**
     * Converts the value of this gene to a character.
     *
     * @return The character value of this gene.
     */
    fun toChar() = value

    /**
     * Converts the gene's value to its string representation.
     *
     * ## Usage:
     * ```
     * val charGene = CharGene('A')
     * val stringValue = charGene.toSimpleString() // Returns "A"
     * ```
     * In this example, `stringValue` will be the string "A", representing the character value of `charGene`.
     *
     * @return A string representation of the gene's character value.
     */
    override fun toSimpleString() = value.toString()

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is CharGene -> false
        else -> value == other.value
    }

    override fun hashCode() = Objects.hash(CharGene::class, value)
}
