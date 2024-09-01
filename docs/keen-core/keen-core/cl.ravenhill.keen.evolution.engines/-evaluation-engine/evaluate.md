//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.engines](../index.md)/[EvaluationEngine](index.md)/[evaluate](evaluate.md)

# evaluate

[common]\
abstract suspend fun [evaluate](evaluate.md)(state: [S](index.md)): Either&lt;[EvaluationException](../../cl.ravenhill.keen.exceptions/-evaluation-exception/index.md), [S](index.md)&gt;

Evaluates the population within the given evolutionary state.

This function is responsible for evaluating the fitness of each individual in the population based on their representation. The evaluation process is typically dependent on the specific problem being solved by the evolutionary algorithm. The function returns the updated evolutionary state after all individuals have been evaluated.

## Asynchronous Execution:

The `evaluate` function is marked as `suspend`, allowing it to be executed asynchronously. This is particularly beneficial in environments where non-blocking operations are necessary, such as in Kotlin/JS or when dealing with large-scale populations.

#### Return

The updated evolutionary state after the evaluation process is complete.

#### Parameters

common

| | |
|---|---|
| state | The current evolutionary state containing the population to be evaluated. |
