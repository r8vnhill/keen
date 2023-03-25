package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.Tree


class TreeCrossover<DNA>(probability: Double) : AbstractCrossover<Tree<DNA>>(probability) {

//    override fun crossover(
//        genes1: MutableList<Gene<Tree<DNA>>>,
//        genes2: MutableList<Gene<Tree<DNA>>>
//    ): Int {
//        TODO("Not yet implemented")
//    }

    override fun crossover(chromosomes: List<Chromosome<Tree<DNA>>>): List<Chromosome<Tree<DNA>>> {
        TODO("Not yet implemented")
    }
}