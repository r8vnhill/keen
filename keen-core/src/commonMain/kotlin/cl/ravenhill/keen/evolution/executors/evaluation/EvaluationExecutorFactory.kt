/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.executors.evaluation

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

class EvaluationExecutorFactory<T, F, R, S>
        where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {
    val creator: ((R) -> Double) -> EvaluationExecutor<T, F, R, S> =
        { function -> CoroutineConcurrentEvaluator(function) }
}
