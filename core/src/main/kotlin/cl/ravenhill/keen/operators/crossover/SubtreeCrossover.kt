/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.constraints.ints.BeAtMost
import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.DoubleConstraintException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ExperimentalKeen
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.utils.trees.Tree
import cl.ravenhill.keen.utils.trees.replaceFirst


/**
 * The `SubtreeCrossover` class represents a specialized crossover operation used in genetic programming, particularly
 * for manipulating chromosomes that encode tree-like structures. This class is part of the experimental features in the
 * Keen library and facilitates the process of combining genetic material from two parent chromosomes to produce new
 * offspring. The crossover operation specifically targets the subtree structures within the chromosomes, making it
 * ideal for applications where the solutions are represented as trees, such as symbolic regression, decision-making
 * models, or genetic representations of program code.

 * ## Functionality:
 * - Executes a subtree crossover by exchanging subtrees between two parent chromosomes.
 * - Employs a `geneRate` to probabilistically determine if a crossover occurs at each gene position, enabling
 *   fine-tuned control over the crossover frequency and ensuring diversity in the offspring.
 * - Maintains the integrity of the tree structure post-crossover, adhering to constraints like maximum tree depth and
 *   node arity.
 * - The `chromosomeRate` controls the likelihood of crossover occurring for a given pair of chromosomes.

 * ## Constraints:
 * - Ensures the chromosome and gene rates are within a valid range (0.0 to 1.0) to maintain probabilistic integrity.
 * - Enforces structural constraints on the resulting trees, such as maximum depth, to prevent issues like bloat or
 *   overly complex structures.
 * - Checks preconditions to ensure compatibility and integrity of parent chromosomes before applying crossover.

 * ## Usage:
 * `SubtreeCrossover` is an essential tool in genetic programming for evolving solutions that are represented as tree
 * structures. By recombining subtrees from parents, it introduces new genetic variations that can be explored in
 * successive generations. It's particularly effective in environments where novel or complex tree structures are
 * desired to solve problems or optimize outcomes.

 * ### Example:
 * ```
 * val crossover = SubtreeCrossover<MyValueType, MyTreeType, MyGeneType>(
 *     exclusivity = false,
 *     chromosomeRate = 1.0,
 *     geneRate = 0.5
 * )
 * val parent1: Chromosome<MyTreeType, MyGeneType> = ...
 * val parent2: Chromosome<MyTreeType, MyGeneType> = ...
 * val offspring = crossover.crossoverChromosomes(listOf(parent1, parent2))
 * // offspring contains two new chromosomes, each a product of subtree crossover between parent1 and parent2
 * ```
 *
 * @param V The type of value held by the tree nodes in the genes.
 * @param T The tree type, extending the `Tree` interface, representing the structure of each gene in the chromosome.
 * @param G The gene type, encapsulating a tree structure within the chromosome.
 * @param exclusivity A flag indicating whether the crossover is exclusive, affecting how the crossover is applied.
 * @param chromosomeRate The probability of crossover between two chromosomes, influencing the overall likelihood of
 *   crossover.
 * @param geneRate The probability of crossover at a specific gene within the chromosome, allowing for granular control
 *   over genetic mixing.
 * @property numOffspring The number of offspring chromosomes produced by the crossover operation. This is always 2.
 * @property numParents The number of parent chromosomes required by the crossover operation. This is always 2.
 * @constructor Creates a new instance of the `SubtreeCrossover` class. Constraints are applied to ensure the
 *   chromosome and gene rates are within a valid range (0.0 to 1.0).
 * @throws CompositeException if the chromosome or gene rates are not within the valid range.
 * @throws DoubleConstraintException if the chromosome or gene rates are not within the valid range.
 */
