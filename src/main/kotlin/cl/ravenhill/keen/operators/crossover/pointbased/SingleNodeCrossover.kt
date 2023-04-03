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
import cl.ravenhill.keen.requirements.IntRequirement
import cl.ravenhill.keen.requirements.IntRequirement.BeAtMost
import cl.ravenhill.keen.requirements.IntRequirement.BeEqualTo
import cl.ravenhill.keen.util.Tree

/**
 * A genetic operator that performs a single node crossover on a pair of parents.
 *
 * This operator selects two nodes, one from each parent, and exchanges the subtrees rooted at those
 * nodes.
 * The resulting offspring chromosomes contain the modified genes, while the original parent
 * chromosomes are left unchanged.
 * The operator has an optional [geneRate] parameter that determines the probability of applying the
 * crossover to each gene.
 * If [geneRate] is less than 1.0, some genes may be left unchanged in the offspring chromosomes.
 *
 * @param V the type of value stored in the tree nodes.
 * @param DNA the type of the tree structure.
 * @property geneRate the probability of applying the crossover to each gene (default is 1.0).
 * @param probability the probability of applying the operator to a pair of parents.
 * @param exclusivity a flag that indicates whether the operator should be exclusive (default is
 *  `false`).
 * @param chromosomeRate the probability of applying the operator to a pair of chromosomes (default
 *  is 1.0).
 *
 * @constructor Creates a new instance of [SingleNodeCrossover] with the specified [probability],
 * [exclusivity], [chromosomeRate], and [geneRate].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class SingleNodeCrossover<V, DNA : Tree<V, DNA>>(
    probability: Double,
    exclusivity: Boolean = false,
    chromosomeRate: Double = 1.0,
    private val geneRate: Double = 1.0
) : AbstractCrossover<DNA>(
    probability,
    exclusivity = exclusivity,
    chromosomeRate = chromosomeRate
) {

    // Inherit documentation from AbstractCrossover.
    override fun crossoverChromosomes(chromosomes: List<Chromosome<DNA>>): List<Chromosome<DNA>> {
        enforcePreconditions(chromosomes)
        return if (chromosomes.all { it.genes.any { g -> g.dna.size > 1 } }) {
            applyCrossover(chromosomes)
        } else {
            returnParents(chromosomes)
        }
    }

    /**
     * Ensures that the parents meet the preconditions for the crossover.
     */
    private fun enforcePreconditions(chromosomes: List<Chromosome<DNA>>) = enforce {
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

    /**
     * Applies the crossover method to a pair of parents.
     *
     * @param chromosomes the parents to apply the crossover to.
     * @return a list with the resulting offspring.
     */
    private fun applyCrossover(chromosomes: List<Chromosome<DNA>>): List<Chromosome<DNA>> {
        // create two mutable lists to hold the new genes for each offspring chromosome
        val genes = mutableListOf<Gene<DNA>>() to mutableListOf<Gene<DNA>>()
        // iterate over each pair of genes from the two parent chromosomes
        chromosomes[0].genes.zip(chromosomes[1].genes).forEach { (gene1, gene2) ->
            // randomly decide whether to perform a crossover at this point
            if (Dice.probability() < geneRate) {
                // randomly select two nodes from the two parent genes
                val node1 = gene1.dna.random(Core.random)
                val node2 = gene2.dna.random(Core.random)
                // search for the subtrees rooted at each of the selected nodes
                val slices = gene1.dna.searchSubtree(node1) to gene2.dna.searchSubtree(node2)
                // replace the selected subtrees in each parent with the subtrees from the other parent
                val newTree1 = gene1.dna.replaceSubtree(slices.first, node2)
                val newTree2 = gene2.dna.replaceSubtree(slices.second, node1)
                enforce {
                    newTree1.height should BeAtMost(Core.maxProgramDepth) {
                        "The new tree's height (${newTree1.height}) exceeds the maximum " +
                                "allowed height (${Core.maxProgramDepth})."
                    }
                    newTree2.height should BeAtMost(Core.maxProgramDepth) {
                        "The new tree's height (${newTree2.height}) exceeds the maximum " +
                                "allowed height (${Core.maxProgramDepth})."
                    }
                }
                // add the new genes to the offspring chromosomes
                genes.first.add(gene1.duplicate(newTree1))
                genes.second.add(gene2.duplicate(newTree2))
            } else {
                // if no crossover is performed, simply add the original genes to the offspring chromosomes
                genes.first.add(gene1)
                genes.second.add(gene2)
            }
        }
        // create two new offspring chromosomes using the modified gene lists
        return listOf(
            chromosomes[0].duplicate(genes.first),
            chromosomes[1].duplicate(genes.second)
        )
    }

    /**
     * Returns the parents as they are.
     */
    private fun returnParents(chromosomes: List<Chromosome<DNA>>) = listOf(
        chromosomes[0].duplicate(chromosomes[0].genes),
        chromosomes[1].duplicate(chromosomes[1].genes)
    )
}
