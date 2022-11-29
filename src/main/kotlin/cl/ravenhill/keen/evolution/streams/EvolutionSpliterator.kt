package cl.ravenhill.keen.evolution.streams

import cl.ravenhill.keen.evolution.EvolutionResult
import cl.ravenhill.keen.evolution.EvolutionStart
import cl.ravenhill.keen.evolution.Evolver
import java.util.Spliterator
import java.util.function.Consumer


class EvolutionSpliterator<DNA>(
    private val start: () -> EvolutionStart<DNA>,
    private val evolver: Evolver<DNA>
) : Spliterator<EvolutionResult<DNA>> {

    private lateinit var next: EvolutionStart<DNA>

    override fun tryAdvance(action: Consumer<in EvolutionResult<DNA>>): Boolean {
        if (!::next.isInitialized) {
            next = start()
        }
        val result = evolver.evolve(next)
        action.accept(result)
        next = result.next()
        return true
    }

    override fun trySplit(): Spliterator<EvolutionResult<DNA>> {
        TODO("Not yet implemented")
    }

    override fun estimateSize(): Long {
        TODO("Not yet implemented")
    }

    override fun characteristics() =
        Spliterator.ORDERED or Spliterator.NONNULL or Spliterator.IMMUTABLE
}