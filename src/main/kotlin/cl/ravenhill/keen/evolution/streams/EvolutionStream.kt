package cl.ravenhill.keen.evolution.streams

import cl.ravenhill.keen.evolution.EvolutionResult
import java.util.stream.Stream

interface EvolutionStream<DNA> : Stream<EvolutionResult<DNA>>
