package cl.ravenhill.keen.evolution.streams

import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.evolution.EvolutionResult
import cl.ravenhill.keen.evolution.EvolutionStart
import cl.ravenhill.keen.limits.Limit
import java.util.stream.Stream

interface EvolutionStream<DNA> : Stream<EvolutionResult<DNA>> {
    fun limit(limit: Limit) = limit.applyTo(this)

    companion object {
        fun <DNA> ofEvolver(
            evolver: Engine<DNA>,
            start: () -> EvolutionStart<DNA>
        ) = ConcreteEvolutionStream(start, evolver)
    }
}
