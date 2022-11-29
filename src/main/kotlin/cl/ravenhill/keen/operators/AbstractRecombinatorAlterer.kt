package cl.ravenhill.keen.operators

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.util.indexes
import cl.ravenhill.keen.util.math.Subset
import cl.ravenhill.keen.util.validateAtLeast
import kotlin.math.min


abstract class AbstractRecombinatorAlterer<DNA>(probability: Double, protected val order: Int) :
        AbstractAlterer<DNA>(probability) {

    init {
        order.validateAtLeast(2, "Order")
    }

    override fun invoke(population: Population<DNA>, generation: Int): AltererResult<DNA> {
        lateinit var result: AltererResult<DNA>
        val pop = population.toMutableList()
        return if (pop.size >= 2) {
            val minOrder = min(order, pop.size)
            val count = Core.rng.indexes(probability, pop.size)
                .map { individuals(it, pop.size, minOrder) }
                .sumOf { recombine(pop, it, generation) }
            AltererResult(pop, count)
        } else {
            AltererResult(pop)
        }
    }

    private fun individuals(index: Int, size: Int, ord: Int): IntArray {
        val ind = Subset.next(size, ord)
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