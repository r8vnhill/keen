package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.genetic.genes.Gene


class TreeCrossover<DNA>(probability: Double) : AbstractCrossover<DNA>(probability) {

    override fun crossover(genes1: MutableList<Gene<DNA>>, genes2: MutableList<Gene<DNA>>): Int {
        TODO("Not yet implemented")
    }
}