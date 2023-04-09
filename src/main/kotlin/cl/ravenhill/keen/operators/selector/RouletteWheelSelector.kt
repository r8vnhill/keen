package cl.ravenhill.keen.operators.selector

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.math.eq
import cl.ravenhill.keen.util.sub
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer
import kotlin.math.min


class RouletteWheelSelector<DNA, G: Gene<DNA, G>>(
    sorted: Boolean = false
) : AbstractProbabilitySelector<DNA, G>(sorted) {

    override fun probabilities(
        population: Population<DNA, G>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA, G>
    ): DoubleArray {
        val fitness = population.fitness.let {
            it sub min(it.min(), 0.0)
        }
        val cums = fitness.reduce { acc, d -> acc + d }
        return if (cums eq 0.0) {
            List(population.size) { 1.0 / population.size }
        } else {
            fitness.map { it / cums }
        }.toDoubleArray()
    }

    override fun toString() = "RouletteWheelSelector { " +
            "sorted: $sorted }"
}

private val <DNA, G: Gene<DNA, G>> Population<DNA, G>.fitness: List<Double>
    get() = this.map { it.fitness }
