//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.executors.evaluation](../index.md)/[CoroutineConcurrentEvaluator](index.md)

# CoroutineConcurrentEvaluator

class [CoroutineConcurrentEvaluator](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt;(evaluationFunction: ([R](index.md)) -&gt; [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), coroutineContext: [CoroutineContext](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html) = Dispatchers.Default) : [EvaluationExecutor](../-evaluation-executor/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt; 

A concurrent evaluator that uses Kotlin coroutines to evaluate individuals in an evolutionary algorithm.

The `CoroutineConcurrentEvaluator` class implements the [EvaluationExecutor](../-evaluation-executor/index.md) interface, providing a mechanism to evaluate individuals within a population concurrently using Kotlin coroutines. This approach is well-suited for scenarios where evaluations are computationally expensive and can benefit from parallel execution.

## Usage:

This class is intended for use in evolutionary algorithms where concurrent evaluation of individuals is required. It leverages the power of Kotlin coroutines to achieve parallelism, making it ideal for large-scale or computationally intensive evaluations.

### Example: Using `CoroutineConcurrentEvaluator` to Evaluate a Population Concurrently

```kotlin
val evaluationFunction: (MyRepresentation) -> Double = { representation -> // fitness calculation logic }
val evaluator = CoroutineConcurrentEvaluator(evaluationFunction)
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
| [CoroutineConcurrentEvaluator](-coroutine-concurrent-evaluator.md) | [common]<br>constructor(evaluationFunction: ([R](index.md)) -&gt; [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), coroutineContext: [CoroutineContext](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html) = Dispatchers.Default) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | [common]<br>open suspend operator override fun [invoke](invoke.md)(state: [S](index.md), force: [ForceEvaluation](../-force-evaluation/index.md)): [S](index.md)<br>Executes the evaluation process on the given evolutionary state concurrently. |
