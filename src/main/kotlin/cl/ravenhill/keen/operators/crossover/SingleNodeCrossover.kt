package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.requirements.IntRequirement.BeEqualTo
import cl.ravenhill.keen.util.Tree

class SingleNodeCrossover<DNA: Tree<*>>(probability: Double) :
        AbstractCrossover<DNA>(probability) {

    override fun crossover(
        genes1: MutableList<Gene<DNA>>,
        genes2: MutableList<Gene<DNA>>
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
            val node1 = g1.dna.random(Core.random)
            val node2 = g2.dna.random(Core.random)
//            val node1 = Core.random.node(g1.dna)
//            val node2 = Core.random.node(g2.dna)
//            if (node1 === node2) return@forEach
//            val parent1 = node1.parent
//            val parent2 = node2.parent
//            swap(parent1, node1, node2)
//            swap(parent2, node2, node1)
        }
        return 1
    }

    override fun crossover(chromosomes: List<Chromosome<DNA>>): List<Chromosome<DNA>> {
        TODO("Not yet implemented")
    }

//    internal fun crossoverTrees(
//        reduceable1: Reduceable<DNA>,
//        reduceable2: Reduceable<DNA>
//    ) {
//        if (probability eq 0.0 || min(reduceable1.size, reduceable2.size) < 2) return
//        val node1 = Core.random.node(reduceable1)
//        val node2 = Core.random.node(reduceable2)
//        if (node1 === node2) return
//        val parent1 = node1.parent
//        val parent2 = node2.parent
//        swap(parent1, node1, node2)
//        swap(parent2, node2, node1)
//    }
//
//    /**
//     * Swaps the nodes if the maximum depth is not exceeded.
//     */
//    private fun swap(parent: Reduceable<DNA>?, node1: Reduceable<DNA>, node2: Reduceable<DNA>) {
//        if ((parent?.height ?: 0) + node2.height <= Core.maxProgramDepth) {
//            parent?.replaceChild(node1, node2)
//        }
//    }
}
