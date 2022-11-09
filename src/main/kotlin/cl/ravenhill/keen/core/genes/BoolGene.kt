/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.core.genes

import cl.ravenhill.keen.core.KeenCore

/**
 * A boolean gene.
 */
sealed class BoolGene : Gene<Boolean> {

    /**
     * Returns the boolean value of the gene.
     */
    fun toBool() = dna

    /**
     * Returns the integer value of the gene.
     */
    fun toInt() = if (dna) 1 else 0

    override fun toString() = "$dna"

    override fun mutate() = if (KeenCore.generator.nextBoolean()) {
        True
    } else {
        False
    }

    override fun copy(dna: Boolean) = if (dna) {
        True
    } else {
        False
    }

    /**
     * A true gene.
     */
    object True : BoolGene() {
        override val dna = true
    }

    /**
     * A false gene.
     */
    object False : BoolGene() {
        override val dna = false
    }
}