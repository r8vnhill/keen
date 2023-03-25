package cl.ravenhill.keen.operators.crossover.permutation

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.indices
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * The Position-Based Crossover (PBX) operator inserts a different number of randomly
 * selected genes in one parent into the same position in one offspring, and the
 * remaining genes are inserted in the same order as in the other parent.
 *
 * @param <DNA> the type of the DNA.
 * @param probability the probability of crossover.
 *
 * __References:__
 *
 * - Starkweather, Timothy, S. McDaniel, Keith E. Mathias, L. Darrell Whitley, and C.
 *   Whitley. "A Comparison of Genetic Sequencing Operators." In ICGA, pp. 69-76. 1991.
 */
class PositionBasedCrossover<DNA>(probability: Double) :
    AbstractPermutationCrossover<DNA>(probability) {

    override fun doCrossover(
        genes1: MutableList<Gene<DNA>>,
        genes2: MutableList<Gene<DNA>>,
        size: Int
    ): Int {
        runBlocking {
            launch {
                val offspring1 = crossoverPair(genes1 to genes2, size)
                genes1.clear()
                genes1.addAll(offspring1)
            }
            launch {
                val offspring2 = crossoverPair(genes2 to genes1, size)
                genes2.clear()
                genes2.addAll(offspring2)
            }
        }
        return 1
    }

    private fun crossoverPair(
        parents: Pair<MutableList<Gene<DNA>>, MutableList<Gene<DNA>>>,
        size: Int
    ): MutableList<Gene<DNA>> {
        // We select random indices to insert the genes from the second parent into the first one.
        val indices = Core.random
            .indices(1 / size.toDouble(), Core.random.nextInt(size))
            .toList()
        // We do the actual selection
        val selected = parents.second.mapIndexed { index, gene -> index to gene }
            .filter { (index, _) -> index in indices }
        // The remaining (unique) genes from the first parent
        val remaining =
            parents.first.filter { gene -> gene !in selected.map { it.second } }
        // We create the offspring
        val offspring = mutableListOf<Gene<DNA>>()
        // We maintain two indices, one for traversing the selected genes and the other
        // for traversing the remaining genes
        var selectIdx = 0
        var remainingIdx = 0
        // We will traverse the indices of the first parent and add a gene from the
        // ``selected`` list if the index is in the ``indices`` list, otherwise we add
        // a gene from the ``remaining`` list
        for (i in 0 until size) {
            offspring.add(if (selected.any { it.first == i }) {
                selected[selectIdx++].second
            } else {
                remaining[remainingIdx++]
            })
        }
        return offspring
    }

    override fun crossover(chromosomes: List<Chromosome<DNA>>): List<Chromosome<DNA>> {
        TODO("Not yet implemented")
    }
}

typealias PBX<DNA> = PositionBasedCrossover<DNA>