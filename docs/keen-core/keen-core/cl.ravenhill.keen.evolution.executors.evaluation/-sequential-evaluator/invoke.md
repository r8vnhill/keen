//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.executors.evaluation](../index.md)/[SequentialEvaluator](index.md)/[invoke](invoke.md)

# invoke

[common]\
open suspend operator override fun [invoke](invoke.md)(state: [S](index.md), force: [ForceEvaluation](../-force-evaluation/index.md)): [S](index.md)

Executes the evaluation process on the given evolutionary state.

This function evaluates the individuals in the population according to the specified [ForceEvaluation](../-force-evaluation/index.md) strategy. The population is updated with the newly evaluated individuals, and the state is returned with this updated population.

#### Return

The updated evolutionary state after evaluation.

#### Parameters

common

| | |
|---|---|
| state | The current evolutionary state to be evaluated. |
| force | The evaluation strategy to use, determining which individuals are evaluated. |