@ExperimentalKeen
class SubtreeCrossover<V, T, G>(
    override val exclusivity: Boolean = false,
    override val chromosomeRate: Double = 1.0,
    val geneRate: Double = 1.0,
) : Crossover<T, G> where T : Tree<V, T>, G : Gene<T, G> {

    init {
        constraints {
            "The chromosome rate must be between 0 and 1." { chromosomeRate must BeInRange(0.0..1.0) }
            "The gene rate must be between 0 and 1." { geneRate must BeInRange(0.0..1.0) }
        }
    }

    override val numOffspring = 2

    override val numParents = 2

    /**
     * Executes the crossover operation on a pair of chromosomes, potentially producing offspring chromosomes with new
     * genetic combinations. This function is a key public interface of the `SubtreeCrossover` class, orchestrating the
     * crossover process.
     *
     * ## Functionality:
     * - Initiates by enforcing preconditions on the chromosomes using the [enforcePreconditions] method.
     * - Determines if the crossover operation is applicable based on the size of the genes in the chromosomes.
     * - If all chromosomes contain genes with more than one node (indicating the presence of non-trivial trees),
     *   the [applyCrossover] method is called to perform the actual crossover operation.
     * - If any of the chromosomes do not meet the criteria for crossover (e.g., genes representing trivial trees),
     *   the original chromosomes are returned without modification.
     *
     * ## Usage:
     * This function is typically invoked in genetic algorithms to combine genetic material from parent chromosomes,
     * contributing to the generation of diverse offspring. It ensures that the crossover process is applied only when
     * appropriate, respecting the structural requirements of the chromosomes.
     *
     * ### Example:
     * ```
     * val crossover = SubtreeCrossover<MyValueType, MyTreeType, MyGeneType>()
     * val parentChromosomes: List<Chromosome<MyTreeType, MyGeneType>> = listOf(parent1, parent2)
     * val offspringChromosomes = crossover.crossoverChromosomes(parentChromosomes)
     * // offspringChromosomes contains the result of the crossover, either new chromosomes or the original parents
     * ```
     *
     * @param chromosomes A list containing exactly two parent chromosomes to undergo crossover.
     * @return A list containing either new offspring chromosomes resulting from crossover or the original parent
     *         chromosomes if crossover is not applicable.
     */
    override fun crossoverChromosomes(chromosomes: List<Chromosome<T, G>>): List<Chromosome<T, G>> {
        enforcePreconditions(chromosomes)
        return if (chromosomes.all { it.genes.any { g -> g.value.size > 1 } }) {
            applyCrossover(chromosomes)
        } else {
            chromosomes
        }
    }

    /**
     * Conducts the crossover process on a pair of chromosomes, producing two new offspring chromosomes. This private
     * function is central to the functionality of the `SubtreeCrossover` class, implementing the core logic of the
     * crossover operation.
     *
     * ## Functionality:
     * - Creates mutable lists to hold new genes for each offspring chromosome.
     * - Iterates over pairs of genes from the two parent chromosomes.
     * - For each pair of genes, decides probabilistically (based on [geneRate]) whether to perform a crossover.
     * - If a crossover is performed, uses the [crossoverGenes] function to swap subtrees between genes and adds the
     *   resulting genes to the offspring chromosomes.
     * - If no crossover is performed, the original genes are added directly to the offspring chromosomes.
     * - Constructs two new offspring chromosomes using the modified gene lists.
     *
     * ## Usage:
     * This function is invoked during the subtree crossover process, handling the detailed steps of recombining genetic
     * material from the parent chromosomes. It ensures that the crossover respects the specified gene rate and adheres
     * to the structural constraints of the chromosomes.
     *
     * ### Implementation Details:
     * - The function creates a balanced approach to genetic recombination, allowing for both conservation of original
     *   genetic material and introduction of new genetic variations.
     * - It ensures that the new offspring chromosomes maintain a structure consistent with the parents while
     *   introducing potential genetic diversity.
     *
     * @param chromosomes A list containing exactly two parent chromosomes to undergo crossover.
     * @return A list containing two new offspring chromosomes resulting from the crossover operation.
     */
    private fun applyCrossover(chromosomes: List<Chromosome<T, G>>): List<Chromosome<T, G>> {
        // create two mutable lists to hold the new genes for each offspring chromosome
        val crossedGenes = mutableListOf<G>() to mutableListOf<G>()
        // iterate over each pair of genes from the two parent chromosomes
        chromosomes[0].zip(chromosomes[1]).forEach { (gene1, gene2) ->
            // randomly decide whether to perform a crossover at this point
            if (Domain.random.nextDouble() < geneRate) {
                val (first, second) = crossoverGenes(gene1, gene2)
                crossedGenes.first.add(first)
                crossedGenes.second.add(second)
            } else {
                // if no crossover is performed, simply add the original genes to the offspring chromosomes
                crossedGenes.first.add(gene1)
                crossedGenes.second.add(gene2)
            }
        }
        // create two new offspring chromosomes using the modified gene lists
        return listOf(
            chromosomes[0].duplicateWithGenes(crossedGenes.first),
            chromosomes[1].duplicateWithGenes(crossedGenes.second)
        )
    }

    /**
     * Executes the crossover operation at the gene level, swapping subtrees between two genes to create new genetic
     * variations. This private function is a key component of the subtree crossover mechanism in the `SubtreeCrossover`
     * class.
     *
     * ## Functionality:
     * - Randomly selects a node from each of the two parent genes' tree structures.
     * - Swaps these selected nodes between the parent genes to create two new trees.
     * - Utilizes the [replaceSubtreeAndCheckHeight] function to replace the selected nodes and to ensure the new trees
     *   do not exceed the maximum allowable height ([Domain.maxProgramDepth]).
     * - Applies constraints to validate that the newly created trees are within the height limit.
     * - Returns a list containing the two new genes, each with its modified tree structure.
     *
     * ## Constraints:
     * - Enforces height constraints on the new trees to prevent the creation of overly complex or deep genetic
     *   structures, which could negatively impact the performance and effectiveness of the genetic algorithm.
     *
     * ## Usage:
     * This function is used internally during the subtree crossover process to perform gene-level manipulations,
     * introducing genetic diversity while maintaining structural integrity and adherence to predefined limits.
     *
     * ### Implementation Details:
     * - The function ensures that the crossover process respects the structural and height limitations of the trees,
     *   thereby balancing the exploration of new genetic spaces with the maintenance of feasible and manageable tree
     *   sizes.
     *
     * @param gene1 The first gene involved in the crossover.
     * @param gene2 The second gene involved in the crossover.
     * @return A list of two new genes, each containing a subtree from the other gene.
     */
    private fun crossoverGenes(gene1: G, gene2: G): List<G> {
        // randomly select two nodes from the two parent genes
        val node1 = gene1.value.random()
        val node2 = gene2.value.random()
        val newTree1 = replaceSubtreeAndCheckHeight(gene1, node1, node2)
        val newTree2 = replaceSubtreeAndCheckHeight(gene2, node2, node1)

        // Apply constraints to check if the new trees are within the height limit
        constraints {
            "New tree height (${newTree1.height}) exceeds maximum limit (${Domain.maxProgramDepth})." {
                newTree1.height must BeAtMost(Domain.maxProgramDepth)
            }
            "New tree height (${newTree2.height}) exceeds maximum limit (${Domain.maxProgramDepth})." {
                newTree2.height must BeAtMost(Domain.maxProgramDepth)
            }
        }

        return listOf(gene1.duplicateWithValue(newTree1), gene2.duplicateWithValue(newTree2))
    }

    /**
     * Replaces a subtree within a source gene's tree with another subtree and validates the resulting tree's height.
     * This private function is a critical part of the subtree crossover operation in the `SubtreeCrossover` class.
     *
     * ## Functionality:
     * - If the source gene's tree has no children, the replacement node is returned as the new tree.
     * - If the source gene's tree has children, a subtree matching the original node is replaced with the replacement
     *   node.
     * - After the replacement, the height of the new tree is checked against a predefined maximum program depth
     *   ([Domain.maxProgramDepth]).
     * - If the new tree's height exceeds the maximum allowed depth, the original tree from the source gene is retained
     *   to prevent excessively deep or complex trees.
     * - Otherwise, the new tree with the replaced subtree is returned.
     *
     * ## Usage:
     * This function is used internally during the subtree crossover process to ensure that the offspring trees
     * generated are within acceptable height limits, maintaining the balance between exploration and exploitation in
     * genetic programming.
     *
     * ### Implementation Details:
     * - The function helps in maintaining the structural integrity of the genetic material post-crossover, avoiding
     *   issues such as bloating in genetic trees.
     * - Utilizes a strict identity check (`===`) to locate the exact subtree to be replaced, ensuring precise genetic
     *   manipulation.
     *
     * @param sourceGene The gene containing the original tree structure.
     * @param originalNode The node in the source gene's tree that is to be replaced.
     * @param replacementNode The new subtree node that will replace the original node.
     * @return The modified tree if it meets the height constraints; otherwise, the original tree.
     */
    private fun replaceSubtreeAndCheckHeight(sourceGene: G, originalNode: T, replacementNode: T) =
        if (sourceGene.value.children.isEmpty()) {
            replacementNode
        } else sourceGene.value.replaceFirst(replacementNode) { it === originalNode }.let { newTree ->
            if (newTree.height > Domain.maxProgramDepth) {
                sourceGene.value
            } else {
                newTree
            }
        }

    /**
     * Validates preconditions for the chromosomes involved in the subtree crossover operation. This private function
     * is an integral part of ensuring that the crossover process is applied correctly and effectively in the
     * `SubtreeCrossover` class.
     *
     * ## Functionality:
     * - Checks that exactly two chromosomes are provided for the crossover operation, as the subtree crossover is
     *   designed for pair-wise chromosome manipulation.
     * - Ensures that all parent chromosomes have the same size, which is essential for a consistent crossover process.
     * - Verifies that the arity of each gene matches the number of its children. This check is crucial to maintain the
     *   structural integrity of the tree encoded in each gene.
     *
     * ## Constraints:
     * - The function enforces strict size requirements on the chromosome list and the individual chromosomes to ensure
     *   compatibility for crossover.
     * - Validates the structural integrity of each gene within the chromosomes to prevent errors during the crossover
     *   process.
     *
     * ## Usage:
     * This function is called internally within the `SubtreeCrossover` class before performing the crossover
     * operation. It acts as a safeguard to ensure that only suitable chromosomes are subjected to crossover, thereby
     * maintaining the reliability and effectiveness of the genetic algorithm.
     *
     * ### Implementation Details:
     * - The function throws an exception if any of the preconditions are not met, which helps in early detection of
     *   issues and prevention of invalid crossover operations.
     *
     * @param chromosomes the list of chromosomes to be subjected to crossover.
     * @throws CompositeException if any of the preconditions are not met.
     * @throws CollectionConstraintException if any of the preconditions are not met.
     */
    private fun enforcePreconditions(chromosomes: List<Chromosome<T, G>>) = constraints {
        "The crossover operator requires two chromosomes." { chromosomes must HaveSize(2) }
        chromosomes.forEach { chromosome ->
            "The parents must have the same size" { chromosomes must HaveSize(chromosomes[0].size) }
            chromosome.forEach { gene ->
                "The gene's arity (${gene.value.arity}) must match the gene's children (${gene.value.children.size})" {
                    gene.value.children must HaveSize(gene.value.arity)
                }
            }
        }
    }
}
