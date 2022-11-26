package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.util.validateAtLeast


open class MultiPointCrossover<DNA>(probability: Double, private val cuts: Int) :
        AbstractCrossover<DNA>(probability) {

    init {
        cuts.validateAtLeast(1) { "The crossover must have at least one cut" }
    }

    override fun crossover(mates: Pair<Chromosome<DNA>, Chromosome<DNA>>): Chromosome<DNA> {
        TODO("Not yet implemented")
    }
}