package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer
import cl.ravenhill.keen.util.validateAtLeast
import org.jetbrains.annotations.Contract

/**
 * Result of an evolution process.
 *
 * @param DNA  The type of the gene's value.
 * @property optimizer The optimization strategy used.
 * @property population The population of the result.
 * @property generation The generation of the result.
 * @property best The best phenotype of the result.
 * @constructor Creates a new [EvolutionResult].
 */
class EvolutionResult<DNA>(
    private val optimizer: PhenotypeOptimizer<DNA>,
    private val population: List<Phenotype<DNA>>,
    val generation: Int
) : Comparable<EvolutionResult<DNA>> {

    val best: Phenotype<DNA>? = population.stream().max(optimizer.comparator).orElse(null)

    /**
     * The next generation of the result.
     */
    operator fun next() = EvolutionStart(population, generation + 1, true)

    override fun compareTo(other: EvolutionResult<DNA>): Int {
        return 0
    }

    override fun toString() = "EvolutionResult { generation: $generation, best: $best }"
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