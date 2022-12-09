package cl.ravenhill.keen.operators.crossover.permutation

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.crossover.AbstractCrossover
import cl.ravenhill.keen.util.validateAtLeast
import cl.ravenhill.keen.util.validatePredicate
import kotlin.math.min


/**
 * The ordered crossover operator is a permutation crossover operator that preserves the relative
 * order of the genes in the offspring.
 * The operator selects a random subsequence of genes from the first parent and inserts them into
 * the second parent in the same order.
 * The remaining genes are inserted into the offspring in the order they appear in the second
 * parent.
 *
 * @param probability The probability of applying the crossover operator.
 *
 */
class OrderedCrossover<DNA>(probability: Double) : AbstractCrossover<DNA>(probability) {

    override fun crossover(genes1: MutableList<Gene<DNA>>, genes2: MutableList<Gene<DNA>>): Int {
        validatePredicate({ genes1.distinct().size == genes1.size }) { "Ordered crossover can't have duplicated genes: $genes1" }
        validatePredicate({ genes2.distinct().size == genes2.size }) { "Ordered crossover can't have duplicated genes: $genes2" }
        val size = min(genes1.size, genes2.size)
        val r1 = Core.rng.nextInt(size)
        val r2 = Core.rng.nextInt(size)
        if (size >= 2) {
            val (start, end) = if (r1 < r2) r1 to r2 else r2 to r1
            val offspring1 = doCrossover(genes1 to genes2, start, end, size)
            val offspring2 = doCrossover(genes2 to genes1, start, end, size)
            genes1.clear()
            genes1.addAll(offspring1)
            genes2.clear()
            genes2.addAll(offspring2)
        }
        return 1
    }

    /**
     * Performs the crossover to produce an offspring.
     */
    private fun doCrossover(
        genes: Pair<MutableList<Gene<DNA>>, MutableList<Gene<DNA>>>,
        start: Int,
        end: Int,
        size: Int
    ): MutableList<Gene<DNA>> {
        // Takes a sublist of genes from the first parent to be inserted into the second parent.
        val sublist = genes.first.subList(start, end + 1)
        // Creates a new list to hold the offspring.
        val offspring = mutableListOf<Gene<DNA>>()
        // Creates a new list to hold the genes from the second parent that are not in the sublist.
        val uniqueGenes = genes.second.toMutableList().apply { removeAll(sublist) }
        // Adds the genes from the second parent that are not in the sublist to the offspring to
        // the new offspring in the same order they appear in the second parent (until the start
        // index).
        offspring.addAll(uniqueGenes.take(start))
        val remaining = uniqueGenes.size - offspring.size
        // Adds the sublist to the new offspring.
        offspring.addAll(sublist)
        // Adds the remaining genes from the second parent to the new offspring.
        offspring.addAll(uniqueGenes.takeLast(remaining))
        offspring.size.validateAtLeast(size)
        return offspring
    }
}