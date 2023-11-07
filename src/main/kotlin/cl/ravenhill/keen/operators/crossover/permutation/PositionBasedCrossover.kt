package cl.ravenhill.keen.operators.crossover.permutation

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.IntConstraint.BeEqualTo
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.indices
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

/**
 * Type alias for the [PositionBasedCrossover] class with generic types ``DNA`` and ``G``.
 */
typealias PBX<DNA, G> = PositionBasedCrossover<DNA, G>


/**
 * A genetic operator that performs Position-Based Crossover (PBX) on a pair of parent chromosomes.
 *
 * PBX is a type of permutation crossover that is used when the relative ordering of genes in the
 * chromosomes is important.
 * It works by selecting a subset of positions in the first parent and copying the genes at those
 * positions into the corresponding positions in the offspring.
 * The remaining positions are filled with the genes from the second parent in the order in which
 * they appear, skipping any genes that have already been copied.
 *
 * This implementation of PBX supports chromosomes with any number of genes and requires two parent
 * chromosomes as input.
 * The crossover probability can be set using the [probability] parameter, which should be a value
 * between 0 and 1.
 *
 * # Pseudo-code
 *
 * ```
 * fun positionBasedCrossover(parent1, parent2):
 *     // Select random positions along the parent strings
 *     positions = selectRandomPositions(parent1.length)
 *     // Initialize the offspring as a copy of the first parent
 *     offspring = copy(parent1)
 *     // Inherit the selected elements from the first parent
 *     for i in positions:
 *         offspring[i] = parent1[i]
 *     // Donate the remaining elements from the second parent
 *     j = 0
 *     for i in range(parent2.length):
 *         if parent2[i] not in offspring:
 *             offspring[positions[j]] = parent2[i]
 *             j = j + 1
 *             if j >= len(positions):
 *                 break
 *     return offspring
 * ```
 *
 * # Example
 *
 * Let's walk through an example of the PBX algorithm using two parent chromosomes:
 * ```
 * P1 = [3, 6, 1, 4, 5, 2, 7, 8]
 * P2 = [6, 2, 1, 8, 3, 5, 7, 4]
 * ```
 *
 * ## Step 1: Select random locations on the parent chromosomes
 *
 * Let's say we select the locations 2, 4, and 7.
 * ```
 * S = [(2, 1), (4, 3), (7, 8)]
 * ```
 *
 * ## Step 2: Inherit elements from the first parent chromosome
 *
 * Next, we inherit the elements from the first parent chromosome at the selected locations, in the
 * order that they appear in the first parent chromosome.
 * ```
 * O: [_, _ , 1, _ , 3, _ , _ , 8]
 * ```
 *
 * ## Step 3: Donate remaining elements from the second parent chromosome
 *
 * Finally, we donate the remaining elements from the second parent chromosome, skipping any
 * elements that have already been copied.
 * ```
 * O: [3, 6, 1, 4, 3, 5, 7, 8]
 * ```
 *
 * @param DNA the type of data stored in the chromosome's genes
 * @param G the type of gene used in the chromosome
 * @property probability the probability of performing crossover on a pair of parent chromosomes
 * @constructor creates a new instance of the PBX crossover operator with the specified probability
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.2.0
 * @version 2.0.0
 */
class PositionBasedCrossover<DNA, G : Gene<DNA, G>>(val probability: Double) :
    AbstractPermutationCrossover<DNA, G>(probability) {

    /// Documentation inherited from [AbstractPermutationCrossover]
    override fun doCrossover(chromosomes: List<Chromosome<DNA, G>>): List<List<G>> = runBlocking {
        constraints {
            "PBX crossover requires two chromosomes" {
                chromosomes.size must BeEqualTo(2)
            }
        }
        val size = chromosomes.minOf { it.size }
        if (size >= 2) {
            val genes1 = chromosomes[0].genes.toMutableList()
            val genes2 = chromosomes[1].genes.toMutableList()
            val job1 = async { crossoverPair(genes1 to genes2, size) }
            val job2 = async { crossoverPair(genes2 to genes1, size) }
            listOf(job1.await(), job2.await())
        } else {
            chromosomes.map { it.genes }
        }
    }

    /**
     * Applies the PBX operator to a pair of chromosomes by selecting random positions
     * and swapping the genes between those positions.
     *
     * @param parents A pair of mutable lists of genes to be crossed over.
     * @param size The length of the chromosomes.
     * @return A mutable list containing the offspring generated by the crossover.
     */
    private fun crossoverPair(
        parents: Pair<MutableList<G>, MutableList<G>>,
        size: Int,
    ): MutableList<G> {
        // Select random indices to insert the genes from the second parent into the first one.
        val indices = Core.random.indices(1 / size.toDouble(), Core.random.nextInt(size))
        // Create a list of offspring genes
        val offspring = mutableListOf<G>()
        // Traverse the parent chromosomes and create the offspring by selecting the genes from
        // the first parent at the selected indices and the remaining genes from the second parent
        parents.first.forEachIndexed { index, gene ->
            if (index in indices) {
                offspring.add(gene)
            } else {
                parents.second.firstOrNull { it !in offspring }?.let {
                    offspring.add(it)
                }
            }
        }
        return offspring
    }

    /// Documentation inherited from [Any]
    override fun toString() = "PositionBasedCrossover(probability=$probability)"
}
