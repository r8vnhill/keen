package cl.ravenhill.keen.evolution.streams

import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.evolution.EvolutionResult
import cl.ravenhill.keen.evolution.EvolutionStart
import java.util.stream.Stream

interface EvolutionStream<DNA> : Stream<EvolutionResult<DNA>> {
    companion object {
        fun <DNA> ofEvolver(
            evolver: Engine<DNA>,
            start: () -> EvolutionStart<DNA>
        ) = ConcreteEvolutionStream(start, evolver)
    }
}
