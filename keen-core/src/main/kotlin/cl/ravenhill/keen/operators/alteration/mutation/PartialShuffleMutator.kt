package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.keen.Domain
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
        val genes = chromosome.genes
        var start = 0
        var end = chromosome.size - 1
        for (i in chromosome.indices) {
            if (Domain.random.nextDouble() < shuffleBoundaryProbability) {
                start = i
                break
            }
        }
        for (i in start..<chromosome.size) {
            if (Domain.random.nextDouble() > shuffleBoundaryProbability) {
                end = i
                break
            }
        }
        return chromosome.duplicateWithGenes(
            chromosome.take(start)
                    + genes.subList(start, end + 1).shuffled(Domain.random)
                    + chromosome.drop(end + 1)
        )
    }

    override fun toString() =
        "PartialShuffleMutator(individualRate=$individualRate, chromosomeRate=$chromosomeRate, " +
                "shuffleBoundaryProbability=$shuffleBoundaryProbability)"

    companion object {
        const val DEFAULT_INDIVIDUAL_RATE = 1.0
        const val DEFAULT_CHROMOSOME_RATE = 1.0
        const val DEFAULT_SHUFFLE_BOUNDARY_PROBABILITY = 1.0
    }
}
