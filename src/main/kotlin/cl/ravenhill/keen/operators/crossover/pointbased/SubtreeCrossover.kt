/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.crossover.pointbased

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.ints.BeAtMost
import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.ConstraintException
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.crossover.AbstractCrossover
import cl.ravenhill.keen.util.trees.Tree

/**
 * A genetic crossover operator designed for tree-based genotypes.
 * This class implements a subtree crossover mechanism, where sub-portions of the parent trees are exchanged to create
 * offspring.
 *
 * ## Functionality:
 * - **Crossover Mechanism**: The crossover involves selecting subtrees from each of the parent genotypes and swapping
 *   them. This process results in two new trees that combine features of both parents.
 * - **Enforcement of Constraints**: The class enforces several constraints to ensure valid crossover operations.
 *   These include checking for the correct number of parents and ensuring the parents have the same size.
 *
 * ## Usage:
 * The `SubtreeCrossover` is especially suitable for genetic programming where the solutions are represented as tree
 * structures.
 * It's commonly used in scenarios where the genetic material can be structured in a hierarchical manner (e.g., symbolic
 * regression, decision trees).
 *
 * ### Example:
 * ```
 * val crossoverOperator = SubtreeCrossover<Reduceable<Int>, Program<Int>, ProgramGene<Int>>()
 * // Use in genetic algorithm configuration
 * ```
 *
 * @param V The type of value stored in the nodes of the tree.
 * @param DNA The specific tree structure that represents an individual's genotype.
 * @param G The type of gene that encapsulates the [DNA] type data.
 * @param exclusivity A flag indicating whether the same individual can be selected as a parent more than once.
 * @param chromosomeRate The rate at which chromosomes are selected for crossover.
 * @param geneRate The rate at which genes within a chromosome undergo crossover.
 *
 * @author <https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class SubtreeCrossover<V, DNA : Tree<V, DNA>, G : Gene<DNA, G>>(
    exclusivity: Boolean = false,
    chromosomeRate: Double = 1.0,
    val geneRate: Double = 1.0,
) : AbstractCrossover<DNA, G>(
    exclusivity = exclusivity,
    chromosomeRate = chromosomeRate
) {

    /**
     * Performs the crossover operation on a set of chromosomes.
     *
     * This method orchestrates the subtree crossover process, ensuring the resulting offspring chromosomes
     * are generated according to the defined crossover logic.
     *
     * ## Process Flow:
     * 1. **Precondition Enforcement**: Validates the input chromosomes to ensure they meet specific criteria
     *    necessary for a valid crossover operation (e.g., having exactly two parents of the same size).
     * 2. **Crossover Execution**: If all chromosomes contain genes with size greater than 1 (indicating
     *    non-trivial trees), the `applyCrossover` method is invoked to perform subtree crossover.
     * 3. **Parent Return**: If any chromosome does not meet the criteria for crossover (e.g., trivial trees
     *    with only one node), the original parent chromosomes are returned without any crossover.
     *
     * ## Usage:
     * This method is invoked internally by the genetic algorithm framework during the crossover phase of
     * the evolutionary process. It is not typically called directly by the user.
     *
     * @param chromosomes A list of parent chromosomes to undergo crossover.
     * @return A list of offspring chromosomes resulting from the crossover operation. If crossover is not applicable,
     *         the original parent chromosomes are returned.
     */
    override fun crossoverChromosomes(chromosomes: List<Chromosome<DNA, G>>): List<Chromosome<DNA, G>> {
        enforcePreconditions(chromosomes)
        return if (chromosomes.all { it.genes.any { g -> g.dna.size > 1 } }) {
            applyCrossover(chromosomes)
        } else {
            listOf(
                chromosomes[0].withGenes(chromosomes[0].genes),
                chromosomes[1].withGenes(chromosomes[1].genes)
            )
        }
    }

    /**
     * Validates the preconditions for the subtree crossover operation.
     *
     * This method checks that the chromosomes passed to the crossover operation meet specific criteria
     * necessary for a valid crossover process. These preconditions are essential to ensure that the
     * crossover can be performed correctly and that the resulting offspring chromosomes are valid.
     *
     * ## Precondition Checks:
     * 1. **Parent Count Validation**: Ensures that exactly two parent chromosomes are provided for the crossover.
     *    This is a fundamental requirement for the subtree crossover operation.
     * 2. **Parent Size Consistency**: Confirms that all parent chromosomes have the same number of genes.
     *    This uniformity is crucial for performing a coherent crossover between chromosomes.
     * 3. **Gene Arity and Children Consistency**: For each gene in the chromosomes, verifies that the number of
     *    children (sub-nodes) in the gene's DNA tree matches the gene's arity. This check is important to maintain
     *    the integrity of the tree structure within each gene.
     *
     * ## Usage:
     * The method is called internally before executing the crossover process to ensure that the chromosomes
     * meet the necessary conditions for a successful and meaningful subtree crossover.
     *
     * @param chromosomes A list of chromosomes to be validated before performing crossover.
     * @throws CompositeException If any of the preconditions are not met, indicating an invalid state
     *                            for crossover. This exception contains a [CollectionConstraintException].
     */
    private fun enforcePreconditions(chromosomes: List<Chromosome<DNA, G>>) = constraints {
        "The crossover must have exactly two parents" { chromosomes must HaveSize(2) }
        chromosomes.forEach { chromosome ->
            "The parents must have the same size" {
                chromosome.genes must HaveSize(chromosomes[0].genes.size)
            }
            chromosome.genes.forEach { gene ->
                val arity = gene.dna.arity
                val size = gene.dna.children.size
                "The gene's arity ($arity) does not match the number of children ($size)." {
                    gene.dna.children must HaveSize(arity)
                }
            }
        }
    }

    /**
     * Executes the subtree crossover process on a pair of parent chromosomes.
     *
     * This method is central to the `SubtreeCrossover` class, orchestrating the actual crossover
     * of genetic material between two parent chromosomes. It iterates through each pair of genes
     * from the parent chromosomes, deciding whether to perform crossover based on the predefined
     * gene rate.
     *
     * ## Process Flow:
     * 1. **Gene Pairing**: Pairs corresponding genes from each of the two parent chromosomes.
     * 2. **Crossover Decision**: Randomly decides whether to perform crossover for each gene pair
     *    based on the crossover probability (`geneRate`).
     * 3. **Gene Crossover**: If the crossover is to occur, the `crossoverGenes` method is called
     *    for the gene pair, and the resulting genes are added to the offspring's gene list.
     * 4. **Gene Retention**: If no crossover occurs for a gene pair, the original genes are retained
     *    and added directly to the offspring chromosomes.
     * 5. **Offspring Chromosome Creation**: Constructs new offspring chromosomes using the
     *    newly formed gene lists.
     *
     * @param chromosomes The list of parent chromosomes to undergo crossover.
     * @return A list containing two new offspring chromosomes resulting from the crossover process.
     */
    fun applyCrossover(chromosomes: List<Chromosome<DNA, G>>): List<Chromosome<DNA, G>> {
        // create two mutable lists to hold the new genes for each offspring chromosome
        val crossedGenes = mutableListOf<G>() to mutableListOf<G>()
        // iterate over each pair of genes from the two parent chromosomes
        chromosomes[0].genes.zip(chromosomes[1].genes).forEach { (gene1, gene2) ->
            // randomly decide whether to perform a crossover at this point
            if (Core.random.nextDouble() < geneRate) {
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
            chromosomes[0].withGenes(crossedGenes.first),
            chromosomes[1].withGenes(crossedGenes.second)
        )
    }

    /**
     * Conducts a subtree crossover between two genes and returns the resulting genes.
     *
     * This method is a key component of the subtree crossover mechanism. It selects random nodes
     * (subtrees) from each of the two parent genes and then swaps these subtrees to produce new
     * genetic structures. The process results in two new genes, each containing elements from both
     * parents, thereby promoting genetic diversity in the offspring.
     *
     * ## Process:
     * 1. **Node Selection**: Randomly selects a node from each parent gene's DNA (tree structure).
     *
     * 2. **Subtree Swapping**: Replaces the selected node in one parent with the node from the other
     * parent. This operation is performed for both parent genes.
     *
     * 3. **Height Check and Adjustment**: Ensures that the resulting trees do not exceed the maximum
     * allowed height, as defined by [Core.maxProgramDepth]. If a tree exceeds this height, a
     * fallback mechanism retains the original gene DNA.
     *
     * 4. **Constraint Application**: Validates that the new trees are within the permissible height
     * limits, enforcing the constraints of the genetic algorithm.
     *
     * @param gene1 The first parent gene involved in the crossover.
     * @param gene2 The second parent gene involved in the crossover.
     * @return A list containing two new genes, each a result of the crossover between the parent genes.
     */
    fun crossoverGenes(gene1: G, gene2: G): List<G> {
        // randomly select two nodes from the two parent genes
        val node1 = gene1.dna.random()
        val node2 = gene2.dna.random()
        val newTree1 = replaceSubtreeAndCheckHeight(gene1, node1, node2)
        val newTree2 = replaceSubtreeAndCheckHeight(gene2, node2, node1)

        // Apply constraints to check if the new trees are within the height limit
        constraints {
            "New tree height (${newTree1.height}) exceeds maximum limit (${Core.maxProgramDepth})." {
                newTree1.height must BeAtMost(Core.maxProgramDepth)
            }
            "New tree height (${newTree2.height}) exceeds maximum limit (${Core.maxProgramDepth})." {
                newTree2.height must BeAtMost(Core.maxProgramDepth)
            }
        }

        return listOf(gene1.withDna(newTree1), gene2.withDna(newTree2))
    }

    /**
     * Replaces a subtree in a gene's DNA with another subtree and checks if the resulting tree's height is within limits.
     *
     * This function is designed to be a part of the subtree crossover process in genetic programming. It replaces a
     * specified subtree (node) in the source gene's DNA with another subtree, and then ensures that the modified tree
     * does not exceed the maximum allowed height. This is crucial for maintaining the structural integrity and
     * feasibility of the solutions within a genetic algorithm.
     *
     * ## Process:
     * 1. **Subtree Replacement**: The specified subtree in the source gene's DNA is replaced with the replacement
     * subtree.
     * This operation modifies the structure of the tree, potentially creating a new genetic composition.
     *
     * 2. **Height Check**: The height of the resulting tree is compared against the maximum permissible height
     * ([Core.maxProgramDepth]). This step ensures that the tree remains within a manageable size and adheres to
     * the constraints of the problem.
     *
     * 3. **Fallback Mechanism**: If the new tree's height exceeds the maximum allowed height, the function returns the
     * original unmodified DNA of the source gene. This prevents the generation of oversized or overly complex trees
     * that could adversely affect the genetic algorithm's performance.
     *
     * @param sourceGene The gene containing the original DNA (tree structure) to be modified.
     * @param originalNode The node (subtree) in the source gene's DNA that is to be replaced.
     * @param replacementNode The new subtree that will replace the original node in the source gene's DNA.
     * @return The modified DNA (tree) after subtree replacement. If the resulting tree exceeds the maximum height,
     *         the original DNA of the source gene is returned.
     */
    fun replaceSubtreeAndCheckHeight(sourceGene: G, originalNode: DNA, replacementNode: DNA): DNA {
        return sourceGene.dna.replaceFirst(replacementNode) { it === originalNode }.let { newTree ->
            if (newTree.height > Core.maxProgramDepth) {
                sourceGene.dna
            } else {
                newTree
            }
        }
    }

}
