package cl.ravenhill.keen.operators

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.requirements.IntRequirement.BeAtLeast
import cl.ravenhill.keen.util.indices
import cl.ravenhill.keen.util.math.neq
import cl.ravenhill.keen.util.sublists


abstract class AbstractRecombinatorAlterer<DNA>(
    probability: Double,
    protected val order: Int,
    private val monogamous: Boolean = false
) : AbstractAlterer<DNA>(probability) {

    init {
        enforce { order should BeAtLeast(2) { "The order must be at least 2" } }
    }

    override fun invoke(
        population: Population<DNA>,
        generation: Int
    ): AltererResult<DNA> {
        val pop = population.toMutableList()
        return if (probability neq 0.0 && pop.size >= 2) {
            val indices = Core.random.indices(probability, pop.size)
            val parents = Core.random.sublists(indices, monogamous, order)
            val count = parents.sumOf { recombine(pop, it, generation) }
            AltererResult(pop, count)
        } else {
            AltererResult(pop)
        }
    }

    /**
     * Recombines the individuals at the given indices.
     *
     * @param population the population to recombine
     * @param indices the indices of the individuals to recombine
     * @param generation the current generation
     * @return the number of individuals that were recombined
     */
    protected abstract fun recombine(
        population: MutableList<Phenotype<DNA>>,
        indices: List<Int>,
        generation: Int
    ): Int
}
