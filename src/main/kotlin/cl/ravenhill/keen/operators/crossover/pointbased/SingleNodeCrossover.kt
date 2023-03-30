/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.crossover.pointbased

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.Dice
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.crossover.AbstractCrossover
import cl.ravenhill.keen.probability
import cl.ravenhill.keen.requirements.IntRequirement.BeEqualTo
import cl.ravenhill.keen.util.Tree

class SingleNodeCrossover<DNA: Tree<*>>(
    probability: Double,
    exclusivity: Boolean = false,
    chromosomeRate: Double = 1.0,
    private val geneRate: Double = 1.0
) : AbstractCrossover<DNA>(
    probability,
    exclusivity = exclusivity,
    chromosomeRate = chromosomeRate
) {
    override fun crossoverChromosomes(chromosomes: List<Chromosome<DNA>>):
            List<Chromosome<DNA>> {
        enforce {
            chromosomes.size should BeEqualTo(2) {
                "The crossover must have exactly two parents"
            }
            chromosomes.forEach { chromosome ->
                chromosome.genes.size should BeEqualTo(chromosomes[0].genes.size) {
                    "The parents must have the same size"
                }
                chromosome.genes.forEach { gene ->
                    gene.dna.children.size should BeEqualTo(gene.dna.arity) {
                        "The gene's arity (${gene.dna.arity}) does not match the number of " +
                                "children (${gene.dna.children.size})."
                    }
                }
            }
        }
        // The crossover is only applied if there is at least one gene with more than one node.
        if (chromosomes.all { it.genes.any { g -> g.dna.size > 1 } }) {
            TODO(
                "Wait for the implementation of a program chromosome where the genes are the " +
                        "nodes of the tree"
            )
            val genes = mutableListOf<Gene<Tree<DNA>>>() to mutableListOf<Gene<Tree<DNA>>>()
            chromosomes[0].genes.zip(chromosomes[1].genes).forEach { (gene1, gene2) ->
                val nodes = mutableListOf<Tree<DNA>>() to mutableListOf<Tree<DNA>>()
                if (Dice.probability() < geneRate) {
                    val node1 = gene1.dna.random(Core.random)
                    val node2 = gene2.dna.random(Core.random)
//                    val slices = gene1.dna.searchSubtree(node1) to gene2.dna.searchSubtree(node2)

                } else {
//                    nodes.first.add(gene1.dna)
//                    nodes.second.add(gene2.dna)
                }
            }
        }
        // If the crossover is not applied, the parents are returned.
        return listOf(chromosomes[0], chromosomes[1])
    }
}
