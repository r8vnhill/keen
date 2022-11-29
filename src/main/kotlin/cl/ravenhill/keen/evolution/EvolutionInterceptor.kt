package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.util.validateAtLeast


class EvolutionInterceptor<DNA>(
    val before: (EvolutionStart<DNA>) -> EvolutionStart<DNA>,
    val after: (EvolutionResult<DNA>) -> EvolutionResult<DNA>
) {
    companion object {
        fun <DNA> identity() = EvolutionInterceptor(
            { before: EvolutionStart<DNA> -> before }
        ) { end: EvolutionResult<DNA> -> end }
    }
}
