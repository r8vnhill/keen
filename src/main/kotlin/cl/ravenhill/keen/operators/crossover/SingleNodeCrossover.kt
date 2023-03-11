package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.requirements.IntRequirement.BeEqualTo
import cl.ravenhill.keen.util.math.eq
import cl.ravenhill.keen.util.node
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
        enforce {
            genes1.size should BeEqualTo(genes2.size) {
                "The parents must have the same size"
            }
            genes1.forEach { gene ->
                gene.dna.children.size should BeEqualTo(gene.dna.arity) {
                    "The gene's arity (${gene.dna.arity}) does not match the number of children " +
                            "(${gene.dna.children.size})."
                }
            }
            genes2.forEach { gene ->
                gene.dna.children.size should BeEqualTo(gene.dna.arity) {
                    "The gene's arity (${gene.dna.arity}) does not match the number of children " +
                            "(${gene.dna.children.size})."
                }
            }
        }
        (genes1 zip genes2).forEach { (g1, g2) ->
            val reduceable1 = g1.dna
            val reduceable2 = g2.dna
            crossoverTrees(reduceable1, reduceable2)
        }
        return 1
    }

    internal fun crossoverTrees(
        reduceable1: Reduceable<DNA>,
        reduceable2: Reduceable<DNA>
    ) {
        if (probability eq 0.0 || min(reduceable1.size, reduceable2.size) < 2) return
        val node1 = Core.random.node(reduceable1)
        val node2 = Core.random.node(reduceable2)
        if (node1 === node2) return
        val parent1 = node1.parent
        val parent2 = node2.parent
        swap(parent1, node1, node2)
        swap(parent2, node2, node1)
    }

    /**
     * Swaps the nodes if the maximum depth is not exceeded.
     */
    private fun swap(parent: Reduceable<DNA>?, node1: Reduceable<DNA>, node2: Reduceable<DNA>) {
        if ((parent?.height ?: 0) + node2.height <= Core.maxProgramDepth) {
            parent?.replaceChild(node1, node2)
        }
    }
}
