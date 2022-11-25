/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.genetic.Genotype

/**
 * Builder methods for Keen core classes.
 */
object Builders {

    /**
     * Creates a new [Engine] with the given ``fitnessFunction`` and ``init`` block.
     */
    fun <DNA> engine(
        fitnessFunction: (Genotype<DNA>) -> Double,
        genotype: Genotype.Factory<DNA>,
        init: Engine.Builder<DNA>.() -> Unit
    ) = Engine.Builder(fitnessFunction, genotype).apply(init).build()

    /**
     * Creates a new [Genotype] with the given ``init`` block.
     */
    fun <DNA> genotype(init: Genotype.Factory<DNA>.() -> Unit) =
        Genotype.Factory<DNA>().apply(init)
}