/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.internals.Verifiable

/**
 * Any unit of genetic material used by the [cl.ravenhill.keen.evolution.Evolver].
 *
 * @param DNA  The type of the genetic material's value.
 */
interface GeneticMaterial<DNA> : Verifiable {

    /**
     * Flattens the genetic material into a list of values.
     */
    fun flatten(): List<DNA>
}