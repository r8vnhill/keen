/*
 *
 *  * "Keen" (c) by R8V.
 *  * "Keen" is licensed under a
 *  * Creative Commons Attribution 4.0 International License.
 *  * You should have received a copy of the license along with this
 *  *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 *
 */

package cl.ravenhill.keen.genetic.genes.numerical

import cl.ravenhill.keen.genetic.genes.Gene

/**
 * [Gene] which holds a numerical value.
 *
 * @param DNA The type of the value.
 */
interface NumberGene<DNA : Number> : Gene<DNA> {

    /**
     * Returns the mean of this gene and the given one.
     */
    fun mean(gene: NumberGene<DNA>): NumberGene<DNA>

    /**
     * Converts this gene to a [Double].
     */
    fun toDouble(): Double

    /**
     * Converts this gene to a [Int].
     */
    fun toInt(): Int

    override fun mutate() =
        duplicate(
            generateSequence { generator() }
                .filter { filter(it) }
                .first())

    val filter: (DNA) -> Boolean
}