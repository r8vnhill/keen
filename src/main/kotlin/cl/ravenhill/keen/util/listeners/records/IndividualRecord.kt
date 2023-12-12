/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.util.listeners.records

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene
import kotlinx.serialization.Serializable

@Serializable
data class IndividualRecord<DNA, G>(val genotype: Genotype<DNA, G>, val fitness: Double) :
    AbstractRecord() where G : Gene<DNA, G> {
    fun toIndividual() = Individual(genotype, fitness)
}
