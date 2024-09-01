//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.executors.evaluation](../index.md)/[SequentialEvaluator](index.md)

# SequentialEvaluator

class [SequentialEvaluator](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt;(evaluationFunction: ([R](index.md)) -&gt; [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)) : [EvaluationExecutor](../-evaluation-executor/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt; 

A sequential evaluator for evaluating individuals in an evolutionary algorithm.

The `SequentialEvaluator` class implements the [EvaluationExecutor](../-evaluation-executor/index.md) interface, providing a mechanism to evaluate individuals within a population in a sequential manner. It applies a given evaluation function to each individual based on the specified [ForceEvaluation](../-force-evaluation/index.md) strategy. This class is suitable for scenarios where evaluations need to be performed in a single-threaded, step-by-step fashion.

## Usage:

This class is intended to be used in evolutionary algorithms where the evaluation of individuals is required. It allows for flexible control over which individuals are evaluated through the [ForceEvaluation](../-force-evaluation/index.md) parameter.

### Example: Using `SequentialEvaluator` to Evaluate a Population

```kotlin
val evaluationFunction: (MyRepresentation) -> Double = { representation -> // fitness calculation logic }
val evaluator = SequentialEvaluator(evaluationFunction)
val evaluatedState = evaluator(currentState, ForceEvaluation.NEW)
```

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features within the individuals. |
| F | The type of feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |

## Constructors

| | |
|---|---|
| [SequentialEvaluator](-sequential-evaluator.md) | [common]<br>constructor(evaluationFunction: ([R](index.md)) -&gt; [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | [common]<br>open suspend operator override fun [invoke](invoke.md)(state: [S](index.md), force: [ForceEvaluation](../-force-evaluation/index.md)): [S](index.md)<br>Executes the evaluation process on the given evolutionary state. |
