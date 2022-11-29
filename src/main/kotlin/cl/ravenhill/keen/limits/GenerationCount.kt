/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.keen.LimitConfigurationException
import cl.ravenhill.keen.evolution.EvolutionResult
import cl.ravenhill.keen.evolution.streams.EvolutionStream
import java.util.stream.Stream


class GenerationCount(private val i: Int) : Match({ generation >= i }) {
    init {
        if (i < 0) {
            throw LimitConfigurationException {
                "Generation count must not be negative, but was $i"
            }
        }
    }

    override fun <DNA> applyTo(stream: EvolutionStream<DNA>): Stream<EvolutionResult<DNA>> {
        return stream.limit(i.toLong())
    }
}
