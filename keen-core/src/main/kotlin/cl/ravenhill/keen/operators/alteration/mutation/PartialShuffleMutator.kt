package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.keen.exceptions.MutatorConfigException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene

/**
 * ## References:
 * 1. Abdoun, Abouchabaka, and Tajani, “Analyzing the Performance of Mutation Operators to Solve the Travelling
 *  Salesman Problem.”
 */
class PartialShuffleMutator<T, G>(
    override val individualRate: Double = DEFAULT_INDIVIDUAL_RATE,
    override val chromosomeRate: Double = DEFAULT_CHROMOSOME_RATE,
    val shuffleBoundaryProbability: Double = DEFAULT_SHUFFLE_BOUNDARY_PROBABILITY
) : Mutator<T, G> where G : Gene<T, G> {

    init {
        constraints {
            "Individual mutation rate must be in the range [0, 1]"(::MutatorConfigException) {
                individualRate must BeInRange(0.0..1.0)
            }

            "Chromosome mutation rate must be in the range [0, 1]"(::MutatorConfigException) {
                chromosomeRate must BeInRange(0.0..1.0)
            }

            "Shuffle boundary probability must be in the range [0, 1]"(::MutatorConfigException) {
                shuffleBoundaryProbability must BeInRange(0.0..1.0)
            }
        }
    }

    override fun mutateChromosome(chromosome: Chromosome<T, G>): Chromosome<T, G> {
        if (shuffleBoundaryProbability == 0.0) return chromosome
        TODO("Not yet implemented")
    }

    companion object {
        const val DEFAULT_INDIVIDUAL_RATE = 1.0
        const val DEFAULT_CHROMOSOME_RATE = 1.0
        const val DEFAULT_SHUFFLE_BOUNDARY_PROBABILITY = 1.0
    }
}
