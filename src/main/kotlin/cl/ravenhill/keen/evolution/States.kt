package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.util.math.MinMax
import cl.ravenhill.keen.util.math.MinMax.Companion.of
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer
import cl.ravenhill.keen.util.validateAtLeast
import org.jetbrains.annotations.Contract
import java.util.stream.Collector

class EvolutionResult<DNA>(
    private val optimizer: PhenotypeOptimizer, population: List<Phenotype<DNA>>,
    generation: Int
) : Comparable<EvolutionResult<DNA>> {
    private val population: List<Phenotype<DNA>>
    private val generation: Int

    @Contract(pure = true)
    operator fun next(): EvolutionStart<DNA> {
        return EvolutionStart(population, generation + 1, true)
    }

    private val _best: Phenotype<DNA>

    init {
        this.population = population
        this.generation = generation
        _best = population.stream().max(optimizer.comparator).orElse(null)
    }

    fun bestPhenotype(): Phenotype<DNA> {
        return _best
    }

    override fun compareTo(other: EvolutionResult<DNA>): Int {
        return 0
    }

    companion object {
        fun <G> toBestPhenotype(): Collector<EvolutionResult<G>, *, Phenotype<G>> = Collector.of(
            ::of,
            MinMax<EvolutionResult<G>>::accept,
            MinMax<EvolutionResult<G>>::combine,
            { mm: MinMax<EvolutionResult<G>> ->
                if (mm.max() != null) mm.max()!!.bestPhenotype() else null
            }
        )
    }
}

class EvolutionStart<T>(
    val population: List<Phenotype<T>>,
    val generation: Int,
    val isDirty: Boolean = true
) {

    init {
        generation.validateAtLeast(0) { "Generation must be non-negative" }
    }

    override fun toString() = "EvolutionStart { " +
            "population: $population, " +
            "generation: $generation, " +
            " }"

    companion object {
        fun <T> empty(): EvolutionStart<T> = EvolutionStart(listOf(), 1)
    }
}