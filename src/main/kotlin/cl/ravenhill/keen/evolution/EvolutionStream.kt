package cl.ravenhill.keen.evolution

import java.util.stream.Stream

interface EvolutionStream<DNA> : Stream<EvolutionResult<DNA>> {
    companion object {
        fun <DNA> ofEvolver(
            evolver: Engine<DNA>,
            start: () -> EvolutionStart<DNA>
        ) = ConcreteEvolutionStream(start, evolver)
    }
}
