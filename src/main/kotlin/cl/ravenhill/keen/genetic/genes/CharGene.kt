/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.util.Ranged
import cl.ravenhill.keen.util.nextChar
import java.util.Objects

/**
 * A gene that represents a character value.
 *
 * The `CharGene` class implements the [ComparableGene] interface and represents a single gene in
 * a chromosome.
 * A `CharGene` instance contains a single character value within a specified [range] and [filter]
 * function.
 * The [generator] function produces a new character value within the specified [range] and that
 * satisfies the [filter].
 * The [withDna] function returns a new `CharGene` instance with the specified character value and
 * the same range and filter as the original.
 *
 * @property dna The character value represented by the gene.
 * @property range The range of possible character values for the gene.
 * @property filter The function used to filter the possible character values for the gene.
 *
 * @constructor Creates a new `CharGene` instance with the specified character value ([dna]),
 *  [range] and [filter].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
class CharGene(
    override val dna: Char,
    override val range: CharRange = ' '..'z',
    val filter: (Char) -> Boolean = { true },
) : ComparableGene<Char, CharGene>, Ranged<Char> {

    // Documentation inherited from Gene
    override fun generator() = Core.random.nextChar(range, filter)

    // Documentation inherited from Gene
    override fun withDna(dna: Char) = CharGene(dna, range, filter)

    /**
     * Verifies if the given DNA sequence is within the specified range and satisfies the filter
     * criteria.
     */
    override fun verify() = dna in range && filter(dna)

    // region : -===================== TYPE CONVERSIONS ===========================================-
    /**
     * Converts the gene to a 16-bit integer ([Char])
     */
    fun toChar() = dna

    /**
     * Converts the gene to a 32-bit integer ([Int])
     */
    fun toInt() = dna.code

    // Documentation inherited from Any
    override fun toString() = "$dna"
    // endregion TYPE CONVERSIONS

    // Documentation inherited from Any
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is CharGene -> false
        other::class != this::class -> false
        else -> dna == other.dna
    }

    // Documentation inherited from Any
    override fun hashCode() = Objects.hash(CharGene::class, dna)
}
