package cl.ravenhill.keen.operators.crossover.permutation

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.requirements.IntRequirement.BeAtLeast
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


/**
 * The ordered crossover operator is a permutation crossover operator that preserves the relative
 * order of the genes in the offspring.
 * The operator selects a random subsequence of genes from the first parent and inserts them into
 * the second parent in the same order.
 * The remaining genes are inserted into the offspring in the order they appear in the second
 * parent.
 *
 * @param probability the probability of applying the recombination operation to an individual in
 *      the population
 * @param chromosomeRate the probability of applying the recombination operation to a chromosome in
 *      the individual (default: 1.0)
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.2.0
 * @version 2.0.0
 */
class OrderedCrossover<DNA, G: Gene<DNA, G>>(probability: Double, chromosomeRate: Double = 1.0) :
        AbstractPermutationCrossover<DNA, G>(probability, chromosomeRate = chromosomeRate) {

    /**
     * Performs ordered crossover on a list of chromosomes.
     *
     * @param chromosomes the list of chromosomes to be crossed over
     * @return a list of the offspring produced by the crossover operation
     */
    override fun doCrossover(chromosomes: List<Chromosome<DNA, G>>): List<List<G>> {
        val size = chromosomes.minOf { it.size }
        if (size >= 2) {
            val r1 = Core.random.nextInt(size)
            val r2 = Core.random.nextInt(size)
            val (start, end) = if (r1 < r2) r1 to r2 else r2 to r1
            val genes1 = chromosomes[0].genes
            val genes2 = chromosomes[1].genes
            lateinit var offspring1: List<G>
            lateinit var offspring2: List<G>
            // Launches the two crossover operations concurrently.
            runBlocking {
                launch {
                    offspring1 = crossoverGenes(genes1 to genes2, start, end, size)
                }
                launch {
                    offspring2 = crossoverGenes(genes2 to genes1, start, end, size)
                }
            }
            return listOf(offspring1, offspring2)
        }
        return chromosomes.map { it.genes }
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
        // Creates a new list to hold the offspring.
        val offspring = mutableListOf<G>()
        // Creates a new list to hold the genes from the second parent that are not in the sublist.
        val uniqueGenes = parents.second.toMutableList().apply { removeAll(sublist.toSet()) }
        // Adds the genes from the second parent that are not in the sublist to the offspring to
        // the new offspring in the same order they appear in the second parent (until the start
        // index).
        offspring.addAll(uniqueGenes.take(start))
        val remaining = uniqueGenes.size - offspring.size
        // Adds the sublist to the new offspring.
        offspring.addAll(sublist)
        // Adds the remaining genes from the second parent to the new offspring.
        offspring.addAll(uniqueGenes.takeLast(remaining))
        // Ensures that the size of the offspring's genes is at least the expected size.
        enforce {
            offspring.size should BeAtLeast(size) {
                "The size of the offspring's genes should be at least $size"
            }
        }
        return offspring
    }
}