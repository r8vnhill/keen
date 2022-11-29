/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.evolution.EvolutionResult
import cl.ravenhill.keen.evolution.streams.EvolutionStream
import java.util.stream.Stream


open class Match(private val predicate: Engine<*>.() -> Boolean) : Limit {
    override fun invoke(engine: Engine<*>) = predicate(engine)
    override fun <DNA> applyTo(stream: EvolutionStream<DNA>): Stream<EvolutionResult<DNA>> {
        TODO("Not yet implemented")
    }
}