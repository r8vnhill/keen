package cl.ravenhill.keen.operators.crossover.permutation

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.requirements.IntRequirement.BeAtLeast
import cl.ravenhill.keen.requirements.IntRequirement.BeEqualTo
import cl.ravenhill.keen.util.indices
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

/**
 * Type alias for the [OrderedCrossover] class with generic types ``DNA`` and ``G``.
 */
typealias OX<DNA, G> = OrderedCrossover<DNA, G>

/**
 * The ordered crossover operator works by selecting a random subsequence of genes from one parent
 * chromosome and inserting them into the corresponding positions in the other parent chromosome.
 * The remaining genes are then added to the offspring in the order in which they appear in the
 * second parent, excluding any duplicates of the genes already inserted from the first parent.
 *
 * This operator is useful when the relative order of the genes is important, and it is desirable to
 * maintain that order in the offspring.
 *
 * This implementation of ordered crossover operates on a list of chromosomes, each of which
 * contains a list of genes that encode the permutation.
 * The operator requires exactly two chromosomes in the input list and returns a list of two
 * offspring.
 *
 * # Pseudo-code
 *
 * ```
 * fun orderedCrossover(parent1, parent2):
 *     offspring1 = makeCopy(parent1)
 *     offspring2 = makeCopy(parent2)
 *     // Select two random indexes
 *     index1, index2 = randomIndexes(parent1).sorted()
 *     // Create the crossing region
 *     crossSection = parent1[index1:index2]
 *     // Replace the values outside the crossing region
 *     for i in 0..parent1.size:
 *         if i < index1 or i >= index2:
 *             if parent2[i] not in crossSection:
 *                 offspring1[i] = parent2[i]
 *             if parent1[i] not in crossSection:
 *                 offspring2[i] = parent1[i]
 *     return offspring1, offspring2
 * ```
 *
 * # Example
 *
 * Let's say we have two parent chromosomes: ``P1 = [1, 2, 3, 4, 5, 6, 7, 8]`` and
 * ``P2 = [8, 7, 6, 5, 4, 3, 2, 1]``.
 *
 * 1. We randomly select two crossover points, ``lo = 2`` and ``hi = 6``, which define the region
 *    where the crossover will occur.
 * 2. The crossover region is copied from ``P1`` to the first offspring chromosome:
 *    ``O1 = [_, _, 3, 4, 5, 6, _, _]``.
 * 3. Next, we iterate through the remaining genes in ``P2`` in order, adding any genes that are not
 *    already in ``O1``: ``O1 = [8, 7, 3, 4, 5, 6, 2, 1]``.
 * 4.
 * 5. We repeat the same process for the second offspring chromosome, swapping the parents.
 *    The crossover region is copied from ``P2`` to the second offspring chromosome:
 *    ``O2 = [_, _, 6, 5, 4, 3, _, _]``
 * 6. Next, we iterate through the remaining genes in ``P1`` in order, adding any genes that are not
 *    already in ``O2``: ``O2 = [1, 2, 6, 5, 4, 3, 7, 8]``.
 *
 * The resulting offspring chromosomes are ``O1 = [8, 7, 3, 4, 5, 6, 2, 1]`` and
 * ``O2 = [1, 2, 6, 5, 4, 3, 7, 8]``.
 *
 * @param DNA the type of the genes in the chromosome
 * @param G the type of the genes in the chromosome
 * @property probability the probability of applying the crossover operator to a pair of chromosomes
 * @property chromosomeRate the rate at which chromosomes are selected for crossover
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.2.0
 * @version 2.0.0
 */
class OrderedCrossover<DNA, G : Gene<DNA, G>>(probability: Double, chromosomeRate: Double = 1.0) :
        AbstractPermutationCrossover<DNA, G>(probability, chromosomeRate = chromosomeRate) {

    /**
     * Performs ordered crossover on a list of chromosomes.
     *
     * @param chromosomes the list of chromosomes to be crossed over
     * @return a list of the offspring produced by the crossover operation
     */
    override fun doCrossover(chromosomes: List<Chromosome<DNA, G>>): List<List<G>> = runBlocking {
        enforce {
            "The Ordered Crossover operator requires exactly two chromosomes" {
                chromosomes.size must BeEqualTo(2)
            }
        }
        val size = chromosomes.minOf { it.size }
        if (size < 2) return@runBlocking chromosomes.map { it.genes }
        val (start, end) = Core.random.indices(2, size).sorted()
        val (genes1, genes2) = chromosomes.map { it.genes }
        // Launches the two crossover operations concurrently.
        val job1 = async { crossoverGenes(genes1 to genes2, start, end, size) }
        val job2 = async { crossoverGenes(genes2 to genes1, start, end, size) }
        listOf(job1.await(), job2.await())
    }

    /**
     * Performs the ordered crossover operation between two parents' genes.
     *
     * @param parents a pair of chromosomes representing the parents
     * @param start the starting index of the subsequence to be taken from the first parent's genes
     * @param end the ending index of the subsequence to be taken from the first parent's genes
     * @param size the expected size of the offspring's genes
     * @return a new list of genes representing the offspring produced by the crossover operation
     */
    private fun crossoverGenes(
        parents: Pair<List<G>, List<G>>,
        start: Int,
        end: Int,
        size: Int
    ): List<G> {
        // Takes a sublist of genes from the first parent to be inserted into the second parent.
        val sublist = parents.first.subList(start, end + 1)
        // Creates a new list to hold the genes from the second parent that are not in the sublist.
        val uniqueGenes = parents.second.filter { it !in sublist }
        // Creates the new offspring list by combining genes from the second parent and the sublist
        // of the first parent.
        val offspring = uniqueGenes.take(start) + sublist + uniqueGenes.drop(start)
        enforce {
            "The size of the offspring's genes should be at least $size" {
                offspring.size must BeAtLeast(size)
            }
        }
        return offspring
    }

    /// Documentation inherited from [Any]
    override fun toString() =
        "OrderedCrossover(probability=$probability, chromosomeRate=$chromosomeRate)"
}