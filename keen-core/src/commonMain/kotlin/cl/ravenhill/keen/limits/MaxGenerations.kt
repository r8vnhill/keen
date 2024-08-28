package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

class MaxGenerations<T, F, R, S>(
    val maxGenerations: Int,
    configuration: ListenerConfiguration<T, F, R>
) : Limit<T, F, R, S, MaxGenerationsListener<T, F, R, S>>(
    MaxGenerationsListener(configuration),
    { state -> state.generation >= maxGenerations }
) where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R>

class MaxGenerationsListener<T, F, R, S>(
    configuration: ListenerConfiguration<T, F, R>
) : EvolutionListener<T, F, R, S> where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R>

fun <T, F, R, S> maxGenerations(
    maxGenerations: Int
): (ListenerConfiguration<T, F, R>) -> MaxGenerations<T, F, R, S> where F : Feature<T, F>,
                                                                        R : Representation<T, F>,
                                                                        S : EvolutionState<T, F, R> =
    { MaxGenerations(maxGenerations, it) }
