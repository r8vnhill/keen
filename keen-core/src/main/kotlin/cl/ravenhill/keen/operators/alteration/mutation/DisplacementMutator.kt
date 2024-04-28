package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.keen.exceptions.MutatorConfigException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene

class DisplacementMutator<T, G>(
    override val individualRate: Double = DEFAULT_INDIVIDUAL_RATE,
    override val chromosomeRate: Double = DEFAULT_CHROMOSOME_RATE,
    val displacement: Int = DEFAULT_DISPLACEMENT
) : Mutator<T, G> where G : Gene<T, G> {
    init {
        constraints {
            "The individual rate ($individualRate) must be in 0.0..1.0"(::MutatorConfigException) {
                individualRate must BeInRange(0.0..1.0)
            }
            "The chromosome rate ($chromosomeRate) must be in 0.0..1.0"(::MutatorConfigException) {
                chromosomeRate must BeInRange(0.0..1.0)
            }
            "The displacement must be a non-negative integer"(
                ::MutatorConfigException
            ) {
                displacement mustNot BeNegative
            }
        }
    }

    override fun mutateChromosome(chromosome: Chromosome<T, G>): Chromosome<T, G> {
        if (displacement == 0) {
            return chromosome
        }
        TODO()
    }

    companion object {
        const val DEFAULT_INDIVIDUAL_RATE = 1.0
        const val DEFAULT_CHROMOSOME_RATE = 1.0
        const val DEFAULT_DISPLACEMENT = 1
    }
}
