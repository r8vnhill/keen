package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Core
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
class CharGene(override val dna: Char, val range: CharRange, val filter: (Char) -> Boolean) :
        ComparableGene<Char> {

    // Documentation inherited from Gene
    override fun generator() = Core.random.nextChar(range, filter)

    // Documentation inherited from Gene
    override fun withDna(dna: Char) = CharGene(dna, range, filter)

    // region : TYPE CONVERSIONS
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
    // endregion

    // Documentation inherited from Any
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is CharGene -> false
        other::class != this::class -> false
        else -> dna == other.dna
    }

    // Documentation inherited from Any
    override fun hashCode() = Objects.hash(CharGene::class, dna)

    companion object {
        /**
         * Creates a new gene with a random value generated from the specified [range] and [filter]
         * constraints.
         *
         * @param range The range of valid characters for the gene.
         *  Defaults to `' '..'z'`.
         * @param filter A filter function that specifies additional constraints on the gene value.
         *  Defaults to a function that always returns `true`.
         *
         * @return A new [CharGene] instance with a randomly generated value.
         */
        fun create(range: CharRange = ' '..'z', filter: (Char) -> Boolean = { true }) =
            CharGene(Core.random.nextChar(range, filter), range, filter)
    }
}
