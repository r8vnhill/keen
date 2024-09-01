//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.executors.evaluation](../index.md)/[EvaluationExecutor](index.md)/[invoke](invoke.md)

# invoke

[common]\
abstract suspend operator fun [invoke](invoke.md)(state: [S](index.md), force: [ForceEvaluation](../-force-evaluation/index.md) = ForceEvaluation.NEW): [S](index.md)

Executes the evaluation process on the evolutionary state.

This operator function is responsible for evaluating the population of individuals within the provided evolutionary state. The `force` parameter controls the evaluation strategy:

- 
   [ForceEvaluation.NEW](../-force-evaluation/-n-e-w/index.md): Evaluate only individuals that have not been evaluated before or have changed.
- 
   [ForceEvaluation.ALL](../-force-evaluation/-a-l-l/index.md): Force the evaluation of all individuals, regardless of their current evaluation status.
- 
   [ForceEvaluation.NONE](../-force-evaluation/-n-o-n-e/index.md): Skip the evaluation process.

The function returns the updated evolutionary state with individuals that have been evaluated according to the specified strategy.

#### Return

The updated evolutionary state after the evaluation process is complete.

#### Parameters

common

| | |
|---|---|
| state | The current evolutionary state containing the population to be evaluated. |
| force | The evaluation strategy to apply. Defaults to [ForceEvaluation.NEW](../-force-evaluation/-n-e-w/index.md). |
