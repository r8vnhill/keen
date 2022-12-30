package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.util.node
import cl.ravenhill.keen.util.validatePredicate
import kotlin.math.min

/**
 * The single node crossover operator is a genetic operator that selects a single node from each
 * parent and swaps them.
 *
 * @param DNA  The type of the genes' values.
 */
class SingleNodeCrossover<DNA>(probability: Double) :
    AbstractCrossover<Reduceable<DNA>>(probability) {

    override fun crossover(
        genes1: MutableList<Gene<Reduceable<DNA>>>,
        genes2: MutableList<Gene<Reduceable<DNA>>>
    ): Int {
        validatePredicate({ genes1.size == genes2.size }) { "The parents must have the same size" }
        (genes1 zip genes2).forEach { (g1, g2) ->
            if (Core.random.nextDouble() < probability) {
                val reduceable1 = g1.dna
                val reduceable2 = g2.dna
                crossoverTrees(reduceable1, reduceable2)
            }
        }
        return 1
    }

    private fun crossoverTrees(
        reduceable1: Reduceable<DNA>,
        reduceable2: Reduceable<DNA>
    ) {
        if (min(reduceable1.size, reduceable2.size) < 2) return
        val node1 = Core.random.node(reduceable1)
        val node2 = Core.random.node(reduceable2)
        val parent1 = node1.parent
        val parent2 = node2.parent
        parent1?.replaceChild(node1, node2)
        parent2?.replaceChild(node2, node1)
    }
}
