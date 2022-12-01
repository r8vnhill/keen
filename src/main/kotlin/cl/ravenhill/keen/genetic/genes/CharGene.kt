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


class CharGene private constructor(override val dna: Char) : Gene<Char> {

    override fun toString() = "$dna"

    override fun mutate() = create()

    override fun duplicate(dna: Char) = CharGene(dna)

    companion object {
        fun create() = CharGene(Core.rng.nextChar())
    }
}