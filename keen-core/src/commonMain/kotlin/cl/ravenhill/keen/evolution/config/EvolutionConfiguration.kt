/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.config

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

data class EvolutionConfiguration<T, F, R, S>(
    val limits: List<Limit<T, F, R, S>>,
    val listeners: List<EvolutionListener<T, F, R, S>>,
) where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R>
