/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.config

import cl.ravenhill.keen.evolution.EvolutionInterceptor
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.listeners.Listener
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Configuration class for setting up an evolutionary algorithm.
 *
 * The `EvolutionConfiguration` data class encapsulates the configuration details required to run an evolutionary
 * algorithm. It holds the limits and listeners that govern the algorithm's execution, allowing users to define
 * termination conditions and monitor the evolutionary process through various lifecycle events.
 *
 * @param T The type of the value held by the features in the evolutionary algorithm.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 * @property limits A list of [Limit]s that define the stopping conditions for the evolutionary algorithm.
 * @property listeners A list of [EvolutionListener]s that monitor the lifecycle of the evolutionary process.
 * @property interceptor An [EvolutionInterceptor] that can modify the behavior of the evolutionary process.
 * @property initialState The initial state of the evolutionary process.
 */
data class EvolutionConfiguration<T, F, R, S>(
    val limits: List<Limit<T, F, R, S, Listener>>,
    val listeners: List<Listener>,
    val interceptor: EvolutionInterceptor<T, F, R, S>,
    val initialState: S
) where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R>
