/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.mixins

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Interface for listening to alteration events in an evolutionary algorithm.
 *
 * The `AlterationListener` interface defines a set of methods for monitoring the alteration phase of an evolutionary
 * algorithm. Alteration refers to the operations that modify individuals in a population, such as crossover, mutation,
 * or other genetic operations. Implementing this interface allows you to respond to the start and end of these
 * operations, enabling custom behavior or logging during the evolutionary process.
 *
 * @param T The type of value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 */
interface AlterationListener<T, F, R, S> where F : Feature<T, F>,
                                               R : Representation<T, F>,
                                               S : EvolutionState<T, F, R> {

    /**
     * Called at the start of the alteration phase in the evolutionary process.
     *
     * This method is invoked when the alteration phase begins, allowing you to perform any necessary setup or
     * logging before the genetic operations are applied to the population. By default, this method does nothing
     * (`Unit`), but it can be overridden to perform custom actions.
     *
     * @param state The current evolutionary state, providing context for the alteration phase.
     */
    fun onAlterationStart(state: S) = Unit

    /**
     * Called at the end of the alteration phase in the evolutionary process.
     *
     * This method is invoked when the alteration phase ends, allowing you to perform any necessary teardown or
     * logging after the genetic operations have been applied to the population. By default, this method does
     * nothing (`Unit`), but it can be overridden to perform custom actions.
     *
     * @param state The current evolutionary state, providing context for the alteration phase.
     */
    fun onAlterationEnd(state: S) = Unit
}
