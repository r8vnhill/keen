/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.mixins.GenerationListener
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

interface EvolutionListener<T, F, R, S> : GenerationListener<T, F, R, S>
        where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> {
    fun onEvolutionStart() = Unit
    fun onEvolutionEnd(state: S) = Unit
}
