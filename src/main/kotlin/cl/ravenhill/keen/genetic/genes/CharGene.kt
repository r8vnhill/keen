/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.util.nextChar
import java.util.Objects
import kotlin.random.Random


class CharGene(override val dna: Char) : ComparableGene<Char> {

    override fun generator() = Core.rng.nextChar()

    override fun duplicate(dna: Char) = CharGene(dna)

    // region : TYPE CONVERSIONS
    /**
     * Converts the gene to a 16-bit integer ([Char])
     */
    fun toChar() = dna

    /**
     * Converts the gene to a 32-bit integer ([Int])
     */
    fun toInt() = dna.code

    override fun toString() = "$dna"
    // endregion

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is CharGene -> false
        other::class != this::class -> false
        else -> dna == other.dna
    }

    override fun hashCode() = Objects.hash(CharGene::class, dna)

    companion object {
        fun create() = CharGene(Core.rng.nextChar())
    }
}
