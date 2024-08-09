/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.config.EvolutionConfiguration
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary

class AbstractEvolverTest : FreeSpec({
    ""
})

private fun <T, F, R, S> arbEvolver(
    initialState: Arb<S>,
    evolutionConfiguration: Arb<EvolutionConfiguration<T, F, R, S>>
) where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> = arbitrary {
    val boundConfiguration = evolutionConfiguration.bind()
    val boundInitialState = initialState.bind()
    object : AbstractEvolver<T, F, R, S>(boundConfiguration) {
        override var state: S = boundInitialState

        override fun iterateGeneration(state: S): S {
            return state
        }

    }
}