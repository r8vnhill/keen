package cl.ravenhill.keen.operators

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.requirements.IntRequirement.BeAtLeast
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.util.indices
import cl.ravenhill.keen.util.math.neq
import cl.ravenhill.keen.util.subset
import kotlin.math.min


abstract class AbstractRecombinatorAlterer<DNA>(
    probability: Double,
    protected val order: Int
) :
        AbstractAlterer<DNA>(probability) {

    init {
        enforce { order should BeAtLeast(2) { "The order must be at least 2" } }
    }

    override fun invoke(
        population: Population<DNA>,
        generation: Int
    ): AltererResult<DNA> {
        val pop = population.toMutableList()
        return if (probability neq 0.0 && pop.size >= 2) {
            val minOrder = min(order, pop.size)
            val count = Core.Dice.random.indices(probability, pop.size)
                .map { individuals(it, pop.size, minOrder) }
                .sumOf { recombine(pop, it, generation) }
            AltererResult(pop, count)
        } else {
            AltererResult(pop)
        }
    }

    private fun individuals(index: Int, size: Int, ord: Int): IntArray {
        val ind = Core.random.subset(size, ord)
        var i = 0
        while (ind[i] < index && i < ind.size - 1) {
            ++i
        }
        ind[i] = index
        return ind
    }

    protected abstract fun recombine(
        population: MutableList<Phenotype<DNA>>,
        individuals: IntArray,
        generation: Int
    ): Int
}