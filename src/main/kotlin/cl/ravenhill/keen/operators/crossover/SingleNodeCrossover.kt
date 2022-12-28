package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.operators.AbstractRecombinatorAlterer
import cl.ravenhill.keen.prog.Reduceable
import kotlin.math.min

/**
 * The single node crossover operator is a genetic operator that selects a single node from each
 * parent and swaps them.
 *
 * @param DNA  The type of the genes' values.
 */
class SingleNodeCrossover<DNA>(probability: Double) :
        AbstractRecombinatorAlterer<Reduceable<DNA>>(probability, 2) {
    override fun recombine(
        population: MutableList<Phenotype<Reduceable<DNA>>>,
        individuals: IntArray,
        generation: Int
    ): Int {
        val genotype1 = population[individuals[0]].genotype
        val genotype2 = population[individuals[1]].genotype
        val size = min(genotype1.size, genotype2.size)
        val chIndex = Core.random.nextInt(size)
        val chromosomes1 = genotype1.chromosomes.toMutableList()
        val chromosomes2 = genotype2.chromosomes.toMutableList()
        crossoverAt(chIndex, chromosomes1 to chromosomes2)
        population[individuals[0]] = Phenotype(genotype1.duplicate(chromosomes1), generation)
        population[individuals[1]] = Phenotype(genotype2.duplicate(chromosomes2), generation)
        return order
    }

    private fun crossoverAt(
        chIndex: Int,
        parents: Pair<MutableList<Chromosome<Reduceable<DNA>>>, MutableList<Chromosome<Reduceable<DNA>>>>
    ) {
        val tree1 = parents.first[chIndex]
        val tree2 = parents.second[chIndex]
        crossoverTrees(tree1, tree2)

    }

    private fun crossoverTrees(
        tree1: Chromosome<Reduceable<DNA>>,
        tree2: Chromosome<Reduceable<DNA>>
    ) {
        TODO("Not yet implemented")
    }

}