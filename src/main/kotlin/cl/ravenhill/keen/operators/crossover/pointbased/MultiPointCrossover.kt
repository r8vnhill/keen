/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.crossover.pointbased

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.crossover.AbstractCrossover

/**
 * Not yet implemented.
 */
open class MultiPointCrossover<DNA, G : Gene<DNA, G>>(
    private val cuts: Int,
    exclusivity: Boolean = false,
    chromosomeRate: Double = 1.0,
) : AbstractCrossover<DNA, G>(exclusivity = exclusivity, chromosomeRate = chromosomeRate) {

    init {
        constraints { "The crossover must have at least one cut" { cuts must BePositive } }
    }

    override fun crossoverChromosomes(chromosomes: List<Chromosome<DNA, G>>): List<Chromosome<DNA, G>> {
        TODO("Multiple point crossover will be implemented on a future release")
    }
}