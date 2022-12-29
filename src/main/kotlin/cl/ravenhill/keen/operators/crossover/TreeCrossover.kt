package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.trees.Tree


class TreeCrossover<DNA>(probability: Double) : AbstractCrossover<Tree<DNA>>(probability) {

    override fun crossover(
        genes1: MutableList<Gene<Tree<DNA>>>,
        genes2: MutableList<Gene<Tree<DNA>>>
    ): Int {
        TODO("Not yet implemented")
    }
}