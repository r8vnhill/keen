/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

interface Evolver<T, F, R, S, L> where F : Feature<T, F>,
                                       R : Representation<T, F>,
                                       S : EvolutionState<T, F, R>,
                                       L : EvolutionListener<T, F, R, S> {

    val listeners: List<L>

    val limits: List<Limit<T, F, R, S, L>>

    val currentState: S

    val allListeners: List<L> get() = listeners + limits.map { it.listener }

    fun evolve(): S
}
