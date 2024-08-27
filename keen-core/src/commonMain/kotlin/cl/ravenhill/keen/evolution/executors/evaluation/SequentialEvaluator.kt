/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.executors.evaluation

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

class SequentialEvaluator<T, F, R, S>(
    private val evaluationFunction: (R) -> Double
) : EvaluationExecutor<T, F, R, S> where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R> {
    override fun invoke(state: S, force: ForceEvaluation): S = TODO()
}
