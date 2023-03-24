package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.requirements.IntRequirement.BeAtLeast
import cl.ravenhill.keen.genetic.genes.Gene


open class MultiPointCrossover<DNA>(probability: Double, private val cuts: Int) :
        AbstractCrossover<DNA>(probability) {

    init {
        enforce { cuts should BeAtLeast(1) { "The crossover must have at least one cut" } }
    }

    override fun crossover(genes1: MutableList<Gene<DNA>>, genes2: MutableList<Gene<DNA>>): Int {
        TODO("Multiple point crossover is to be implemented on a future release")
    }

    override fun recombineChromosomes(chromosomes: List<Chromosome<DNA>>): List<Chromosome<DNA>> {
        TODO("Not yet implemented")
    }
}