package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer
import cl.ravenhill.keen.util.validateAtLeast
import org.jetbrains.annotations.Contract

class EvolutionResult<DNA>(
    private val optimizer: PhenotypeOptimizer<DNA>, population: List<Phenotype<DNA>>,
    generation: Int
) : Comparable<EvolutionResult<DNA>> {
    private val population: List<Phenotype<DNA>>
    val generation: Int

    @Contract(pure = true)
    operator fun next(): EvolutionStart<DNA> {
        return EvolutionStart(population, generation + 1, true)
    }

    val best: Phenotype<DNA>?

    init {
        this.population = population
        this.generation = generation
        best = population.stream().max(optimizer.comparator).orElse(null)
    }

    override fun compareTo(other: EvolutionResult<DNA>): Int {
        return 0
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