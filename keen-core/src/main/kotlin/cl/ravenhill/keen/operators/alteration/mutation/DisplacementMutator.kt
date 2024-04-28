package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.keen.exceptions.MutatorConfigException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene

class DisplacementMutator<T, G>(
    override val individualRate: Double = DEFAULT_INDIVIDUAL_RATE,
    override val chromosomeRate: Double = DEFAULT_CHROMOSOME_RATE,
    val displacementBoundaryProbability: Double = DEFAULT_DISPLACEMENT_BOUNDARY_PROBABILITY
) : Mutator<T, G> where G : Gene<T, G> {
    init {
        constraints {
            "The individual rate ($individualRate) must be in 0.0..1.0"(::MutatorConfigException) {
                individualRate must BeInRange(0.0..1.0)
            }
            "The chromosome rate ($chromosomeRate) must be in 0.0..1.0"(::MutatorConfigException) {
                chromosomeRate must BeInRange(0.0..1.0)
            }
            "The displacement boundary probability ($displacementBoundaryProbability) must be in 0.0..1.0"(
                ::MutatorConfigException
            ) {
                displacementBoundaryProbability must BeInRange(0.0..1.0)
            }
        }
    }

    override fun mutateChromosome(chromosome: Chromosome<T, G>): Chromosome<T, G> {
        TODO("Not yet implemented")
    }

    companion object {
        const val DEFAULT_INDIVIDUAL_RATE = 1.0
        const val DEFAULT_CHROMOSOME_RATE = 1.0
        const val DEFAULT_DISPLACEMENT_BOUNDARY_PROBABILITY = 0.5
    }
}
