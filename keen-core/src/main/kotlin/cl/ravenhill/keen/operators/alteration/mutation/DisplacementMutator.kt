package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.keen.exceptions.MutatorConfigException

class DisplacementMutator(individualRate: Double, chromosomeRate: Double, displacementBoundaryProbability: Double) {
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
}
