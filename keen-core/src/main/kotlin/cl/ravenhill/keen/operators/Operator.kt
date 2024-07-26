/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.evolution.states.State
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.mixins.FitnessEvaluable
import cl.ravenhill.keen.repr.Representation

interface Operator<T, F> where F : Feature<T, F> {
    operator fun <S, R> invoke(
        state: S,
        outputSize: Int,
        buildState: (List<Individual<T, F, R>>) -> S
    ): S where S : State<T, F, R>, R : Representation<T, F>
}
