/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.operators.crossover.pointbased

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.crossover.AbstractCrossover
import cl.ravenhill.enforcer.requirements.IntRequirement.BePositive

/**
 * Not yet implemented.
 */
open class MultiPointCrossover<DNA, G : Gene<DNA, G>>(probability: Double, private val cuts: Int) :
        AbstractCrossover<DNA, G>(probability) {

    init {
        enforce { "The crossover must have at least one cut" { cuts must BePositive } }
    }

    override fun crossoverChromosomes(chromosomes: List<Chromosome<DNA, G>>): List<Chromosome<DNA, G>> {
        TODO("Multiple point crossover will be implemented on a future release")
    }
}